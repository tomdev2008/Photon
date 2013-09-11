package com.yhd.arch.photon.actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.Promise;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;

import com.yhd.arch.photon.common.PackagedMessage;
import com.yhd.arch.photon.common.RemoteRequest;
import com.yhd.arch.photon.common.RemoteResponse;
import com.yhd.arch.photon.common.ResponseFactory;
import com.yhd.arch.photon.event.status.method.ActorStatusChange;
import com.yhd.arch.photon.router.DefaultRouterManager;

public class AckActor extends UntypedActor {

	private Promise<RemoteResponse> promise;
	private PackagedMessage msg;
	private ActorRef router;
	private RemoteRequest request;
	private static Logger logger = LoggerFactory.getLogger(AckActor.class);
	private ActorRef remoteActor;

	public AckActor(PackagedMessage message, Duration duration, ActorRef remoteRef) {
		this.msg = message;
		this.promise = this.msg.getPromise();
		this.request = this.msg.getRequest();
		this.remoteActor = remoteRef;
		this.getContext().setReceiveTimeout(duration);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if (message instanceof RemoteResponse) {
			RemoteResponse response = (RemoteResponse) message;
			this.promise.success(response);
			if (MethodActorStatus.TEMPORARY_DISABLE.equals(msg.getRemoteStatus())) {
				getContext().parent().tell(ActorStatusChange.ENABLEEVENT, ActorRef.noSender());
			}
			getContext().stop(getSelf());
		} else if (message.equals(ReceiveTimeout.getInstance())) {
			logger.error("Request to " + remoteActor.toString() + " read timeout; " + this.request);
			if (request.isRetryAble()) {
				this.router = DefaultRouterManager.getInstance().selectRouter(this.request.getRouteeMeta());
				getContext().parent().tell(ActorStatusChange.TIMEOUTEVENT, ActorRef.noSender());
				router.tell(msg, ActorRef.noSender());
			} else {
				this.promise.success(ResponseFactory.createTimeOutResponse("TimeOutException::Request read timeout"));
			}
			getContext().stop(getSelf());
		}
	}

}
