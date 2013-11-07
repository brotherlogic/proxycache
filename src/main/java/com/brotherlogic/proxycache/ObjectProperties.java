package com.brotherlogic.proxycache;

public @interface ObjectProperties {

	/**
	 * @return The length of time it takes to stale a given property
	 */
	long staletime() default 0;

}
