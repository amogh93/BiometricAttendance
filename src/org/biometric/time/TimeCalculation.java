package org.biometric.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeCalculation {

	static int hours, minutes, seconds;

	static DateFormat df;

	public static Date CalculateTime(Date init_time, Date add_time) throws Exception {
		df = new SimpleDateFormat("HH");
		hours = Integer.valueOf(df.format(add_time));
		df = new SimpleDateFormat("mm");
		minutes = Integer.valueOf(df.format(add_time));
		df = new SimpleDateFormat("ss");
		seconds = Integer.valueOf(df.format(add_time));

		Calendar cal = Calendar.getInstance();
		cal.setTime(init_time);

		cal.add(Calendar.HOUR, hours);
		cal.add(Calendar.MINUTE, minutes);
		cal.add(Calendar.SECOND, seconds);

		df = new SimpleDateFormat("HH:mm:ss");

		return cal.getTime();
	}

}
