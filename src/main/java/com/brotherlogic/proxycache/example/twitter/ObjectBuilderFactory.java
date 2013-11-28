package com.brotherlogic.proxycache.example.twitter;

import com.brotherlogic.proxycache.discogs.StandardOAuthService;

/**
 * The builder factory for builders
 * 
 * @author simon
 * 
 */
public final class ObjectBuilderFactory {

    private static ObjectBuilderFactory singleton;

    /**
     * Blocking constructor
     */
    private ObjectBuilderFactory() {

    }

    /**
     * @param clz
     *            The class to get the builder for
     * @param serv
     *            The underlying web service
     * @return An object builder for this object
     * 
     * @param <X>
     *            the object to build for
     */
    public <X> ObjectBuilder<X> getObjectBuilder(final Class<X> clz, final StandardOAuthService serv) {
        return new ObjectBuilder<>(clz, serv);
    }

    /**
     * Gets an instance of the builder factory
     * 
     * @return The Object Builder Factory
     */
    public static ObjectBuilderFactory getInstance() {
        if (singleton == null) {
            singleton = new ObjectBuilderFactory();
        }
        return singleton;
    }

}
