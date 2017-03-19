package com.ylinor.client.physics.systems.modifiers;

import java.util.List;

import org.joml.Vector3f;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.physics.components.AABB;
import com.ylinor.client.physics.components.CollisionState;
import com.ylinor.client.physics.components.Physics;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.Pair;
import com.ylinor.library.util.math.AxisAlignedBB;

public class StepHeight extends MotionModifier {
    @Wire
    private ComponentMapper<AABB> aabbMapper;

    @Wire
    private ComponentMapper<CollisionState> collisionStateMapper;

    @Wire
    private ComponentMapper<Physics> physicsMapper;
    
    @Wire
    private Terrain terrain;

    @SuppressWarnings("unchecked")
    @Override
    public Object apply(int entityId, Vector3f motion, Vector3f initialMotion, Object previousOuput) {
        
        Physics physics = physicsMapper.get(entityId);
        AABB aabb = aabbMapper.get(entityId);
        boolean flag = ((Pair<AxisAlignedBB, Boolean>) previousOuput).getValue();
        AxisAlignedBB aabbMovedOnlyOnY = ((Pair<AxisAlignedBB, Boolean>) previousOuput).getKey();

        if (physics.stepHeight > 0.0F && flag && (initialMotion.x != motion.x || initialMotion.z != motion.z)) {
            float d11 = motion.x;
            float d7 = motion.y;
            float d8 = motion.z;
            AxisAlignedBB bbBeforeStep = aabb.aabb.copy();
            aabb.aabb.set(aabbMovedOnlyOnY);
            motion.y = (float) physics.stepHeight;
            List<AxisAlignedBB> list = terrain.getCollisionBoxes(
                    aabb.aabb.addCoord(initialMotion.x, motion.y, initialMotion.z));
            AxisAlignedBB axisalignedbb2 = aabb.aabb.copy();
            AxisAlignedBB axisalignedbb3 = axisalignedbb2.addCoord(initialMotion.x, 0.0f, initialMotion.z);
            float d9 = motion.y;
            int l = 0;

            for (int i1 = list.size(); l < i1; ++l) {
                d9 = (list.get(l)).calculateYOffset(axisalignedbb3, d9);
            }

            axisalignedbb2.offsetLocal(0.0f, d9, 0.0f);
            float d15 = initialMotion.x;
            int j1 = 0;

            for (int k1 = list.size(); j1 < k1; ++j1) {
                d15 = (list.get(j1)).calculateXOffset(axisalignedbb2, d15);
            }

            axisalignedbb2.offsetLocal(d15, 0.0f, 0.0f);
            float d16 = initialMotion.z;
            int l1 = 0;

            for (int i2 = list.size(); l1 < i2; ++l1) {
                d16 = (list.get(l1)).calculateZOffset(axisalignedbb2, d16);
            }

            axisalignedbb2.offsetLocal(0.0f, 0.0f, d16);
            AxisAlignedBB axisalignedbb4 = aabb.aabb.copy();
            float d17 = motion.y;
            int j2 = 0;

            for (int k2 = list.size(); j2 < k2; ++j2) {
                d17 = (list.get(j2)).calculateYOffset(axisalignedbb4, d17);
            }

            axisalignedbb4.offsetLocal(0.0f, d17, 0.0f);
            float d18 = initialMotion.x;
            int l2 = 0;

            for (int i3 = list.size(); l2 < i3; ++l2) {
                d18 = (list.get(l2)).calculateXOffset(axisalignedbb4, d18);
            }

            axisalignedbb4.offsetLocal(d18, 0.0f, 0.0f);
            float d19 = initialMotion.z;
            int j3 = 0;

            for (int k3 = list.size(); j3 < k3; ++j3) {
                d19 = (list.get(j3)).calculateZOffset(axisalignedbb4, d19);
            }

            axisalignedbb4.offsetLocal(0.0f, 0.0f, d19);
            float d20 = d15 * d15 + d16 * d16;
            float d10 = d18 * d18 + d19 * d19;
            if (d20 > d10) {
                motion.x = d15;
                motion.z = d16;
                motion.y = -d9;
                aabb.aabb.set(axisalignedbb2);
            } else {
                motion.x = d18;
                motion.z = d19;
                motion.y = -d17;
                aabb.aabb.set(axisalignedbb4);
            }

            int l3 = 0;

            for (int i4 = list.size(); l3 < i4; ++l3) {
                motion.y = (list.get(l3)).calculateYOffset(aabb.aabb, motion.y);
            }

            aabb.aabb.offsetLocal(0.0f, motion.y, 0.0f);
            if (d11 * d11 + d8 * d8 >= motion.x * motion.x + motion.z * motion.z) {
                motion.x = d11;
                motion.y = d7;
                motion.z = d8;
                aabb.aabb.set(bbBeforeStep);
            }
        }
        
        
        return null;
    }

    @Override
    public boolean shouldApply(int entityId) {
        return aabbMapper.has(entityId) && collisionStateMapper.has(entityId);
    }
}