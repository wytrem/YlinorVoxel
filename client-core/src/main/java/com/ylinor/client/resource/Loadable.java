package com.ylinor.client.resource;

/**
 * Class that contains assets to load (except
 * {@link Preloadable})
 *
 * @author Litarvan
 * @since 1.0.0
 */
@FunctionalInterface
public interface Loadable {
    void load();
}
