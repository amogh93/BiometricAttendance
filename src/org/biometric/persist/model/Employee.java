package org.biometric.persist.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Employee")
public class Employee {

	@Id
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(name = "joining_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date joiningDate;

	@Column(name = "date_of_birth", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;

	@Column(nullable = false)
	private String email;

	@Column(name = "contact_number", nullable = false)
	private String contactNumber;

	@Lob
	@Column(name = "employee_image")
	private byte[] image;

	@Column(name = "employee_gender")
	private char gender;

	@OneToOne(mappedBy = "employee")
	private LeavesRemaining leave_count;

	@OneToMany(mappedBy = "employee")
	private Set<LeaveSchedule> schedule = new HashSet<LeaveSchedule>();

	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

	@OneToOne(mappedBy = "employee")
	private EmployeeLogin employeeLogin;

	@OneToOne(mappedBy = "employee")
	private AccessLevel accessLevel;

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	private Set<Attendance> attendance = new HashSet<Attendance>();

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	private Set<BiometricTemplate> template = new HashSet<BiometricTemplate>();

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	private Set<AttendanceEncrypted> attendanceEncrypted = new HashSet<AttendanceEncrypted>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public EmployeeLogin getEmployeeLogin() {
		return employeeLogin;
	}

	public void setEmployeeLogin(EmployeeLogin employeeLogin) {
		this.employeeLogin = employeeLogin;
	}

	public Set<Attendance> getAttendance() {
		return attendance;
	}

	public void setAttendance(Set<Attendance> attendance) {
		this.attendance = attendance;
	}

	public Set<BiometricTemplate> getTemplate() {
		return template;
	}

	public void setTemplate(Set<BiometricTemplate> template) {
		this.template = template;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public AccessLevel getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(AccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	public Set<AttendanceEncrypted> getAttendanceEncrypted() {
		return attendanceEncrypted;
	}

	public void setAttendanceEncrypted(Set<AttendanceEncrypted> attendanceEncrypted) {
		this.attendanceEncrypted = attendanceEncrypted;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public LeavesRemaining getLeave_count() {
		return leave_count;
	}

	public void setLeave_count(LeavesRemaining leave_count) {
		this.leave_count = leave_count;
	}

	public Set<LeaveSchedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(Set<LeaveSchedule> schedule) {
		this.schedule = schedule;
	}

}
