package com.yhd.arch.photon.common;

import java.io.Serializable;

import com.yhd.arch.photon.exception.PhotonException;

public interface RemoteResponse  extends Serializable {

	public void setMessageType(int messageType);
	
	public int getMessageType() throws PhotonException;
	
	public String getCause();
	
	public Object getReturn();

	public void setReturn(Object obj);
}
