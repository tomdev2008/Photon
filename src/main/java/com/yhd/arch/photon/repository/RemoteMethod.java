package com.yhd.arch.photon.repository;

import java.lang.reflect.Method;

import com.yhd.arch.photon.common.Constants;
import com.yhd.arch.photon.exception.RemoteException;

public class RemoteMethod {

	private Method method;
	private Object service;
	@SuppressWarnings("rawtypes")
	private Class[] parameterClasses;
	private int parameterLength;
	
	public  RemoteMethod(Object service,Method method){
		this.service = service;
		this.method = method;
		this.parameterClasses = regulateTypes(this.method.getParameterTypes());
		this.parameterLength = this.parameterClasses.length;
	}
	
	@SuppressWarnings("rawtypes")
	private Class[] regulateTypes(Class[] types){
		for(int i=0;i<types.length;i++){
			if(types[i] == byte.class){
				types[i] = Byte.class;
			}else if(types[i] == short.class){
				types[i] = Short.class;
			}else if(types[i] == int.class){
				types[i] = Integer.class;
			}else if(types[i] == boolean.class){
				types[i] = Boolean.class;
			}else if(types[i] == long.class){
				types[i] = Long.class;
			}else if(types[i] == float.class){
				types[i] = Float.class;
			}else if(types[i] == double.class){
				types[i] = Double.class;
			}else {
			}
		}
		return types;
	}
	
	public int getParameterSize(){
		return this.parameterLength;
	}
	/**
	 * 如果返回值等于参数个数，表示完全匹配,-1 ,all not match
	 * @param paramClassNames
	 * @return
	 * @throws RemoteException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int matching(String[] paramClassNames) throws RemoteException{
		int k = 0;
		for(int i=0;i<paramClassNames.length;i++){
			if(paramClassNames[i].equals(Constants.VALUE_NULL)){
				continue;
			}
			Class paramClass = null;
			try {
				paramClass = Class.forName(paramClassNames[i]);
				
			} catch (ClassNotFoundException e) {
				throw new RemoteException("no class:"+paramClassNames[i]+" for parameter");
			}
			if(paramClass == this.parameterClasses[i]){
				k++;
			}
			if(!this.parameterClasses[i].isAssignableFrom(paramClass)){
				return -1;
			}
		}
		return k;
	}
	
	public Method getMethod(){
		return this.method;
	}

	/**
	 * @return the service
	 */
	public Object getService() {
		return service;
	}
}
