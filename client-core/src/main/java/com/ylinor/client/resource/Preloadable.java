package com.ylinor.client.resource;

/**
 * Représente une classe d'assets (ex: {@link ScreenAssets}) contenant des
 * assets preloadable via la fonction {@link #preload()}
 *
 * @author Litarvan
 * @since 1.0.0
 */
@FunctionalInterface
public interface Preloadable
{
    /**
     * Preload les assets de cette classe
     */
    void preload();
}
