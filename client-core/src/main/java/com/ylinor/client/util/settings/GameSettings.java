package com.ylinor.client.util.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;

public class GameSettings
{

    @JsonProperty
    private int i2 = 4;

    @JsonProperty
    private KeyMap keyMapping = new KeyMap();

    @JsonProperty
    private VisualSettings visualSettings = new VisualSettings();

    /**
     * Empty constructor for json serialization
     */
    public GameSettings()
    {
    }

    /**
     * Used to create an instance of GameSettings with @param settingsFile
     */
    public static GameSettings get(File settingsFile) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        GameSettings settings;
        if(!settingsFile.exists())
        {
            settingsFile.createNewFile();
            Files.write(settingsFile.toPath(), Collections.singletonList("{ }"));
            settings = new GameSettings();
            settings.save(settingsFile);
        }
        settings = mapper.readValue(settingsFile, GameSettings.class);
        return settings;
    }

    /**
     * Used to save the configuration in @param settingsFile
     */
    public void save(File settingsFile) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(settingsFile, this);
    }

    public int getI2()
    {
        return i2;
    }

    public void setI2(int i2)
    {
        this.i2 = i2;
    }

    public KeyMap getKeyMapping()
    {
        return keyMapping;
    }

    public VisualSettings getVisualSettings()
    {
        return visualSettings;
    }
}
