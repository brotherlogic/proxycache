package com.brotherlogic.proxycache;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Pagination {

	String botPath() default "";

	String botUrl() default "";

	String topPath() default "";

	String topUrl() default "";

}
