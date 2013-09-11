package com.yhd.arch.photon.invoker;

import java.lang.reflect.Method;

import com.yhd.arch.photon.common.Constants;
import com.yhd.arch.photon.common.RemoteMetaData;
import com.yhd.arch.photon.common.RemoteRequest;
import com.yhd.arch.photon.exception.PhotonException;
import com.yhd.arch.photon.router.RouteeMeta;
import com.yhd.arch.photon.util.ActorNameUtil;

public class DefaultRequest implements RemoteRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3733661157239791325L;
	private int callType;
	private int timeout = 2000;
	private int readTimeout = 1000;
	private String serviceName;
	private String methodName;
	private Object[] parameters;
	private int messageType;
	private String uuID;
	// private transient Class<?>[] parameterClasses;
	private transient long createMillisTime;
	private transient RouteeMeta routeeMeta;
	private transient int _retryLimit = 3;
	private transient int _useCount = 1;
	private boolean retryAble = false;
	@SuppressWarnings("unused")
	private transient RemoteMetaData remoteMeta;

	public DefaultRequest() {
		super();
	}

	public DefaultRequest(RemoteMetaData remoteMeta, Method method, Object[] args) {
		if (method != null && remoteMeta != null) {
			this.remoteMeta = remoteMeta;
			this.serviceName = remoteMeta.getServiceName();
			this.methodName = method.getName();
			this.parameters = args;
			this.messageType = Constants.MESSAGE_TYPE_SERVICE;
			// this.parameterClasses = method.getParameterTypes();
			this.timeout = remoteMeta.getTimeout() < 10 ? 10 : remoteMeta.getTimeout();
			this.readTimeout = remoteMeta.getReadTimeout() < 20 ? 20 : remoteMeta.getReadTimeout();
			String fullMethodPath = ActorNameUtil.mangleName(method);
			this.uuID = this.serviceName + "_" + fullMethodPath;
			this.retryAble = remoteMeta.isRetry();
			this.routeeMeta = new RouteeMeta(serviceName, fullMethodPath);

		}
	}

	public Object getObject() {
		return this;
	}

	public void setCallType(int callType) {
		this.callType = callType;
	}

	public int getCallType() {
		return this.callType;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public long getCreateMillisTime() {
		return this.createMillisTime;
	}

	public void createMillisTime() {
		this.createMillisTime = System.currentTimeMillis();
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public Object[] getParameters() throws PhotonException {
		return this.parameters;
	}

	public int getMessageType() {
		return this.messageType;
	}

	public RouteeMeta getRouteeMeta() {
		return routeeMeta;
	}

	@Override
	public boolean isRetryAble() {
		if (retryAble && ++_useCount <= _retryLimit) {
			return true;
		}
		return false;
	}

	@Override
	public int getReadTimeout() {
		return this.readTimeout;
	}

	@Override
	public String getUUID() {
		return this.uuID;
	}

	@Override
	public String toString() {
		return "DefaultRequest [callType=" + callType + ", timeout=" + timeout + ", readTimeout=" + readTimeout + ", serviceName="
				+ serviceName + ", methodName=" + methodName + ", messageType=" + messageType + ", uuID=" + uuID + ", createMillisTime="
				+ createMillisTime + ", routeeMeta=" + routeeMeta + ", _retryLimit=" + _retryLimit + ", _useCount=" + _useCount
				+ ", retryAble=" + retryAble + ", remoteMeta=" + remoteMeta + "]";
	}

}
