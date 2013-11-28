package com.brotherlogic.proxycache;

/**
 * The properties of the objects
 * 
 * @author simon
 * 
 */
public @interface ObjectProperties {

    /**
     * The length of time it takes to stale a given property
     */
    long staletime() default 0;

}
