package com.yhd.arch.photon.invoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.Future;
import scala.concurrent.Promise;

import akka.dispatch.Futures;

import com.yhd.arch.photon.common.Constants;
import com.yhd.arch.photon.common.FutureFactory;
import com.yhd.arch.photon.common.RemoteMetaData;
import com.yhd.arch.photon.common.RemoteRequest;
import com.yhd.arch.photon.common.RemoteResponse;
import com.yhd.arch.photon.exception.PhotonException;

class ProxyInvoker implements InvocationHandler {

	private static Logger logger = LoggerFactory.getLogger(ProxyInvoker.class);
	private RemoteMetaData metaData;
	private Set<String> ingoreMethods = new HashSet<String>();

	@SuppressWarnings("rawtypes")
	public ProxyInvoker(RemoteMetaData metaData) {
		this.metaData = metaData;
		Method[] objectMethodArray = Object.class.getMethods();
		for (Method method : objectMethodArray) {
			this.ingoreMethods.add(method.getName());
		}
		// init remote connects
		Class objType = this.metaData.getObjType();
		for (String host : this.metaData.getHostList()) {
			for (Method method : objType.getMethods()) {
				if (!this.ingoreMethods.contains(method.getName())) {
					ActorInvoker.getInstance().register(host, this.metaData.getServiceName(), method);
				}
			}
		}
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		if (method.getName().equals("toString")) {
			return proxy.getClass().getName();
		} else if (method.getName().equals("equals")) {
			if (args == null || args.length != 1 || args[0].getClass() != proxy.getClass()) {
				return false;
			}
			return method.equals(args[0].getClass().getDeclaredMethod("equals", new Class[] { Object.class }));
		} else if (method.getName().equals("hashCode")) {
			return method.hashCode();
		}
		RemoteRequest request = new DefaultRequest(this.metaData, method, args);

		if (Constants.CALL_SYNC.equalsIgnoreCase(this.metaData.getCallMode())) {
			// 同步返回
			RemoteResponse response = null;
			try {
				response = ActorInvoker.getInstance().invokeSync(request);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
			if (response == null)
				return null;
			if (response.getMessageType() == Constants.MESSAGE_TYPE_SERVICE) {
				return response.getReturn();
			} else if (response.getMessageType() == Constants.MESSAGE_TYPE_EXCEPTION) {
				throw new PhotonException(response.getCause());
			} else if (response.getMessageType() == Constants.MESSAGE_TYPE_SERVICE_EXCEPTION) {
				throw (Throwable) response.getReturn();
			}
			throw new PhotonException("no result to call");
		} else if (Constants.CALL_FUTURE.equals(this.metaData.getCallMode())) {
			// future set
			try {
				Promise<RemoteResponse> promise = Futures.promise();
				Future<RemoteResponse> future = promise.future();
				FutureFactory.setFuture(future);
				ActorInvoker.getInstance().invokeFuture(request,promise);
				
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				FutureFactory.remove();
				throw e;
			}

			return getVirReturnForOneWay(method.getReturnType());
		} else if (Constants.CALL_ONEWAY.equals(this.metaData.getCallMode())) {
			// 执行不返回
			ActorInvoker.getInstance().invokeOneWay(request);
			return getVirReturnForOneWay(method.getReturnType());
		}
		throw new PhotonException("callmethod configure is error:" + this.metaData.getCallMode());
	}

	@SuppressWarnings("rawtypes")
	private Object getVirReturnForOneWay(Class returnType) {
		if (returnType == byte.class) {
			return (byte) 0;
		} else if (returnType == short.class) {
			return (short) 0;
		} else if (returnType == int.class) {
			return 0;
		} else if (returnType == boolean.class) {
			return false;
		} else if (returnType == long.class) {
			return 0l;
		} else if (returnType == float.class) {
			return 0.0f;
		} else if (returnType == double.class) {
			return 0.0d;
		} else {
			return null;
		}
	}

}
