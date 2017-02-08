package com.ylinor.client.resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Use for assets/assets's getter as preloaded (loaded before displaying the
 * loading screen)
 *
 * @author Litarvan
 * @since 1.0.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Preloaded {
}
