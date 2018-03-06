package org.biometric.persist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Table(name = "employee_access")
public class AccessLevel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "access_level", columnDefinition = "varchar(255) default 'RESTRICTED'")
	private String accessLevel;

	@OneToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
