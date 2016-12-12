package com.ylinor.client.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ylinor.client.YlinorClient;
import com.ylinor.library.network.ClientNetwork;

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

        ClientNetwork network = new ClientNetwork();
        network.start("127.0.0.1", 25565);

        new LwjglApplication(new YlinorClient(), config);
    }
}
