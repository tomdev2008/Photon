package com.yhd.arch.photon.router;

import akka.actor.UntypedActor;

public class PrintlnActor extends UntypedActor {

	@Override
	public void onReceive(Object arg0) throws Exception {
		System.out.println(arg0 + "," + getSelf());
	}

}
