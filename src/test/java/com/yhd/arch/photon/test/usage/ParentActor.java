package com.yhd.arch.photon.test.usage;

import com.yhd.arch.photon.usage.ClientActorUsage;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class ParentActor extends UntypedActor {

	public ParentActor()
	{
		 ClientActorUsage.getInstance().add(this.getContext());
	}
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		 if(message.equals("child"))
		 {
			 ActorRef childActor= getContext().actorOf(Props.create(ChildrenActor.class));
			 childActor.tell("child", this.getSelf());
			 //childActor.tell("kill", this.getSelf());
		 }
	}

}
