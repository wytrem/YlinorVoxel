package com.ylinor.client.physics.systems.modifiers;

import java.util.List;

import org.joml.Vector3f;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.physics.components.AABB;
import com.ylinor.client.physics.components.CollisionState;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.Pair;
import com.ylinor.library.util.math.AxisAlignedBB;


public class MoveColliding extends MotionModifier {
    @Wire
    private ComponentMapper<AABB> aabbMapper;

    @Wire
    private ComponentMapper<CollisionState> collisionStateMapper;

    @Wire
    private Terrain terrain;

    @Override
    public Object apply(int entityId, Vector3f motion, Vector3f initialMotion, Object previousOuput) {
        AABB aabb = aabbMapper.get(entityId);

        List<AxisAlignedBB> collisionBoxes = terrain.getCollisionBoxes(aabb.aabb.addCoord(motion.x, motion.y, motion.z));

        for (int i = 0; i < collisionBoxes.size(); i++) {
            motion.y = collisionBoxes.get(i)
                                     .calculateYOffset(aabb.aabb, motion.y);
        }
        AxisAlignedBB aabbMovedOnlyOnY = aabb.aabb.copy();
        boolean flag = collisionStateMapper.get(entityId).onGround || initialMotion.y != motion.y && initialMotion.y < 0.0f;
        aabb.aabb.offsetLocal(0.0f, motion.y, 0.0f);
        for (int i = 0; i < collisionBoxes.size(); i++) {
            motion.x = collisionBoxes.get(i)
                                     .calculateXOffset(aabb.aabb, motion.x);
        }

        aabb.aabb.offsetLocal(motion.x, 0.0f, 0.0f);
        for (int i = 0; i < collisionBoxes.size(); i++) {
            motion.z = collisionBoxes.get(i)
                                     .calculateZOffset(aabb.aabb, motion.z);
        }

        aabb.aabb.offsetLocal(0.0f, 0.0f, motion.z);

        return Pair.of(aabbMovedOnlyOnY, flag);
    }

    @Override
    public boolean shouldApply(int entityId) {
        return aabbMapper.has(entityId) && collisionStateMapper.has(entityId);
    }
}