/**
 * 
 */
package com.yhd.arch.photon.router.balancer;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.yhd.arch.photon.common.Constants;

/**
 * @author Archer
 * @param <T>
 * @param <P>
 * 
 */
public abstract class AbstractBalancer<T, R> implements ILoadBalancer<T, R> {

	protected volatile Circle<Integer, T> _circle = new Circle<Integer, T>();
	protected Lock lock = new ReentrantLock();
	protected Random random = new Random();
	protected AtomicInteger position = new AtomicInteger(random.nextInt(Constants.INTEGER_BARRIER));
	protected Iterable<String> whiteList = null;

	@Override
	public R select() {
		if (_circle == null || _circle.size() == 0) {
			return null;
		} else if (_circle.size() == 1) {
			T t = _circle.firstVlue();

			return valid(t) ? getValue(t) : null;
		} else {
			return doSelect();
		}
	}

	protected abstract R getValue(T t);

	protected abstract R doSelect();

	protected abstract boolean valid(T t);

	protected R getValueFromCircle(int code) {
		int size = _circle.size();
		T t = null;
		if (size > 0) {
			int tmp = code;
			while (size > 0) {
				tmp = _circle.lowerKey(tmp);
				t = _circle.get(tmp);
				if (t != null && valid(t)) {
					break;
				} else {
					t = null;
				}
				size--;
			}
		}
		return getValue(t);
	}

	@Override
	public void setWhiteList(Iterable<String> list) {
		this.whiteList = list;

	}

}
