package org.biometric.persist;

import org.biometric.persist.model.BiometricTemplate;
import org.biometric.persist.model.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SaveTemplate {

	private static Employee employee;

	public static void saveToDb(byte[] b, int fingpos) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			BiometricTemplate template = new BiometricTemplate();
			template.setTemplate(b);
			template.setFinger_position(fingpos);
			template.setEmployee(employee);
			session.save(template);
			session.getTransaction().commit();
		}
	}

	public static void setEmployee(Employee employee, byte[] template, int fingpos) {
		SaveTemplate.employee = employee;
		saveToDb(template, fingpos);
	}

}
