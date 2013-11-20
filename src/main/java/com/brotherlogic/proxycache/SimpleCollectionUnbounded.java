package com.brotherlogic.proxycache;

import java.io.IOException;

import com.brotherlogic.proxycache.discogs.StandardOAuthService;
import com.brotherlogic.proxycache.example.twitter.ObjectBuilder;
import com.brotherlogic.proxycache.example.twitter.ObjectBuilderFactory;
import com.google.gson.JsonArray;

public class SimpleCollectionUnbounded<X> extends UnboundedList<X> {

	String path;
	String url;

	public SimpleCollectionUnbounded(Class<X> clz, StandardOAuthService serv,
			String mainUrl, String path) {
		super(clz, serv);
		url = mainUrl;
		this.path = path;
	}

	@Override
	protected int fillBottom() {
		// We don't fill the bottom
		return 0;
	}

	@Override
	protected int fillTop() throws IOException {
		// Get the builder
		ObjectBuilder<X> builder = ObjectBuilderFactory.getInstance()
				.getObjectBuilder(getUnderlyingClass(), getService());

		JsonArray col = builder.resolvePath(path,
				getService().get(url).getAsJsonObject()).getAsJsonArray();
		for (int i = col.size() - 1; i >= 0; i--) {
			// Add to the top of the collection
			X obj = builder.build(col.get(i).getAsJsonObject());
			add(0, obj);
		}

		return col.size();
	}
}
