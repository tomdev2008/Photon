/**
 * 
 */
package com.yhd.arch.photon.router;

import junit.framework.TestCase;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.typesafe.config.ConfigFactory;
import com.yhd.arch.photon.router.container.MethodContainer;

/**
 * @author Archer
 * 
 */
public class TestGetLeaves extends TestCase {
	public void testLeaf() throws InterruptedException {
		ActorSystem system = ActorSystem.create("TestRouter", ConfigFactory.load());
		ActorRef a0 = system.actorOf(Props.create(PrintlnActor.class), "a0");
		ActorRef a1 = system.actorOf(Props.create(PrintlnActor.class), "a1");
		ActorRef a2 = system.actorOf(Props.create(PrintlnActor.class), "a2");
		ActorRef a3 = system.actorOf(Props.create(PrintlnActor.class), "a3");
		ActorRef a4 = system.actorOf(Props.create(PrintlnActor.class), "a4");
		ActorRef a5 = system.actorOf(Props.create(PrintlnActor.class), "a5");
		RouteeMeta meta = new RouteeMeta("aaa", "mm", "0", a0);
		MethodContainer mc = new MethodContainer(meta);
		RouteeMeta m1 = new RouteeMeta("aaa", "mm", "1", a1);
		m1.setWeight(1);
		RouteeMeta m2 = new RouteeMeta("aaa", "mm", "2", a2);
		m2.setWeight(2);
		RouteeMeta m3 = new RouteeMeta("aaa", "mm", "3", a3);
		m3.setWeight(3);
		RouteeMeta m4 = new RouteeMeta("aaa", "mm", "4", a4);
		m4.setWeight(1);
		RouteeMeta m5 = new RouteeMeta("aaa", "mm", "5", a5);
		m5.setWeight(1);
		mc.putMeta(m1);
		mc.putMeta(m2);
		mc.putMeta(m3);
		mc.putMeta(m4);
		mc.putMeta(m5);
		Iterable<RouteeMeta> l = mc.getMixupLeafs(meta);
		PhotonRouter pr = new PhotonRouter(l, RouterType.WEIGHT_ROUNDROBIN);
		ActorRef ch = system.actorOf(Props.empty().withRouter(pr), "customRouter");
		loop(ch);
		Thread.sleep(10000000);
	}

	private void loop(ActorRef r) {
		int count = 30;
		for (int i = 1; i <= count; i++) {
			r.tell(i, ActorRef.noSender());
		}
	}
}
