package com.ylinor.client.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.ylinor.client.YlinorClient;
import com.ylinor.packets.Protocol;


/**our,
 * Le lanceur de l'appli pour bureau
 */
public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 600);
        config.setHdpiMode(Lwjgl3ApplicationConfiguration.HdpiMode.Logical);
        
        String serverIp = Protocol.OFFICIAL_SERVER_IP;

        if (args.length > 0) {
            if (args[0].equals("--debug")) {
                serverIp = Protocol.DEBUG_SERVER_IP;
            }
        }
        
        YlinorClient client = new YlinorClient(serverIp);
        config.setTitle("Ylinor client - " + client.getVersion());

        new Lwjgl3Application(client, config);
    }
}
