package cn.oreo.common.util.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NowUtil {
	public static String getDate(){
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");
		String now = df.format(date);
		return now;
	}
	public static String getTime(){
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		String now = df.format(date);
		return now;
	}
	public static String getDateAndTime(){
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		String now = df.format(date);
		return now;
	}
}
