package com.ylinor.client.util.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class GameSettings
{

    @JsonProperty
    private int i2 = 4;

    @JsonProperty
    private KeyMap keyMapping;

    /**
     * Empty constructor for json serialization
     */
    public GameSettings()
    {
    }

    /**
     * Used to create an instance of GameSettings
     */
    public static GameSettings get(File settingsFile) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(settingsFile, GameSettings.class);
    }

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
}
