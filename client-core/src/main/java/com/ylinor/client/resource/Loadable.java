package com.ylinor.client.resource;

/**
 * Représente une classe contenant des assets à charger (hors assets
 * {@link Preloadable})
 *
 * @author Litarvan
 * @since 1.0.0
 */
@FunctionalInterface
public interface Loadable {
    void load();
}
