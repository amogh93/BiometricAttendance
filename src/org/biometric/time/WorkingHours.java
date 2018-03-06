package org.biometric.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.biometric.persist.model.Attendance;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;

public class WorkingHours {

	private static Date in_time, out_time;

	public static Date getDefaultWorkingHours() throws Exception {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.parse("00:00:00");
	}

	public static void calculateWorkingHours() throws Exception {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Attendance.class);
			List result = detachedCriteria.getExecutableCriteria(session).list();
			if (result.size() > 0) {
				Iterator itr = result.iterator();

				while (itr.hasNext()) {
					Attendance attendance = (Attendance) itr.next();
					in_time = attendance.getIn_time();
					out_time = attendance.getOut_time();
					if (out_time.toString().equals("00:00:00")) {
						attendance.setWorking_hours(getDefaultWorkingHours());
						session.update(attendance);
						session.flush();
					} else {
						attendance.setWorking_hours(getTimeDifference(out_time, in_time));
						session.update(attendance);
						session.flush();
					}
				}
			}
		}
	}

	public static Date getTimeDifference(Date outTime, Date inTime) throws Exception {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date d1 = outTime;
		Date d2 = inTime;
		long diff = d1.getTime() - d2.getTime();

		long timeInSeconds = diff / 1000;
		long hours, minutes, seconds;
		hours = timeInSeconds / 3600;
		timeInSeconds = timeInSeconds - (hours * 3600);
		minutes = timeInSeconds / 60;
		timeInSeconds = timeInSeconds - (minutes * 60);
		seconds = timeInSeconds;

		String timeDiff = hours + ":" + minutes + ":" + seconds;

		return df.parse(timeDiff);
	}

}
