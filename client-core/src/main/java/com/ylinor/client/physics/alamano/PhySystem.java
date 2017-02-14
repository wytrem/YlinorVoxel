package com.ylinor.client.physics.alamano;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.physics.Heading;
import com.ylinor.client.physics.Position;
import com.ylinor.library.api.ecs.systems.TickingIteratingSystem;
import com.ylinor.library.api.ecs.systems.Timer;
import com.ylinor.library.api.terrain.Terrain;


public class PhySystem extends TickingIteratingSystem {

    public PhySystem() {
        super(Aspect.all(Physics.class, Position.class, Heading.class));
    }

    private ComponentMapper<Physics> physicsMapper;
    private ComponentMapper<Position> positionMapper;
    private ComponentMapper<Heading> headingMapper;

    @Wire
    private Terrain terrain;

    @Wire
    private YlinorClient client;
    
    @Wire
    private Timer timer;
    
    protected void tickEntity(int entityId) {
        Heading heading = headingMapper.get(entityId);
        Position position = positionMapper.get(entityId);
        Physics physics = physicsMapper.get(entityId);

        physics.tick(world.delta, heading, terrain);
        position.position.set(physics.posX, physics.posY, physics.posZ);
    }

    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
