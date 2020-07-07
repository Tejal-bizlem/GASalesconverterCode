package com.db.mongo.ga;

public class GFG {
	public static int findLastIndex(String str, Character x) {
		int index = -1;

		try {
			for (int i = 0; i < str.length(); ++i) {
				if (str.charAt(i) == x) {
					index = i;
				}
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		return index;
	}

	public static String replaceLastChar(String str, Character x) {
		try {
			int index = findLastIndex(str, x);
			if (index == -1) {
				System.out.println("Character not found");
			} else {
				str = str.substring(0, index) + "@" + str.substring(index + 1, str.length());
			}
		} catch (Exception var3) {
			var3.printStackTrace();
		}

		return str;
	}

	public static void main(String[] args) {
		String str = "viki_tripathi_gmail.com";
		Character x = '_';
		str = replaceLastChar(str, x);
		System.out.println("String is " + str);
	}
}