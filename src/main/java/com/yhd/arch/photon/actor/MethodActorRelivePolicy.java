/**
 * 
 */
package com.yhd.arch.photon.actor;

/**
 * @author Archer
 * 
 */
public class MethodActorRelivePolicy implements IRelivePolicy {

	private static final int DEFAULT_RELIVE_INTERVAL = 500;
	private int tryCount = 0;
	private int threshold = 10;
	private int SCALE = 2;
	private int COUNT_LIMIT = 60000;
	private long start = 0;
	private long interval = DEFAULT_RELIVE_INTERVAL;
	private long TIME_LIMIT = 60000;

	@Override
	public boolean isLive() {
		boolean value = false;
		boolean vc = meetCountPolicy();
		boolean vt = meetTimePolicy();
		if (vc || vt) {
			interval = interval * SCALE;
			interval = interval < TIME_LIMIT ? interval : TIME_LIMIT;
			threshold = threshold * SCALE;
			threshold = threshold < COUNT_LIMIT ? threshold : COUNT_LIMIT;
			value = true;
		}
		return value;
	}

	private boolean meetTimePolicy() {
		boolean v = false;
		if (start == 0) {
			start = System.currentTimeMillis();
		} else {
			long tmp = System.currentTimeMillis() - start;
			if (tmp > interval) {
				start = System.currentTimeMillis();
				v = true;
			}
		}
		return v;
	}

	private boolean meetCountPolicy() {
		boolean v = false;
		tryCount++;
		if (tryCount >= threshold) {
			tryCount = 0;
			v = true;
		}
		return v;
	}
}
