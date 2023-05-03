package com.safetynetalert.api.logMessage;

public class LogMessage {

	public static String getMessage(String message) {
		return message;
	}

	public static String getMessage(String classe,
			String message,
			String endpoint,
			String method) {
		return classe +
				Const.SEPARATEUR +
				message +
				Const.SEPARATEUR +
				endpoint +
				Const.SEPARATEUR +
				method;
	}

	public static String getMessageBirthdate(String message) {
		return "Filed Name" +
				Const.SEPARATEUR +
				"birthdateError Message" +
				Const.SEPARATEUR +
				message;
	}
}
