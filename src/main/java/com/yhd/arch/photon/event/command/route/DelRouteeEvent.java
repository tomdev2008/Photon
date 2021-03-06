/**
 * 
 */
package com.yhd.arch.photon.event.command.route;

import com.yhd.arch.photon.event.command.ICommandEvent;
import com.yhd.arch.photon.router.RouteeMeta;

/**
 * @author Archer
 * 
 */
public class DelRouteeEvent implements ICommandEvent {

	private RouteeMeta meta;

	public DelRouteeEvent(RouteeMeta meta) {
		super();
		this.meta = meta;
	}

	public RouteeMeta getMeta() {
		return meta;
	}

	public void setMeta(RouteeMeta meta) {
		this.meta = meta;
	};

}
