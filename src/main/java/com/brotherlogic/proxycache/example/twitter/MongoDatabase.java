package com.brotherlogic.proxycache.example.twitter;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonObject;

public class MongoDatabase extends Database {
	Map<Class<?>, Collection<Method>> getMap = new TreeMap<Class<?>, Collection<Method>>();
	Map<Class<?>, Collection<Method>> setMap = new TreeMap<Class<?>, Collection<Method>>();

	private void prepareMaps(Class<?> cls) {
		Map<String, Method> potGetMethods = new TreeMap<String, Method>();
		Map<String, Method> potSetMethods = new TreeMap<String, Method>();
		for (Method m : cls.getMethods()) {
			if (m.getName().startsWith("get"))
				potGetMethods.put(m.getName().substring(3), m);
			if (m.getName().startsWith("set"))
				potSetMethods.put(m.getName().substring(3), m);
		}

		for (String name : potSetMethods.keySet()) {
			// Only process mutable properties
		}
	}

	@Override
	public <X> X retrieve(JsonObject properties, Class<X> clz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> Collection<X> retrieveObjects(JsonObject properties, Class<X> clz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <X> void store(X object) {
		// TODO Auto-generated method stub

	}

}
