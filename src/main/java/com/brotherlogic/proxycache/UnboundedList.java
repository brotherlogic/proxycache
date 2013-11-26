package com.brotherlogic.proxycache;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import com.brotherlogic.proxycache.discogs.StandardOAuthService;

public abstract class UnboundedList<X> extends LinkedList<X> {

	private final Class<X> clz;

	int overallSize;

	private final StandardOAuthService service;

	boolean topFilled = false;

	public UnboundedList(Class<X> cls, StandardOAuthService serv) {
		clz = cls;
		service = serv;
	}

	@Override
	public void add(int index, X obj) {
		super.add(index, obj);
		overallSize++;
	}

	@Override
	public boolean add(X obj) {
		boolean val = super.add(obj);
		overallSize++;
		return val;
	}

	protected abstract int fillBottom() throws IOException;

	protected abstract int fillTop() throws IOException;

	protected StandardOAuthService getService() {
		return service;
	}

	public Class<X> getUnderlyingClass() {
		return clz;
	}

	@Override
	public Iterator<X> iterator() {
		final Iterator<X> superIterator = super.iterator();
		return new Iterator<X>() {

			int index = 0;

			@Override
			public boolean hasNext() {
				if (!topFilled) {
					try {
						fillTop();
					} catch (IOException e) {
						e.printStackTrace();
					}
					topFilled = true;
					return index < size();
				}

				if (index >= size()) {
					try {
						fillBottom();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				return index < size();

			}

			@Override
			public X next() {
				return get(index++);
			}

			@Override
			public void remove() {
				// remove(index++);
			}
		};
	}

	@Override
	public int size() {
		return overallSize;
	}

}
