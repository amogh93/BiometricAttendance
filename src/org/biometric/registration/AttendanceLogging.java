package org.biometric.registration;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.biometric.match.MatchBiometrics;
import javax.swing.UIManager;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;

public class AttendanceLogging extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MatchBiometrics biometrics = new MatchBiometrics();
	public static JFrame frmLogAttendance;
	private static JLabel id_label;
	private static JLabel name_label;
	private static JLabel type_label;
	private static JLabel last_entry_label;
	private static JLabel fingerprint_label;
	private static JLabel status_label;
	private static JButton btnStop, btnStart;
	private static JLabel image_label;
	private JPanel panel_1;
	Thread t;
	private JLabel label;

	/**
	 * Launch the application.
	 */

	public static void enableButton() {
		btnStart.setEnabled(true);
		btnStop.setEnabled(false);
	}

	public static void setIdLabel(String text) {
		id_label.setText(text);
	}

	public static void setNameLabel(String text) {
		name_label.setText(text);
	}

	public static void setTypeLabel(String text) {
		type_label.setText(text);
	}

	public static void setLastEntryLabel(String text) {
		last_entry_label.setText(text);
	}

	public static void setFingerprintLabel(BufferedImage img) {
		fingerprint_label.setIcon(new ImageIcon(img.getScaledInstance(130, 150, Image.SCALE_DEFAULT)));
	}

	public static void setImageLabel(BufferedImage img) {
		image_label.setIcon(new ImageIcon(img.getScaledInstance(130, 150, Image.SCALE_DEFAULT)));
	}

	public static void setStatusLabel(String text) {
		status_label.setText(text);
	}

	public static void clearLabels() {
		id_label.setText("");
		name_label.setText("");
		type_label.setText("");
		last_entry_label.setText("");
		fingerprint_label.setIcon(null);
		image_label.setIcon(null);
		status_label.setText("");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AttendanceLogging window = new AttendanceLogging();
					window.frmLogAttendance.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AttendanceLogging() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmLogAttendance = new JFrame();
		frmLogAttendance.setTitle("Log Attendance ");
		frmLogAttendance.setBounds(100, 100, 590, 366);
		frmLogAttendance.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmLogAttendance.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fingerprint",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(16, 16, 142, 173);
		frmLogAttendance.getContentPane().add(panel);
		panel.setLayout(null);

		fingerprint_label = new JLabel("");
		fingerprint_label.setBounds(6, 16, 130, 150);
		panel.add(fingerprint_label);

		JLabel lblId = new JLabel("ID:");
		lblId.setBounds(177, 26, 80, 14);
		frmLogAttendance.getContentPane().add(lblId);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(177, 59, 80, 14);
		frmLogAttendance.getContentPane().add(lblName);

		JLabel lblType = new JLabel("");
		lblType.setBounds(177, 91, 80, 14);
		frmLogAttendance.getContentPane().add(lblType);

		JLabel lblLastEntry = new JLabel("Entry for:");
		lblLastEntry.setBounds(177, 124, 80, 14);
		frmLogAttendance.getContentPane().add(lblLastEntry);

		id_label = new JLabel("1001");
		id_label.setBounds(246, 26, 142, 14);
		frmLogAttendance.getContentPane().add(id_label);

		name_label = new JLabel("Amogh Kakirde");
		name_label.setBounds(246, 59, 142, 14);
		frmLogAttendance.getContentPane().add(name_label);

		type_label = new JLabel("");
		type_label.setBounds(246, 91, 142, 14);
		frmLogAttendance.getContentPane().add(type_label);

		last_entry_label = new JLabel("In-time");
		last_entry_label.setBounds(246, 124, 142, 14);
		frmLogAttendance.getContentPane().add(last_entry_label);

		status_label = new JLabel("");
		status_label.setBounds(16, 200, 142, 14);
		frmLogAttendance.getContentPane().add(status_label);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t = new Thread(biometrics);
				t.start();
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});
		btnStart.setBounds(177, 244, 89, 23);
		frmLogAttendance.getContentPane().add(btnStart);

		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				t.suspend();
			}
		});
		btnStop.setBounds(292, 244, 89, 23);
		frmLogAttendance.getContentPane().add(btnStop);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Photo", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(415, 16, 142, 173);
		frmLogAttendance.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		image_label = new JLabel("");
		image_label.setBounds(6, 16, 130, 150);
		panel_1.add(image_label);

		label = new JLabel("\u00A9 Copyright 2017. Saraswat Infotech Ltd");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label.setBounds(16, 303, 548, 14);
		frmLogAttendance.getContentPane().add(label);

	}
}
