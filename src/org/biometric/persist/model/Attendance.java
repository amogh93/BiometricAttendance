package org.biometric.persist.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@Table(name = "employee_attendance")
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "in_time")
	@Temporal(TemporalType.TIME)
	private Date in_time;

	@Temporal(TemporalType.TIME)
	@Column(name = "out_time", columnDefinition = "time default '00:00:00'")
	private Date out_time;

	@Column(name = "date")
	@Temporal(TemporalType.DATE)
	private Date date;

	@Column(name = "in_logging_mode")
	private String in_logging_mode;

	@Column(name = "out_logging_mode")
	private String out_logging_mode;

	@Column(name = "status")
	private String status;

	@Column
	@Temporal(TemporalType.TIME)
	private Date working_hours;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getIn_time() {
		return in_time;
	}

	public void setIn_time(Date in_time) {
		this.in_time = in_time;
	}

	public Date getOut_time() {
		return out_time;
	}

	public void setOut_time(Date out_time) {
		this.out_time = out_time;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getIn_logging_mode() {
		return in_logging_mode;
	}

	public void setIn_logging_mode(String in_logging_mode) {
		this.in_logging_mode = in_logging_mode;
	}

	public String getOut_logging_mode() {
		return out_logging_mode;
	}

	public void setOut_logging_mode(String out_logging_mode) {
		this.out_logging_mode = out_logging_mode;
	}

	public Date getWorking_hours() {
		return working_hours;
	}

	public void setWorking_hours(Date working_hours) {
		this.working_hours = working_hours;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
