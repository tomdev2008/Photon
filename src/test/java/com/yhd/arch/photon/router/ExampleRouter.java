package com.yhd.arch.photon.router;

import scala.collection.immutable.Seq;
import akka.actor.ActorRef;
import akka.actor.SupervisorStrategy;
import akka.dispatch.Dispatchers;
import akka.routing.CustomRoute;
import akka.routing.CustomRouterConfig;
import akka.routing.Destination;
import akka.routing.RouteeProvider;

public class ExampleRouter extends CustomRouterConfig {

	@Override
	public String routerDispatcher() {
		return Dispatchers.DefaultDispatcherId();
	}

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return SupervisorStrategy.defaultStrategy();
	}

	@Override
	public CustomRoute createCustomRoute(final RouteeProvider routeeProvider) {
		return new CustomRoute() {

			@Override
			public Seq<Destination> destinationsFor(ActorRef sender, Object msg) {
				java.util.List<ActorRef> routees = routeeProvider.getRoutees();
				return akka.japi.Util.immutableSingletonSeq(new Destination(sender, routees.get(0)));
			}
		};
	}

	public static void main(String[] args) {
		System.out.println(ActorRef.noSender().equals(ActorRef.noSender()));
	}
}