package com.yhd.arch.photon.exception;

public class InvalidReturnValueException extends PhotonException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8549185626635835847L;

	public InvalidReturnValueException() {
		super();
	}

	public InvalidReturnValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidReturnValueException(String message) {
		super(message);
	}

	public InvalidReturnValueException(Throwable cause) {
		super(cause);
	}
}
