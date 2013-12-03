package com.brotherlogic.proxycache;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import com.brotherlogic.proxycache.discogs.StandardOAuthService;

/**
 * A double unbounded list
 * 
 * @author simon
 * 
 * @param <X>
 */
public abstract class UnboundedList<X> extends LinkedList<X> {

	private final Class<X> clz;

	private int overallSize;

	private final StandardOAuthService service;

	private boolean topFilled = false;

	public boolean getTopFilled() {
		return topFilled;
	}

	/**
	 * @param cls
	 *            The class for this list
	 * @param serv
	 *            The web service
	 */
	public UnboundedList(final Class<X> cls, final StandardOAuthService serv) {
		clz = cls;
		service = serv;
	}

	@Override
	public void add(final int index, final X obj) {
		super.add(index, obj);
		overallSize++;
	}

	@Override
	public boolean add(final X obj) {
		boolean val = super.add(obj);
		overallSize++;
		return val;
	}

	/**
	 * Fill the bottom of the list
	 * 
	 * @return The number of elements added
	 * @throws IOException
	 *             If we can't fill
	 */
	protected abstract int fillBottom() throws IOException;

	/**
	 * Fill the top of the list
	 * 
	 * @return The number of elements adde
	 * @throws IOException
	 *             If we can't fill
	 */
	protected abstract int fillTop() throws IOException;

	/**
	 * @return The web service for this list
	 */
	protected StandardOAuthService getService() {
		return service;
	}

	/**
	 * @return The underlying class
	 */
	public Class<X> getUnderlyingClass() {
		return clz;
	}

	@Override
	public Iterator<X> iterator() {
		return new Iterator<X>() {

			private int index = 0;

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

	@Override
	public Object[] toArray() {

		// This ensures the collection is full
		Iterator<X> it = this.iterator();
		while (it.hasNext()) {
			it.next();
		}

		return super.toArray();
	}

}
