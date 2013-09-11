package com.yhd.arch.photon.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.util.Timeout;

import com.yhd.arch.photon.common.PackagedMessage;
import com.yhd.arch.photon.common.RemoteRequest;
import com.yhd.arch.photon.common.ResponseFactory;
import com.yhd.arch.photon.event.status.IStatusEvent;
import com.yhd.arch.photon.event.status.method.EnableEvent;
import com.yhd.arch.photon.event.status.method.InvokeTimeoutEvent;
import com.yhd.arch.photon.router.DefaultRouterManager;
import com.yhd.arch.photon.router.RouteeMeta;
import com.yhd.arch.photon.usage.ClientActorUsage;

public class MethodActor extends UntypedActor {
	// private final LoggingAdapter log =
	// Logging.getLogger(getContext().system(), this);
	private ActorRef remoteActor = null;
	private final MethodActorStatusManager manager;
	private final RouteeMeta routeeMeta;

	public MethodActor(ActorRef remoteActor, MethodActorStatusManager manager, RouteeMeta routeeMeta) {
		this.remoteActor = remoteActor;
		this.manager = manager;
		this.routeeMeta = routeeMeta;
		ClientActorUsage.getInstance().add(this.getContext());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof RemoteRequest) {
			remoteActor.tell(message, ActorRef.noSender());
		} else if (message instanceof PackagedMessage) {
			PackagedMessage msg = (PackagedMessage) message;
			if (manager.isUseFul()) {
				msg.setRemoteStatus(manager.getStatus());
				// hasspace
				if (!ClientActorUsage.getInstance().hasSpace()) {
					msg.getPromise().success(ResponseFactory.createFailResponse("OutOfUsageLimitException:request is over limiter!"));
					return;
				}
				Timeout timeout = new Timeout(msg.getRequest().getReadTimeout());
				ActorRef ackActor = getContext().actorOf(Props.create(AckActor.class, msg, timeout.duration(), remoteActor));
				remoteActor.tell(msg.getRequest(), ackActor);
			} else {
				if (msg.getRequest().isRetryAble()) {
					DefaultRouterManager.getInstance().selectRouter(routeeMeta).tell(message, ActorRef.noSender());
				}
			}
		} else if (message instanceof IStatusEvent) {
			if (message instanceof InvokeTimeoutEvent) {
				if (!MethodActorStatus.TEMPORARY_DISABLE.equals(manager.getStatus())) {
					manager.changeStatus(MethodActorStatus.TEMPORARY_DISABLE);
				}
			} else if (message instanceof EnableEvent) {
				if (!MethodActorStatus.ENABLE.equals(manager.getStatus())) {
					manager.changeStatus(MethodActorStatus.ENABLE);
				}
			}
		} else {
			unhandled(message);
		}
	}

	@Override
	public void postStop() throws Exception {
		ClientActorUsage.getInstance().remove(this.getContext());
		super.postStop();
	}

}
