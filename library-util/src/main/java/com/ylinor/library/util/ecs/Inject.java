package com.ylinor.library.util.ecs;

public @interface Inject {
    /**
     * If true, also inject inherited fields.
     */
    boolean injectInherited() default true;


    /**
     * Throws a {@link NullPointerException} if field can't be injected.
     */
    boolean failOnNull() default true;


    /**
     *
     */
    String name() default "";
}
