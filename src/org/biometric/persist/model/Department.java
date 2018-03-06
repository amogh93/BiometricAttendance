package org.biometric.persist.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "department")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "department")
	private String department_name;

	@OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
	private Set<Employee> employee = new HashSet<Employee>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public Set<Employee> getEmployee() {
		return employee;
	}

	public void setEmployee(Set<Employee> employee) {
		this.employee = employee;
	}

}
