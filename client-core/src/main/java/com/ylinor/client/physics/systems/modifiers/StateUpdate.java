package com.ylinor.client.physics.systems.modifiers;

import org.joml.Vector3f;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.physics.components.AABB;
import com.ylinor.client.physics.components.CollisionState;
import com.ylinor.client.physics.components.Position;

public class StateUpdate extends MotionModifier {
    
    @Wire
    private ComponentMapper<AABB> aabbMapper;

    @Wire
    private ComponentMapper<Position> positionMapper;
    
    @Wire
    private ComponentMapper<CollisionState> collisionStateMapper;
    
    @Override
    public Object apply(int entityId, Vector3f motion, Vector3f initialMotion, Object previousOuput) {
        Position position = positionMapper.get(entityId);
        AABB aabb = aabbMapper.get(entityId);
        CollisionState collisionState = collisionStateMapper.get(entityId);
        

        resetPositionToBB(position, aabb);
        collisionState.isCollidedHorizontally = initialMotion.x != motion.x || initialMotion.z != motion.z;
        collisionState.isCollidedVertically = initialMotion.y != motion.y;

        collisionState.onGround = collisionState.isCollidedVertically && initialMotion.y < 0.0f;
        collisionState.isCollided = collisionState.isCollidedHorizontally || collisionState.isCollidedVertically;
        return null;
    }

    @Override
    public boolean shouldApply(int entityId) {
        return aabbMapper.has(entityId) && collisionStateMapper.has(entityId) && positionMapper.has(entityId);
    }
    
    public void resetPositionToBB(Position position, AABB aabb) {
        position.position.x = (aabb.aabb.minX + aabb.aabb.maxX) / 2.0f;
        position.position.y = aabb.aabb.minY;
        position.position.z = (aabb.aabb.minZ + aabb.aabb.maxZ) / 2.0f;
    }
}
