package com.DashboardSales;

public class BizUtil {
	public static boolean isNullString(String p_text) {
		return p_text == null || p_text.trim().length() <= 0 || "null".equalsIgnoreCase(p_text.trim());
	}

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs != null && (strLen = cs.length()) != 0) {
			for (int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(cs.charAt(i))) {
					return false;
				}
			}

			return true;
		} else {
			return true;
		}
	}
}