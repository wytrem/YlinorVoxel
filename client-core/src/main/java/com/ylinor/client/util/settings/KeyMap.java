package com.ylinor.client.util.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyMap
{

    @JsonProperty
    public int FORWARD;

    @JsonProperty
    public int BACKWARD;

    @JsonProperty
    public int STRAFE_LEFT;

    @JsonProperty
    public int STRAFE_RIGHT;

    @JsonProperty
    public int JUMP;

    @JsonProperty
    public int CANCEL;

    public KeyMap()
    {
    }

    public void resetDefaults()
    {
        
    }
}
