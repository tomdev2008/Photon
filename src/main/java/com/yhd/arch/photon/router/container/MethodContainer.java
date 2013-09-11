/**
 * 
 */
package com.yhd.arch.photon.router.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.yhd.arch.photon.router.RouteeMeta;
import com.yhd.arch.photon.router.helper.MixupHelper;
import com.yhd.arch.photon.router.helper.RouteeMixupHelper;
import com.yhd.arch.photon.router.helper.RouteeWeightComparator;

/**
 * @author Archer
 * 
 */
public class MethodContainer implements Container<RouteeMeta> {

	private String clazzName;
	private String methodName;
	private Map<String, RouteeMeta> routeeMap = new ConcurrentHashMap<String, RouteeMeta>();
	private Random r = new Random();
	private static Comparator<RouteeMeta> routeeComparator = new RouteeWeightComparator();
	private static MixupHelper<RouteeMeta, RouteeMeta> mixuper = new RouteeMixupHelper(routeeComparator);

	public MethodContainer(RouteeMeta meta) {
		this.clazzName = meta.getClazzName();
		this.methodName = meta.getMethodName();
		putMeta(meta);
	}

	public void putMeta(RouteeMeta meta) {
		routeeMap.put(meta.getActorId(), meta);
	}

	public void removeMeta(RouteeMeta meta) {
		routeeMap.remove(meta.getActorId());
	}

	@Override
	public Iterable<RouteeMeta> getLeafs(RouteeMeta meta) {
		List<RouteeMeta> rList = new ArrayList<RouteeMeta>();
		Collection<RouteeMeta> c = routeeMap.values();
		if (c != null) {
			for (RouteeMeta m : c) {
				if (m != null) {
					rList.add(m);
				}
			}
		}
		return rList;
	}

	@Override
	public Iterable<RouteeMeta> getMixupLeafs(RouteeMeta meta) {
		return mixuper.mixupCollection(routeeMap.values());
	}

}
