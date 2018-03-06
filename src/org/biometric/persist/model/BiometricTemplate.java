package org.biometric.persist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Table(name = "employee_biometric_template")
public class BiometricTemplate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Lob
	@Column(name = "template")
	private byte[] template;

	@Column(name = "finger_position")
	private int finger_position;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public int getFinger_position() {
		return finger_position;
	}

	public void setFinger_position(int finger_position) {
		this.finger_position = finger_position;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public byte[] getTemplate() {
		return template;
	}

	public void setTemplate(byte[] template) {
		this.template = template;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
