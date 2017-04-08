package com.ylinor.server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public final class ServerConfiguration {
    private static Map<String, String> defaultProperties;
    private Map<String, String> properties;

    static {
        defaultProperties = new HashMap<>();

        defaultProperties.put("maxPlayers", "50");
        defaultProperties.put("database.host", "localhost");
        defaultProperties.put("database.port", "3306");
        defaultProperties.put("database.user", "root");
        defaultProperties.put("database.password", "");
        defaultProperties.put("database.dbname", "ylinor");
    }

    public ServerConfiguration() {
        this.properties = new HashMap<>();
    }

    public void read(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.charAt(0) == '#')
                    continue;

                if (line.contains(":")) {
                    String key = line.substring(0, line.indexOf(':'));
                    String value = line.substring(line.indexOf(':') + 1);

                    properties.put(key.trim(), value.trim());
                }
            }
        }
    }

    public void write(File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Map.Entry<String, String> property : properties.entrySet()) {
                writer.println(String.format("%s: %s", property.getKey(), property.getValue()));
            }
        }
    }

    public void loadDefaults(boolean overwrite) {
        for (Map.Entry<String, String> defaultProperty : defaultProperties.entrySet()) {
            if (!properties.containsKey(defaultProperty.getKey()) || overwrite) {
                properties.put(defaultProperty.getKey(), defaultProperty.getValue());
            }
        }
    }

    public void removeUnknownKeys() {
        for (String key : new HashMap<String, String>(properties).keySet()) {
            if (!defaultProperties.containsKey(key)) {
                properties.remove(key);
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
