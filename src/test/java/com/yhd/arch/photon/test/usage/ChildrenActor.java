package com.yhd.arch.photon.test.usage;

import akka.actor.UntypedActor;

public class ChildrenActor  extends UntypedActor{

	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message.equals("child"))
		{
			System.out.println("has created!");
		}else if(message.equals("kill"))
		{
			this.getContext().stop(getSelf());
		}
	}

}
