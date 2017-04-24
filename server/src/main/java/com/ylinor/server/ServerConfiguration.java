package com.ylinor.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public final class ServerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ServerConfiguration.class);

    private static Map<String, String> defaultProperties;
    private Map<String, String> properties;

    static {
        defaultProperties = new HashMap<>();

        defaultProperties.put("maxPlayers", "50");
        defaultProperties.put("database.host", "localhost");
        defaultProperties.put("database.port", "8529");
        defaultProperties.put("database.user", "root");
        defaultProperties.put("database.password", "");
        defaultProperties.put("database.dbname", "ylinor");
    }

    public ServerConfiguration() {
        this.properties = new HashMap<>();
    }

    @Inject
    public void read(@Named("configFile") File file) {
        loadDefaults(false);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.charAt(0) == '#')
                    continue;

                if (line.contains(":")) {
                    String key = line.substring(0, line.indexOf(':')).trim();
                    String value = line.substring(line.indexOf(':') + 1).trim();

                    if (defaultProperties.containsKey(key)) {
                        properties.put(key, value);
                    }
                }
            }
        }
        catch (IOException e) {
            logger.warn("Could not load configuration from file '" + file.getAbsolutePath() + "':", e);
        }
    }

    public void write(File file) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Map.Entry<String, String> property : properties.entrySet()) {
                writer.println(String.format("%s: %s", property.getKey(), property.getValue()));
            }
        }
        catch (IOException e) {
            logger.warn("Could not write configuration to file '" + file.getAbsolutePath() + "':", e);
        }
    }

    public void loadDefaults(boolean overwrite) {
        for (Map.Entry<String, String> defaultProperty : defaultProperties.entrySet()) {
            if (!properties.containsKey(defaultProperty.getKey()) || overwrite) {
                properties.put(defaultProperty.getKey(), defaultProperty.getValue());
            }
        }
    }

    public String getProperty(String property) {
        return properties.get(property);
    }

    public void setProperty(String property, String value) {
        properties.put(property, value);
    }
}
