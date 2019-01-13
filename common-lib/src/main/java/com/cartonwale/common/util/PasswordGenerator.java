package com.cartonwale.common.util;

import java.security.SecureRandom;

public class PasswordGenerator {

	private static SecureRandom random = new SecureRandom();

	/** different dictionaries used */
	private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMERIC = "0123456789";
	private static final String SPECIAL_CHARS = "!@$&*=";
	private static final int PASSWORD_LEN = 8;
	private static final String DIC = ALPHA_CAPS + ALPHA + NUMERIC + SPECIAL_CHARS;

	/**
	 * Method will generate random string based on the parameters
	 * 
	 * @param len
	 *            the length of the random string
	 * @param dic
	 *            the dictionary used to generate the password
	 * @return the random password
	 */
	public static String generatePassword() {
		String result = "";
		for (int i = 0; i < PASSWORD_LEN; i++) {
			int index = random.nextInt(DIC.length());
			result += DIC.charAt(index);
		}
		return result;
	}
}
