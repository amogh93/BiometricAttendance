package org.biometric.persist.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "employee_login")
public class EmployeeLogin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "last_login", columnDefinition = "datetime default '0000-00-00 00:00:00'")
	private Date last_login;

	@OneToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getLast_login() {
		return last_login;
	}

	public void setLast_login(Date last_login) {
		this.last_login = last_login;
	}
}
