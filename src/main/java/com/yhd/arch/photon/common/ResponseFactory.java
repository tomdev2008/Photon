package com.yhd.arch.photon.common;

import com.yhd.arch.photon.invoker.DefaultResponse;

public class ResponseFactory {

	public static RemoteResponse createFailResponse(String message)
	{
		RemoteResponse response = null;
		if(message == null){
			message = "Service has Exception";
		}
		response = new DefaultResponse(Constants.MESSAGE_TYPE_EXCEPTION, message);
		return response;
	}
	public static RemoteResponse createTimeOutResponse(String message)
	{
		RemoteResponse response = null;
		if(message == null){
			message = "Timeout Exception";
		}
		response = new DefaultResponse(Constants.MESSAGE_TYPE_EXCEPTION, message);
		return response;
	}
	public static RemoteResponse createServiceExceptionResponse(Throwable e){
		RemoteResponse response = null;
		if(e == null){
			return createFailResponse(null);
		}
		response = new DefaultResponse(Constants.MESSAGE_TYPE_SERVICE_EXCEPTION, e.getCause());
		return response;
	}
	public static RemoteResponse createSuccessResponse(Object returnObj){
		RemoteResponse response = null;
		response = new DefaultResponse(Constants.MESSAGE_TYPE_SERVICE,returnObj);
		return response;
	}
}
