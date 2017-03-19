package com.ylinor.client.physics.systems.modifiers;

import org.joml.Vector3f;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.physics.components.CollisionState;
import com.ylinor.client.physics.components.Physics;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.util.Pair;
import com.ylinor.library.util.math.BlockPos;


public class UpdateFallState extends MotionModifier {

    @Wire
    private ComponentMapper<CollisionState> collisionStateMapper;

    @Wire
    private ComponentMapper<Physics> physicsMapper;

    protected void updateFallState(Physics physics, int entityId, double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        if (onGroundIn) {
            if (physics.fallDistance > 0.0F) {
                state.getBlockType()
                     .onFallenUpon(pos, entityId, physics.fallDistance);
            }

            physics.fallDistance = 0.0F;
        }
        else if (y < 0.0D) {
            physics.fallDistance = (float) ((double) physics.fallDistance - y);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object apply(int entityId, Vector3f motion, Vector3f initialMotion, Object previousOuput) {

        Physics physics = physicsMapper.get(entityId);
        CollisionState collisionState = collisionStateMapper.get(entityId);

        Pair<BlockState, BlockPos> blockPair = (Pair<BlockState, BlockPos>) previousOuput;
        this.updateFallState(physics, entityId, motion.y, collisionState.onGround, blockPair.getKey(), blockPair.getValue());
        return blockPair;
    }

    @Override
    public boolean shouldApply(int entityId) {
        return physicsMapper.has(entityId) && collisionStateMapper.has(entityId);
    }
}
