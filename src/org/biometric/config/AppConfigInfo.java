package org.biometric.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class AppConfigInfo {

	public static String loadProperty(String key) {
		FileInputStream fis = null;
		String configData = "";
		try {
			fis = new FileInputStream("app-config.properties");
			Properties prop = new Properties();
			prop.load(fis);
			configData = prop.getProperty(key);
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {

			}
		}
		return configData;
	}

	public static boolean UpdateProperties(String key, String value) {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		boolean isPropertyUpdated = false;
		try {
			fis = new FileInputStream("app-config.properties");
			Properties prop = new Properties();
			prop.load(fis);
			fis.close();

			fos = new FileOutputStream("app-config.properties");
			prop.setProperty(key, value);
			prop.store(fos, null);
			isPropertyUpdated = true;
			fos.close();
		} catch (Exception e) {

		}
		return isPropertyUpdated;
	}

	public static String getUserName() {
		return loadProperty("username");
	}

	public static String getDefaultStorage() {
		return loadProperty("default-storage");
	}

	public static String getPassword() {
		return loadProperty("password");
	}

	public static String getFullWorkingHours() {
		return loadProperty("full-working-hours");
	}

	public static String getHalfWorkingHours() {
		return loadProperty("half-working-hours");
	}

}
