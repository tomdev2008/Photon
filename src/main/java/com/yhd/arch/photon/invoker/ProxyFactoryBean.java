package com.yhd.arch.photon.invoker;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;

import com.yhd.arch.photon.common.Constants;
import com.yhd.arch.photon.common.RemoteMetaData;

@SuppressWarnings("rawtypes")
public class ProxyFactoryBean implements FactoryBean {

	private Object obj;
	private Class objType;
	private String serviceName;
	private String interfaceClass;
	private String callMode = Constants.CALL_SYNC;
	private String hosts;
	private int timeout = 2000;// 2s
	private int readTimeout = 1000;
	private boolean retry = false;

	@SuppressWarnings("unused")
	private void init() throws Exception {
		this.objType = Class.forName(this.interfaceClass);
		this.obj = Proxy.newProxyInstance(ProxyFactoryBean.class.getClassLoader(), new Class[] { this.objType }, new ProxyInvoker(
				new RemoteMetaData(this.serviceName, this.callMode, this.timeout, this.readTimeout, this.objType, this.hosts, this.retry)));
	}

	public Object getObject() throws Exception {
		return this.obj;
	}

	public Class getObjectType() {
		return this.objType;
	}

	public boolean isSingleton() {
		return true;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setInterfaceClass(String interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public void setCallMode(String callMode) {
		this.callMode = callMode;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

}
