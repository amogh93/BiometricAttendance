package org.biometric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class App {
	public static void main(String[] args) {

		Properties prop = new Properties();
		OutputStream output = null;

		try {
			File file = new File("app-config.properties");

			FileOutputStream fos = new FileOutputStream(file);
			// output = new FileOutputStream(file);

			// set the properties value
			prop.setProperty("default-storage", "D:/");
			prop.setProperty("username", "admin");
			prop.setProperty("password", "598b1jV2BRGe6D5T+7/8mQ==");
			prop.setProperty("full-working-hours", "08:30:00");
			prop.setProperty("half-working-hours", "04:15:00");

			// save properties to project root folder
			prop.store(fos, null);
			System.out.println("Done");
			fos.close();

			FileInputStream fis = new FileInputStream("app-config.properties");
			prop.load(fis);
			System.out.println(prop.getProperty("password"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}