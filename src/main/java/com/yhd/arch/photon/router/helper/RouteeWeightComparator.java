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
public class RouteeWeightComparator implements Comparator<RouteeMeta> {

	@Override
	public int compare(RouteeMeta o1, RouteeMeta o2) {
		int v = 0;
		if (o1 != null && o2 != null) {
			v = o2.getWeight() - o1.getWeight();
		}
		return v;
	}
}
