package com.ylinor.client.physics.systems.modifiers;

import org.joml.Vector3f;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.physics.components.Position;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.util.Pair;
import com.ylinor.library.util.math.BlockPos;
import com.ylinor.library.util.math.MathHelper;

public class CalculatingWalkingBlock extends MotionModifier {

    @Wire
    private ComponentMapper<Position> positionMapper;
    
    
    @Wire
    private Terrain terrain;
    
    @Override
    public Object apply(int entityId, Vector3f motion, Vector3f initialMotion, Object previousOuput) {
        Position position = positionMapper.get(entityId);
        
        int j4 = MathHelper.floor_float(position.position.x);
        int l4 = MathHelper.floor_float(position.position.y - 0.20000000298023224f);
        int i5 = MathHelper.floor_float(position.position.z);
        BlockPos blockpos = new BlockPos(j4, l4, i5);
        BlockState iblockstate = terrain.getBlockState(blockpos);
        if (iblockstate.getAttributes().getMaterial() == Material.AIR) {
            BlockPos blockpos1 = blockpos.down();
            BlockState iblockstate1 = terrain.getBlockState(blockpos1);
//            BlockType block1 = iblockstate1.getBlockType();
//             if (block1 instanceof BlockFence || block1 instanceof
//             BlockWall || block1 instanceof BlockFenceGate) {
             iblockstate = iblockstate1;
             blockpos = blockpos1;
//             }
        }
        return Pair.of(iblockstate, blockpos);
    }

    @Override
    public boolean shouldApply(int entityId) {
        return positionMapper.has(entityId);
    }
}
