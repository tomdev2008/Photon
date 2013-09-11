package com.yhd.arch.photon.actor;

import com.yhd.arch.photon.common.Constants;
import com.yhd.arch.photon.common.RemoteRequest;
import com.yhd.arch.photon.repository.ServiceRepository;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.DefaultResizer;
import akka.routing.Resizer;
import akka.routing.SmallestMailboxRouter;

public class ServiceMethodActor  extends UntypedActor {

	private ActorRef routerWorkerActor;
	
	public ServiceMethodActor(ServiceRepository sr,int workerActorCounts)
	{
		int lowerBound = Constants.DEFAULT_RESIZER_LOWBOUND;
		int upperBound = workerActorCounts;
		Resizer resizer=new DefaultResizer(lowerBound, upperBound);
		this.routerWorkerActor=this.getContext().actorOf(Props.create(WorkerActor.class, sr).withRouter(new SmallestMailboxRouter(resizer)));
	}
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof RemoteRequest)
		{
			RemoteRequest msg=(RemoteRequest)message;
			msg.createMillisTime();
			this.routerWorkerActor.tell(msg, getSender());
		}
	}

}
