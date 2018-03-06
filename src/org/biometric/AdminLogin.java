package org.biometric;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;

import org.biometric.config.AppConfigInfo;
import org.biometric.crypto.CryptoUtils;
import org.biometric.updation.Authenticator;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminLogin {

	public JFrame frmAdminLogin;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminLogin window = new AdminLogin();
					window.frmAdminLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAdminLogin = new JFrame();
		frmAdminLogin.setTitle("Admin login");
		frmAdminLogin.setBounds(100, 100, 536, 356);
		frmAdminLogin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAdminLogin.getContentPane().setLayout(null);

		JLabel label = new JLabel("");
		label.setBounds(10, 11, 124, 103);
		try {
			label.setIcon(new ImageIcon(
					ImageIO.read(Authenticator.class.getClassLoader().getResourceAsStream("resources/SIL logo.jpg"))
							.getScaledInstance(136, 90, Image.SCALE_SMOOTH)));
		} catch (Exception ex) {

		}
		frmAdminLogin.getContentPane().add(label);

		JLabel label_1 = new JLabel("Biometric Attendance System");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		label_1.setBounds(144, 24, 365, 72);
		frmAdminLogin.getContentPane().add(label_1);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(128, 158, 80, 14);
		frmAdminLogin.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(128, 208, 80, 14);
		frmAdminLogin.getContentPane().add(lblPassword);

		textField = new JTextField();
		textField.setBounds(218, 155, 185, 20);
		frmAdminLogin.getContentPane().add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(218, 205, 185, 20);
		frmAdminLogin.getContentPane().add(passwordField);

		JLabel lblPleaseLoginTo = new JLabel("Please login to continue");
		lblPleaseLoginTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseLoginTo.setBounds(128, 114, 275, 14);
		frmAdminLogin.getContentPane().add(lblPleaseLoginTo);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (textField.getText().equals("")) {
					JOptionPane.showMessageDialog(frmAdminLogin, "Please enter username.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (passwordField.getText().equals("")) {
					JOptionPane.showMessageDialog(frmAdminLogin, "Please enter password.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (textField.getText().equals(AppConfigInfo.getUserName())
							&& passwordField.getText().equals(CryptoUtils.doDecrypt(AppConfigInfo.getPassword()))) {
						new MainScreen().frmBiometricAttendanceSystem.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(frmAdminLogin, "Invalid username or password.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});
		btnLogin.setBounds(218, 253, 89, 23);
		frmAdminLogin.getContentPane().add(btnLogin);

		JLabel label_2 = new JLabel("\u00A9 Copyright 2017. Saraswat Infotech Ltd");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_2.setBounds(10, 304, 499, 14);
		frmAdminLogin.getContentPane().add(label_2);
	}
}
