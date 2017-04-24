package com.ylinor.client.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.ylinor.client.YlinorClient;


/**
 * Le lanceur de l'appli pour bureau
 */
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 600);

        YlinorClient client = new YlinorClient();
        config.setTitle("Ylinor client - " + client.getVersion());

        new Lwjgl3Application(client, config);
    }
}
