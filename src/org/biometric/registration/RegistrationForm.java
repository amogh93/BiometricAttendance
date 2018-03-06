package org.biometric.registration;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import SecuGen.FDxSDKPro.jni.JSGFPLib;
import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.jni.SGFDxDeviceName;
import SecuGen.FDxSDKPro.jni.SGFDxErrorCode;
import SecuGen.FDxSDKPro.jni.SGFingerInfo;
import SecuGen.FDxSDKPro.jni.SGImpressionType;
import SecuGen.FDxSDKPro.jni.SGPPPortAddr;

import javax.swing.UIManager;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPasswordField;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.biometric.persist.SaveEmployeeInformation;
import org.biometric.persist.SaveTemplate;
import org.biometric.persist.model.Department;
import org.biometric.persist.model.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.imgscalr.Scalr;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.event.ChangeEvent;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;

import org.biometric.crypto.CryptoUtils;
import org.biometric.fingers.FingerNumber;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Font;
import javax.swing.SwingConstants;

public class RegistrationForm {

	static long ret;
	static long err;
	static final long TIMEOUT = 10000;
	static final long QUALITY = 60;
	private static SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
	private byte[] regMin1 = new byte[400];
	private byte[] regMin2 = new byte[400];
	private byte[] regMin3 = new byte[400];
	private byte[] regMin4 = new byte[400];
	private byte[] regMin5 = new byte[400];
	private byte[] emp_img;
	BufferedImage img1_1 = null;
	BufferedImage img1_2 = null;
	BufferedImage img1_3 = null;
	BufferedImage img1_4 = null;
	BufferedImage img1_5 = null;
	BufferedImage img2 = null;
	static JSGFPLib fplib = null;
	static SGFingerInfo fingerInfo = null;
	static boolean isCaptured1 = false;
	static boolean isCaptured2 = false;
	static boolean isCaptured3 = false;
	static boolean isCaptured4 = false;
	static boolean isCaptured5 = false;
	static boolean isPhotoSelected = false;
	static boolean doNotUploadPhoto = false;
	static boolean isDuplicateId = false;
	private int fp1, fp2, fp3, fp4, fp5;

	private DatePicker dob_picker, joining_date_picker;
	public JFrame frmNewRegistrationForm;
	private JTextField emp_id;
	private JTextField emp_name;
	private JTextField emp_contact;
	private JTextField emp_email;
	private JTextField emp_username;
	private JPasswordField emp_password;
	private JFileChooser fileChooser;
	private JLabel image_label;
	private JLabel status_message;
	private JLabel fingerprint_label;
	private JLabel fingerprint_label1;
	private JLabel fingerprint_label2;
	private JLabel fingerprint_label3;
	private JLabel fingerprint_label4;
	private JComboBox fingpos1, fingpos2, fingpos3, fingpos4, fingpos5;
	private JCheckBox no_photo_check;
	private JButton btnRegister;
	private DateFormat date_format = new SimpleDateFormat("yyyy-MM-ss");
	private JComboBox deptBox;
	private JCheckBox auto_generate;
	private JRadioButton rdbtnMale, rdbtnFemale;
	private boolean isRecordExist = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrationForm window = new RegistrationForm();
					window.frmNewRegistrationForm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RegistrationForm() {
		initialize();
		try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
				Session session = factory.openSession()) {
			session.beginTransaction();
			DetachedCriteria criteria = DetachedCriteria.forClass(Department.class);
			criteria.addOrder(Order.asc("id"));
			List dept_list = criteria.getExecutableCriteria(session).list();
			if (dept_list.size() > 0) {
				Iterator dept_itr = dept_list.iterator();
				while (dept_itr.hasNext()) {
					Department d = (Department) dept_itr.next();
					deptBox.addItem(d.getDepartment_name());
				}
			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNewRegistrationForm = new JFrame();
		frmNewRegistrationForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmNewRegistrationForm.setTitle("New Registration Form");
		frmNewRegistrationForm.setExtendedState(JFrame.MAXIMIZED_HORIZ);
		frmNewRegistrationForm.setBounds(100, 100, 1223, 576);
		frmNewRegistrationForm.getContentPane().setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fingerprint 1",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(440, 51, 142, 173);
		frmNewRegistrationForm.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		fingerprint_label = new JLabel("");
		fingerprint_label.setBounds(6, 16, 130, 150);
		panel_1.add(fingerprint_label);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fingerprint 2",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(592, 51, 142, 173);
		frmNewRegistrationForm.getContentPane().add(panel_2);
		panel_2.setLayout(null);

		fingerprint_label1 = new JLabel("");
		fingerprint_label1.setBounds(6, 16, 130, 150);
		panel_2.add(fingerprint_label1);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fingerprint 3",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(744, 51, 142, 173);
		frmNewRegistrationForm.getContentPane().add(panel_3);
		panel_3.setLayout(null);

		fingerprint_label2 = new JLabel("");
		fingerprint_label2.setBounds(6, 16, 130, 150);
		panel_3.add(fingerprint_label2);

		JLabel status = new JLabel("Status:");
		status.setBounds(0, 524, 46, 14);
		frmNewRegistrationForm.getContentPane().add(status);

		status_message = new JLabel("");
		status_message.setBounds(43, 524, 388, 14);
		frmNewRegistrationForm.getContentPane().add(status_message);
		status_message.setVisible(false);

		JButton capture_fingerprint = new JButton("Capture");
		capture_fingerprint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (capture(img1_1, regMin1, fingerprint_label, fingpos1.getSelectedIndex())) {
					isCaptured1 = true;
					fp1 = fingpos1.getSelectedIndex();
				}
			}
		});
		capture_fingerprint.setBounds(445, 235, 137, 23);
		frmNewRegistrationForm.getContentPane().add(capture_fingerprint);

		JLabel lblEmployeeId = new JLabel("Employee ID:");
		lblEmployeeId.setBounds(10, 47, 91, 14);
		frmNewRegistrationForm.getContentPane().add(lblEmployeeId);

		emp_id = new JTextField();
		emp_id.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
						Session session = factory.openSession()) {
					DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
					criteria.add(Restrictions.eq("id", emp_id.getText()));
					List list = criteria.getExecutableCriteria(session).list();
					if (list.size() > 0) {
						isRecordExist = true;
						Employee employee = (Employee) list.iterator().next();
						emp_id.setEditable(false);
						emp_name.setText(employee.getName());
						emp_name.setEditable(false);
						dob_picker.setText(employee.getDateOfBirth().toString());
						joining_date_picker.setText(employee.getJoiningDate().toString());
						emp_contact.setText(employee.getContactNumber());
						emp_email.setText(employee.getEmail());
						if (employee.getGender() == 'M') {
							rdbtnMale.setSelected(true);
							rdbtnFemale.setSelected(false);
						} else if (employee.getGender() == 'F') {
							rdbtnMale.setSelected(false);
							rdbtnFemale.setSelected(true);
						}
						rdbtnFemale.setEnabled(false);
						rdbtnMale.setEnabled(false);

						deptBox.setSelectedItem(employee.getDepartment().getDepartment_name());
						deptBox.setEnabled(false);
						emp_username.setText(employee.getEmployeeLogin().getUserName());
						emp_password.setText(CryptoUtils.doDecrypt(employee.getEmployeeLogin().getPassword()));
						dob_picker.setEnabled(false);
						joining_date_picker.setEnabled(false);
						auto_generate.setEnabled(false);
						emp_username.setEditable(false);
						emp_password.setEditable(false);
					} else {
						btnRegister.setEnabled(true);
					}
				}
			}
		});
		emp_id.setBounds(111, 44, 264, 20);
		frmNewRegistrationForm.getContentPane().add(emp_id);
		emp_id.setColumns(10);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 84, 91, 14);
		frmNewRegistrationForm.getContentPane().add(lblName);

		emp_name = new JTextField();
		emp_name.setBounds(111, 81, 264, 20);
		frmNewRegistrationForm.getContentPane().add(emp_name);
		emp_name.setColumns(10);

		JLabel lblDob = new JLabel("DOB:");
		lblDob.setBounds(10, 121, 91, 14);
		frmNewRegistrationForm.getContentPane().add(lblDob);

		JLabel lblJoiningDate = new JLabel("Joining date:");
		lblJoiningDate.setBounds(10, 154, 91, 14);
		frmNewRegistrationForm.getContentPane().add(lblJoiningDate);

		JLabel lblContactNo = new JLabel("Contact no:");
		lblContactNo.setBounds(10, 188, 91, 14);
		frmNewRegistrationForm.getContentPane().add(lblContactNo);

		emp_contact = new JTextField();
		emp_contact.setDocument(new JTextFieldLimit(10));
		emp_contact.setBounds(111, 185, 264, 20);
		frmNewRegistrationForm.getContentPane().add(emp_contact);
		emp_contact.setColumns(10);

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(10, 227, 91, 14);
		frmNewRegistrationForm.getContentPane().add(lblEmail);

		emp_email = new JTextField();
		emp_email.setBounds(111, 224, 264, 20);
		frmNewRegistrationForm.getContentPane().add(emp_email);
		emp_email.setColumns(10);

		auto_generate = new JCheckBox("Auto generate username and password (for attendance-portal)");
		auto_generate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (auto_generate.isSelected()) {
					emp_username.setEditable(false);
					emp_password.setEditable(false);

				} else if (!auto_generate.isSelected()) {
					emp_username.setEditable(true);
					emp_password.setEditable(true);
				}
			}
		});
		auto_generate.setBounds(10, 337, 397, 23);
		frmNewRegistrationForm.getContentPane().add(auto_generate);

		JPanel auto_generate_panel = new JPanel();
		auto_generate_panel.setBorder(null);
		auto_generate_panel.setBounds(10, 367, 371, 82);
		frmNewRegistrationForm.getContentPane().add(auto_generate_panel);
		auto_generate_panel.setLayout(null);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(0, 11, 81, 14);
		auto_generate_panel.add(lblUsername);

		emp_username = new JTextField();
		emp_username.setBounds(101, 8, 264, 20);
		auto_generate_panel.add(emp_username);
		emp_username.setColumns(10);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(0, 47, 91, 14);
		auto_generate_panel.add(lblPassword);

		emp_password = new JPasswordField();
		emp_password.setBounds(101, 44, 264, 20);
		auto_generate_panel.add(emp_password);

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setBounds(111, 261, 65, 23);
		rdbtnMale.setSelected(true);
		frmNewRegistrationForm.getContentPane().add(rdbtnMale);

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setBounds(178, 261, 81, 23);
		frmNewRegistrationForm.getContentPane().add(rdbtnFemale);

		ButtonGroup myButtonGroup = new ButtonGroup();
		myButtonGroup.add(rdbtnMale);
		myButtonGroup.add(rdbtnFemale);

		btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (isRecordExist) {
					try (SessionFactory factory = new Configuration().configure().buildSessionFactory();
							Session session = factory.openSession()) {
						session.beginTransaction();
						Employee employee = (Employee) session.load(Employee.class, emp_id.getText());
						if (isCaptured1 && isCaptured2 && isCaptured3 && isCaptured4 && isCaptured5) {
							if (isPhotoSelected && !doNotUploadPhoto) {
								SaveTemplate.setEmployee(employee, regMin1, fp1);
								SaveTemplate.setEmployee(employee, regMin1, fp2);
								SaveTemplate.setEmployee(employee, regMin1, fp3);
								SaveTemplate.setEmployee(employee, regMin1, fp4);
								SaveTemplate.setEmployee(employee, regMin1, fp5);
								employee.setImage(emp_img);
								session.save(employee);
								session.getTransaction().commit();
								JOptionPane.showMessageDialog(frmNewRegistrationForm, "Biometric registration done.",
										"Registration successful", JOptionPane.INFORMATION_MESSAGE);
								clearFields();
							} else if (!isPhotoSelected && doNotUploadPhoto) {
								status_message.setText(" Please wait...");

								SaveTemplate.setEmployee(employee, regMin1, fp1);
								SaveTemplate.setEmployee(employee, regMin1, fp2);
								SaveTemplate.setEmployee(employee, regMin1, fp3);
								SaveTemplate.setEmployee(employee, regMin1, fp4);
								SaveTemplate.setEmployee(employee, regMin1, fp5);
								session.save(employee);
								session.getTransaction().commit();

								JOptionPane.showMessageDialog(frmNewRegistrationForm, "Biometric registration done.",
										"Registration successful", JOptionPane.INFORMATION_MESSAGE);
								clearFields();
							} else {
								JOptionPane.showMessageDialog(frmNewRegistrationForm, "Please upload your photo.",
										"Error", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(frmNewRegistrationForm,
									"Please capture all the five fingerprints.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					if (emp_id.getText().equals("")) {
						JOptionPane.showMessageDialog(frmNewRegistrationForm, "Please enter employee id.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (emp_id.getText().indexOf("-") != -1) {
						JOptionPane.showMessageDialog(frmNewRegistrationForm, "'-' not allowed in employee id.",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else if (emp_name.getText().equals("")) {
						JOptionPane.showMessageDialog(frmNewRegistrationForm, "Please enter employee name.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (emp_contact.getText().equals("")) {
						JOptionPane.showMessageDialog(frmNewRegistrationForm, "Please enter contact number.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (emp_email.getText().equals("")) {
						JOptionPane.showMessageDialog(frmNewRegistrationForm, "Please enter email address.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else if (!Pattern.matches("[0-9]{10}", emp_contact.getText())) {
						JOptionPane.showMessageDialog(frmNewRegistrationForm, "Incorrect contact number format.",
								"Error", JOptionPane.ERROR_MESSAGE);
					} else if (!Pattern.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
							+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", emp_email.getText())) {
						JOptionPane.showMessageDialog(frmNewRegistrationForm, "Incorrect email format.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						try {
							String id = emp_id.getText().toString();
							String name = emp_name.getText().toString();
							Date dob = java.sql.Date.valueOf(dob_picker.getDate());
							Date joining_date = java.sql.Date.valueOf(joining_date_picker.getDate());
							String contact = emp_contact.getText().toString();
							String email = emp_email.getText().toString();
							char gender = 'M';
							if (rdbtnMale.isSelected()) {
								gender = 'M';
							} else if (rdbtnFemale.isSelected()) {
								gender = 'F';
							}
							if (!auto_generate.isSelected()) {
								if (emp_username.getText().equals("")) {
									JOptionPane.showMessageDialog(frmNewRegistrationForm,
											"Please enter username for attendance portal.", "Error",
											JOptionPane.ERROR_MESSAGE);
								} else if (emp_password.getText().equals("")) {
									JOptionPane.showMessageDialog(frmNewRegistrationForm,
											"Please enter password for attendance portal.", "Error",
											JOptionPane.ERROR_MESSAGE);
								} else {
									String user_name = emp_username.getText().toString();
									String password = emp_password.getText().toString();
									if (isCaptured1 && isCaptured2 && isCaptured3 && isCaptured4 && isCaptured5) {
										if (isPhotoSelected && !doNotUploadPhoto) {
											status_message.setText(" Please wait...");
											String message = SaveEmployeeInformation.saveToDb(id, name, dob,
													joining_date, contact, email, user_name, password, regMin1, regMin2,
													regMin3, regMin4, regMin5, emp_img, gender, fp1, fp2, fp3, fp4, fp5,
													deptBox.getSelectedIndex());
											JOptionPane.showMessageDialog(frmNewRegistrationForm, message,
													"Registration successful", JOptionPane.INFORMATION_MESSAGE);
											clearFields();
										} else if (!isPhotoSelected && doNotUploadPhoto) {
											status_message.setText(" Please wait...");
											String message = SaveEmployeeInformation.saveToDb(id, name, dob,
													joining_date, contact, email, user_name, password, regMin1, regMin2,
													regMin3, regMin4, regMin5, gender, fp1, fp2, fp3, fp4, fp5,
													deptBox.getSelectedIndex());
											JOptionPane.showMessageDialog(frmNewRegistrationForm, message,
													"Registration successful", JOptionPane.INFORMATION_MESSAGE);
											clearFields();
										} else {
											JOptionPane.showMessageDialog(frmNewRegistrationForm,
													"Please upload your photo.", "Error", JOptionPane.ERROR_MESSAGE);
										}
									} else {
										JOptionPane.showMessageDialog(frmNewRegistrationForm,
												"Please capture all the five fingerprints.", "Error",
												JOptionPane.ERROR_MESSAGE);
									}
								}
							} else {
								if (isCaptured1 && isCaptured2 && isCaptured3 && isCaptured4 && isCaptured5) {
									if (isPhotoSelected && !doNotUploadPhoto) {
										status_message.setText(" Please wait...");
										String message = SaveEmployeeInformation.saveToDb(id, name, dob, joining_date,
												contact, email, regMin1, regMin2, regMin3, regMin4, regMin5, emp_img,
												gender, fp1, fp2, fp3, fp4, fp5, deptBox.getSelectedIndex());
										JOptionPane.showMessageDialog(frmNewRegistrationForm, message,
												"Registration successful", JOptionPane.INFORMATION_MESSAGE);
										clearFields();
									} else if (!isPhotoSelected && doNotUploadPhoto) {
										status_message.setText(" Please wait...");
										String message = SaveEmployeeInformation.saveToDb(id, name, dob, joining_date,
												contact, email, regMin1, regMin2, regMin3, regMin4, regMin5, gender,
												fp1, fp2, fp3, fp4, fp5, deptBox.getSelectedIndex());
										JOptionPane.showMessageDialog(frmNewRegistrationForm, message,
												"Registration successful", JOptionPane.INFORMATION_MESSAGE);
										clearFields();
									} else {
										JOptionPane.showMessageDialog(frmNewRegistrationForm,
												"Please upload your photo.", "Error", JOptionPane.ERROR_MESSAGE);
									}

								} else {
									JOptionPane.showMessageDialog(frmNewRegistrationForm,
											"Please capture all the five fingerprints.", "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(frmNewRegistrationForm, ex.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}

					}
				}
			}
		});
		btnRegister.setBounds(81, 471, 89, 23);
		frmNewRegistrationForm.getContentPane().add(btnRegister);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Photo", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(440, 322, 142, 173);
		frmNewRegistrationForm.getContentPane().add(panel);
		panel.setLayout(null);

		image_label = new JLabel("");
		image_label.setBounds(6, 16, 130, 150);
		panel.add(image_label);

		JButton btnUploadPhoto = new JButton("Choose photo");
		btnUploadPhoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frmNewRegistrationForm);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						img2 = ImageIO.read(selectedFile);
						img2 = Scalr.resize(img2, 300, 400);
						image_label.setIcon(new ImageIcon(img2.getScaledInstance(130, 150, Image.SCALE_DEFAULT)));
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(img2, "jpeg", baos);
						baos.flush();
						emp_img = baos.toByteArray();
						baos.close();
						isPhotoSelected = true;
					} catch (Exception ex) {

					}
				} else {
					isPhotoSelected = false;
				}
			}
		});
		btnUploadPhoto.setBounds(440, 504, 138, 23);
		frmNewRegistrationForm.getContentPane().add(btnUploadPhoto);

		JLabel lblGender = new JLabel("Gender:");
		lblGender.setBounds(10, 265, 46, 14);
		frmNewRegistrationForm.getContentPane().add(lblGender);

		JButton button = new JButton("Capture");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (capture(img1_2, regMin2, fingerprint_label1, fingpos2.getSelectedIndex())) {
					isCaptured2 = true;
					fp2 = fingpos2.getSelectedIndex();
				}
			}
		});
		button.setBounds(597, 235, 137, 23);
		frmNewRegistrationForm.getContentPane().add(button);

		JButton button_1 = new JButton("Capture");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (capture(img1_3, regMin3, fingerprint_label2, fingpos3.getSelectedIndex())) {
					isCaptured3 = true;
					fp3 = fingpos3.getSelectedIndex();
				}
			}
		});
		button_1.setBounds(749, 235, 137, 23);
		frmNewRegistrationForm.getContentPane().add(button_1);

		ArrayList<DateTimeFormatter> date_fromatter = new ArrayList<DateTimeFormatter>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		date_fromatter.add(formatter);

		DatePickerSettings setting = new DatePickerSettings();
		setting.setFormatForDatesCommonEra("yyyy-MM-dd");
		setting.getEnableYearMenu();
		setting.getEnableMonthMenu();
		setting.setFormatsForParsing(date_fromatter);

		dob_picker = new DatePicker(setting);
		dob_picker.setDateToToday();
		dob_picker.setBounds(111, 112, 140, 20);
		frmNewRegistrationForm.getContentPane().add(dob_picker);

		setting = new DatePickerSettings();
		setting.setFormatForDatesCommonEra("yyyy-MM-dd");
		setting.getEnableYearMenu();
		setting.getEnableMonthMenu();

		joining_date_picker = new DatePicker(setting);
		joining_date_picker.setDateToToday();
		joining_date_picker.setBounds(111, 146, 140, 20);
		frmNewRegistrationForm.getContentPane().add(joining_date_picker);

		JLabel lblyyyymmdd = new JLabel("(yyyy-mm-dd)");
		lblyyyymmdd.setBounds(258, 121, 90, 14);
		frmNewRegistrationForm.getContentPane().add(lblyyyymmdd);

		JLabel label = new JLabel("(yyyy-mm-dd)");
		label.setBounds(258, 148, 90, 14);
		frmNewRegistrationForm.getContentPane().add(label);

		JPanel panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fingerprint 4",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.setBounds(896, 51, 142, 173);
		frmNewRegistrationForm.getContentPane().add(panel_4);

		fingerprint_label3 = new JLabel("");
		fingerprint_label3.setBounds(6, 16, 130, 150);
		panel_4.add(fingerprint_label3);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fingerprint 5",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_5.setBounds(1048, 51, 142, 173);
		frmNewRegistrationForm.getContentPane().add(panel_5);

		fingerprint_label4 = new JLabel("");
		fingerprint_label4.setBounds(6, 16, 130, 150);
		panel_5.add(fingerprint_label4);

		JButton button_2 = new JButton("Capture");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (capture(img1_4, regMin4, fingerprint_label3, fingpos4.getSelectedIndex())) {
					isCaptured4 = true;
					fp4 = fingpos4.getSelectedIndex();
				}
			}
		});
		button_2.setBounds(901, 235, 137, 23);
		frmNewRegistrationForm.getContentPane().add(button_2);

		JButton button_3 = new JButton("Capture");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (capture(img1_5, regMin5, fingerprint_label4, fingpos5.getSelectedIndex())) {
					isCaptured5 = true;
					fp5 = fingpos5.getSelectedIndex();
				}
			}
		});
		button_3.setBounds(1053, 235, 137, 23);
		frmNewRegistrationForm.getContentPane().add(button_3);

		JLabel lblFillTheFollowing = new JLabel("Fill the following details to create new employee.");
		lblFillTheFollowing.setBounds(10, 11, 365, 14);
		frmNewRegistrationForm.getContentPane().add(lblFillTheFollowing);

		fingpos1 = new JComboBox();
		fingpos1.setModel(new DefaultComboBoxModel(FingerNumber.values()));
		fingpos1.setBounds(440, 20, 142, 20);
		frmNewRegistrationForm.getContentPane().add(fingpos1);

		fingpos2 = new JComboBox();
		fingpos2.setModel(new DefaultComboBoxModel(FingerNumber.values()));
		fingpos2.setBounds(592, 20, 142, 20);
		frmNewRegistrationForm.getContentPane().add(fingpos2);

		fingpos3 = new JComboBox();
		fingpos3.setModel(new DefaultComboBoxModel(FingerNumber.values()));
		fingpos3.setBounds(744, 20, 142, 20);
		frmNewRegistrationForm.getContentPane().add(fingpos3);

		fingpos4 = new JComboBox();
		fingpos4.setModel(new DefaultComboBoxModel(FingerNumber.values()));
		fingpos4.setBounds(896, 20, 142, 20);
		frmNewRegistrationForm.getContentPane().add(fingpos4);

		fingpos5 = new JComboBox();
		fingpos5.setModel(new DefaultComboBoxModel(FingerNumber.values()));
		fingpos5.setBounds(1048, 20, 142, 20);
		frmNewRegistrationForm.getContentPane().add(fingpos5);

		no_photo_check = new JCheckBox("Do not upload photo");
		no_photo_check.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (no_photo_check.isSelected()) {
					panel.setEnabled(false);
					image_label.setEnabled(false);
					btnUploadPhoto.setEnabled(false);
					doNotUploadPhoto = true;
				} else {
					panel.setEnabled(true);
					image_label.setEnabled(true);
					btnUploadPhoto.setEnabled(true);
					doNotUploadPhoto = false;
				}
			}
		});
		no_photo_check.setBounds(438, 292, 296, 23);
		frmNewRegistrationForm.getContentPane().add(no_photo_check);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFields();
			}
		});
		btnReset.setBounds(205, 471, 89, 23);
		frmNewRegistrationForm.getContentPane().add(btnReset);

		JLabel lblDepartment = new JLabel("Department:");
		lblDepartment.setBounds(10, 304, 91, 14);
		frmNewRegistrationForm.getContentPane().add(lblDepartment);

		deptBox = new JComboBox();
		deptBox.setBounds(111, 301, 264, 20);
		frmNewRegistrationForm.getContentPane().add(deptBox);

		JLabel label_1 = new JLabel("\u00A9 Copyright 2017. Saraswat Infotech Ltd");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_1.setBounds(920, 524, 277, 14);
		frmNewRegistrationForm.getContentPane().add(label_1);
	}

	public boolean capture(BufferedImage img, byte[] b, JLabel label, int fingPos) {
		try {
			fplib = new JSGFPLib();
			fplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
			fplib.OpenDevice(SGPPPortAddr.AUTO_DETECT);
			fplib.GetDeviceInfo(deviceInfo);
			img = new BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);
			byte[] buffer = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			int[] img_qlty = new int[1];

			err = fplib.GetImageEx(buffer, TIMEOUT, 0, QUALITY);

			if (err == SGFDxErrorCode.SGFDX_ERROR_NONE) {
				status_message.setText("Reading biometric...");
				fingerInfo = new SGFingerInfo();
				fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, buffer, img_qlty);

				fingerInfo.FingerNumber = fingPos;
				fingerInfo.ImageQuality = img_qlty[0];
				fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
				fingerInfo.ViewNumber = 1;

				fplib.CreateTemplate(fingerInfo, buffer, b);
				status_message.setVisible(true);
				status_message.setText(" Fingerprint captured successfully");
				label.setIcon(new ImageIcon(img.getScaledInstance(130, 150, Image.SCALE_DEFAULT)));
				return true;
			} else {
				status_message.setVisible(true);
				status_message.setText(" Failed to capture finger.");
				label.setIcon(null);
				return false;
			}

		} catch (Exception ex) {
			status_message.setVisible(true);
			status_message.setText(" Please connect the reader and try again.");
			JOptionPane.showMessageDialog(frmNewRegistrationForm, "Fingerprint reader not found.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	private void clearFields() {
		fingerprint_label.setIcon(null);
		image_label.setIcon(null);
		fingerprint_label1.setIcon(null);
		fingerprint_label2.setIcon(null);
		fingerprint_label3.setIcon(null);
		fingerprint_label4.setIcon(null);
		emp_id.setText("");
		emp_name.setText("");
		emp_name.setEditable(true);
		emp_contact.setText("");
		emp_email.setText("");
		status_message.setText("");
		emp_username.setText("");
		emp_password.setText("");
		isCaptured1 = false;
		isCaptured2 = false;
		isCaptured3 = false;
		isCaptured4 = false;
		isCaptured5 = false;
		isPhotoSelected = false;
		doNotUploadPhoto = false;
		isDuplicateId = false;
		dob_picker.setDateToToday();
		joining_date_picker.setDateToToday();
		dob_picker.setEnabled(true);
		joining_date_picker.setEnabled(true);
		auto_generate.setEnabled(true);
		emp_username.setEditable(true);
		emp_password.setEditable(true);
		rdbtnMale.setEnabled(true);
		rdbtnFemale.setEnabled(true);
		emp_id.setEditable(true);
		deptBox.setEnabled(true);
		isRecordExist = false;
	}

	public class JTextFieldLimit extends PlainDocument {
		private int limit;

		JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null)
				return;

			if ((getLength() + str.length()) <= limit) {
				super.insertString(offset, str, attr);
			}
		}
	}
}
