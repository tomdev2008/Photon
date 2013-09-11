/**
 * 
 */
package com.yhd.arch.photon.router.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * @author Archer
 * 
 */
public abstract class MixupHelper<T, V> {

	private Random r = new Random();
	private Comparator<T> comparator;

	public MixupHelper(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	public Collection<V> mixupCollection(Collection<T> values) {
		List<V> rlist = new ArrayList<V>();
		if (values != null && comparator != null) {
			List<T> list = new ArrayList<T>(values);
			Collections.sort(list, comparator);
			for (T m : list) {
				if (m != null) {
					for (int i = 0; i < getLoop(m); i++) {
						add2List(m, rlist);
					}
				}
			}
		}
		return rlist;

	}

	private void add2List(T m, List<V> list) {
		if (list != null) {
			int i = getPosition(list, m);
			list.add(i, getValue(m));
		}

	}

	private int getPosition(List<V> list, T m) {
		int pos = 0;
		int size = list.size();
		if (size > 1) {
			int loop = size - 1;
			for (int i = 0; i < loop; i++) {
				if (list.get(i).equals(list.get((i + 1)))) {
					if (!getValue(m).equals(list.get(i))) {
						pos = i + 1;
						break;
					}

				}
			}
			pos = pos == r.nextInt(size) ? 1 : pos;
		}
		return pos;
	}

	public abstract int getLoop(T t);

	public abstract V getValue(T t);
}
