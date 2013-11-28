package com.brotherlogic.proxycache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Pagination of web requests
 * 
 * @author simon
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Pagination {

    /**
     * The bottom path
     */
    String botPath() default "";

    /**
     * The bottom url
     */
    String botUrl() default "";

    /**
     * The top path
     */
    String topPath() default "";

    /**
     * The top url
     */
    String topUrl() default "";

}
