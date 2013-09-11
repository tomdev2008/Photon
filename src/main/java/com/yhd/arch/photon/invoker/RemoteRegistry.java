package com.yhd.arch.photon.invoker;

import java.util.HashMap;
import java.util.Map;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.yhd.arch.photon.common.Constants;
import com.yhd.arch.photon.exception.RemoteException;
import com.yhd.arch.photon.repository.ServiceRepository;
import com.yhd.arch.photon.util.SystemUtil;
import akka.actor.ActorSystem;

public class RemoteRegistry {
	private Map<String,Object> services;
	private Integer port=null;
	private String hostName=null;
	public  static boolean isInit = false;
	public final String AkkaSystemName=Constants.AkkaSystemName;
	public ActorSystem system ;
	private ServiceRepository sr;
	private Integer workerCounts;
	public RemoteRegistry(){
		
	}
	public void init() throws Exception{
		isInit=true;
		if(this.services != null)
		{
			this.sr = new ServiceRepository();
			Config config=null;
			Map<String,String> parseMap=new HashMap<String, String>();
			if(hostName==null)
			{
				hostName=SystemUtil.getFirstNoLoopbackIP4Address();
			}
			parseMap.put("akka.remote.netty.tcp.hostname", hostName);
			if(port!=null)
			{
				parseMap.put("akka.remote.netty.tcp.port", port.toString());
			}
			config=ConfigFactory.parseMap(parseMap).withFallback(ConfigFactory.load().getConfig("server"));
			system = ActorSystem.create(AkkaSystemName,config);
			if(this.workerCounts==null)
			{
				this.workerCounts=Constants.DEFAULT_WORKERCOUNT;
			}
			for(String serviceName : this.services.keySet())
			{
				this.sr.registerService(serviceName, this.services.get(serviceName),system,this.workerCounts);
			}
		}
	}
	public void register(String serviceName,Object service) throws RemoteException
	{
		if(this.services == null){
			this.services = new HashMap<String,Object>();
		}
		if(this.services.containsKey(serviceName)){
			throw new RemoteException("service:"+serviceName+" has been existent");
		}
		this.services.put(serviceName, service);
		
	}
	public void setServices(Map<String, Object> services) {
		this.services = services;
	}
	
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public Integer getWorkerCounts() {
		return workerCounts;
	}
	public void setWorkerCounts(Integer workerCounts) {
		this.workerCounts = workerCounts;
	}
}
