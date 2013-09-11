package com.yhd.arch.photon.common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoteMetaData {

	private String serviceName;
	private String callMode;
	private int timeout;
	private int readTimeout;
	private Boolean flowControl;
	private Long limit;
	@SuppressWarnings("rawtypes")
	private Class objType;
	private String hosts;
	private List<String> hostList=new ArrayList<String>();
	private Pattern hostPattern = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
	private boolean retry;

	@SuppressWarnings("rawtypes")
	public RemoteMetaData(String serviceName, String callMode, int timeout, int readTimeout, Class objType, String hosts, boolean isRetry) {
		this.serviceName = serviceName;
		this.callMode = callMode;
		this.timeout = timeout;
		this.objType = objType;
		this.hosts = hosts;
		this.readTimeout = readTimeout;
		this.retry = isRetry;
		checkHost();
	}

	private void checkHost()
	{
	  	String[] hostArray= this.getHosts().split(Constants.HOSTS_CONNECTOR);
	  	for(String host:hostArray)
	  	{
	  		
	  		Matcher matcher=hostPattern.matcher(host);
	  		if(matcher.find())
	  		{
	  			hostList.add(host.trim());
	  		}
	  		else
	  		{
	  			throw new IllegalArgumentException("Hosts is illegalArgument："+hosts);
	  		}
	  	}
	}
	@SuppressWarnings("rawtypes")
	public RemoteMetaData(String serviceName, String callMode, int timeout, Class objType, String hosts, Boolean flowControl, Long limit,
			boolean isRetry) {
		this.serviceName = serviceName;
		this.callMode = callMode;
		this.timeout = timeout;
		this.objType = objType;
		this.hosts = hosts;
		this.flowControl = flowControl;
		this.limit = limit;
		this.retry = isRetry;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getCallMode() {
		return callMode;
	}

	public void setCallMode(String callMode) {
		this.callMode = callMode;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Boolean getFlowControl() {
		return flowControl;
	}

	public void setFlowControl(Boolean flowControl) {
		this.flowControl = flowControl;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	@SuppressWarnings("rawtypes")
	public Class getObjType() {
		return objType;
	}

	@SuppressWarnings("rawtypes")
	public void setObjType(Class objType) {
		this.objType = objType;
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	public List<String> getHostList() {
		return hostList;
	}

	@Override
	public String toString() {
		return "RemoteMetaData [serviceName=" + serviceName + ", callMode=" + callMode + ", timeout=" + timeout + ", readTimeout="
				+ readTimeout + ", flowControl=" + flowControl + ", limit=" + limit + ", objType=" + objType + ", hosts=" + hosts
				+ ", retry=" + retry + "]";
	}
}
