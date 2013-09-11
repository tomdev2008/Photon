package com.yhd.arch.photon.common;

import akka.actor.ActorRef;

import com.yhd.arch.photon.router.RouteeMeta;

public interface IRouterManager {
	void addMethodRoutee(RouteeMeta RouteeMeta, boolean autoRebuild);

	void removeMethodRoutee(RouteeMeta RouteeMeta, boolean autoRebuild);

	void buildRouter(RouteeMeta RouteeMeta);

	ActorRef selectRouter(RouteeMeta RouteeMeta);
}
