/**
 * 
 */
package com.yhd.arch.photon.event.status.method;

import com.yhd.arch.photon.event.status.IStatusEvent;

/**
 * @author Archer
 * 
 */
public class ActorStatusChange {

	public static IStatusEvent TIMEOUTEVENT = new InvokeTimeoutEvent();
	public static IStatusEvent ENABLEEVENT = new EnableEvent();

}
