package com.brotherlogic.proxycache.example.twitter;

public class ObjectBuilderFactory {

	private static ObjectBuilderFactory singleton;

	public static ObjectBuilderFactory getInstance() {
		if (singleton == null)
			singleton = new ObjectBuilderFactory();
		return singleton;
	}

	private ObjectBuilderFactory() {

	}

	public <X> ObjectBuilder<X> getObjectBuilder(Class<X> clz) {
		return new ObjectBuilder<>(clz);
	}

}
