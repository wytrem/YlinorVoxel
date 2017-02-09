package com.ylinor.client.physics;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;


/**
 * Manages physics in the world. This system applies the value of the
 * {@link Motion} component to the {@link AABB#position} vector with collision
 * check.
 * 
 * @author wytrem
 */
public class PhysicsSystem extends IteratingSystem {

//    @Wire
//    private YlinorClient client;
//
//    @Wire
//    private Terrain terrain;
//
//    @Wire
//    private ComponentMapper<Motion> motionMapper;
//
//    @Wire
//    private ComponentMapper<AABB> aabbMapper;
//
//    @Wire
//    private ComponentMapper<OnGround> onGroundMapper;
//
    public PhysicsSystem() {
//        super(Aspect.all(Motion.class, AABB.class));
        super(Aspect.all());
    }
//
//    private transient TempVars tempVars;
//
//    @Override
//    protected void begin() {
//        tempVars = TempVars.get();
//    }

    @Override
    protected void process(int entityId) {
//        Motion motion = motionMapper.get(entityId);
//        AABB aabb = aabbMapper.get(entityId);
//
//        // tempVars.vect0 is the position changement to be applied this tick
//        motion.motion.mul(world.getDelta(), tempVars.vect0);
//
//        // tempvars.vect1 is the new position (i.e. aabb.position + tempVars.vect0)
//        aabb.position.add(tempVars.vect0, tempVars.vect1);
//
//        if (!terrain.getBlockType(tempVars.vect1.x, tempVars.vect1.y, tempVars.vect1.z)
//                    .isCollidable()) {
//            aabb.position.set(tempVars.vect1);
//
//            if (tempVars.vect0.y > 0) {
//                onGroundMapper.remove(entityId);
//            }
//        }
//        else {
//            if (tempVars.vect0.y < 0) {
//                onGroundMapper.create(entityId);
//            }
//        }
//
//        motion.motion.mul(0.80f);
    }

//    @Override
//    protected void end() {
//        tempVars.release();
//    }
}
