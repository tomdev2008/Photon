package com.yhd.arch.photon.router;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;

import akka.actor.UntypedActor;

public class SlowActor extends UntypedActor {

	AtomicInteger ai = new AtomicInteger();

	@Override
	public void onReceive(Object arg0) throws Exception {
		System.out.println(self() + " " + ai.getAndIncrement());
		BigDecimal l = new BigDecimal(14564.342334);
		for (int i = 1; i < 2500000; i++) {
			l.divide(new BigDecimal(43453), RoundingMode.HALF_UP);
		}
	}
}
