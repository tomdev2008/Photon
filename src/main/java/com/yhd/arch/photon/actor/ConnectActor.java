package com.yhd.arch.photon.actor;

import scala.concurrent.duration.Duration;
import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.Identify;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;

import com.yhd.arch.photon.event.command.route.AddRouteeEvent;
import com.yhd.arch.photon.router.RouteeMeta;
import com.yhd.arch.photon.util.ActorSystemUtil;

public class ConnectActor  extends UntypedActor {

	private boolean reconnect = false;
	private int _dely = 1;
	private String path;
	private RouteeMeta routeeMeta;
	private ActorRef remoteActor = null;
	private MethodActorStatusManager manager;

	public ConnectActor(RouteeMeta routeeMeta)
	{
		this.routeeMeta = routeeMeta;
		this.path = this.routeeMeta.getActorId();
		this.manager = new MethodActorStatusManager(routeeMeta);
		this.reconnect = false;
		this._dely = 1;
		sendIdentifyRequest();
	}

	private void sendIdentifyRequest() {
		getContext().actorSelection(path).tell(new Identify(path), getSelf());
	}
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if (message instanceof ActorIdentity) {

			remoteActor = ((ActorIdentity) message).getRef(); // connect，添加router
			if (remoteActor != null) {
				this.getContext().watch(this.remoteActor);
				ActorRef clientMethod = this.getContext().actorOf(
						Props.create(MethodActor.class, this.remoteActor, this.manager, this.routeeMeta));
				this.routeeMeta.setActor(clientMethod);
				ActorSystemUtil.getRouterManager().tell(new AddRouteeEvent(this.routeeMeta), ActorRef.noSender());
				manager.changeStatus(MethodActorStatus.ENABLE);
				reconnect = false;
				_dely = 1;
			} else {
				_dely = _dely * 2;
				getContext().system().scheduler().scheduleOnce(Duration.create(_dely, "seconds"), new Runnable() {

					@Override
					public void run() {
						sendIdentifyRequest();
					}
				}, getContext().dispatcher());

			}
		} else if (message instanceof Terminated) {
			System.out.println(message);
			final Terminated t = (Terminated) message;
			if (t.getActor().equals(remoteActor)) {
				// router remove
				manager.changeStatus(MethodActorStatus.DISABLE);
				if (!reconnect) {
					sendIdentifyRequest();
					reconnect = true;
				}
				// getContext().stop(getSelf());
			}
		} else {
			unhandled(message);
		}

	}

}
