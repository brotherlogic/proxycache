package com.brotherlogic.proxycache.example.twitter;

import com.brotherlogic.proxycache.discogs.StandardOAuthService;

public class ObjectBuilderFactory {

	private static ObjectBuilderFactory singleton;

	private ObjectBuilderFactory() {

	}

	public <X> ObjectBuilder<X> getObjectBuilder(Class<X> clz,
			StandardOAuthService serv) {
		return new ObjectBuilder<>(clz, serv);
	}

	public static ObjectBuilderFactory getInstance() {
		if (singleton == null)
			singleton = new ObjectBuilderFactory();
		return singleton;
	}

}
