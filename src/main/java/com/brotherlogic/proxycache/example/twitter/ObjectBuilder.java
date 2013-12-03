package com.brotherlogic.proxycache.example.twitter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

import com.brotherlogic.proxycache.LinkURL;
import com.brotherlogic.proxycache.Pagination;
import com.brotherlogic.proxycache.SimpleCollectionUnbounded;
import com.brotherlogic.proxycache.WebList;
import com.brotherlogic.proxycache.runners.StandardOAuthService;
import com.google.gson.JsonArray;
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
    private final StandardOAuthService service;

    /**
     * 
     * @param clz
     *            The class that this builder builds
     * @param serv
     *            An underlying we connection
     */
    public ObjectBuilder(final Class<X> clz, final StandardOAuthService serv) {
        baseClass = clz;
        service = serv;
    }

    /**
     * Applies the method to the annotation
     * 
     * @param m
     *            The method to run
     * @param anno
     *            The underlying annotation
     * @param object
     *            The object to run on
     * @param source
     *            The underlying json source
     * @return The json returned element
     */
    private JsonElement applyAnnotationMethod(final Method m, final LinkURL anno, final X object, final JsonObject source) {
        String path = anno.path();
        JsonElement elem = resolvePath(path, source);
        return elem;
    }

    /**
     * Builds a colleciton on the object
     * 
     * @param m
     *            The method for the collection
     * @param object
     *            The object to run on
     * @param source
     *            The json source
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void applyCollectionMethod(final Method m, final X object, final JsonObject source) {
        try {
            LinkURL anno = m.getAnnotation(LinkURL.class);
            Class<?> clz = Class.forName(anno.prodClass());

            Pagination pag = m.getAnnotation(Pagination.class);

            if (pag == null) {

                // If this is a reading type collection it'll have a url
                if (anno.url().length() > 0) {

                    SimpleCollectionUnbounded scu = new SimpleCollectionUnbounded<>(clz, service, replace(anno.url(), object, source), anno.path());
                    m.invoke(object, new Object[] { scu });
                } else {
                    Collection c = new LinkedList();
                    JsonArray arr = resolvePath(anno.path(), source).getAsJsonArray();
                    Class colClz = Class.forName(anno.prodClass());
                    ObjectBuilder builder = ObjectBuilderFactory.getInstance().getObjectBuilder(colClz, service);
                    for (int i = 0; i < arr.size(); i++) {
                        c.add(builder.build(arr.get(i).getAsJsonObject()));
                    }
                    m.invoke(object, new Object[] { c });
                }
            } else {
                WebList<?> wl = new WebList<>(clz, service, replace(anno.url(), object, source), anno.path(), pag);
                m.invoke(object, new Object[] { wl });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Applies a method to the object
     * 
     * @param m
     *            The method to run
     * @param object
     *            The object to run the method on
     * @param source
     *            The json source
     */
    private void applyMethod(final Method m, final X object, final JsonObject source) {
        try {
            // Look for an annotation
            LinkURL anno = m.getAnnotation(LinkURL.class);

            if (anno != null && anno.path().length() > 0) {
                JsonElement elem = applyAnnotationMethod(m, anno, object, source);
                if (elem != null) {
                    m.invoke(object, buildParams(m, new JsonElement[] { elem }));
                }
            } else {
                // Look for the item in the json source
                if (source.has(getJSONName(m))) {
                    m.invoke(object, buildParams(m, source, new String[] { getJSONName(m) }));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds the object using the json source
     * 
     * @param source
     *            The json to build from
     * @return A built object using the supplied json
     */
    public X build(final JsonObject source) {
        try {
            X object = baseClass.getConstructor(new Class[0]).newInstance(new Object[0]);

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

    /**
     * Builds the object using string json
     * 
     * @param json
     *            The string rep of the json
     * @return The built object
     */
    public X build(final String json) {
        return build(new JsonParser().parse(json).getAsJsonObject());
    }

    /**
     * Build up the parameters
     * 
     * @param m
     *            The method to run
     * @param pNames
     *            The parameter names
     * @return Json Converted to Object parameters
     */
    private Object[] buildParams(final Method m, final JsonElement[] pNames) {
        Object[] toRet = new Object[pNames.length];
        for (int i = 0; i < toRet.length; i++) {
            Class<?> type = m.getParameterTypes()[i];
            toRet[i] = convert(pNames[i], type);
        }

        return toRet;
    }

    /**
     * 
     * @param m
     *            The method to build parameters for
     * @param source
     *            The json source to build from
     * @param pNames
     *            The names of the parameters
     * @return an Object array of the right parameters
     */
    private Object[] buildParams(final Method m, final JsonObject source, final String[] pNames) {
        Object[] toRet = new Object[pNames.length];
        for (int i = 0; i < toRet.length; i++) {
            Class<?> type = m.getParameterTypes()[i];
            toRet[i] = convert(source, pNames[i], type);
        }

        return toRet;
    }

    /**
     * Converts a json parameter to the specified class
     * 
     * @param elem
     *            The json
     * @param clz
     *            The class to convert to
     * @return The converted object
     */
    private Object convert(final JsonElement elem, final Class<?> clz) {

        // Match up the object types
        if (clz.equals(Long.class) || clz.equals(long.class)) {
            return elem.getAsLong();
        } else if (clz.equals(String.class)) {
            return elem.getAsString();
        } else if (clz.equals(Integer.class) || clz.equals(int.class)) {
            return elem.getAsInt();
        }

        return null;
    }

    /**
     * Converts an object
     * 
     * @param source
     *            The json source
     * @param paramName
     *            The name of the parameter to convert
     * @param clz
     *            The underlying class to convert
     * @return The converted object
     */
    private Object convert(final JsonElement source, final String paramName, final Class<?> clz) {
        return convert(source.getAsJsonObject().get(paramName), clz);
    }

    /**
     * Gets the json name for the method
     * 
     * @param m
     *            The method
     * @return The json name
     */
    private String getJSONName(final Method m) {
        return m.getName().substring(3).toLowerCase();
    }

    /**
     * Gets the set method for this class
     * 
     * @return A {@link Collection} of {@link Method} which are set methods
     */
    private Collection<Method> getSetMethods() {
        Collection<Method> methods = new LinkedList<Method>();

        for (Method m : baseClass.getMethods()) {
            if (m.getName().startsWith("set")) {
                methods.add(m);
            }
        }
        return methods;
    }

    /**
     * Refreshes the object in question
     * 
     * @param object
     *            The object to be refreshed
     * @param source
     *            The json to refresh with
     */
    public void refreshObject(final X object, final JsonObject source) {
        for (Method m : getSetMethods()) {
            // Look for collection based set methods
            if (m.getParameterTypes().length == 1 && Collection.class.isAssignableFrom(m.getParameterTypes()[0])) {
                applyCollectionMethod(m, object, source);
            } else {
                applyMethod(m, object, source);
            }
        }
    }

    /**
     * Performs a replacement
     * 
     * @param url
     *            The url to load from
     * @param obj
     *            The object to replace on
     * @param source
     *            The json source
     * @return The resolved value
     */
    public String replace(final String url, final X obj, final JsonObject source) {
        String newUrl = url;
        while (newUrl.indexOf("<") > 0) {
            String parameter = newUrl.substring(newUrl.indexOf("<") + 1, newUrl.indexOf(">"));

            String rep = "";
            try {
                rep = baseClass.getMethod("get" + Character.toUpperCase(parameter.charAt(0)) + parameter.substring(1), new Class[0]).invoke(obj, new Object[0]).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            newUrl = newUrl.replace("<" + parameter + ">", rep);
        }

        while (newUrl.indexOf("[") >= 0) {
            String parameter = newUrl.substring(newUrl.indexOf("[") + 1, newUrl.indexOf("]"));

            String rep = "";
            try {
                rep = resolvePath(parameter, source).getAsString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            newUrl = newUrl.replace("[" + parameter + "]", rep);
        }

        return newUrl;
    }

    /**
     * Resolves a given path
     * 
     * @param path
     *            The path to be resolved
     * @param obj
     *            The Json Object to resolve
     * @return The resolved element
     */
    public JsonElement resolvePath(final String path, final JsonObject obj) {
        String[] elems = path.split("->");
        JsonElement elem = obj;
        for (int i = 0; i < elems.length; i++) {
            elem = elem.getAsJsonObject().get(elems[i]);
        }

        return elem;
    }

}
