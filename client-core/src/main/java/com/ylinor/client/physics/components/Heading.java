package com.ylinor.client.physics.components;

import org.joml.Vector3f;

import com.ylinor.library.util.ecs.component.Component;


/**
 * Marks the entities that can look in a given direction.
 *
 * @author wytrem
 */
public class Heading extends Component {
    public final Vector3f heading = new Vector3f();
}
