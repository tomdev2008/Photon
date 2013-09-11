package com.yhd.arch.photon.common;

import java.io.Serializable;

import com.yhd.arch.photon.exception.PhotonException;
import com.yhd.arch.photon.router.RouteeMeta;

public interface RemoteRequest extends Serializable {

	public void setCallType(int callType);

	public int getCallType();

	public int getTimeout();

	public int getReadTimeout();

	public long getCreateMillisTime();

	public void createMillisTime();

	public String getServiceName();

	public String getMethodName();
	
	public String getUUID();

	//public String[] getParamClassName() throws PhotonException;

	public Object[] getParameters() throws PhotonException;

	public int getMessageType();

	public Object getObject();

	public boolean isRetryAble();

	public RouteeMeta getRouteeMeta();
}
