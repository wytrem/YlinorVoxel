package com.ylinor.client.physics.systems.modifiers;

import org.joml.Vector3f;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.physics.components.Physics;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.util.Pair;
import com.ylinor.library.util.math.BlockPos;


public class FinalStateUpdate extends MotionModifier {

    @Wire
    private ComponentMapper<Physics> physicsMapper;

    @SuppressWarnings("unchecked")
    @Override
    public Object apply(int entityId, Vector3f motion, Vector3f initialMotion, Object previousOuput) {

        Physics physics = physicsMapper.get(entityId);

        Pair<BlockState, BlockPos> blockPair = (Pair<BlockState, BlockPos>) previousOuput;

        if (initialMotion.x != motion.x) {
            physics.motion.x = 0.0f;
        }

        if (initialMotion.z != motion.z) {
            physics.motion.z = 0.0f;
        }

        if (initialMotion.y != motion.y) {
            blockPair.getKey().getBehaviour().onLanded(entityId);
            physics.motion.y = 0.0f;
        }

        return null;
    }

    @Override
    public boolean shouldApply(int entityId) {
        return physicsMapper.has(entityId);
    }
}
