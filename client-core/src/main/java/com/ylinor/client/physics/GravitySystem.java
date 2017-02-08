package com.ylinor.client.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;


/**
 * Applies the gravity to every {@link Gravitable} on the {@link Motion}
 * vertical axis property.
 *
 * @author wytrem
 */
public class GravitySystem extends IteratingSystem {

    @Wire
    private ComponentMapper<Motion> motionMapper;

    @Wire
    private ComponentMapper<Gravitable> gravitableMapper;

    @Wire
    private ComponentMapper<OnGround> onGroundMapper;

    public GravitySystem() {
        super(Aspect.all(Gravitable.class, Motion.class));
    }

    @Override
    protected void process(int entityId) {
        if (!onGroundMapper.has(entityId)) {
            Gravitable gravitable = gravitableMapper.get(entityId);
            Motion motion = motionMapper.get(entityId);

            motion.motion.y -= gravitable.g * world.delta;
        }
    }

}
