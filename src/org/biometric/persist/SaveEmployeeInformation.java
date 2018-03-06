package org.biometric.persist;

import java.util.Date;

import org.biometric.crypto.CryptoUtils;
import org.biometric.persist.model.AccessLevel;
import org.biometric.persist.model.Department;
import org.biometric.persist.model.Employee;
import org.biometric.persist.model.EmployeeLogin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class SaveEmployeeInformation {

	public static String saveToDb(String id, String name, Date dob, Date joining_date, String contact_num,
			String email_id, byte[] template, byte[] template1, byte[] template2, byte[] template3, byte[] template4,
			byte[] image, char gender, int fingpos1, int fingpos2, int fingpos3, int fingpos4, int fingpos5, int dept) {
		String generated_username = "NA";
		
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Department department = session.load(Department.class, dept);
			Employee employee = new Employee();
			EmployeeLogin login = new EmployeeLogin();
			AccessLevel level = new AccessLevel();
			employee.setId(id);
			employee.setName(name);
			employee.setDateOfBirth(dob);
			employee.setJoiningDate(joining_date);
			employee.setContactNumber(contact_num);
			employee.setEmail(email_id);
			employee.setImage(image);
			employee.setGender(gender);
			level.setEmployee(employee);
			employee.setAccessLevel(level);
			login.setEmployee(employee);
			generated_username = generateUserName(id, name);
			login.setUserName(generated_username);
			login.setPassword(CryptoUtils.doEncrypt("admin1234"));
			SaveTemplate.setEmployee(employee, template, fingpos1);
			SaveTemplate.setEmployee(employee, template1, fingpos2);
			SaveTemplate.setEmployee(employee, template2, fingpos3);
			SaveTemplate.setEmployee(employee, template3, fingpos4);
			SaveTemplate.setEmployee(employee, template4, fingpos5);
			session.save(employee);
			session.save(login);
			session.save(level);
			session.getTransaction().commit();
		}
		return "Your auto-generated username is " + generated_username + " and password is admin1234";
	}

	public static String saveToDb(String id, String name, Date dob, Date joining_date, String contact_num,
			String email_id, String user_name, String password, byte[] template, byte[] template1, byte[] template2,
			byte[] template3, byte[] template4, byte[] image, char gender, int fingpos1, int fingpos2, int fingpos3,
			int fingpos4, int fingpos5, int dept) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			Employee employee = new Employee();

			Department department = session.load(Department.class, dept);

			EmployeeLogin login = new EmployeeLogin();
			AccessLevel level = new AccessLevel();
			employee.setId(id);
			employee.setName(name);
			employee.setDateOfBirth(dob);
			employee.setJoiningDate(joining_date);
			employee.setContactNumber(contact_num);
			employee.setEmail(email_id);
			employee.setImage(image);
			employee.setGender(gender);
			employee.setDepartment(department);
			level.setEmployee(employee);
			login.setEmployee(employee);
			login.setUserName(user_name);
			login.setPassword(CryptoUtils.doEncrypt(password));
			SaveTemplate.setEmployee(employee, template, fingpos1);
			SaveTemplate.setEmployee(employee, template1, fingpos2);
			SaveTemplate.setEmployee(employee, template2, fingpos3);
			SaveTemplate.setEmployee(employee, template3, fingpos4);
			SaveTemplate.setEmployee(employee, template4, fingpos5);
			session.save(employee);
			session.save(login);
			session.save(level);
			session.getTransaction().commit();
		}
		return "Your username is " + user_name + " and password is " + password;
	}

	public static String saveToDb(String id, String name, Date dob, Date joining_date, String contact_num,
			String email_id, byte[] template, byte[] template1, byte[] template2, byte[] template3, byte[] template4,
			char gender, int fingpos1, int fingpos2, int fingpos3, int fingpos4, int fingpos5, int dept) {
		String generated_username = "NA";
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			session.beginTransaction();

			Department department = session.load(Department.class, dept);

			Employee employee = new Employee();
			EmployeeLogin login = new EmployeeLogin();
			AccessLevel level = new AccessLevel();
			employee.setId(id);
			employee.setName(name);
			employee.setDateOfBirth(dob);
			employee.setJoiningDate(joining_date);
			employee.setContactNumber(contact_num);
			employee.setEmail(email_id);
			employee.setGender(gender);
			employee.setDepartment(department);
			level.setEmployee(employee);
			employee.setAccessLevel(level);
			login.setEmployee(employee);
			generated_username = generateUserName(id, name);
			login.setUserName(generated_username);
			login.setPassword(CryptoUtils.doEncrypt("admin1234"));
			SaveTemplate.setEmployee(employee, template, fingpos1);
			SaveTemplate.setEmployee(employee, template1, fingpos2);
			SaveTemplate.setEmployee(employee, template2, fingpos3);
			SaveTemplate.setEmployee(employee, template3, fingpos4);
			SaveTemplate.setEmployee(employee, template4, fingpos5);
			session.save(employee);
			session.save(login);
			session.save(level);
			session.getTransaction().commit();
		}
		return "Your auto-generated username is " + generated_username + " and password is admin1234";
	}

	public static String saveToDb(String id, String name, Date dob, Date joining_date, String contact_num,
			String email_id, String user_name, String password, byte[] template, byte[] template1, byte[] template2,
			byte[] template3, byte[] template4, char gender, int fingpos1, int fingpos2, int fingpos3, int fingpos4,
			int fingpos5, int dept) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			session.beginTransaction();

			Department department = session.load(Department.class, dept);

			Employee employee = new Employee();
			EmployeeLogin login = new EmployeeLogin();
			AccessLevel level = new AccessLevel();
			employee.setId(id);
			employee.setName(name);
			employee.setDateOfBirth(dob);
			employee.setJoiningDate(joining_date);
			employee.setContactNumber(contact_num);
			employee.setEmail(email_id);
			employee.setGender(gender);
			employee.setDepartment(department);
			level.setEmployee(employee);
			login.setEmployee(employee);
			login.setUserName(user_name);
			login.setPassword(CryptoUtils.doEncrypt(password));
			SaveTemplate.setEmployee(employee, template, fingpos1);
			SaveTemplate.setEmployee(employee, template1, fingpos2);
			SaveTemplate.setEmployee(employee, template2, fingpos3);
			SaveTemplate.setEmployee(employee, template3, fingpos4);
			SaveTemplate.setEmployee(employee, template4, fingpos5);
			session.save(employee);
			session.save(login);
			session.save(level);
			session.getTransaction().commit();
		}
		return "Your username is " + user_name + " and password is " + password;
	}

	public static String generateUserName(String emp_id, String emp_name) {
		String[] temp = emp_name.split(" ");
		String generated_username = temp[0] + emp_id;
		generated_username = generated_username.toLowerCase().trim();
		return generated_username;
	}

}
