package org.biometric.settings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.biometric.config.AppConfigInfo;
import org.biometric.crypto.CryptoUtils;

public class UpdateAppSettings {

	public static boolean updateStoragePath(String path) {
		String storage_path = path;
		if (!storage_path.endsWith("/")) {
			storage_path = storage_path.concat("/");
		}
		AppConfigInfo.UpdateProperties("default-storage", storage_path);
		return true;
	}

	public static boolean updateWorkigHours(Date full_day, Date half_day) {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		AppConfigInfo.UpdateProperties("full-working-hours", df.format(full_day));
		AppConfigInfo.UpdateProperties("half-working-hours", df.format(half_day));
		return true;
	}

	public static boolean updateAdminPassword(String old_password, String new_password) {
		if (!old_password.equals(CryptoUtils.doDecrypt(AppConfigInfo.getPassword()))) {
			return false;
		}

		AppConfigInfo.UpdateProperties("password", CryptoUtils.doEncrypt(new_password));
		return true;
	}

}
