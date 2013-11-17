package com.brotherlogic.proxycache.utils;

import java.util.LinkedList;

public abstract class UnboundedList<X> extends LinkedList<X> {

	int overallSize;

	protected abstract int fillBottom();

	protected abstract int fillTop(int index);

	@Override
	public int size() {
		return overallSize;
	}

	public static void main(String[] args) {
		UnboundedList<String> blah = new UnboundedList<String>() {

			@Override
			protected int fillBottom() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			protected int fillTop(int index) {
				// TODO Auto-generated method stub
				return 0;
			}

		};

		for (int i = 0; i < blah.size(); i++) {
			System.out.println(blah.get(i));
		}
	}

}
