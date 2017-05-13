package com.ylinor.client.desktop;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.ylinor.auth.client.YlinorUser;
import com.ylinor.auth.client.model.AuthException;
import com.ylinor.client.YlinorClient;
import com.ylinor.library.api.protocol.Protocol;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;


/**our,
 * Le lanceur de l'appli pour bureau
 */
public class DesktopLauncher {
    
    private static final Logger logger = LoggerFactory.getLogger(DesktopLauncher.class);
    
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 600);
        config.setHdpiMode(Lwjgl3ApplicationConfiguration.HdpiMode.Logical);
        
        OptionParser parser = new OptionParser();
        parser.accepts("debug");
        OptionSpec<String> token = parser.accepts("token").withRequiredArg();
        OptionSpec<String> email = parser.accepts("email").withRequiredArg();
        OptionSpec<String> password = parser.accepts("password").withRequiredArg();
        OptionSpec<String> serverIp = parser.accepts("serverIp").withRequiredArg().defaultsTo(Protocol.OFFICIAL_SERVER_IP);

        OptionSet options = parser.parse(args);
        
        
        YlinorUser ylinorUser = null;
        if (!options.has(token)) {
            if (options.has(email) && options.has(password)) {
                try {
                    ylinorUser = new YlinorUser();
                    ylinorUser.login(options.valueOf(email), options.valueOf(password));
                    ylinorUser.fetch(ylinorUser.getToken());
                }
                catch (AuthException | IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
                
                logger.info("Successfully logged in user {}.", ylinorUser.user().getUsername());
            }
            else {
                throw new IllegalArgumentException("Could not login.");
            }
        }
        else {
            try {
                ylinorUser = new YlinorUser();
                ylinorUser.fetch(options.valueOf(token));
            }
            catch (AuthException | IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        
        YlinorClient client = new YlinorClient(options.valueOf(serverIp), ylinorUser);
        config.setTitle("Ylinor client - " + client.getVersion());

        new Lwjgl3Application(client, config);
    }
}
