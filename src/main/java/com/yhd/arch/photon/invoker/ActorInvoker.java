package com.yhd.arch.photon.invoker;

import java.lang.reflect.Method;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.Promise;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.util.Timeout;

import com.yhd.arch.photon.actor.ConnectActor;
import com.yhd.arch.photon.common.Constants;
import com.yhd.arch.photon.common.Invoker;
import com.yhd.arch.photon.common.PackagedMessage;
import com.yhd.arch.photon.common.RemoteRequest;
import com.yhd.arch.photon.common.RemoteResponse;
import com.yhd.arch.photon.exception.PhotonException;
import com.yhd.arch.photon.exception.RemoteException;
import com.yhd.arch.photon.router.DefaultRouterManager;
import com.yhd.arch.photon.router.RouteeMeta;
import com.yhd.arch.photon.util.ActorNameUtil;
import com.yhd.arch.photon.util.ActorSystemUtil;
import com.yhd.arch.photon.util.SystemUtil;

public class ActorInvoker implements Invoker {

	public ActorSystem system;
	private static Invoker invoker;

	private ActorInvoker() {
		system = ActorSystemUtil.getActorSystem();

	}

	private synchronized static void createInvoker() {
		if (invoker != null) {
			return;
		}
		invoker = new ActorInvoker();
	}

	public static Invoker getInstance() {
		if (invoker == null) {
			createInvoker();
		}
		return invoker;
	}

	public void register(String connect, String serviceName, Method method) {
		// TODO Auto-generated method stub
		String fullMethodPath = ActorNameUtil.mangleName(method);
		String path = SystemUtil.formatRemoteAddress(connect, serviceName+"_"+fullMethodPath);
		RouteeMeta routeeMeta = new RouteeMeta(serviceName, fullMethodPath, path, null);
		system.actorOf(Props.create(ConnectActor.class, routeeMeta));
	}

	public RemoteResponse invokeSync(RemoteRequest request) throws PhotonException {
		// TODO Auto-generated method stub
		Future<RemoteResponse> future = invokeFuture(request);
		if (future == null)
			return null;
		Timeout timeout = new Timeout(request.getTimeout());
		RemoteResponse response;
		try {
			response = (RemoteResponse) Await.result(future, timeout.duration());
		} catch (Exception e) {
			throw new PhotonException(e.getMessage(), e.getCause());
		}
		return response;
	}

	public Future<RemoteResponse> invokeFuture(RemoteRequest request) throws RemoteException {
		// TODO Auto-generated method stub
		request.setCallType(Constants.CALLTYPE_REPLY);
		ActorRef actor = DefaultRouterManager.getInstance().selectRouter(request.getRouteeMeta());
		if (actor != null && !actor.equals(ActorSystemUtil.emptyRouter())) {
			Promise<RemoteResponse> promise = Futures.promise();
			Future<RemoteResponse> future = promise.future();
			request.createMillisTime();
			PackagedMessage message = new PackagedMessage(request, promise);
			actor.tell(message, ActorRef.noSender());
			return future;
		} else {
			throw new RemoteException("Can't find remote node for " + request.getRouteeMeta());
		}
	}

	@Override
	public void invokeFuture(RemoteRequest request, Promise<RemoteResponse> promise) throws RemoteException {
		// TODO Auto-generated method stub
		request.setCallType(Constants.CALLTYPE_REPLY);
		ActorRef actor = DefaultRouterManager.getInstance().selectRouter(request.getRouteeMeta());
		if (actor != null) {
			request.createMillisTime();
			PackagedMessage message = new PackagedMessage(request, promise);
			actor.tell(message, ActorRef.noSender());
		} else {
			throw new RemoteException("Can't find remote node for " + request.getRouteeMeta());
		}
	}

	public void invokeOneWay(RemoteRequest request) {
		// TODO Auto-generated method stub
		request.setCallType(Constants.CALLTYPE_NOREPLY);
		request.createMillisTime();
		ActorRef actor = DefaultRouterManager.getInstance().selectRouter(request.getRouteeMeta());
		actor.tell(request, ActorRef.noSender());
	}
}
