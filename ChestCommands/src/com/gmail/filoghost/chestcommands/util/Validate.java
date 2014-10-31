package com.gmail.filoghost.chestcommands.util;

public class Validate {

	public static void notNull(Object object, String error) {
		if (object == null) {
			throw new NullPointerException(error);
		}
	}
	
	public static void isTrue(boolean statement, String error) {
		if (!statement) {
			throw new IllegalArgumentException(error);
		}
	}
	
	public static void isFalse(boolean statement, String error) {
		if (statement) {
			throw new IllegalArgumentException(error);
		}
	}
	
}
