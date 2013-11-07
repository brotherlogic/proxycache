package com.brotherlogic.proxycache;

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
