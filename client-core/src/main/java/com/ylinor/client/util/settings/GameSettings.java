package com.ylinor.client.util.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class GameSettings
{

    @JsonProperty
    public int i2 = 4;

    @JsonProperty
    public KeyMap keyMapping;

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

}
