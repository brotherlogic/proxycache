package com.brotherlogic.proxycache;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import com.brotherlogic.proxycache.discogs.StandardOAuthService;
import com.brotherlogic.proxycache.example.twitter.ObjectBuilder;
import com.brotherlogic.proxycache.example.twitter.ObjectBuilderFactory;
import com.google.gson.JsonObject;

/**
 * Represents an object manager in the system. Object managers are able to
 * process basic objects
 * 
 * @author simon
 * 
 * @param <X>
 *            The generic class of this manager
 */
public class ObjectManager<X> {

	/** The class defined by this manager */
	private final Class<X> underlyingClass;

	/** This does the hevay lifting on the builder */
	private final ObjectBuilder<X> builder;

	/** For handling web requests */
	private final StandardOAuthService service;

	/**
	 * Base constructor
	 * 
	 * @param uClass
	 *            The class that this object manages
	 */
	public ObjectManager(Class<X> uClass, StandardOAuthService serv) {
		underlyingClass = uClass;
		service = serv;
		builder = ObjectBuilderFactory.getInstance().getObjectBuilder(uClass);
	}

	public X get(String id) {
		// Check the local cache

		// Check for staling

		// Update the object if necessary

		// Return the object
		return null;
	}

	public X get() throws IOException {
		// Check the local cache

		// Check for staling

		// Update the object if necessary
		LinkURL link = underlyingClass.getAnnotation(LinkURL.class);
		String path = link.url();
		JsonObject objRep = service.get(path).getAsJsonObject();
		X obj = builder.build(objRep);

		// Return the object
		return obj;
	}

	private void runMethod(Method m, URL url, String path) {

	}
}
