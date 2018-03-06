package org.biometric;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.biometric.report.GeneratePDF;
import org.biometric.settings.AppSettings;
import org.biometric.updation.Authenticator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;

public class MainScreen {

	public JFrame frmBiometricAttendanceSystem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen();
					window.frmBiometricAttendanceSystem.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBiometricAttendanceSystem = new JFrame();
		frmBiometricAttendanceSystem.setTitle("BioAttendance Admin");
		frmBiometricAttendanceSystem.setBounds(100, 100, 536, 356);
		frmBiometricAttendanceSystem.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmBiometricAttendanceSystem.getContentPane().setLayout(null);

		JButton btnGenerateReport = new JButton("");
		btnGenerateReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new GeneratePDF().frmGenerateAttendanceReport.setVisible(true);
			}
		});
		btnGenerateReport.setBounds(45, 146, 81, 87);
		try {
			Image emp_img = ImageIO
					.read(Authenticator.class.getClassLoader().getResourceAsStream("resources/report.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			btnGenerateReport.setIcon(new ImageIcon(emp_img));
		} catch (Exception ex) {

		}
		frmBiometricAttendanceSystem.getContentPane().add(btnGenerateReport);

		JButton btnSettings = new JButton("");
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AppSettings().frmSettings.setVisible(true);
			}
		});
		btnSettings.setBounds(395, 146, 81, 87);
		try {
			Image emp_img = ImageIO
					.read(Authenticator.class.getClassLoader().getResourceAsStream("resources/settings.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			btnSettings.setIcon(new ImageIcon(emp_img));
		} catch (Exception ex) {

		}
		frmBiometricAttendanceSystem.getContentPane().add(btnSettings);

		JButton btnUpdateEmployee = new JButton("");
		btnUpdateEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Authenticator().frmUpdateEmployee.setVisible(true);
			}
		});
		try {
			Image emp_img = ImageIO.read(Authenticator.class.getClassLoader().getResourceAsStream("resources/edit.png"))
					.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
			btnUpdateEmployee.setIcon(new ImageIcon(emp_img));
		} catch (Exception ex) {

		}
		btnUpdateEmployee.setBounds(222, 146, 81, 87);
		frmBiometricAttendanceSystem.getContentPane().add(btnUpdateEmployee);

		JLabel label = new JLabel("");
		label.setBounds(10, 11, 124, 103);
		try {
			label.setIcon(new ImageIcon(
					ImageIO.read(Authenticator.class.getClassLoader().getResourceAsStream("resources/SIL logo.jpg"))
							.getScaledInstance(136, 90, Image.SCALE_SMOOTH)));
		} catch (Exception ex) {

		}
		frmBiometricAttendanceSystem.getContentPane().add(label);

		JLabel lblAdminMenu = new JLabel("Admin menu");
		lblAdminMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblAdminMenu.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		lblAdminMenu.setBounds(144, 24, 365, 72);
		frmBiometricAttendanceSystem.getContentPane().add(lblAdminMenu);

		JLabel label_1 = new JLabel("\u00A9 Copyright 2017. Saraswat Infotech Ltd");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_1.setBounds(10, 293, 499, 14);
		frmBiometricAttendanceSystem.getContentPane().add(label_1);

		JLabel lblGenerateReport = new JLabel("Generate report");
		lblGenerateReport.setHorizontalAlignment(SwingConstants.CENTER);
		lblGenerateReport.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblGenerateReport.setBounds(23, 238, 124, 20);
		frmBiometricAttendanceSystem.getContentPane().add(lblGenerateReport);

		JLabel lblUpdateEmployee = new JLabel("Update employee");
		lblUpdateEmployee.setHorizontalAlignment(SwingConstants.CENTER);
		lblUpdateEmployee.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblUpdateEmployee.setBounds(201, 238, 124, 20);
		frmBiometricAttendanceSystem.getContentPane().add(lblUpdateEmployee);

		JLabel lblSettings = new JLabel("Settings");
		lblSettings.setHorizontalAlignment(SwingConstants.CENTER);
		lblSettings.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
		lblSettings.setBounds(374, 238, 124, 20);
		frmBiometricAttendanceSystem.getContentPane().add(lblSettings);
	}
}
