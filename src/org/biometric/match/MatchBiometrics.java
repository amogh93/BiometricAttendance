package org.biometric.match;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.biometric.logAttendance.MarkAttendance;
import org.biometric.persist.model.BiometricTemplate;
import org.biometric.persist.model.Employee;
import org.biometric.registration.AttendanceLogging;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import SecuGen.FDxSDKPro.jni.JSGFPLib;
import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import SecuGen.FDxSDKPro.jni.SGFDxDeviceName;
import SecuGen.FDxSDKPro.jni.SGFDxErrorCode;
import SecuGen.FDxSDKPro.jni.SGFDxSecurityLevel;
import SecuGen.FDxSDKPro.jni.SGFingerInfo;
import SecuGen.FDxSDKPro.jni.SGFingerPosition;
import SecuGen.FDxSDKPro.jni.SGImpressionType;
import SecuGen.FDxSDKPro.jni.SGPPPortAddr;

public class MatchBiometrics implements Runnable {

	static long ret;
	static long timeout = 2000;
	static long quality = 60;

	private SGDeviceInfoParam deviceInfo = new SGDeviceInfoParam();
	private byte[] regMin1 = new byte[400];
	private byte[] regMin2 = new byte[400];
	BufferedImage img1 = null;
	BufferedImage img2 = null;
	private JSGFPLib fplib = null;
	private SGFingerInfo fingerInfo = null;

	@Override
	public void run() {
		while (true) {
			try {
				fplib = new JSGFPLib();
				fplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
				fplib.OpenDevice(SGPPPortAddr.AUTO_DETECT);
				fplib.GetDeviceInfo(deviceInfo);

				img1 = new BufferedImage(deviceInfo.imageWidth, deviceInfo.imageHeight, BufferedImage.TYPE_BYTE_GRAY);
				byte[] buffer = ((DataBufferByte) img1.getRaster().getDataBuffer()).getData();
				int[] img_qlty = new int[1];

				long er = fplib.GetImageEx(buffer, timeout, 0, quality);
				fingerInfo = new SGFingerInfo();
				fplib.GetImageQuality(deviceInfo.imageWidth, deviceInfo.imageHeight, buffer, img_qlty);

				fingerInfo.FingerNumber = SGFingerPosition.SG_FINGPOS_UK;
				fingerInfo.ImageQuality = img_qlty[0];
				fingerInfo.ImpressionType = SGImpressionType.SG_IMPTYPE_LP;
				fingerInfo.ViewNumber = 1;

				fplib.CreateTemplate(fingerInfo, buffer, regMin1);

				if (er == SGFDxErrorCode.SGFDX_ERROR_NONE) {
					matchTemplate();
				} else {

				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(AttendanceLogging.frmLogAttendance, "Fingerprint reader not found.",
						"Error", JOptionPane.ERROR_MESSAGE);
				AttendanceLogging.enableButton();
				break;
			}
		}
	}

	public void matchTemplate() {

		fplib = new JSGFPLib();
		fplib.Init(SGFDxDeviceName.SG_DEV_AUTO);
		fplib.OpenDevice(SGPPPortAddr.AUTO_DETECT);
		fplib.GetDeviceInfo(deviceInfo);
		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BiometricTemplate.class);

			List result = detachedCriteria.getExecutableCriteria(session).list();

			BiometricTemplate template = new BiometricTemplate();
			long secu_level = SGFDxSecurityLevel.SL_NORMAL;
			boolean[] match = new boolean[1];
			match[0] = false;
			Iterator itr = result.iterator();
			while (itr.hasNext()) {
				template = (BiometricTemplate) itr.next();
				regMin2 = template.getTemplate();
				fplib.MatchTemplate(regMin2, regMin1, secu_level, match);
				if (match[0]) {
					Employee e = (Employee) template.getEmployee();

					try {
						if (e.getImage() != null) {
							InputStream in = new ByteArrayInputStream(e.getImage());
							img2 = ImageIO.read(in);
						} else {
							img2 = ImageIO.read(MatchBiometrics.class.getClassLoader()
									.getResourceAsStream("resources/no_image_available.jpg"));
						}
						AttendanceLogging.setImageLabel(img2);

					} catch (Exception ex) {

					}
					AttendanceLogging.setFingerprintLabel(img1);
					AttendanceLogging.setIdLabel(e.getId());
					AttendanceLogging.setNameLabel(e.getName());
					AttendanceLogging.setTypeLabel("");
					AttendanceLogging.setStatusLabel("Matched.");
					MarkAttendance.mark(e.getId(), e);
					try {
						Thread.sleep(3000);
					} catch (Exception ex) {

					}
					AttendanceLogging.clearLabels();
					break;
				} else {

				}
			}
			if (!match[0]) {
				AttendanceLogging.setStatusLabel("No match found.");
			}
		}
	}
}
