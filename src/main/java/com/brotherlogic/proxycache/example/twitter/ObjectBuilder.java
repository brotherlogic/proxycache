package com.brotherlogic.proxycache.example.twitter;

import java.lang.reflect.InvocationTargetException;

import com.google.gson.JsonObject;

/**
 * Builder class for a given object
 * 
 * @author simon
 * 
 * @param <X>
 *            The type of object that this builds
 */
public class ObjectBuilder<X> {

	private Class<X> baseClass;

	public X produceObject(JsonObject source) {
		try {
			X object = baseClass.getConstructor(new Class[0]).newInstance(
					new Object[0]);

			refreshObject(object, source);

			return object;
		} catch (NoSuchMethodException e) {
			// Shouldn't reach here
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// Shouldn't reach here
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// Shouldn't reach here
			e.printStackTrace();
		} catch (InstantiationException e) {
			// Shouldn't reach here
			e.printStackTrace();
		}

		return null;
	}

	private void refreshObject(X object, JsonObject source) {
		// Get the link annotated
	}

}
