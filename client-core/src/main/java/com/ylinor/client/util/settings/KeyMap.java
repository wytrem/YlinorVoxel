package com.ylinor.client.util.settings;

import com.badlogic.gdx.Input;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyMap
{

    @JsonProperty("forward")
    public int forward;

    @JsonProperty("backward")
    public int backward;

    @JsonProperty("strafeLeft")
    public int strafeLeft;

    @JsonProperty("strafeRight")
    public int strafeRight;

    @JsonProperty("jump")
    public int jump;

    @JsonProperty("cancel")
    public int cancel;

    @JsonProperty("snick")
    public int snick;

    public KeyMap()
    {
    }

    public void resetDefaults()
    {
        forward = Input.Keys.Z;
        backward = Input.Keys.S;
        strafeLeft = Input.Keys.Q;
        strafeRight = Input.Keys.D;
        jump = Input.Keys.SPACE;
        cancel = Input.Keys.ESCAPE;
        snick = Input.Keys.SHIFT_LEFT;
    }
}
