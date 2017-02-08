package com.ylinor.client.physics;

import org.joml.Vector3f;

import com.artemis.Component;


/**
 * Stands for an entity AABB.
 * 
 * @author wytrem
 */
public class AABB extends Component {
    public final Vector3f position = new Vector3f();
    public final Vector3f size = new Vector3f();

    public boolean collides(AABB other) {
        boolean collisionX = position.x + size.x >= other.position.x && other.position.x + other.size.x >= position.x;
        boolean collisionY = position.y + size.y >= other.position.y && other.position.y + other.size.y >= position.y;
        boolean collisionZ = position.z + size.z >= other.position.z && other.position.z + other.size.z >= position.z;
        return collisionX && collisionY && collisionZ;
    }
}
