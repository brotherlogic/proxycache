package com.brotherlogic.proxycache.example.twitter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

import com.brotherlogic.proxycache.LinkURL;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Builder class for a given object
 * 
 * @author simon
 * 
 * @param <X>
 *            The type of object that this builds
 */
public class ObjectBuilder<X> {

	private final Class<X> baseClass;

	public ObjectBuilder(Class<X> clz) {
		baseClass = clz;
	}

	private JsonElement applyAnnotationMethod(Method m, LinkURL anno, X object,
			JsonObject source) {
		String path = anno.path();
		JsonElement elem = resolvePath(path, source);
		return elem;
	}

	private void applyMethod(Method m, X object, JsonObject source) {
		try {
			// Look for an annotation
			LinkURL anno = m.getAnnotation(LinkURL.class);
			if (anno != null && anno.path().length() > 0) {
				JsonElement elem = applyAnnotationMethod(m, anno, object,
						source);
				m.invoke(object, buildParams(m, new JsonElement[] { elem }));
			} else {
				// Look for the item in the json source
				if (source.has(getJSONName(m))) {
					m.invoke(
							object,
							buildParams(m, source,
									new String[] { getJSONName(m) }));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Cannot apply this method - just fall out
	}

	public X build(JsonObject source) {
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

	public X build(String json) {
		return build(new JsonParser().parse(json).getAsJsonObject());
	}

	private Object[] buildParams(Method m, JsonElement[] pNames) {
		Object[] toRet = new Object[pNames.length];
		for (int i = 0; i < toRet.length; i++) {
			Class<?> type = m.getParameterTypes()[i];
			toRet[i] = convert(pNames[i], type);
		}

		return toRet;
	}

	private Object[] buildParams(Method m, JsonObject source, String[] pNames) {
		Object[] toRet = new Object[pNames.length];
		for (int i = 0; i < toRet.length; i++) {
			Class<?> type = m.getParameterTypes()[i];
			toRet[i] = convert(source, pNames[i], type);
		}

		return toRet;
	}

	private Object convert(JsonElement elem, Class<?> clz) {

		// Match up the object types
		if (clz.equals(Long.class) || clz.equals(long.class))
			return elem.getAsLong();
		else if (clz.equals(String.class))
			return elem.getAsString();

		System.out.println("Can't convert " + clz);

		return null;
	}

	private Object convert(JsonElement source, String paramName, Class<?> clz) {
		return convert(source.getAsJsonObject().get(paramName), clz);
	}

	private String getJSONName(Method m) {
		return m.getName().substring(3).toLowerCase();
	}

	private Collection<Method> getSetMethods() {
		Collection<Method> methods = new LinkedList<Method>();

		for (Method m : baseClass.getMethods())
			if (m.getName().startsWith("set"))
				methods.add(m);

		return methods;
	}

	private void refreshObject(X object, JsonObject source) {
		for (Method m : getSetMethods()) {
			applyMethod(m, object, source);
		}
	}

	private JsonElement resolvePath(String path, JsonObject obj) {
		String[] elems = path.split("->");
		JsonElement elem = obj;
		for (int i = 0; i < elems.length; i++)
			elem = elem.getAsJsonObject().get(elems[i]);

		return elem;
	}

}
