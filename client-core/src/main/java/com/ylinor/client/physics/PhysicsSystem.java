package com.ylinor.client.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.ylinor.client.YlinorClient;

public class PhysicsSystem extends IteratingSystem {
    
    @Wire
    private YlinorClient client;
    
    @Wire
    private World world;

    private ComponentMapper<Velocity> velocityMapper;
    private ComponentMapper<Position> positionMapper;

    public PhysicsSystem() {
        super(Aspect.all(Velocity.class, Position.class));
    }

    @Override
    protected void process(int entityId) {
        Velocity vel = velocityMapper.get(entityId);
        Position pos = positionMapper.get(entityId);
        
        pos.x += vel.dx * world.getDelta();
        pos.y += vel.dy * world.getDelta();
        pos.z += vel.dz * world.getDelta();
    }
}
