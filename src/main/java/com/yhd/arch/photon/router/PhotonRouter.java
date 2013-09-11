/**
 * 
 */
package com.yhd.arch.photon.router;

import scala.collection.immutable.Seq;
import akka.actor.ActorRef;
import akka.actor.SupervisorStrategy;
import akka.dispatch.Dispatchers;
import akka.routing.CustomRoute;
import akka.routing.CustomRouterConfig;
import akka.routing.Destination;
import akka.routing.RouteeProvider;

import com.yhd.arch.photon.exception.RemoteException;
import com.yhd.arch.photon.router.balancer.ILoadBalancer;
import com.yhd.arch.photon.router.balancer.WRRBalancer;

/**
 * @author Archer
 * 
 */
public class PhotonRouter extends CustomRouterConfig {

	private Iterable<RouteeMeta> _routees;
	private ILoadBalancer<RouteeMeta, ActorRef> _balancer;
	private RouteeMeta _meta;

	public PhotonRouter(Iterable<RouteeMeta> routees, RouterType type) {
		this._routees = routees;
		this._balancer = new WRRBalancer(routees);
	}

	public PhotonRouter(Iterable<RouteeMeta> routees, RouterType weightRoundrobin, RouteeMeta meta) {
		this._routees = routees;
		this._balancer = new WRRBalancer(routees);
		this._meta = meta;

	}

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
		routeeProvider.registerRoutees(RouteeUtil.convert2ActorList(_routees));
		return new CustomRoute() {

			@Override
			public Seq<Destination> destinationsFor(ActorRef sender, Object msg) {
				ActorRef a = _balancer.select();
				if (a != null) {
					return akka.japi.Util.immutableSingletonSeq(new Destination(sender, _balancer.select()));
				} else {
					throw new RemoteException("Can't find remote node for " + _meta);
				}

			}
		};
	}
}
