/**
 * 
 */
package com.yhd.arch.photon.router.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yhd.arch.photon.router.RouteeMeta;

/**
 * @author Archer
 * 
 */
public class AppContainer implements Container<RouteeMeta> {

	private Map<String, ServiceContainer> serviceMap = new ConcurrentHashMap<String, ServiceContainer>();

	@Override
	public void putMeta(RouteeMeta meta) {
		ServiceContainer sc = serviceMap.get(meta.getClazzName());
		if (sc != null) {
			sc.putMeta(meta);
		} else {
			sc = new ServiceContainer(meta);
		}
		serviceMap.put(meta.getClazzName(), sc);
	}

	@Override
	public void removeMeta(RouteeMeta meta) {
		ServiceContainer sc = serviceMap.get(meta.getClazzName());
		if (sc != null) {
			sc.removeMeta(meta);
		}
	}

	@Override
	public Iterable<RouteeMeta> getLeafs(RouteeMeta meta) {
		Iterable<RouteeMeta> leafs = null;
		ServiceContainer sc = serviceMap.get(meta.getClazzName());
		if (sc != null) {
			leafs = sc.getLeafs(meta);
		}
		return leafs;
	}

	@Override
	public Iterable<RouteeMeta> getMixupLeafs(RouteeMeta meta) {
		Iterable<RouteeMeta> leafs = null;
		ServiceContainer sc = serviceMap.get(meta.getClazzName());
		if (sc != null) {
			leafs = sc.getMixupLeafs(meta);
		}
		return leafs;
	}
}
