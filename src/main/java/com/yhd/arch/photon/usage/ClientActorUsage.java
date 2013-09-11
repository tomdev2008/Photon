package com.yhd.arch.photon.usage;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

import akka.actor.ActorContext;

public class ClientActorUsage {

	private static ClientActorUsage instance = new ClientActorUsage();
	private final UsageCapacity limiter;
	private final CopyOnWriteArraySet<ActorContext> actorSet;
	private volatile long curCount;

	private ClientActorUsage() {
		limiter = new DefaultUsageCapacity();
		actorSet = new CopyOnWriteArraySet<ActorContext>();
	}

	public static ClientActorUsage getInstance() {
		return instance;
	}

	public boolean hasSpace() {
		return !limiter.isLimit(curCount);
	}

	public long calculate() {
		long size = 0;
		Iterator<ActorContext> it = actorSet.iterator();
		while (it.hasNext()) {
			ActorContext ac = it.next();
			size = size + ac.children().size();

		}
		return size;
	}

	public UsageCapacity getLimiter() {
		return limiter;
	}

	public boolean add(ActorContext actor) {
		boolean b = actorSet.add(actor);
		if (b) {
			curCount = calculate();
		}
		return b;
	}

	public boolean remove(ActorContext actor) {
		boolean b = actorSet.remove(actor);
		if (b) {
			curCount = calculate();
		}
		return b;
	}
}
