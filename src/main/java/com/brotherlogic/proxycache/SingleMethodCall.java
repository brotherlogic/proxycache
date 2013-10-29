package com.brotherlogic.proxycache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SingleMethodCall {
	Method m;
	String[][] pathElems;
	Class<?>[] paramTypes;
	URL url;
	
	public Object processMethod(Object obj) throws InvocationTargetException, IllegalAccessException
	{
		Object[] params = buildParameterList();
		Object o = m.invoke(obj, params);
		return o;
	}
	
	private Object[] buildParameterList()
	{
		JsonElement elem = readURL(url);
		
		Object[] params = new Object[paramTypes.length];	
		for(int i = 0 ; i < params.length ; i++)
		{
			JsonObject obj = elem.getAsJsonObject();
			for(int j = 0 ; j < pathElems.length ; j++)
				obj = obj.get(pathElems[i][j]).getAsJsonObject();
			params[i] = convertJson(obj,paramTypes[i]);
		}
		
		return params;
	}
	
	private Object convertJson(JsonObject obj, Class<?> convertTo)
	{
		return new Gson().fromJson(obj, convertTo);
	}
	
	private JsonElement readURL(URL url)
	{
		return null;
	}
}	
