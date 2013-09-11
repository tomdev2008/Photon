package com.yhd.arch.photon.common;

import java.lang.reflect.Method;

import com.yhd.arch.photon.exception.PhotonException;
import com.yhd.arch.photon.exception.RemoteException;

import scala.concurrent.Future;
import scala.concurrent.Promise;


public interface Invoker {
	RemoteResponse invokeSync(RemoteRequest request)throws PhotonException;
	public void invokeOneWay(RemoteRequest request);
	public Future<RemoteResponse> invokeFuture(RemoteRequest request)throws RemoteException;
	public void invokeFuture(RemoteRequest request,Promise<RemoteResponse> promise)throws RemoteException;
	public void register(String connect,String serviceName,Method method);
}
