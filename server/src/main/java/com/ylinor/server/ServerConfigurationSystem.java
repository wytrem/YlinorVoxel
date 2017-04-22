package com.ylinor.server;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylinor.library.util.ecs.system.NonProcessingSystem;


@Singleton
public class ServerConfigurationSystem extends NonProcessingSystem {

    private static final Logger logger = LoggerFactory.getLogger(ServerConfigurationSystem.class);

    @Inject
    private ServerConfiguration configuration;
    @Inject
    @Named("configFile")
    private File configFile;

    @Override
    public void initialize() {
        save();
    }

    public void save() {
        logger.info("Saving configuration to file '{}'", configFile.getAbsolutePath());
        configuration.write(configFile);
    }

    public String get(String key) {
        return configuration.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    @Override
    public void dispose() {
        save();
    }

    public ServerConfiguration getConfiguration() {
        return configuration;
    }
}
