package org.biometric.persist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class LeavesRemaining {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@Column(name = "cl_count")
	private long cl_count;

	@Column(name = "pl_count")
	private long pl_count;

	@Column(name = "sl_count")
	private long sl_count;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public long getCl_count() {
		return cl_count;
	}

	public void setCl_count(long cl_count) {
		this.cl_count = cl_count;
	}

	public long getPl_count() {
		return pl_count;
	}

	public void setPl_count(long pl_count) {
		this.pl_count = pl_count;
	}

	public long getSl_count() {
		return sl_count;
	}

	public void setSl_count(long sl_count) {
		this.sl_count = sl_count;
	}
}
