package com.ylinor.client.util.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VisualSettings
{

    @JsonProperty
    public boolean useVsync = true;

    public VisualSettings()
    {
    }
}
