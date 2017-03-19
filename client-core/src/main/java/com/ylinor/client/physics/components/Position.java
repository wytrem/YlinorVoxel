package com.ylinor.client.physics.components;

import org.joml.Vector3f;

import com.artemis.Component;


public class Position extends Component {
    public final Vector3f prevPosition = new Vector3f();
    public final Vector3f position = new Vector3f();
}
