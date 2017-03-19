package com.ylinor.client.physics.systems.modifiers;

import org.joml.Vector3f;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.physics.components.AABB;
import com.ylinor.client.physics.components.CollisionState;
import com.ylinor.client.physics.components.Sneaking;
import com.ylinor.library.api.terrain.Terrain;

public class Sneak extends MotionModifier {
    @Wire
    private ComponentMapper<Sneaking> sneakingMapper;

    @Wire
    private ComponentMapper<AABB> aabbMapper;

    @Wire
    private ComponentMapper<CollisionState> collisionStateMapper;

    @Wire
    private Terrain terrain;

    @Override
    public Object apply(int entityId, Vector3f motion, Vector3f initialMotion, Object previousOuput) {
        AABB aabb = aabbMapper.get(entityId);

        for (; motion.x != 0.0f && terrain.getCollisionBoxes(aabb.aabb.offset(motion.x, -1.0f, 0.0f))
                                          .isEmpty(); initialMotion.x = motion.x) {
            if (motion.x < 0.05f && motion.x >= -0.05f) {
                motion.x = 0.0f;
            }
            else if (motion.x > 0.0f) {
                motion.x -= 0.05f;
            }
            else {
                motion.x += 0.05f;
            }
        }

        for (; motion.z != 0.0f && terrain.getCollisionBoxes(aabb.aabb.offset(0.0f, -1.0f, motion.z))
                                          .isEmpty(); initialMotion.z = motion.z) {
            if (motion.z < 0.05f && motion.z >= -0.05f) {
                motion.z = 0.0f;
            }
            else if (motion.z > 0.0f) {
                motion.z -= 0.05f;
            }
            else {
                motion.z += 0.05f;
            }
        }

        for (; motion.x != 0.0f && motion.z != 0.0f && terrain.getCollisionBoxes(aabb.aabb.offset(motion.x, -1.0f, motion.z))
                                                              .isEmpty(); initialMotion.z = motion.z) {
            if (motion.x < 0.05f && motion.x >= -0.05f) {
                motion.x = 0.0f;
            }
            else if (motion.x > 0.0f) {
                motion.x -= 0.05f;
            }
            else {
                motion.x += 0.05f;
            }

            initialMotion.x = motion.x;
            if (motion.z < 0.05D && motion.z >= -0.05f) {
                motion.z = 0.0f;
            }
            else if (motion.z > 0.0f) {
                motion.z -= 0.05f;
            }
            else {
                motion.z += 0.05f;
            }
        }
        
        return null;
    }

    @Override
    public boolean shouldApply(int entityId) {
        return aabbMapper.has(entityId) && collisionStateMapper.has(entityId) && collisionStateMapper.get(entityId).onGround && sneakingMapper.has(entityId);
    }
}