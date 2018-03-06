package org.biometric.logAttendance;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.biometric.persist.model.Attendance;
import org.biometric.persist.model.Employee;
import org.biometric.registration.AttendanceLogging;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class MarkAttendance {

	private static Attendance a;

	public static void mark(String id, Employee e) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Query query = session.createQuery("from Attendance where employee_id=:id and date=:date order by id desc");
			query.setParameter("id", id);
			query.setParameter("date", new Date());
			query.setMaxResults(1);
			try {
				List result = query.getResultList();
				if (result.size() > 0) {
					a = (Attendance) result.iterator().next();
					if (a.getOut_time().toString().equals("00:00:00")) {
						a.setOut_time(new Date());
						a.setOut_logging_mode("BIOMETRIC");
						session.update(a);
						session.getTransaction().commit();
						AttendanceLogging.setLastEntryLabel("Out-time");
					} else if (!a.getOut_time().toString().equals("00:00:00")) {
						a = new Attendance();
						a.setDate(new Date());
						a.setEmployee(e);
						a.setIn_time(new Date());
						a.setIn_logging_mode("BIOMETRIC");
						a.setOut_logging_mode("-");
						session.save(a);
						session.getTransaction().commit();
						AttendanceLogging.setLastEntryLabel("In-time");
					}
				} else {
					a = new Attendance();
					a.setDate(new Date());
					a.setEmployee(e);
					a.setIn_time(new Date());
					a.setIn_logging_mode("BIOMETRIC");
					a.setOut_logging_mode("-");
					session.save(a);
					session.getTransaction().commit();
					AttendanceLogging.setLastEntryLabel("In-time");
				}
			} catch (Exception ex) {

			}
		}
	}

}
