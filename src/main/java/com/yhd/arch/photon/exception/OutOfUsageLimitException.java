package com.yhd.arch.photon.exception;

public class OutOfUsageLimitException extends PhotonException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7021273757828710271L;

	public OutOfUsageLimitException(){
		super();
	}
	
	public OutOfUsageLimitException(String msg){
		super(msg);
	}
	
	public OutOfUsageLimitException(String msg,Throwable cause){
		super(msg,cause);
	}
	public OutOfUsageLimitException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

}
