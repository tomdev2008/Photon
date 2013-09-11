/**
 * 
 */
package com.yhd.arch.photon.router;

import junit.framework.TestCase;
import scala.concurrent.duration.Duration;

import com.yhd.arch.photon.util.ActorSystemUtil;

/**
 * @author Archer
 * 
 */
public class AkkaDelayTest extends TestCase {

	public void print() {
		System.out.println("test");
	}

	public void testDuration() throws InterruptedException {
		ActorSystemUtil.getActorSystem().scheduler()
				.schedule(Duration.create(1, "seconds"), Duration.create(10, "seconds"), new Runnable() {

					@Override
					public void run() {
						print();
					}
				}, ActorSystemUtil.getActorSystem().dispatcher());
		Thread.sleep(1100000);
	}
}
