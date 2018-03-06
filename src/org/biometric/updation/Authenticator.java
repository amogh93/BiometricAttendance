package org.biometric.updation;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.biometric.config.AppConfigInfo;
import org.biometric.crypto.CryptoUtils;
import org.biometric.persist.model.BiometricTemplate;
import org.biometric.persist.model.Employee;
import org.biometric.persist.model.EmployeeLogin;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.imgscalr.Scalr;

import SecuGen.FDxSDKPro.jni.JSGFPLib;
import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.jni.SGFDxDeviceName;
import SecuGen.FDxSDKPro.jni.SGFDxErrorCode;
import SecuGen.FDxSDKPro.jni.SGFingerInfo;
import SecuGen.FDxSDKPro.jni.SGFingerPosition;
import SecuGen.FDxSDKPro.jni.SGImpressionType;
import SecuGen.FDxSDKPro.jni.SGPPPortAddr;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.event.ChangeEvent;
import javax.swing.DefaultComboBoxModel;
import org.biometric.fingers.FingerNumber;
import java.awt.Font;
import javax.swing.SwingConstants;

public class Authenticator {

	public JFrame frmUpdateEmployee;
	private JTextField user_name_textField;
	private JPasswordField password_passwordField;
	private JTextField emp_id_textField;
	private JTextField name_textField;
	private JTextField dob_textField;
	private JTextField email_textField;
	private JTextField contact_textField;
	private JTextField gender_textField;
	private JTextField username_textField;
	private JPasswordField passwordField;
	private JPanel panel_1, panel_2, panel_3;
	private static JLabel fingerprint_label1, emp_img_label;
	private JButton btnCapture, button_4, btnUpdate, btnUpdate_1, load_emp_btn, btnDelete;
	private byte[] regMin1 = new byte[400];
	private byte[] emp_img = new byte[400];
	BufferedImage img1_1 = null;
	BufferedImage img2 = null;
	static boolean isCaptured = false;
	static boolean isPhotoSelected = false;
	static long ret;
	static long err;
	static final long TIMEOUT = 10000;
	static final long QUALITY = 60;
	private static SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
	static JSGFPLib fplib = null;
	static SGFingerInfo fingerInfo = null;
	private JFileChooser fileChooser;
	private JLabel status_message;
	private String old_id, old_email, old_contact, old_password;
	private JComboBox fingpos_box;
	private JCheckBox chckbxCangePhoto;
	private JLabel lblNewFingerNumber;
	private JComboBox comboBox, emp_info_box;
	private JButton cancel_btn;
	private JLabel label_8;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Authenticator window = new Authenticator();
					window.frmUpdateEmployee.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Authenticator() {
		initialize();
		hideFields();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmUpdateEmployee = new JFrame();
		frmUpdateEmployee.setTitle("Update Employee");
		frmUpdateEmployee.setResizable(false);
		frmUpdateEmployee.setBounds(100, 100, 850, 639);
		frmUpdateEmployee.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmUpdateEmployee.getContentPane().setLayout(null);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Authentication", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 11, 350, 229);
		frmUpdateEmployee.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel lblEnterFollowingDetails = new JLabel("Enter following details to proceed...");
		lblEnterFollowingDetails.setBounds(10, 21, 217, 14);
		panel_1.add(lblEnterFollowingDetails);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 58, 79, 14);
		panel_1.add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 94, 79, 14);
		panel_1.add(lblPassword);

		user_name_textField = new JTextField();
		user_name_textField.setBounds(100, 55, 168, 20);
		panel_1.add(user_name_textField);
		user_name_textField.setColumns(10);

		password_passwordField = new JPasswordField();
		password_passwordField.setBounds(100, 88, 168, 20);
		panel_1.add(password_passwordField);

		JButton btnProceed = new JButton("Load Info");
		btnProceed.setBounds(127, 119, 89, 23);
		panel_1.add(btnProceed);

		emp_info_box = new JComboBox();
		emp_info_box.setBounds(10, 167, 206, 20);
		panel_1.add(emp_info_box);

		load_emp_btn = new JButton("Load Employee");
		load_emp_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] temp = emp_info_box.getSelectedItem().toString().split("-");
				if (!retriveInfo(temp[0])) {
					JOptionPane.showMessageDialog(frmUpdateEmployee, "Failed to load employee info.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		load_emp_btn.setBounds(220, 166, 120, 23);
		panel_1.add(load_emp_btn);

		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog.setDefaultLookAndFeelDecorated(true);
				int response = JOptionPane.showConfirmDialog(frmUpdateEmployee,
						"This action will delete all records of selected employee including biometric record, login details and attendance records. Do you want to continue?",
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					String[] temp = emp_info_box.getSelectedItem().toString().split("-");
					if (new UpdateEmployee().deleteEmployee(temp[0])) {
						JOptionPane.showMessageDialog(frmUpdateEmployee,
								"Employee " + temp[0] + " deleted successfully.");
						eraseFields();
					} else {
						JOptionPane.showMessageDialog(frmUpdateEmployee, "Error occured in delete operation.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (response == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(frmUpdateEmployee, "Delete operation cancelled.",
							"Operation cancelled", JOptionPane.INFORMATION_MESSAGE);
				} else {

				}
			}
		});
		btnDelete.setBounds(220, 195, 120, 23);
		panel_1.add(btnDelete);

		panel_2 = new JPanel();
		panel_2.setBorder(
				new TitledBorder(null, "Employee Information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(10, 241, 824, 342);
		frmUpdateEmployee.getContentPane().add(panel_2);
		panel_2.setLayout(null);

		JLabel label = new JLabel("Employee ID:");
		label.setBounds(10, 41, 119, 14);
		panel_2.add(label);

		JLabel label_1 = new JLabel("Name:");
		label_1.setBounds(10, 73, 119, 14);
		panel_2.add(label_1);

		JLabel label_2 = new JLabel("DOB:");
		label_2.setBounds(10, 104, 119, 14);
		panel_2.add(label_2);

		JLabel label_3 = new JLabel("Email:");
		label_3.setBounds(10, 136, 119, 14);
		panel_2.add(label_3);

		JLabel label_4 = new JLabel("Contact No:");
		label_4.setBounds(10, 171, 119, 14);
		panel_2.add(label_4);

		JLabel label_5 = new JLabel("Gender");
		label_5.setBounds(10, 204, 119, 14);
		panel_2.add(label_5);

		JLabel label_6 = new JLabel("Username:");
		label_6.setBounds(10, 239, 119, 14);
		panel_2.add(label_6);

		JLabel label_7 = new JLabel("Password:");
		label_7.setBounds(10, 274, 119, 14);
		panel_2.add(label_7);

		emp_id_textField = new JTextField();
		emp_id_textField.setColumns(10);
		emp_id_textField.setBounds(124, 38, 198, 20);
		panel_2.add(emp_id_textField);

		name_textField = new JTextField();
		name_textField.setColumns(10);
		name_textField.setBounds(124, 70, 198, 20);
		panel_2.add(name_textField);

		dob_textField = new JTextField();
		dob_textField.setColumns(10);
		dob_textField.setBounds(124, 101, 198, 20);
		panel_2.add(dob_textField);

		email_textField = new JTextField();
		email_textField.setColumns(10);
		email_textField.setBounds(124, 129, 198, 20);
		panel_2.add(email_textField);

		contact_textField = new JTextField();
		contact_textField.setColumns(10);
		contact_textField.setDocument(new JTextFieldLimit(10));
		contact_textField.setBounds(124, 161, 198, 20);
		panel_2.add(contact_textField);

		gender_textField = new JTextField();
		gender_textField.setColumns(10);
		gender_textField.setBounds(124, 198, 198, 20);
		panel_2.add(gender_textField);

		username_textField = new JTextField();
		username_textField.setColumns(10);
		username_textField.setBounds(124, 233, 198, 20);
		panel_2.add(username_textField);

		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Photo", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_7.setBounds(527, 41, 142, 173);
		panel_2.add(panel_7);

		emp_img_label = new JLabel("");
		emp_img_label.setBounds(6, 16, 130, 150);
		panel_7.add(emp_img_label);

		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(10, 459, 46, 14);
		panel_2.add(lblStatus);

		button_4 = new JButton("Choose Photo");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frmUpdateEmployee);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						img2 = ImageIO.read(selectedFile);
						img2 = Scalr.resize(img2, 300, 400);
						emp_img_label.setIcon(new ImageIcon(img2.getScaledInstance(130, 150, Image.SCALE_DEFAULT)));
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
		button_4.setBounds(527, 214, 142, 23);
		panel_2.add(button_4);

		passwordField = new JPasswordField();
		passwordField.setBounds(123, 271, 199, 20);
		panel_2.add(passwordField);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean b1 = false, b2 = false, b3 = false, b4 = false;
				if (!Pattern.matches("[0-9]{10}", contact_textField.getText())) {
					JOptionPane.showMessageDialog(frmUpdateEmployee, "Incorrect contact number format.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!Pattern.matches(
						"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
						email_textField.getText())) {
					JOptionPane.showMessageDialog(frmUpdateEmployee, "Incorrect email format.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (!email_textField.getText().equals(old_email)) {
						b1 = new UpdateEmployee().updateEmail(old_id, email_textField.getText().toString());
					}
					if (!contact_textField.getText().equals(old_contact)) {
						b2 = new UpdateEmployee().updateContact(old_id, contact_textField.getText().toString());
					}
					if (!passwordField.getText().equals(old_password)) {
						b3 = new UpdateEmployee().updatePassword(old_id, passwordField.getText());
					}
					if (isPhotoSelected && chckbxCangePhoto.isSelected()) {
						b4 = new UpdateEmployee().updateImage(old_id, emp_img);
						isPhotoSelected = false;
					}
				}

				if (b1 || b2 || b3 || b4) {
					JOptionPane.showMessageDialog(frmUpdateEmployee, "Employee information updated.");
					eraseFields();
				}
			}
		});
		btnUpdate.setBounds(124, 302, 89, 23);
		panel_2.add(btnUpdate);

		chckbxCangePhoto = new JCheckBox("Change photo");
		chckbxCangePhoto.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (chckbxCangePhoto.isSelected()) {
					button_4.setEnabled(true);
				} else {
					button_4.setEnabled(false);
				}
			}
		});
		chckbxCangePhoto.setBounds(526, 11, 142, 23);
		panel_2.add(chckbxCangePhoto);

		cancel_btn = new JButton("Cancel");
		cancel_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eraseFields();
			}
		});
		cancel_btn.setBounds(233, 302, 89, 23);
		panel_2.add(cancel_btn);

		panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Biometric Details",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(370, 11, 467, 229);
		frmUpdateEmployee.getContentPane().add(panel_3);
		panel_3.setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Fingerprint ",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 21, 142, 173);
		panel_3.add(panel);

		fingerprint_label1 = new JLabel("");
		fingerprint_label1.setBounds(6, 16, 130, 150);
		panel.add(fingerprint_label1);

		btnCapture = new JButton("Capture");
		btnCapture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (capture(img1_1, regMin1, fingerprint_label1)) {
					isCaptured = true;
				}
			}
		});
		btnCapture.setBounds(10, 195, 142, 23);
		panel_3.add(btnCapture);

		JLabel lblReplace = new JLabel("Replace:");
		lblReplace.setBounds(162, 78, 66, 14);
		panel_3.add(lblReplace);

		fingpos_box = new JComboBox();
		fingpos_box.setBounds(162, 94, 201, 20);
		panel_3.add(fingpos_box);

		btnUpdate_1 = new JButton("Update");
		btnUpdate_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean b = false;
				if (isCaptured) {
					String[] temp = fingpos_box.getSelectedItem().toString().split("-");
					b = new UpdateEmployee().updateBiometric(old_id, regMin1, Integer.valueOf(temp[0]),
							comboBox.getSelectedIndex());
				} else {
					JOptionPane.showMessageDialog(frmUpdateEmployee, "Please capture your fingerprint.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

				if (b) {
					JOptionPane.showMessageDialog(frmUpdateEmployee, "Employee information updated.");
					eraseFields();
				}
			}
		});
		btnUpdate_1.setBounds(220, 195, 89, 23);
		panel_3.add(btnUpdate_1);

		lblNewFingerNumber = new JLabel("New finger number:");
		lblNewFingerNumber.setBounds(162, 21, 201, 14);
		panel_3.add(lblNewFingerNumber);

		comboBox = new JComboBox();
		comboBox.setEnabled(false);
		comboBox.setBounds(162, 37, 201, 20);
		panel_3.add(comboBox);

		JLabel status_label = new JLabel("Status:");
		status_label.setBounds(10, 586, 46, 14);
		frmUpdateEmployee.getContentPane().add(status_label);

		status_message = new JLabel("");
		status_message.setBounds(57, 586, 777, 14);
		frmUpdateEmployee.getContentPane().add(status_message);

		label_8 = new JLabel("\u00A9 Copyright 2017. Saraswat Infotech Ltd");
		label_8.setHorizontalAlignment(SwingConstants.RIGHT);
		label_8.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_8.setBounds(557, 586, 277, 14);
		frmUpdateEmployee.getContentPane().add(label_8);

		btnProceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user_name_textField.getText().equals("")) {
					JOptionPane.showMessageDialog(frmUpdateEmployee, "Please enter username.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (password_passwordField.getText().equals("")) {
					JOptionPane.showMessageDialog(frmUpdateEmployee, "Please enter password.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (!loadInfoInComboBox(user_name_textField.getText(), password_passwordField.getText())) {
						emp_info_box.setEnabled(false);
						load_emp_btn.setEnabled(false);
						JOptionPane.showMessageDialog(frmUpdateEmployee, "Incorrect username or password.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});
	}

	public void hideFields() {
		emp_id_textField.setEditable(false);
		passwordField.setEnabled(false);
		passwordField.setEditable(false);
		name_textField.setEditable(false);
		dob_textField.setEditable(false);
		email_textField.setEnabled(false);
		email_textField.setEditable(false);
		contact_textField.setEnabled(false);
		contact_textField.setEditable(false);
		gender_textField.setEditable(false);
		username_textField.setEditable(false);
		btnCapture.setEnabled(false);
		button_4.setEnabled(false);
		btnUpdate.setEnabled(false);
		panel_2.setEnabled(false);
		panel_3.setEnabled(false);
		fingpos_box.setEnabled(false);
		btnUpdate_1.setEnabled(false);
		comboBox.setEnabled(false);
		emp_info_box.setEnabled(false);
		load_emp_btn.setEnabled(false);
		cancel_btn.setEnabled(false);
		chckbxCangePhoto.setEnabled(false);
		btnDelete.setEnabled(false);
	}

	public void enableFields() {
		email_textField.setEnabled(true);
		email_textField.setEditable(true);
		contact_textField.setEditable(true);
		contact_textField.setEnabled(true);
		passwordField.setEnabled(true);
		passwordField.setEditable(true);
		btnCapture.setEnabled(true);
		btnUpdate.setEnabled(true);
		panel_2.setEnabled(true);
		panel_3.setEnabled(true);
		fingpos_box.setEnabled(true);
		btnUpdate_1.setEnabled(true);
		comboBox.setEnabled(true);
		cancel_btn.setEnabled(true);
		chckbxCangePhoto.setEnabled(true);
		btnDelete.setEnabled(true);
	}

	public boolean retriveInfo(String id) {
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession();) {
			session.beginTransaction();
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Employee.class);
			detachedCriteria.add(Restrictions.eq("id", id));

			List result = detachedCriteria.getExecutableCriteria(session).list();

			if (result.size() > 0) {
				Employee employee = (Employee) result.iterator().next();
				EmployeeLogin login = employee.getEmployeeLogin();
				old_id = employee.getId();
				emp_id_textField.setText(old_id);
				name_textField.setText(employee.getName());
				dob_textField.setText(employee.getDateOfBirth().toString());
				old_email = employee.getEmail();
				email_textField.setText(old_email);
				old_contact = employee.getContactNumber();
				contact_textField.setText(old_contact);
				if (employee.getGender() == 'M') {
					gender_textField.setText("Male");
				} else if (employee.getGender() == 'F') {
					gender_textField.setText("Female");
				} else {
					gender_textField.setText("Other");
				}
				username_textField.setText(login.getUserName());
				old_password = CryptoUtils.doDecrypt(login.getPassword());
				passwordField.setText(old_password);
				try {
					if (employee.getImage() != null) {
						emp_img_label.setIcon(new ImageIcon((getBufferedImage(employee.getImage()))
								.getScaledInstance(130, 150, Image.SCALE_DEFAULT)));
					} else {
						emp_img_label.setIcon(new ImageIcon(ImageIO
								.read(Authenticator.class.getClassLoader()
										.getResourceAsStream("resources/no_image_available.jpg"))
								.getScaledInstance(130, 150, Image.SCALE_DEFAULT)));
					}
				} catch (Exception ex) {

				}
				Set bio_list = employee.getTemplate();
				if (bio_list.size() > 0) {
					String[] fingpos = { "UNKNOWN_FINGER", "RIGHT_THUMB", "RIGHT_INDEX", "RIGHT_MIDDLE", "RIGHT_RING",
							"RIGHT_LITTLE", "LEFT_THUMB", "LEFT_INDEX", "LEFT_MIDDLE", "LEFT_RING", "LEFT_LITTLE" };
					Iterator bio_itr = bio_list.iterator();
					while (bio_itr.hasNext()) {
						BiometricTemplate template = (BiometricTemplate) bio_itr.next();
						fingpos_box
								.addItem(template.getFinger_position() + "-" + fingpos[template.getFinger_position()]);
					}
				}
				comboBox.setModel(new DefaultComboBoxModel(FingerNumber.values()));
				enableFields();
				return true;
			}
		}
		return false;
	}

	public BufferedImage getBufferedImage(byte[] b) {
		InputStream in = new ByteArrayInputStream(b);
		BufferedImage bImg = null;
		try {
			bImg = ImageIO.read(in);
		} catch (Exception ex) {

		}
		return bImg;
	}

	public boolean capture(BufferedImage img, byte[] b, JLabel label) {
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

				fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_LI;
				fingerInfo.ImageQuality = img_qlty[0];
				fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
				fingerInfo.ViewNumber = 1;

				fplib.CreateTemplate(fingerInfo, buffer, b);
				status_message.setVisible(true);
				status_message.setText(" Fingerprint captured successfully");
				label.setIcon(new ImageIcon(img.getScaledInstance(130, 150, Image.SCALE_DEFAULT)));
				isCaptured = true;
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
			JOptionPane.showMessageDialog(frmUpdateEmployee, "Fingerprint reader not found.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	public void eraseFields() {
		emp_id_textField.setEditable(false);
		emp_id_textField.setText("");
		passwordField.setEnabled(false);
		passwordField.setEditable(false);
		passwordField.setText("");
		name_textField.setEditable(false);
		name_textField.setText("");
		dob_textField.setEditable(false);
		dob_textField.setText("");
		email_textField.setEnabled(false);
		email_textField.setEditable(false);
		email_textField.setText("");
		contact_textField.setEnabled(false);
		contact_textField.setEditable(false);
		contact_textField.setText("");
		gender_textField.setEditable(false);
		gender_textField.setText("");
		username_textField.setEditable(false);
		username_textField.setText("");
		btnCapture.setEnabled(false);
		button_4.setEnabled(false);
		btnUpdate.setEnabled(false);
		panel_2.setEnabled(false);
		panel_3.setEnabled(false);
		fingpos_box.setEnabled(false);
		btnUpdate_1.setEnabled(false);
		emp_img_label.setIcon(null);
		fingpos_box.removeAllItems();
		comboBox.removeAllItems();
		comboBox.setEnabled(false);
		isCaptured = false;
		isPhotoSelected = false;
		fingerprint_label1.setIcon(null);
		password_passwordField.setText("");
		emp_info_box.removeAllItems();
		load_emp_btn.setEnabled(false);
		cancel_btn.setEnabled(false);
		chckbxCangePhoto.setEnabled(false);
		status_message.setText("");
		btnDelete.setEnabled(false);
		emp_info_box.setEnabled(false);
	}

	public boolean loadInfoInComboBox(String user_name, String password) {
		if (user_name.equals(AppConfigInfo.getUserName())
				&& password.equals(CryptoUtils.doDecrypt(AppConfigInfo.getPassword()))) {
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
						emp_info_box.addItem(emp_data[0] + "-" + emp_data[1]);
					}
				}
			}
			emp_info_box.setEnabled(true);
			load_emp_btn.setEnabled(true);
			return true;
		}
		return false;
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
