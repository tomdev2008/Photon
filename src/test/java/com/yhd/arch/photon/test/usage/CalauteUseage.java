package com.yhd.arch.photon.test.usage;

import org.junit.Test;

import com.yhd.arch.photon.usage.ClientActorUsage;
import com.yhd.arch.photon.util.ActorSystemUtil;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class CalauteUseage {
	private ActorSystem system=ActorSystemUtil.getActorSystem() ;
	@Test
	public void ShowSize() throws Exception
	{
		ActorRef pt= system.actorOf(Props.create(ParentActor.class),"test");
		System.out.println(ClientActorUsage.getInstance().calculate());
		for(int i=0;i<100;i++)
		{
				pt.tell("child", ActorRef.noSender());
		}
		Thread.sleep(1000);
		System.out.println(ClientActorUsage.getInstance().calculate());
	}
}
