package com.brotherlogic.proxycache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * A single method call
 * 
 * @author simon
 * 
 */
public class SingleMethodCall {
    private Method m;
    private Class<?>[] paramTypes;
    private String[][] pathElems;
    private URL url;

    /**
     * @return The parameter list
     */
    private Object[] buildParameterList() {
        JsonElement elem = readURL(url);

        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < params.length; i++) {
            JsonObject obj = elem.getAsJsonObject();
            for (int j = 0; j < pathElems.length; j++) {
                obj = obj.get(pathElems[i][j]).getAsJsonObject();
            }
            params[i] = convertJson(obj, paramTypes[i]);
        }

        return params;
    }

    /**
     * @param obj
     *            The object to convert
     * @param convertTo
     *            The class to convert to
     * @return The converted object
     */
    private Object convertJson(final JsonObject obj, final Class<?> convertTo) {
        return new Gson().fromJson(obj, convertTo);
    }

    /**
     * @param obj
     *            The object to process on
     * @return The processed method
     * @throws InvocationTargetException
     *             if we can't run on this object
     * @throws IllegalAccessException
     *             If we can't access the method
     */
    public Object processMethod(final Object obj) throws InvocationTargetException, IllegalAccessException {
        Object[] params = buildParameterList();
        Object o = m.invoke(obj, params);
        return o;
    }

    /**
     * @param webURL
     *            The URL to read
     * @return The read element
     */
    private JsonElement readURL(final URL webURL) {
        return null;
    }
}
