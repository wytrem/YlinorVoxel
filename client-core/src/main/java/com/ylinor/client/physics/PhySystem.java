package com.ylinor.client.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.YlinorClient;
import com.ylinor.library.api.ecs.systems.TickingIteratingSystem;
import com.ylinor.library.api.ecs.systems.Timer;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.TempVars;


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
    
    private TempVars tempVars;
    
    @Override
    protected void begin() {
        tempVars = TempVars.get();
    }
    
    protected void tickEntity(int entityId) {
        Heading heading = headingMapper.get(entityId);
        Position position = positionMapper.get(entityId);
        Physics physics = physicsMapper.get(entityId);

        physics.tick(heading, terrain);
        position.position.set(physics.posX, physics.posY, physics.posZ);
    }
    
    @Override
    protected void end() {
        tempVars.release();
    }

    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
