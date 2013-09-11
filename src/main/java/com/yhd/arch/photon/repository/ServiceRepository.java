package com.yhd.arch.photon.repository;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.yhd.arch.photon.actor.ServiceMethodActor;
import com.yhd.arch.photon.exception.RemoteException;
import com.yhd.arch.photon.util.ActorNameUtil;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class ServiceRepository {
	
	private Set<String> ingoreMethods = new HashSet<String>();
	private Map<String,Object> services = new ConcurrentHashMap<String,Object>();
	private Map<String,RemoteMethod> serviceMethods=new ConcurrentHashMap<String, RemoteMethod>();
	
	private final ServiceRepository sr;
	public  ServiceRepository()
	{
		Method[] objectMethodArray = Object.class.getMethods();
		for(Method method : objectMethodArray){
			this.ingoreMethods.add(method.getName());
		}
		
		Method[] classMethodArray = Class.class.getMethods();
		for(Method method : classMethodArray){
			this.ingoreMethods.add(method.getName());
		}
		sr=this;
	}
	public void registerService(String serviceName,Object service,ActorSystem system,int workerCounts) throws ClassNotFoundException{
		this.services.put(serviceName, service);
		//注册method level actor
		Method[] methodArray = service.getClass().getMethods();
		//ServiceMethods serviceMethods = new ServiceMethods(serviceName,service);
		for(Method method : methodArray){
			if(!this.ingoreMethods.contains(method.getName())){
				method.setAccessible(true);
				String fullMethodPath=ActorNameUtil.mangleName(method);
				serviceMethods.put(serviceName+"_"+fullMethodPath,new RemoteMethod(service,method));
				system.actorOf(Props.create(ServiceMethodActor.class,sr,workerCounts),serviceName+"_"+fullMethodPath);
			}
		}
		//this.methods.put(serviceName, serviceMethods);
	}

	public Object getService(String serviceName){
		return this.services.get(serviceName);
	}
	
	public Collection<String> getServiceNames() {
		return this.services.keySet();
	}
	
	public RemoteMethod getMethod(String uuID) throws RemoteException
	{
		/*ServiceMethods serviceMethods = this.methods.get(serviceName);
		if(serviceMethods!=null)
		{
			return serviceMethods.getMethod(methodName,new RemoteParams(paramClassNames));
		}*/
		return serviceMethods.get(uuID);
		
		//return null;
	}
}
