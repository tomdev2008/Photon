package com.yhd.arch.photon.exception;

public class TimeoutException extends RemoteException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -476924769990467898L;
	
	public TimeoutException(String msg)
	{
		super(msg);
	}

}
