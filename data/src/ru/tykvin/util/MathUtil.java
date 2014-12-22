package ru.tykvin.util;

public class MathUtil {

	private static long coefficient = 10000000;
	public static int correction = 1;

	public static void setCorrection(String value) {
		value.replaceAll(",", ".");
		if (value.indexOf(".") > 0) {
			StringBuilder sb = new StringBuilder("1");
			for (int i = 0; i < value.split("\\.")[1].length(); i++) {
				sb.append("0");
			}
			int k = Integer.parseInt(sb.toString());
			coefficient *= k;
			correction = Integer.parseInt(value.split("\\.")[1]);
		} else {
			correction = Integer.parseInt(value);
		}
	}
	
	public static Double toDouble(Long l) {
		return (double) (l / coefficient);
	}

	public static long toLong(String value) {
		if (value != null && !value.isEmpty()) {
			value = value.replaceAll(",", "").replaceAll("\\.", "");
			int cnt = 8;
			while (value.charAt(0) == '0') {
				value = value.substring(1);
				cnt--;
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < cnt; i++) {
				sb.append(i < value.length() ? value.charAt(i) : "0");
			}
			return Long.parseLong(sb.toString());
		} else {
			return 0;
		}
	}

}
