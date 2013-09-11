/**
 * 
 */
package com.yhd.arch.photon.actor;

import com.yhd.arch.photon.router.RouteeMeta;

/**
 * @author Archer
 * 
 */
public class MethodActorStatusManager implements IStatusManager<MethodActorStatus> {

	private IRelivePolicy _policy;
	private RouteeMeta _meta;

	public MethodActorStatusManager(RouteeMeta meta) {
		super();
		this._meta = meta;
		_policy = new MethodActorRelivePolicy();
	}

	@Override
	public void changeStatus(MethodActorStatus status) {
		this._meta.setStatus(status);

	}

	@Override
	public boolean isUseFul() {
		boolean value = false;
		if (this._meta.getStatus().equals(MethodActorStatus.ENABLE)) {
			value = true;
		} else if (this._meta.getStatus().equals(MethodActorStatus.TEMPORARY_DISABLE) && _policy.isLive()) {
			value = true;
		}
		return value;
	}

	public MethodActorStatus getStatus() {
		return this._meta.getStatus();
	}

}
