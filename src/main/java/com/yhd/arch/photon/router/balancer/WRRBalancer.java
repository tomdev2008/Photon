/**
 * 
 */
package com.yhd.arch.photon.router.balancer;

import akka.actor.ActorRef;

import com.yhd.arch.photon.actor.MethodActorStatus;
import com.yhd.arch.photon.common.Constants;
import com.yhd.arch.photon.router.RouteeMeta;

/**
 * @author Archer
 * 
 */
public class WRRBalancer extends AbstractBalancer<RouteeMeta, ActorRef> {

	public WRRBalancer(Iterable<RouteeMeta> routees) {
		super();
		update(routees);
	}

	@Override
	protected ActorRef doSelect() {
		int key = position.getAndIncrement();
		int totalSize = _circle.size();
		int realPos = key % totalSize;
		if (key > Constants.INTEGER_BARRIER) {
			position.set(0);
		}
		return getValueFromCircle(realPos);
	}

	@Override
	protected ActorRef getValue(RouteeMeta t) {
		ActorRef r = ActorRef.noSender();
		if (t != null) {
			r = t.getActor();
		}
		return r;
	}

	@Override
	protected boolean valid(RouteeMeta t) {
		boolean v = false;
		if (t != null && MethodActorStatus.ENABLE.equals(t.getStatus())) {
			v = true;
		}
		return v;
	}

	@Override
	public void update(Iterable<RouteeMeta> list) {
		Circle<Integer, RouteeMeta> circle = new Circle<Integer, RouteeMeta>();
		int size = 0;
		for (RouteeMeta meta : list) {
			circle.put(size++, meta);
		}
		_circle = circle;
	}

}
