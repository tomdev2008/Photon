/**
 * 
 */
package com.yhd.arch.photon.router;

import akka.actor.UntypedActor;

import com.yhd.arch.photon.event.command.route.AddRouteeEvent;
import com.yhd.arch.photon.event.command.route.DelRouteeEvent;

/**
 * @author Archer
 * 
 */
public class RouterManagerActor extends UntypedActor {

	IRouterManager _manager = DefaultRouterManager.getInstance();

	@Override
	public void onReceive(Object obj) throws Exception {
		if (obj != null) {
			if (obj instanceof AddRouteeEvent) {
				AddRouteeEvent e = (AddRouteeEvent) obj;
				_manager.addMethodRoutee(e.getMeta(), true);
			} else if (obj instanceof DelRouteeEvent) {
				DelRouteeEvent e = (DelRouteeEvent) obj;
				_manager.removeMethodRoutee(e.getMeta(), true);
			}
		}
	}
}
