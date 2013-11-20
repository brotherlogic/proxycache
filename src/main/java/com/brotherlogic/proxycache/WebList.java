package com.brotherlogic.proxycache;

import java.io.IOException;

import com.google.gson.JsonElement;

public class WebList<X> extends UnboundedList<X> {

	Pagination pagScheme;

	@Override
	protected int fillBottom() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int fillTop() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String generateBottomURL(JsonElement curr) {

	}

	public String generateTopURL(JsonElement curr) {

	}

}
