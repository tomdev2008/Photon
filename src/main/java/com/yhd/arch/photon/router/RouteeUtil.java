/**
 * 
 */
package com.yhd.arch.photon.router;

import java.util.LinkedList;

import akka.actor.ActorRef;

/**
 * @author Archer
 * 
 */
public class RouteeUtil {

	public static Iterable<ActorRef> convert2ActorList(Iterable<RouteeMeta> routeeList) {
		LinkedList<ActorRef> list = new LinkedList<ActorRef>();
		if (routeeList != null) {
			for (RouteeMeta r : routeeList) {
				list.add(r.getActor());
			}
		}
		return list;
	}
}
