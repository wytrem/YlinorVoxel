package com.ylinor.client.util.settings;

import com.fasterxml.jackson.annotation.JsonProperty;


public class VisualSettings {

    @JsonProperty("use_vsync")
    public boolean useVsync;

    public VisualSettings() {
    }

    public void resetDefaults() {
        useVsync = true;
    }
}
