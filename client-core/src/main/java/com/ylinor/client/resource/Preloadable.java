package com.ylinor.client.resource;

/**
 * Representing an assets's class (ex: {@link ScreenAssets}) that contains
 * preloadable assets through the function {@link #preload()}
 *
 * @author Litarvan
 * @since 1.0.0
 */
@FunctionalInterface
public interface Preloadable {
    /**
     * Preload les assets de cette classe
     */
    void preload();
}
