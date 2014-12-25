package ru.tykvin.loader;

import java.util.Calendar;
import java.util.Date;

public class Constants {
	public static String timesFormat = "";
	public static Date beginConsumingTime;
	
	static {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.add(Calendar.HOUR_OF_DAY, 7);
		beginConsumingTime = c.getTime();
	}
}
