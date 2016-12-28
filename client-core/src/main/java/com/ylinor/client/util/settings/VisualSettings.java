package com.ylinor.client.util.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VisualSettings
{

    @JsonProperty("use_vsync")
    public boolean useVsync = true;

    public VisualSettings()
    {
    }
    
    public void resetDefaults()
    {
    }
}
