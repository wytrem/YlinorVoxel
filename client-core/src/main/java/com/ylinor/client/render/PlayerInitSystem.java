package com.ylinor.client.render;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.ylinor.client.physics.Heading;
import com.ylinor.client.physics.InputControlledEntity;
import com.ylinor.client.physics.Position;
import com.ylinor.client.physics.Velocity;
import com.ylinor.client.physics.Physics;


/**
 * Initializes the player entity in the world. Used only for debugging.
 * 
 * @author wytrem
 */
public class PlayerInitSystem extends BaseSystem {

    @Override
    protected void initialize() {
        Archetype playerArchetype = new ArchetypeBuilder().add(RenderViewEntity.class)
                                                          .add(InputControlledEntity.class)
                                                          .add(Heading.class)
                                                          .add(Position.class)
                                                          .add(Velocity.class)
                                                          .add(EyeHeight.class)
                                                          .add(Physics.class)
                                                          .build(world);
        
        
        Entity player = world.createEntity(playerArchetype);
        player.getComponent(EyeHeight.class).eyePadding.y = 1.65f * 0.85f;
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }
}
