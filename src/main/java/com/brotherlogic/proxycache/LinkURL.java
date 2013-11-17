package com.brotherlogic.proxycache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LinkURL {

	/**
	 * @return A reference to the url which generates this attribute
	 */
	String url() default "";

	/**
	 * @return the json path to the actual attribute
	 */
	String path() default "";
}
