/**
 * 
 */
package com.yhd.arch.photon.exception;

public class InvalidParamException extends PhotonException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8242693325670325410L;

	public InvalidParamException() {
		super();
	}

	public InvalidParamException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParamException(String message) {
		super(message);
	}

	public InvalidParamException(Throwable cause) {
		super(cause);
	}

}
