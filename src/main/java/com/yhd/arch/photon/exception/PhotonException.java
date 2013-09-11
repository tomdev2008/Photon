package com.yhd.arch.photon.exception;

public class PhotonException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2314850484565363028L;
	
	public PhotonException() {
		super();
	}

	public PhotonException(String message, Throwable cause) {
		super(message, cause);
	}

	public PhotonException(String message) {
		super(message);
	}

	public PhotonException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

}
