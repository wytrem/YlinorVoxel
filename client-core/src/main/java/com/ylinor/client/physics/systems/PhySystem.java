package com.ylinor.client.physics.systems;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.physics.components.AABB;
import com.ylinor.client.physics.components.CollisionState;
import com.ylinor.client.physics.components.Heading;
import com.ylinor.client.physics.components.Physics;
import com.ylinor.client.physics.components.Position;
import com.ylinor.client.physics.components.Size;
import com.ylinor.client.physics.components.Sneaking;
import com.ylinor.client.physics.systems.modifiers.CalculatingWalkingBlock;
import com.ylinor.client.physics.systems.modifiers.FinalStateUpdate;
import com.ylinor.client.physics.systems.modifiers.MotionModifier;
import com.ylinor.client.physics.systems.modifiers.MoveColliding;
import com.ylinor.client.physics.systems.modifiers.Sneak;
import com.ylinor.client.physics.systems.modifiers.StateUpdate;
import com.ylinor.client.physics.systems.modifiers.StepHeight;
import com.ylinor.client.physics.systems.modifiers.UpdateFallState;
import com.ylinor.library.api.ecs.systems.TickingIteratingSystem;
import com.ylinor.library.api.ecs.systems.Timer;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.TempVars;
import com.ylinor.library.util.math.MathHelper;


public class PhySystem extends TickingIteratingSystem {

    @Wire
    private ComponentMapper<Physics> physicsMapper;

    @Wire
    private ComponentMapper<Position> positionMapper;

    @Wire
    private ComponentMapper<Heading> headingMapper;

    @Wire
    private ComponentMapper<Size> sizeMapper;

    @Wire
    private ComponentMapper<Sneaking> sneakingMapper;

    @Wire
    private ComponentMapper<AABB> aabbMapper;

    @Wire
    private ComponentMapper<CollisionState> collisionStateMapper;

    @Wire
    private Terrain terrain;

    @Wire
    private YlinorClient client;

    @Wire
    private Timer timer;

    private TempVars tempVars;

    public PhySystem() {
        super(Aspect.all(Physics.class, Position.class, Heading.class, Size.class, AABB.class, CollisionState.class));
    }

    private List<MotionModifier> modifiers;

    @Override
    protected void initialize() {
        modifiers = new ArrayList<>();

        modifiers.add(new Sneak());
        modifiers.add(new MoveColliding());
        modifiers.add(new StepHeight());
        modifiers.add(new StateUpdate());
        modifiers.add(new CalculatingWalkingBlock());
        modifiers.add(new UpdateFallState());
        modifiers.add(new FinalStateUpdate());

        for (MotionModifier modifier : modifiers) {
            world.inject(modifier);
        }
    }

    @Override
    protected void begin() {
        tempVars = TempVars.get();
    }

    private int entityId;
    private Heading heading;
    private Position position;
    private Physics physics;
    private Size size;
    private AABB aabb;
    private CollisionState collisionState;
    private boolean isSneaking;

    protected void tickEntity(int entityId) {
        this.entityId = entityId;
        heading = headingMapper.get(entityId);
        position = positionMapper.get(entityId);
        physics = physicsMapper.get(entityId);
        size = sizeMapper.get(entityId);
        aabb = aabbMapper.get(entityId);
        isSneaking = sneakingMapper.has(entityId);
        collisionState = collisionStateMapper.get(entityId);

        tick();
    }

    public void resetPositionToBB() {
        position.position.x = (aabb.aabb.minX + aabb.aabb.maxX) / 2.0f;
        position.position.y = aabb.aabb.minY;
        position.position.z = (aabb.aabb.minZ + aabb.aabb.maxZ) / 2.0f;
    }

    public void setPosition(int entityId, float x, float y, float z) {
        positionMapper.get(entityId).position.set(x, y, z);
        float halfWidth = sizeMapper.get(entityId).width / 2.0F;
        aabbMapper.get(entityId).aabb.set(x - halfWidth, y, z - halfWidth, x + halfWidth, y + sizeMapper.get(entityId).height, z + halfWidth);
    }

    public void tick() {
        physics.jumpMovementFactor = physics.speedInAir;

        position.prevPosition.set(position.position);
        physics.prevRotationPitch = physics.rotationPitch;
        physics.prevRotationYaw = physics.rotationYaw;

        if (Math.abs(physics.motion.x) < 0.003f) {
            physics.motion.x = 0.0f;
        }

        if (Math.abs(physics.motion.y) < 0.003f) {
            physics.motion.y = 0.0f;
        }

        if (Math.abs(physics.motion.z) < 0.003f) {
            physics.motion.z = 0.0f;
        }

        if (physics.jumpTicks > 0) {
            physics.jumpTicks--;
        }

        if (physics.isJumping) {
            if (collisionState.onGround && physics.jumpTicks == 0) {
                physics.jump();
                physics.jumpTicks = 10;
            }
        }
        else {
            physics.jumpTicks = 0;
        }

        physics.rotationYaw = (float) Math.atan2(heading.heading.x, heading.heading.z) * -MathHelper.RAD_TO_DEG;

        physics.moveStrafing *= 0.98f;
        physics.moveForward *= 0.98f;
        moveEntityWithHeading(physics.moveStrafing, physics.moveForward);
    }

    public void moveEntity(Vector3f initialMotion) {
        if (collisionState.noClip) {
            aabb.aabb.offsetLocal(initialMotion);
            resetPositionToBB();
        }
        else {
            // float d0 = position.position.x;
            // float d1 = position.position.y;
            // float d2 = position.position.z;

            Vector3f motion = new Vector3f(initialMotion);

            Object out = null;

            for (MotionModifier modifier : modifiers) {
                if (modifier.shouldApply(entityId)) {
                    out = modifier.apply(entityId, motion, initialMotion, out);
                }
            }

            //
            // if (this.canTriggerWalking() && !flag && !this.isRiding()) {
            // float d12 = position.position.x - d0;
            // float d13 = position.position.y - d1;
            // float d14 = position.position.z - d2;
            // if (block != Blocks.LADDER) {
            // d13 = 0.0f;
            // }
            //
            // if (block != null && this.onGround) {
            // block.onEntityWalk(worldObj, blockpos, this);
            // }
            //
            // this.distanceWalkedModified = (float) ((float)
            // this.distanceWalkedModified + (float) MathHelper.sqrt_float(d12 *
            // d12 + d14 * d14) * 0.6D);
            // this.distanceWalkedOnStepModified = (float) ((float)
            // this.distanceWalkedOnStepModified + (float)
            // MathHelper.sqrt_float(d12 * d12 + d13 * d13 + d14 * d14) * 0.6D);
            // if (this.distanceWalkedOnStepModified > (float)
            // this.nextStepDistance && iblockstate.getMaterial() !=
            // Material.AIR) {
            // this.nextStepDistance = (int) this.distanceWalkedOnStepModified +
            // 1;
            // if (this.isInWater()) {
            // float f = MathHelper.sqrt_float(this.motion.x * this.motion.x *
            // 0.20000000298023224D + this.motion.y * this.motion.y +
            // this.motion.z
            // * this.motion.z * 0.20000000298023224D) * 0.35F;
            // if (f > 1.0F) {
            // f = 1.0F;
            // }
            //
            // this.playSound(this.getSwimSound(), f, 1.0F +
            // (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            // }
            //
            // this.playStepSound(blockpos, block);
            // }
            // }

            // try {
            // this.doBlockCollisions();
            // }
            // catch (Throwable throwable) {
            // CrashReport crashreport = CrashReport.makeCrashReport(throwable,
            // "Checking entity block collision");
            // CrashReportCategory crashreportcategory =
            // crashreport.makeCategory("Entity being checked for collision");
            // this.addEntityCrashInfo(crashreportcategory);
            // throw new ReportedException(crashreport);
            // }
        }
    }

    public void debug(Physics physics, String str, Object... objects) {
        if (physics.jumpTicks > 0) {
            System.out.println(String.format(str, objects));
        }
    }

    public void moveEntityWithHeading(float strafe, float forward) {

        // NOUVEAU

        float f6 = 0.91F;
        tempVars.blockPos0.set(position.position.x, aabb.aabb.minY - 1.0D, position.position.z);
        if (collisionState.onGround) {
            f6 = terrain.getBlockState(tempVars.blockPos0)
                        .getAttributes()
                        .getSlipperiness() * 0.91F;
        }

        float f7 = 0.16277136F / (f6 * f6 * f6);
        float f8;
        if (collisionState.onGround) {
            f8 = physics.getAIMoveSpeed() * f7;
        }
        else {
            f8 = physics.jumpMovementFactor;
        }

        moveRelative(strafe, forward, f8);

        f6 = 0.91F;
        if (this.collisionState.onGround) {
            f6 = terrain.getBlockState(tempVars.blockPos0)
                        .getAttributes()
                        .getSlipperiness() * 0.91F;
        }

        // if(this.isOnLadder()) {
        // float f9 = 0.15F;
        // this.motion.x = MathHelper.clamp_double(this.motion.x,
        // -0.15000000596046448D, 0.15000000596046448D);
        // this.motion.z = MathHelper.clamp_double(this.motion.z,
        // -0.15000000596046448D, 0.15000000596046448D);
        // this.fallDistance = 0.0F;
        // if(this.motion.y < -0.15D) {
        // this.motion.y = -0.15D;
        // }
        //
        // boolean flag = this.isSneaking() && this instanceof EntityPlayer;
        // if(flag && this.motion.y < 0.0D) {
        // this.motion.y = 0.0D;
        // }
        // }

        moveEntity(physics.motion);
        // if(this.isCollidedHorizontally && this.isOnLadder()) {
        // this.motion.y = 0.2D;
        // }

        physics.motion.y -= 0.08D;

        physics.motion.y *= 0.9800000190734863D;
        physics.motion.x *= (double) f6;
        physics.motion.z *= (double) f6;
        // blockpos$pooledmutableblockpos.release();
    }

    public void moveRelative(float strafe, float forward, float friction) {
        float f = strafe * strafe + forward * forward;
        if (f >= 1.0E-4F) {
            f = (float) Math.sqrt(f);
            if (f < 1.0F) {
                f = 1.0F;
            }

            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;
            float f1 = MathHelper.sin(physics.rotationYaw * MathHelper.DEG_TO_RAD);
            float f2 = MathHelper.cos(physics.rotationYaw * MathHelper.DEG_TO_RAD);
            physics.motion.x += (strafe * f2 - forward * f1);
            physics.motion.z += (forward * f2 + strafe * f1);
        }
    }

    protected float func_189749_co() {
        return 0.8F;
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
