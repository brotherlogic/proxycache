package com.brotherlogic.proxycache.example.twitter;

import java.util.Collection;

import com.google.gson.JsonObject;

public abstract class Database {
	public abstract <X> X retrieve(JsonObject properties, Class<X> clz);

	public abstract <X> Collection<X> retrieveObjects(JsonObject properties,
			Class<X> clz);

	public abstract <X> void store(X object);

	public <X> void storeObjects(Collection<X> objects) {
		for (X obj : objects)
			store(obj);
	}
}
