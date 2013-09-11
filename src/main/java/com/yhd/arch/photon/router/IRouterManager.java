package com.yhd.arch.photon.router;

import akka.actor.ActorRef;

public interface IRouterManager {
	public void buildRouter(RouteeMeta meta);

	public ActorRef selectRouter(RouteeMeta methodMeta);

	public void addMethodRoutee(RouteeMeta methodMeta, boolean autoRebuild);

	public void removeMethodRoutee(RouteeMeta methodMeta, boolean autoRebuild);

}
