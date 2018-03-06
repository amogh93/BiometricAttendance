package org.biometric;

import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.biometric.registration.AttendanceLogging;
import org.biometric.registration.RegistrationForm;
import org.biometric.updation.Authenticator;

import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WelcomeScreen {

	private JFrame frmMainApp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeScreen window = new WelcomeScreen();
					window.frmMainApp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WelcomeScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMainApp = new JFrame();
		frmMainApp.setTitle("BioAttendance by SIL");
		frmMainApp.setBounds(100, 100, 536, 356);
		frmMainApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMainApp.getContentPane().setLayout(null);

		JLabel label = new JLabel("");
		label.setBounds(10, 11, 124, 103);
		try {
			label.setIcon(new ImageIcon(
					ImageIO.read(Authenticator.class.getClassLoader().getResourceAsStream("resources/SIL logo.jpg"))
							.getScaledInstance(136, 90, Image.SCALE_SMOOTH)));
		} catch (Exception ex) {

		}
		frmMainApp.getContentPane().add(label);

		JLabel lblBiometricAttendanceSystem = new JLabel("Biometric Attendance System");
		lblBiometricAttendanceSystem.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		lblBiometricAttendanceSystem.setHorizontalAlignment(SwingConstants.CENTER);
		lblBiometricAttendanceSystem.setBounds(144, 22, 365, 72);
		frmMainApp.getContentPane().add(lblBiometricAttendanceSystem);

		JButton btnForEmployee = new JButton("");
		btnForEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RegistrationForm().frmNewRegistrationForm.setVisible(true);
			}
		});
		btnForEmployee.setBounds(45, 146, 81, 87);
		try {
			Image emp_img = ImageIO.read(Authenticator.class.getClassLoader().getResourceAsStream("resources/emp.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			btnForEmployee.setIcon(new ImageIcon(emp_img));
		} catch (Exception ex) {

		}
		frmMainApp.getContentPane().add(btnForEmployee);

		JButton btnForAdmin = new JButton("");
		btnForAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AdminLogin().frmAdminLogin.setVisible(true);
			}
		});
		btnForAdmin.setBounds(395, 146, 81, 87);
		try {
			Image admin_img = ImageIO
					.read(Authenticator.class.getClassLoader().getResourceAsStream("resources/admin.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			btnForAdmin.setIcon(new ImageIcon(admin_img));
		} catch (Exception ex) {

		}
		frmMainApp.getContentPane().add(btnForAdmin);

		JLabel lblForEmployees = new JLabel("Create employee");
		lblForEmployees.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblForEmployees.setHorizontalAlignment(SwingConstants.CENTER);
		lblForEmployees.setBounds(22, 244, 124, 20);
		frmMainApp.getContentPane().add(lblForEmployees);

		JLabel lblForAdmin = new JLabel("Admin menu");
		lblForAdmin.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblForAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		lblForAdmin.setBounds(381, 244, 110, 20);
		frmMainApp.getContentPane().add(lblForAdmin);

		JLabel label_1 = new JLabel("\u00A9 Copyright 2017. Saraswat Infotech Ltd");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(10, 293, 499, 14);
		frmMainApp.getContentPane().add(label_1);

		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AttendanceLogging().frmLogAttendance.setVisible(true);
			}
		});
		button.setBounds(222, 146, 81, 87);
		try {
			Image admin_img = ImageIO
					.read(Authenticator.class.getClassLoader().getResourceAsStream("resources/biometric.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			button.setIcon(new ImageIcon(admin_img));
		} catch (Exception ex) {

		}
		frmMainApp.getContentPane().add(button);

		JLabel lblMarkAttendance = new JLabel("Mark attendance");
		lblMarkAttendance.setHorizontalAlignment(SwingConstants.CENTER);
		lblMarkAttendance.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblMarkAttendance.setBounds(202, 244, 124, 20);
		frmMainApp.getContentPane().add(lblMarkAttendance);

	}
}
