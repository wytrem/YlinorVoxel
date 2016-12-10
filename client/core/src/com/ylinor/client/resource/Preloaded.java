package com.ylinor.client.resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marque un asset/getter d'asset, comme étant préchargé,
 * c'est à dire chargé avant l'affiche du screen de loading,
 * car utilisé avant/pendant celui-ci.
 *
 * @author Litarvan
 * @since 1.0.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Preloaded
{
}
