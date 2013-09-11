package com.yhd.arch.photon.router.container;

import com.yhd.arch.photon.router.RouteeMeta;

public interface Container<T> {

	public void putMeta(RouteeMeta meta);

	public void removeMeta(RouteeMeta meta);

	public Iterable<T> getLeafs(RouteeMeta meta);

	public Iterable<T> getMixupLeafs(RouteeMeta meta);

}
