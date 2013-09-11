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
public class ServiceContainer implements Container<RouteeMeta> {

	private String serviceName;
	private Map<String, MethodContainer> methodMap = new ConcurrentHashMap<String, MethodContainer>();

	public ServiceContainer(RouteeMeta meta) {
		this.serviceName = meta.getClazzName();
		putMeta(meta);
	}

	public void putMeta(RouteeMeta meta) {
		String n = meta.getMethodName();
		MethodContainer m = methodMap.get(n);
		if (m != null) {
			m.putMeta(meta);
		} else {
			m = new MethodContainer(meta);
		}
		methodMap.put(n, m);
	}

	@Override
	public void removeMeta(RouteeMeta meta) {
		MethodContainer m = methodMap.get(meta.getMethodName());
		if (m != null) {
			m.removeMeta(meta);
		}
	}

	@Override
	public Iterable<RouteeMeta> getLeafs(RouteeMeta meta) {
		Iterable<RouteeMeta> leafs = null;
		MethodContainer container = methodMap.get(meta.getMethodName());
		if (container != null) {
			leafs = container.getLeafs(meta);
		}
		return leafs;
	}

	@Override
	public Iterable<RouteeMeta> getMixupLeafs(RouteeMeta meta) {
		Iterable<RouteeMeta> leafs = null;
		MethodContainer container = methodMap.get(meta.getMethodName());
		if (container != null) {
			leafs = container.getMixupLeafs(meta);
		}
		return leafs;
	}
}
