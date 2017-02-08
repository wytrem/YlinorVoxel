package com.ylinor.client.util.settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;


public class GameSettings {

    @JsonProperty("key_mapping")
    private KeyMap keyMapping = new KeyMap();

    @JsonProperty("visual_settings")
    private VisualSettings visualSettings = new VisualSettings();

    /**
     * Empty constructor for json serialization.
     */
    public GameSettings() {
    }

    /**
     * Used to create an instance of GameSettings with @param settingsFile
     */
    public static GameSettings get(File settingsFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GameSettings settings;
        if (!settingsFile.exists()) {
            settingsFile.createNewFile();
            Files.write(settingsFile.toPath(), Collections.singletonList("{ }"));
            settings = new GameSettings();
            settings.resetDefaults();
            settings.save(settingsFile);
        }
        settings = mapper.readValue(settingsFile, GameSettings.class);
        return settings;
    }

    public void resetDefaults() {
        keyMapping.resetDefaults();
        visualSettings.resetDefaults();
    }

    /**
     * Used to save the configuration in @param settingsFile
     */
    public void save(File settingsFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(settingsFile, this);
    }

    public KeyMap getKeyMapping() {
        return keyMapping;
    }

    public VisualSettings getVisualSettings() {
        return visualSettings;
    }
}
