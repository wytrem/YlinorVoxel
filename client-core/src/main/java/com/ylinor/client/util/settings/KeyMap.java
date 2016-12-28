package com.ylinor.client.util.settings;

import com.badlogic.gdx.Input;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyMap
{

    @JsonProperty("FORWARD")
    public int FORWARD;

    @JsonProperty("BACKWARD")
    public int BACKWARD;

    @JsonProperty("STRAFE_LEFT")
    public int STRAFE_LEFT;

    @JsonProperty("STRAFE_RIGHT")
    public int STRAFE_RIGHT;

    @JsonProperty("JUMP")
    public int JUMP;

    @JsonProperty("CANCEL")
    public int CANCEL;

    public KeyMap()
    {
    }

    public void resetDefaults()
    {
        FORWARD = Input.Keys.Z;
        BACKWARD = Input.Keys.S;
        STRAFE_LEFT = Input.Keys.Q;
        STRAFE_RIGHT = Input.Keys.D;
        JUMP = Input.Keys.SPACE;
        CANCEL = Input.Keys.ESCAPE;
    }
}
