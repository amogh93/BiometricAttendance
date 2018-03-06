package org.biometric.updation;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.biometric.crypto.CryptoUtils;
import org.biometric.persist.model.Attendance;
import org.biometric.persist.model.BiometricTemplate;
import org.biometric.persist.model.Employee;
import org.biometric.persist.model.EmployeeLogin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class UpdateEmployee {

	public boolean updateEmail(String id, String email) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Employee.class);
			detachedCriteria.add(Restrictions.eq("id", id));
			List result = detachedCriteria.getExecutableCriteria(session).list();
			Employee e = (Employee) result.iterator().next();
			session.beginTransaction();
			e.setEmail(email);
			session.update(e);
			session.getTransaction().commit();
		}
		return true;
	}

	public boolean updateContact(String id, String contact_no) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Employee.class);
			detachedCriteria.add(Restrictions.eq("id", id));
			List result = detachedCriteria.getExecutableCriteria(session).list();
			Employee e = (Employee) result.iterator().next();
			session.beginTransaction();
			e.setContactNumber(contact_no);
			session.update(e);
			session.getTransaction().commit();
		}
		return true;
	}

	public boolean updatePassword(String id, String password) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Employee.class);
			detachedCriteria.add(Restrictions.eq("id", id));
			List result = detachedCriteria.getExecutableCriteria(session).list();
			Employee e = (Employee) result.iterator().next();
			EmployeeLogin login = e.getEmployeeLogin();
			session.beginTransaction();
			login.setPassword(CryptoUtils.doEncrypt(password));
			session.update(login);
			session.getTransaction().commit();
		}
		return true;
	}

	public boolean updateBiometric(String id, byte[] f1, int fingpos, int new_fingpos) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			Employee e = (Employee) session.load(Employee.class, id);
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BiometricTemplate.class);
			detachedCriteria.add(Restrictions.eq("finger_position", fingpos));
			detachedCriteria.add(Restrictions.eq("employee", e));
			List bio_result = detachedCriteria.getExecutableCriteria(session).list();

			Iterator itr = bio_result.iterator();

			session.beginTransaction();
			BiometricTemplate template = (BiometricTemplate) itr.next();
			template.setTemplate(f1);
			template.setFinger_position(new_fingpos);
			session.update(template);
			session.getTransaction().commit();
		}
		return true;
	}

	public boolean updateImage(String id, byte[] f) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			Employee e = (Employee) session.load(Employee.class, id);
			session.beginTransaction();
			e.setImage(f);
			session.update(e);
			session.getTransaction().commit();
		}
		return true;
	}

	public boolean deleteEmployee(String id) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Employee employee = (Employee) session.load(Employee.class, id);
			session.delete(employee.getAccessLevel());
			session.delete(employee.getEmployeeLogin());
			Set bio_template = employee.getTemplate();
			if (bio_template.size() > 0) {
				Iterator itr = bio_template.iterator();
				while (itr.hasNext()) {
					BiometricTemplate template = (BiometricTemplate) itr.next();
					session.delete(template);
				}
			}

			Set emp_attn = employee.getAttendance();
			if (emp_attn.size() > 0) {
				Iterator emp_attn_itr = emp_attn.iterator();
				while (emp_attn_itr.hasNext()) {
					Attendance attendance = (Attendance) emp_attn_itr.next();
					session.delete(attendance);
				}
			}

			session.delete(employee);
			session.getTransaction().commit();
		}
		return true;
	}
}
