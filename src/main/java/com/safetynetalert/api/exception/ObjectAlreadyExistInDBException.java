package com.safetynetalert.api.exception;

public class ObjectAlreadyExistInDBException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 6660329750451400722L;

	public ObjectAlreadyExistInDBException(String message) {
		super(message);
	}
}
