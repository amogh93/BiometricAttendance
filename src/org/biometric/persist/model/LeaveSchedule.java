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

@Entity
@Table(name = "employee_leaves")
public class LeaveSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "from_date")
	@Temporal(TemporalType.DATE)
	private Date from_date;

	@Column(name = "to_date")
	@Temporal(TemporalType.DATE)
	private Date to_date;

	@Column(name = "description")
	private String description;

	@Column(name = "apprval_status")
	private String approval_status;

	@Column(name = "leave_type")
	private String leave_type;

	@Column(name = "time_of_request")
	@Temporal(TemporalType.TIMESTAMP)
	private Date time_of_request;

	@Column(name = "time_of_approval", columnDefinition = "datetime default '0000-00-00 00:00:00'")
	@Temporal(TemporalType.TIMESTAMP)
	private Date time_of_approval;

	@Column(name = "approved_by")
	private String approved_by;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getFrom_date() {
		return from_date;
	}

	public void setFrom_date(Date from_date) {
		this.from_date = from_date;
	}

	public Date getTo_date() {
		return to_date;
	}

	public void setTo_date(Date to_date) {
		this.to_date = to_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApproval_status() {
		return approval_status;
	}

	public void setApproval_status(String approval_status) {
		this.approval_status = approval_status;
	}

	public String getLeave_type() {
		return leave_type;
	}

	public void setLeave_type(String leave_type) {
		this.leave_type = leave_type;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getTime_of_request() {
		return time_of_request;
	}

	public void setTime_of_request(Date time_of_request) {
		this.time_of_request = time_of_request;
	}

	public Date getTime_of_approval() {
		return time_of_approval;
	}

	public void setTime_of_approval(Date time_of_approval) {
		this.time_of_approval = time_of_approval;
	}

	public String getApproved_by() {
		return approved_by;
	}

	public void setApproved_by(String approved_by) {
		this.approved_by = approved_by;
	}
}
