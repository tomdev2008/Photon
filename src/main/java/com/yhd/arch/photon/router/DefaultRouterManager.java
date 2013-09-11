package com.yhd.arch.photon.router;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.routing.BroadcastRouter;
import akka.routing.ConsistentHashingRouter;
import akka.routing.RoundRobinRouter;
import akka.routing.RouterConfig;
import akka.routing.SmallestMailboxRouter;

import com.yhd.arch.photon.router.container.AppContainer;
import com.yhd.arch.photon.util.ActorSystemUtil;

public class DefaultRouterManager implements IRouterManager {

	private static IRouterManager _routerManager = new DefaultRouterManager();
	private static AppContainer _appContainer = new AppContainer();
	private static Map<String, ActorRef> _routerMap = new ConcurrentHashMap<String, ActorRef>();
	private static Logger logger = LoggerFactory.getLogger(DefaultRouterManager.class);

	private DefaultRouterManager() {

	}

	public static IRouterManager getInstance() {
		return _routerManager;
	}

	@Override
	public void buildRouter(RouteeMeta meta) {
		Iterable<RouteeMeta> olist = _appContainer.getLeafs(meta);
		Iterable<ActorRef> aList = RouteeUtil.convert2ActorList(olist);
		RouterConfig r = null;
		if (olist != null && olist.iterator().hasNext()) {
			ActorSystem system = ActorSystemUtil.getActorSystem();
			RouterType type = meta.getType();
			switch (type) {
			case ROUNDROBIN:
				r = RoundRobinRouter.create(aList);
				break;
			case WEIGHT_ROUNDROBIN:
				olist = _appContainer.getMixupLeafs(meta);
				r = new PhotonRouter(olist, RouterType.WEIGHT_ROUNDROBIN, meta);
				break;
			case CONSISTENT_HASH:
				r = ConsistentHashingRouter.create(aList);
				break;
			case BRODCAST:
				r = BroadcastRouter.create(aList);
				break;
			case LEAST_USAGE:
				r = SmallestMailboxRouter.create(aList);
			default:
				r = SmallestMailboxRouter.create(aList);
				break;
			}
			ActorRef rActor = system.actorOf(Props.empty().withRouter(r));
			ActorRef oldRouter = _routerMap.get(meta.getUniqueName());
			_routerMap.put(meta.getUniqueName(), rActor);
			if (oldRouter != null) {
				oldRouter.tell(PoisonPill.getInstance(), ActorRef.noSender());
			}
		} else {
			_routerMap.put(meta.getUniqueName(), ActorSystemUtil.emptyRouter());
		}
	}

	@Override
	public ActorRef selectRouter(RouteeMeta methodMeta) {
		ActorRef ref = _routerMap.get(methodMeta.getUniqueName());
		long timeout = System.currentTimeMillis() + ActorSystemUtil.getInvokeLag();
		if (isEmptyRouter(ref)) {
			while (System.currentTimeMillis() < timeout) {
				Thread.yield();
				ref = _routerMap.get(methodMeta.getUniqueName());
			}
		}
		return ref;
	}

	private boolean isEmptyRouter(ActorRef ref) {
		return (ref == null || ActorSystemUtil.emptyRouter().equals(ref));
	}

	@Override
	public void addMethodRoutee(RouteeMeta methodMeta, boolean autoRebuild) {
		try {
			_appContainer.putMeta(methodMeta);
			if (autoRebuild) {
				buildRouter(methodMeta);
			}
		} catch (Exception e) {
			logger.error("Add routee failed!!! " + methodMeta.toString(), e);
		}

	}

	@Override
	public void removeMethodRoutee(RouteeMeta methodMeta, boolean autoRebuild) {
		try {
			_appContainer.removeMeta(methodMeta);
			if (autoRebuild) {
				buildRouter(methodMeta);
			}
		} catch (Exception e) {
			logger.error("Remove routee failed!!! " + methodMeta.toString());
		}

	}

}
