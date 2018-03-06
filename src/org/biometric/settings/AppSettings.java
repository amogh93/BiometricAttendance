package org.biometric.settings;

import java.awt.EventQueue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.biometric.config.AppConfigInfo;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;
import javax.swing.SwingConstants;

public class AppSettings {

	public JFrame frmSettings;
	private JPanel storage_panel;
	private JPanel work_hours_panel;
	private JTextField report_path;
	private JSpinner full_day_spinner, half_day_spinner;
	private JLabel lblNewLabel_1;
	private JPanel admin_profile_panel;
	private JLabel lblNewPassword;
	private JLabel lblReenterPassword;
	private JCheckBox work_hours_check;
	private JCheckBox admin_profile_check;
	private JCheckBox storage_check;
	private JPasswordField existing_password;
	private JPasswordField new_password;
	private JPasswordField re_entered_password;
	private JButton apply_button;
	private boolean report_status, work_hours_status, admin_password_status;
	private JLabel password_status_label;
	private JLabel label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppSettings window = new AppSettings();
					window.frmSettings.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws Exception
	 */
	public AppSettings() {
		initialize();
		disablePanel();
		disableAdminProfilePanelFields();
		disableReportPanelFields();
		disableWorkHourPanelFields();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws Exception
	 */
	private void initialize() {
		frmSettings = new JFrame();
		frmSettings.setTitle("Settings");
		frmSettings.setBounds(100, 100, 594, 420);
		frmSettings.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmSettings.getContentPane().setLayout(null);

		JLabel lblSettings = new JLabel("Check the appropriate checkbox to change the values");
		lblSettings.setBounds(10, 8, 558, 14);
		frmSettings.getContentPane().add(lblSettings);

		storage_panel = new JPanel();
		storage_panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Storage",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		storage_panel.setBounds(20, 33, 548, 65);
		frmSettings.getContentPane().add(storage_panel);
		storage_panel.setLayout(null);

		JLabel lblDefaultPathfor = new JLabel("Default path (for reports):");
		lblDefaultPathfor.setBounds(10, 27, 149, 14);
		storage_panel.add(lblDefaultPathfor);

		report_path = new JTextField();
		report_path.setText(AppConfigInfo.getDefaultStorage());
		report_path.setBounds(171, 23, 367, 20);
		storage_panel.add(report_path);
		report_path.setColumns(10);

		work_hours_panel = new JPanel();
		work_hours_panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Work-hours",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		work_hours_panel.setBounds(20, 112, 548, 90);
		frmSettings.getContentPane().add(work_hours_panel);
		work_hours_panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Full-day hours (hh:mm:ss):");
		lblNewLabel.setBounds(10, 26, 161, 14);
		work_hours_panel.add(lblNewLabel);

		JLabel lblHalfdayHours = new JLabel("Half-day hours (hh:mm:ss):");
		lblHalfdayHours.setBounds(10, 54, 161, 14);
		work_hours_panel.add(lblHalfdayHours);

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date df = new Date(), dh = new Date();
		try {
			df = sdf.parse(AppConfigInfo.getFullWorkingHours());
			dh = sdf.parse(AppConfigInfo.getHalfWorkingHours());
		} catch (Exception ex) {

		}
		full_day_spinner = new JSpinner(new SpinnerDateModel());
		full_day_spinner.setEditor(new JSpinner.DateEditor(full_day_spinner, sdf.toPattern()));
		full_day_spinner.setValue(df);
		full_day_spinner.setBounds(171, 23, 122, 20);
		work_hours_panel.add(full_day_spinner);

		half_day_spinner = new JSpinner(new SpinnerDateModel());
		half_day_spinner.setEditor(new JSpinner.DateEditor(half_day_spinner, sdf.toPattern()));
		half_day_spinner.setValue(dh);
		half_day_spinner.setBounds(171, 51, 122, 20);
		work_hours_panel.add(half_day_spinner);

		admin_profile_panel = new JPanel();
		admin_profile_panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Admin profile",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		admin_profile_panel.setBounds(20, 211, 548, 123);
		frmSettings.getContentPane().add(admin_profile_panel);
		admin_profile_panel.setLayout(null);

		lblNewLabel_1 = new JLabel("Existing password:");
		lblNewLabel_1.setBounds(10, 28, 151, 14);
		admin_profile_panel.add(lblNewLabel_1);

		lblNewPassword = new JLabel("New password:");
		lblNewPassword.setBounds(10, 54, 151, 14);
		admin_profile_panel.add(lblNewPassword);

		lblReenterPassword = new JLabel("Re-enter password:");
		lblReenterPassword.setBounds(10, 79, 151, 14);
		admin_profile_panel.add(lblReenterPassword);

		existing_password = new JPasswordField();
		existing_password.setBounds(171, 25, 159, 20);
		admin_profile_panel.add(existing_password);

		new_password = new JPasswordField();
		new_password.setBounds(171, 51, 159, 20);
		admin_profile_panel.add(new_password);

		re_entered_password = new JPasswordField();
		re_entered_password.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (!re_entered_password.getText().equals(new_password.getText())) {
					password_status_label.setText("Password does not match.");
				} else if (re_entered_password.getText().equals(new_password.getText())) {
					password_status_label.setText("");
				}

			}
		});
		re_entered_password.setBounds(171, 76, 159, 20);
		admin_profile_panel.add(re_entered_password);

		password_status_label = new JLabel("");
		password_status_label.setBounds(340, 79, 198, 14);
		admin_profile_panel.add(password_status_label);

		storage_check = new JCheckBox("");
		storage_check.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (storage_check.isSelected()) {
					storage_panel.setEnabled(true);
					enableReportPanelFields();
				} else {
					storage_panel.setEnabled(false);
					disableReportPanelFields();
				}
			}
		});
		storage_check.setBounds(0, 56, 21, 23);
		frmSettings.getContentPane().add(storage_check);

		work_hours_check = new JCheckBox("");
		work_hours_check.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (work_hours_check.isSelected()) {
					work_hours_panel.setEnabled(true);
					enableWorkHourPanelFields();
				} else {
					work_hours_panel.setEnabled(false);
					disableWorkHourPanelFields();
				}
			}
		});
		work_hours_check.setBounds(0, 149, 21, 23);
		frmSettings.getContentPane().add(work_hours_check);

		admin_profile_check = new JCheckBox("");
		admin_profile_check.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (admin_profile_check.isSelected()) {
					admin_profile_panel.setEnabled(true);
					enableAdminProfilePanelFields();
				} else {
					admin_profile_panel.setEnabled(false);
					disableAdminProfilePanelFields();
				}
			}
		});
		admin_profile_check.setBounds(0, 263, 21, 23);
		frmSettings.getContentPane().add(admin_profile_check);

		apply_button = new JButton("Apply");
		apply_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (storage_check.isSelected()) {
					if (report_path.getText().equals("")) {
						JOptionPane.showMessageDialog(frmSettings, "Please enter the storage path.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						report_status = UpdateAppSettings.updateStoragePath(report_path.getText());
					}
				}

				if (work_hours_check.isSelected()) {
					if (full_day_spinner.getValue().toString().equals("")) {
						JOptionPane.showMessageDialog(frmSettings, "Please enter the full day hours.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (half_day_spinner.getValue().toString().equals("")) {
						JOptionPane.showMessageDialog(frmSettings, "Please enter the half day hours.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						work_hours_status = UpdateAppSettings.updateWorkigHours((Date) full_day_spinner.getValue(),
								(Date) half_day_spinner.getValue());
					}
				}

				if (admin_profile_check.isSelected()) {
					if (existing_password.getText().equals("")) {
						JOptionPane.showMessageDialog(frmSettings, "Please enter your existing password.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (new_password.getText().equals("")) {
						JOptionPane.showMessageDialog(frmSettings, "Please enter new password.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (re_entered_password.getText().equals("")) {
						JOptionPane.showMessageDialog(frmSettings, "Please re-enter new password.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (!new_password.getText().equals(re_entered_password.getText())) {
						JOptionPane.showMessageDialog(frmSettings, "Re-entered password does not match.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (re_entered_password.getText().equals(existing_password.getText())) {
						JOptionPane.showMessageDialog(frmSettings,
								"New password cannot be same as the existing password.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						admin_password_status = UpdateAppSettings.updateAdminPassword(existing_password.getText(),
								re_entered_password.getText());
						if (!admin_password_status) {
							JOptionPane.showMessageDialog(frmSettings, "Wrong password entered.", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}

				if (report_status || work_hours_status || admin_password_status) {
					JOptionPane.showMessageDialog(frmSettings, "Settings applied successfully.");
				}
			}
		});
		apply_button.setBounds(239, 348, 89, 23);
		frmSettings.getContentPane().add(apply_button);

		label = new JLabel("\u00A9 Copyright 2017. Saraswat Infotech Ltd");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label.setBounds(363, 368, 205, 14);
		frmSettings.getContentPane().add(label);
	}

	private void disablePanel() {
		storage_panel.setEnabled(false);
		work_hours_panel.setEnabled(false);
		admin_profile_panel.setEnabled(false);
	}

	private void disableReportPanelFields() {
		report_path.setEnabled(false);
	}

	private void enableReportPanelFields() {
		report_path.setEnabled(true);
	}

	private void disableWorkHourPanelFields() {
		full_day_spinner.setEnabled(false);
		half_day_spinner.setEnabled(false);
	}

	private void enableWorkHourPanelFields() {
		full_day_spinner.setEnabled(true);
		half_day_spinner.setEnabled(true);
	}

	private void disableAdminProfilePanelFields() {
		existing_password.setEnabled(false);
		new_password.setEnabled(false);
		re_entered_password.setEnabled(false);
	}

	private void enableAdminProfilePanelFields() {
		existing_password.setEnabled(true);
		new_password.setEnabled(true);
		re_entered_password.setEnabled(true);
	}

}
