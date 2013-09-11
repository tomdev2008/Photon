package com.yhd.arch.photon.router;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.BroadcastRouter;
import akka.routing.ConsistentHashingRouter;
import akka.routing.RoundRobinRouter;
import akka.routing.SmallestMailboxRouter;

import com.typesafe.config.ConfigFactory;

public class TestRouter extends TestCase {

	public void testSimpleRR() {
		ActorSystem system = ActorSystem.create("TestRouter", ConfigFactory.load());
		ActorRef roundRobinRouter = system.actorOf(Props.create(PrintlnActor.class).withRouter(new RoundRobinRouter(5)), "router1");
		loop(roundRobinRouter);
	}

	public void testRouterConfig() {
		ActorSystem system = ActorSystem.create("TestRouter", ConfigFactory.load());
		RoundRobinRouter rrr = null;
		ActorRef a1 = system.actorOf(Props.create(PrintlnActor.class), "a1");
		ActorRef a2 = system.actorOf(Props.create(PrintlnActor.class), "a2");
		Iterable<ActorRef> l = Arrays.asList(new ActorRef[] { a1, a2, a2, a2 });
		rrr = RoundRobinRouter.create(l);
		ActorRef roundRobinRouter = system.actorOf(Props.empty().withRouter(rrr), "router2");
		loop(roundRobinRouter);
	}

	public void testSmallesUseage() {
		ActorSystem system = ActorSystem.create("TestRouter", ConfigFactory.load());
		SmallestMailboxRouter smr = null;
		ActorRef a1 = system.actorOf(Props.create(PrintlnActor.class), "a1");
		ActorRef a2 = system.actorOf(Props.create(FasterActor.class), "a2");
		ActorRef a3 = system.actorOf(Props.create(SlowActor.class), "a3");
		Iterable<ActorRef> l = Arrays.asList(new ActorRef[] { a1, a2, a3 });
		smr = SmallestMailboxRouter.create(l);
		ActorRef roundRobinRouter = system.actorOf(Props.empty().withRouter(smr), "router3");
		loop(roundRobinRouter);
	}

	public void testBroadCast() {
		ActorSystem system = ActorSystem.create("TestRouter", ConfigFactory.load());
		ActorRef a1 = system.actorOf(Props.create(PrintlnActor.class), "a1");
		ActorRef a2 = system.actorOf(Props.create(PrintlnActor.class), "a2");
		ActorRef a3 = system.actorOf(Props.create(PrintlnActor.class), "a3");
		Iterable<ActorRef> l = Arrays.asList(new ActorRef[] { a1, a2, a3 });
		BroadcastRouter rc = BroadcastRouter.create(l);
		ActorRef roundRobinRouter = system.actorOf(Props.empty().withRouter(rc));
		loop(roundRobinRouter);
	}

	public void testConsistentHash() {
		ActorSystem system = ActorSystem.create("TestRouter", ConfigFactory.load());
		ActorRef a1 = system.actorOf(Props.create(PrintlnActor.class), "a1");
		ActorRef a2 = system.actorOf(Props.create(PrintlnActor.class), "a2");
		ActorRef a3 = system.actorOf(Props.create(PrintlnActor.class), "a3");
		Iterable<ActorRef> l = Arrays.asList(new ActorRef[] { a1, a2, a3 });
		ConsistentHashingRouter rc = ConsistentHashingRouter.create(l);
		ActorRef ch = system.actorOf(Props.empty().withRouter(rc));
		loop(ch);
	}

	private void loop(ActorRef r) {
		for (int i = 1; i <= 10; i++) {
			r.tell(i, ActorRef.noSender());
		}
	}

	public void testRoundRoubinEffectioncy() {
		runRouter(100000, 2);
		runRouter(100000, 4);
		runRouter(100000, 8);
		runRouter(100000, 16);
		runRouter(100000, 32);
		runRouter(100000, 64);
		runRouter(100000, 128);
		runRouter(100000, 512);
	}

	public static void runRouter(int loopCount, int routerCount) {
		List<ActorRef> routees = new ArrayList<ActorRef>();
		ActorSystem system = ActorSystem.create("TestRouter", ConfigFactory.load());
		for (int i = 0; i < routerCount; i++) {
			ActorRef a = system.actorOf(Props.create(PrintlnActor.class));
			routees.add(a);
		}
		RoundRobinRouter rrr = RoundRobinRouter.create(routees);
		ActorRef r = system.actorOf(Props.empty().withRouter(rrr));
		long start = System.currentTimeMillis();
		while (loopCount > 0) {
			loopCount--;
			r.tell("1", ActorRef.noSender());
		}
		System.out.println(routerCount + " routees Cost:" + (System.currentTimeMillis() - start));
	}

	public void testActorCreate() {
		ActorSystem system = ActorSystem.create("TestRouter", ConfigFactory.load());
		int loop = 100000;
		long start = System.nanoTime();
		for (int i = 0; i < loop; i++) {
			ActorRef a1 = system.actorOf(Props.create(PrintlnActor.class));
		}
		long total = (System.nanoTime() - start);
		System.out.println("create " + loop + " actor cost" + total + " ,per:" + (total / loop));

	}
}
