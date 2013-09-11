/**
 * 
 */
package com.yhd.arch.photon.router.helper;

import java.util.Comparator;

import com.yhd.arch.photon.router.RouteeMeta;

/**
 * @author Archer
 * 
 */
public class RouteeMixupHelper extends MixupHelper<RouteeMeta, RouteeMeta> {

	public RouteeMixupHelper(Comparator<RouteeMeta> comparator) {
		super(comparator);
	}

	@Override
	public int getLoop(RouteeMeta meta) {
		return meta.getWeight();
	}

	@Override
	public RouteeMeta getValue(RouteeMeta meta) {
		return meta;
	}

}
