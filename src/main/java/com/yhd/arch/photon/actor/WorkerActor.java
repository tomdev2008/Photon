package com.yhd.arch.photon.actor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.yhd.arch.photon.common.Constants;
import com.yhd.arch.photon.common.RemoteRequest;
import com.yhd.arch.photon.common.RemoteResponse;
import com.yhd.arch.photon.common.ResponseFactory;
import com.yhd.arch.photon.exception.PhotonException;
import com.yhd.arch.photon.exception.TimeoutException;
import com.yhd.arch.photon.repository.RemoteMethod;
import com.yhd.arch.photon.repository.ServiceRepository;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class WorkerActor  extends UntypedActor {

	LoggingAdapter log=Logging.getLogger(getContext().system(), this);
	private ServiceRepository sr;
	public WorkerActor(ServiceRepository sr)
	{
		this.sr=sr;
	}
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof RemoteRequest)
		{
			RemoteRequest request=(RemoteRequest)message;
			long currentTime = System.currentTimeMillis();
			if(request.getCreateMillisTime()+request.getTimeout() < currentTime){
				//TimeOut Exception
				StringBuffer msg = new StringBuffer();
				msg.append("source:"+getSender().path().toSerializationFormat()).append(" request timeout:"+request.getTimeout()).append("  createTime:").append(request.getCreateMillisTime())
				.append("\r\n");
				Object[] params = request.getParameters();
				if(params != null && params.length > 0){
					for(Object param : params){
						msg.append("<><>").append(String.valueOf(param));
					}
					msg.append("\r\n");
				}
				TimeoutException te=new TimeoutException(msg.toString());
				log.error(te.getMessage(), te);
				return;
			}
			RemoteResponse response =doBusiness(request);
			if(response!=null)
			{
				getSender().tell(response,getSelf());
			}
		}
	}
	private RemoteResponse doBusiness(RemoteRequest request)
	{
		RemoteResponse response=null;
		RemoteMethod method=null;
		try {
			//method = this.sr.getMethod(request.getServiceName(), 
				//	request.getMethodName(), request.getParamClassName());
			method=this.sr.getMethod(request.getUUID());
		} catch (PhotonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//create failresponse
			if(request.getCallType() == Constants.CALLTYPE_REPLY){
				response = ResponseFactory.createFailResponse(e.getMessage());
			}
		
		}
		if(method != null){
			Method method_ = method.getMethod();
			Object returnObj = null;
			try {
				//timeout control 
				returnObj = method_.invoke(method.getService(), request.getParameters());
			}catch (InvocationTargetException e) {
				
				Throwable e2 = e.getTargetException();
				if(e2 != null){
					//logger.error(e2.getMessage(),e2);
				}
				if(request.getCallType() == Constants.CALLTYPE_REPLY){
					return ResponseFactory.createServiceExceptionResponse(e2);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//logger.error(e1.getMessage(),e1);
				if(request.getCallType() == Constants.CALLTYPE_REPLY){
					response = doFailResponse(request,e1);
				}
			}
			if(request.getCallType() == Constants.CALLTYPE_REPLY){
				response = ResponseFactory.createSuccessResponse(returnObj);
			}
		}
		return response;
	}
	private RemoteResponse doFailResponse(RemoteRequest request,Exception e){
		//logger.error(e.getMessage(),e);
		if(request.getCallType() == Constants.CALLTYPE_REPLY){
			return ResponseFactory.createFailResponse(e.getClass().getName()+":::"+e.getMessage());
		}
		return null;
	}

}
