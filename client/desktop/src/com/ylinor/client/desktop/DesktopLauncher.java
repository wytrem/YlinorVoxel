package com.ylinor.client.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ylinor.client.YlinorClient;

/**
 * Le lanceur de l'appli pour bureau
 */
public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 600;

        new LwjglApplication(new YlinorClient(), config);
    }
}
