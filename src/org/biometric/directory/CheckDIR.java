package org.biometric.directory;

import java.io.File;

import org.biometric.config.AppConfigInfo;

public class CheckDIR {

	public static boolean isExists() {
		try {
			File file = new File(AppConfigInfo.getDefaultStorage());
			if (file.exists()) {
				return true;
			}
		} catch (Exception e) {

		}
		return false;
	}

	public static boolean makeDir() {
		return new File(AppConfigInfo.getDefaultStorage()).mkdirs();
	}
}
