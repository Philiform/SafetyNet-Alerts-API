package com.safetynetalert.api.exception;

public class ObjectNotExistInDBException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -44421262090989084L;

	public ObjectNotExistInDBException(String message) {
		super(message);
	}
}
