package com.yhd.arch.photon.common;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.util.Timeout;

import com.yhd.arch.photon.exception.PhotonException;
import com.yhd.arch.photon.util.ActorSystemUtil;

import scala.concurrent.Await;
import scala.concurrent.Future;

public class FutureFactory {
	private static Logger logger = LoggerFactory.getLogger(FutureFactory.class);
	private static ThreadLocal<Future<RemoteResponse>>  threadFuture = new ThreadLocal<Future<RemoteResponse>>();
	
	public static Future<RemoteResponse> getFuture()
	{
		Future<RemoteResponse> future=threadFuture.get();
		threadFuture.remove();
		return future;
	}
	public static void setFuture(Future<RemoteResponse> future) throws PhotonException{
		if(threadFuture.get() != null){
			threadFuture.remove();
			String msg = "you must call \"ServiceFutureFactory.getFuture()\" before second call service if you use future call";
			logger.error(msg);
			throw new PhotonException(msg);
		}
		threadFuture.set(future);
	}
	
	public static void remove(){
		threadFuture.remove();
	}

	public static <T> T getResult(Class<T> res) throws Exception
	{
		Timeout timeout = new Timeout(2,TimeUnit.SECONDS);
		return getResult(res,timeout);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getResult(Class<T> res,Timeout timeout) throws Exception{
		return (T)getResult(timeout);
	}
	
	public static Object getResult(Timeout timeout) throws Exception{
		RemoteResponse result=	(RemoteResponse) Await.result(getFuture(),timeout.duration());
		return result.getReturn();
	}
	public static <T> T getResultFromResponse(Class<T> res,Future<RemoteResponse> fr) throws Exception
	{
		RemoteResponse response= getExternalResult(fr);
		return (T)response.getReturn();
	}
	public static <T> T getResultFromResponse(Class<T> res,Future<RemoteResponse> fr,Timeout timeout) throws Exception
	{
		RemoteResponse response= getExternalResult(fr,timeout);
		return (T)response.getReturn();
	}
	public static  <T> T getExternalResult(Future<T> fr) throws Exception
	{
		Timeout timeout = new Timeout(2,TimeUnit.SECONDS);
		return getExternalResult(fr,timeout);
	}
	public static  <T> T getExternalResult(Future<T> fr,Timeout timeout) throws Exception
	{
		return Await.result(fr,timeout.duration());
	}
	public static <T> Future<T> getMergeFuture(List<Future<RemoteResponse>> futureList,final MergeResultHandle<T> handle)
	{
		Future<Iterable<RemoteResponse>> future = Futures.sequence(futureList,ActorSystemUtil.getActorSystem().dispatcher());
		Future<T> fr= future.map((new Mapper<Iterable<RemoteResponse>,T>(){
			 @Override
	         public T apply(Iterable<RemoteResponse> parameter) {
				 T result=null;
				 result= handle.handle(parameter);
				 return result;
			 }
		}),ActorSystemUtil.getActorSystem().dispatcher());
		return fr;
	}
}
