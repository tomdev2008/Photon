/**
 * 
 */
package com.yhd.arch.photon.util;

import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.Props;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.yhd.arch.photon.actor.DeadLetterActor;
import com.yhd.arch.photon.common.Constants;
import com.yhd.arch.photon.router.RouterManagerActor;

/**
 * @author Archer
 * 
 */
public class ActorSystemUtil {

	private static ActorSystemUtil SYSTEMUTIL = new ActorSystemUtil();
	private static ActorSystem _system;
	private static ActorRef _routerManagerActor;
	private static long DEFAULT_INVOKE_LAG = 1000l;
	private static ActorRef _emptyRouter;

	private ActorSystemUtil() {
		Map<String, String> parseMap = new HashMap<String, String>();
		String hostName = SystemUtil.getFirstNoLoopbackIP4Address();
		parseMap.put("akka.remote.netty.tcp.hostname", hostName);
		parseMap.put("akka.remote.netty.tcp.port", "0");
		Config cf = ConfigFactory.parseMap(parseMap).withFallback(ConfigFactory.load().getConfig("client"));
		_system = ActorSystem.create(Constants.ClientAkkaSystemName, cf);
		_routerManagerActor = _system.actorOf(Props.create(RouterManagerActor.class));
		_emptyRouter = _system.actorOf(Props.empty(), "EmptyRouter");
		ActorRef actor = _system.actorOf(Props.create(DeadLetterActor.class));
		_system.eventStream().subscribe(actor, DeadLetter.class);
	}

	public static ActorSystem getActorSystem() {
		return _system;
	}

	public static long getInvokeLag() {
		return DEFAULT_INVOKE_LAG;
	}

	public static ActorRef getRouterManager() {
		return _routerManagerActor;
	}

	public static ActorRef emptyRouter() {
		return _emptyRouter;
	}

}
