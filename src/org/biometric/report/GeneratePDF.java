package org.biometric.report;

import java.awt.EventQueue;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;

import org.biometric.config.AppConfigInfo;
import org.biometric.directory.CheckDIR;
import org.biometric.persist.model.Employee;
import org.biometric.time.WorkingHours;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Font;

public class GeneratePDF {

	public JFrame frmGenerateAttendanceReport;
	private JRadioButton summarized_radio, detailed_radio;
	private JCheckBox particular_employee_checkbox;
	private JComboBox employee_combo_box;
	private JButton btnGenerate;
	private JLabel employee_label;
	private JLabel lblyyyymmdd;
	private JLabel label;
	private DatePicker from_date_picker, to_date_picker;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GeneratePDF window = new GeneratePDF();
					window.frmGenerateAttendanceReport.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GeneratePDF() {
		initialize();
		employee_label.setEnabled(false);
		employee_combo_box.setEnabled(false);

		lblyyyymmdd = new JLabel("(yyyy-mm-dd)");
		lblyyyymmdd.setBounds(270, 36, 104, 14);
		frmGenerateAttendanceReport.getContentPane().add(lblyyyymmdd);

		label = new JLabel("(yyyy-mm-dd)");
		label.setBounds(270, 70, 104, 14);
		frmGenerateAttendanceReport.getContentPane().add(label);

		JLabel label_1 = new JLabel("\u00A9 Copyright 2017. Saraswat Infotech Ltd");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_1.setBounds(10, 248, 414, 14);
		frmGenerateAttendanceReport.getContentPane().add(label_1);
		loadComboBoxData();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGenerateAttendanceReport = new JFrame();
		frmGenerateAttendanceReport.setTitle("Generate Attendance Report");
		frmGenerateAttendanceReport.setBounds(100, 100, 450, 300);
		frmGenerateAttendanceReport.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmGenerateAttendanceReport.getContentPane().setLayout(null);

		JLabel lblEnterTheFollowing = new JLabel("Enter the following details to generate the report");
		lblEnterTheFollowing.setBounds(10, 11, 414, 14);
		frmGenerateAttendanceReport.getContentPane().add(lblEnterTheFollowing);

		JLabel lblFrom = new JLabel("From:");
		lblFrom.setBounds(10, 42, 58, 14);
		frmGenerateAttendanceReport.getContentPane().add(lblFrom);

		JLabel lblTo = new JLabel("To:");
		lblTo.setBounds(10, 73, 58, 14);
		frmGenerateAttendanceReport.getContentPane().add(lblTo);

		DatePickerSettings setting = new DatePickerSettings();
		setting.setFormatForDatesCommonEra("yyyy-MM-dd");
		setting.getEnableYearMenu();
		setting.getEnableMonthMenu();

		from_date_picker = new DatePicker(setting);
		from_date_picker.setDateToToday();
		from_date_picker.setBounds(109, 36, 140, 20);
		frmGenerateAttendanceReport.getContentPane().add(from_date_picker);

		setting = new DatePickerSettings();
		setting.setFormatForDatesCommonEra("yyyy-MM-dd");
		setting.getEnableYearMenu();
		setting.getEnableMonthMenu();

		to_date_picker = new DatePicker(setting);
		to_date_picker.setDateToToday();
		to_date_picker.setBounds(109, 70, 140, 20);
		frmGenerateAttendanceReport.getContentPane().add(to_date_picker);

		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(10, 103, 58, 14);
		frmGenerateAttendanceReport.getContentPane().add(lblType);

		summarized_radio = new JRadioButton("Summarized");
		summarized_radio.setSelected(true);
		summarized_radio.setBounds(104, 97, 104, 23);
		frmGenerateAttendanceReport.getContentPane().add(summarized_radio);

		detailed_radio = new JRadioButton("Detailed");
		detailed_radio.setBounds(206, 97, 89, 23);
		frmGenerateAttendanceReport.getContentPane().add(detailed_radio);

		ButtonGroup myButtonGroup = new ButtonGroup();
		myButtonGroup.add(summarized_radio);
		myButtonGroup.add(detailed_radio);

		particular_employee_checkbox = new JCheckBox("Generate for particular employee");
		particular_employee_checkbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (particular_employee_checkbox.isSelected()) {
					employee_label.setEnabled(true);
					employee_combo_box.setEnabled(true);
				} else {
					employee_label.setEnabled(false);
					employee_combo_box.setEnabled(false);
				}
			}
		});
		particular_employee_checkbox.setBounds(6, 135, 414, 23);
		frmGenerateAttendanceReport.getContentPane().add(particular_employee_checkbox);

		employee_label = new JLabel("Employee ID:");
		employee_label.setBounds(10, 168, 89, 14);
		frmGenerateAttendanceReport.getContentPane().add(employee_label);

		employee_combo_box = new JComboBox();
		employee_combo_box.setBounds(109, 165, 218, 20);
		frmGenerateAttendanceReport.getContentPane().add(employee_combo_box);

		btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Date from_date = java.sql.Date.valueOf(from_date_picker.getDate());
				Date to_date = java.sql.Date.valueOf(to_date_picker.getDate());

				if (!CheckDIR.isExists()) {
					CheckDIR.makeDir();
				}

				try {
					if (from_date.compareTo(to_date) > 0) {
						JOptionPane.showMessageDialog(frmGenerateAttendanceReport,
								"From date must come before to date.", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						if (particular_employee_checkbox.isSelected()) {
							String temp[] = employee_combo_box.getSelectedItem().toString().split("-");
							String emp_id = temp[0];
							try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
									Session session = factory.openSession()) {
								Employee employee = (Employee) session.load(Employee.class, emp_id);
								if (summarized_radio.isSelected()) {
									try {
										WorkingHours.calculateWorkingHours();
										GenerateReport.getReport(from_date, to_date, employee, session);
									} catch (Exception ex) {
										ex.printStackTrace();
									}

								} else if (detailed_radio.isSelected()) {
									try {
										WorkingHours.calculateWorkingHours();

										ReportGenerator.generateReport(from_date, to_date, employee, session);
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							}
						} else {
							if (summarized_radio.isSelected()) {
								try {
									GenerateReport.getReport(from_date, to_date);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							} else if (detailed_radio.isSelected()) {
								try {
									ReportGenerator.generateReport(from_date, to_date);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
						JOptionPane.showMessageDialog(frmGenerateAttendanceReport,
								"Attendance report generated at " + AppConfigInfo.getDefaultStorage());
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frmGenerateAttendanceReport, "Failed to generate attendance report.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnGenerate.setBounds(169, 206, 89, 23);
		frmGenerateAttendanceReport.getContentPane().add(btnGenerate);
	}

	private void loadComboBoxData() {
		try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
				Session session = factory.openSession()) {
			DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
			Projection projection1 = Projections.property("id");
			Projection projection2 = Projections.property("name");
			ProjectionList pList = Projections.projectionList();
			pList.add(projection1);
			pList.add(projection2);
			criteria.setProjection(pList);
			List list = criteria.getExecutableCriteria(session).list();
			if (list.size() > 0) {
				Iterator itr = list.iterator();
				while (itr.hasNext()) {
					Object[] emp_data = (Object[]) itr.next();
					employee_combo_box.addItem(emp_data[0] + "-" + emp_data[1]);
				}
			}
		}
	}
}
