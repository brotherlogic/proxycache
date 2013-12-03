package com.brotherlogic.proxycache;

import java.io.IOException;

import com.brotherlogic.proxycache.example.twitter.ObjectBuilder;
import com.brotherlogic.proxycache.example.twitter.ObjectBuilderFactory;
import com.brotherlogic.proxycache.runners.StandardOAuthService;
import com.google.gson.JsonObject;

/**
 * Represents an object manager in the system. Object managers are able to process basic objects
 * 
 * @author simon
 * 
 * @param <X>
 *            The generic class of this manager
 */
public class ObjectManager<X> {

    /** This does the hevay lifting on the builder */
    private final ObjectBuilder<X> builder;

    /** For handling web requests */
    private final StandardOAuthService service;

    /** The class defined by this manager */
    private final Class<X> underlyingClass;

    /**
     * Base constructor
     * 
     * @param uClass
     *            The class that this object manages
     * @param serv
     *            The web server to run requests with
     */
    public ObjectManager(final Class<X> uClass, final StandardOAuthService serv) {
        underlyingClass = uClass;
        service = serv;
        builder = ObjectBuilderFactory.getInstance().getObjectBuilder(uClass, serv);
    }

    /**
     * Gets an object
     * 
     * @return The object to get
     * @throws IOException
     *             If we can't communicate correctly
     */
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

    /**
     * Gets
     * 
     * @param id
     *            The Id to get from
     * @return The object with the relevant id
     */
    public X get(final String id) {
        // Check the local cache

        // Check for staling

        // Update the object if necessary

        // Return the object
        return null;
    }

    /**
     * Refreshs the supplied object
     * 
     * @param obj
     *            The object to be refreshed
     * @throws IOException
     *             If we can't communicate
     */
    public void refresh(final X obj) throws IOException {
        LinkURL link = underlyingClass.getAnnotation(LinkURL.class);
        String path = link.url();
        String rPath = builder.replace(path, obj, null);
        JsonObject objRep = service.get(rPath).getAsJsonObject();
        builder.refreshObject(obj, objRep);
    }
}
