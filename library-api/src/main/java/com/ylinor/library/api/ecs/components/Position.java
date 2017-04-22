package com.ylinor.library.api.ecs.components;

import org.joml.Vector3f;

import com.ylinor.library.util.ecs.component.Component;

public class Position extends Component {
    public final Vector3f prevPosition = new Vector3f();
    public final Vector3f position = new Vector3f();
}
