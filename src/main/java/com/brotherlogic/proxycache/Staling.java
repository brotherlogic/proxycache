package com.brotherlogic.proxycache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Interface for defining when objects have become staled - i.e. they need to be recached
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Staling {
    /**
     * Flag to show that this object has staled - the time here is in seconds
     */
    int value() default 86400;
}
