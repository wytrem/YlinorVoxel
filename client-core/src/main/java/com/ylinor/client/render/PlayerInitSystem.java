package com.ylinor.client.render;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.ylinor.client.physics.AABB;
import com.ylinor.client.physics.Gravitable;
import com.ylinor.client.physics.Heading;
import com.ylinor.client.physics.InputControlledEntity;
import com.ylinor.client.physics.Motion;
import com.ylinor.client.physics.Velocity;

/**
 * Initializes the player entity in the world. Used only for debugging.
 * 
 * @author wytrem
 */
public class PlayerInitSystem extends BaseSystem {

    @Wire
    private ComponentMapper<AABB> aabbMapper;

    @Wire
    private ComponentMapper<EyeHeight> eyeHeightMapper;
    
    @Override
    protected void initialize() {
        Archetype playerArchetype = new ArchetypeBuilder().add(RenderViewEntity.class)
                                                          .add(InputControlledEntity.class)
                                                          .add(AABB.class)
                                                          .add(Heading.class)
                                                          .add(Motion.class)
                                                          .add(Velocity.class)
                                                          .add(EyeHeight.class)
                                                          .add(Gravitable.class)
                                                          .build(world);

        Entity player = world.createEntity(playerArchetype);
        player.getComponent(AABB.class).position.set(0,258,0);
        player.getComponent(EyeHeight.class).eyePadding.y = 1.6f;
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }
}
