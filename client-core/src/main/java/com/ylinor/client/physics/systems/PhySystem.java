package com.ylinor.client.physics.systems;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.joml.Vector3f;

import com.ylinor.client.YlinorClient;
import com.ylinor.library.api.ecs.components.AABB;
import com.ylinor.library.api.ecs.components.CollisionState;
import com.ylinor.library.api.ecs.components.Heading;
import com.ylinor.library.api.ecs.components.Physics;
import com.ylinor.library.api.ecs.components.Position;
import com.ylinor.library.api.ecs.components.Rotation;
import com.ylinor.library.api.ecs.components.Size;
import com.ylinor.library.api.ecs.components.Sneaking;
import com.ylinor.library.api.ecs.systems.TickingIteratingSystem;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.material.Material;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.util.TempVars;
import com.ylinor.library.util.ecs.entity.Aspect;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.math.AxisAlignedBB;
import com.ylinor.library.util.math.BlockPos;
import com.ylinor.library.util.math.MathHelper;

@Singleton
public class PhySystem extends TickingIteratingSystem {

    @Inject
    private Terrain terrain;

    @Inject
    private YlinorClient client;

    private TempVars tempVars;

    public PhySystem() {
        super(Aspect.all(Physics.class, Position.class, Heading.class, Rotation.class, Size.class, AABB.class, CollisionState.class));
    }

    @Override
    protected void begin() {
        tempVars = TempVars.get();
    }

    private Entity entityId;
    private Heading heading;
    private Position position;
    private Physics physics;
    private Size size;
    private AABB aabb;
    private CollisionState collisionState;
    private Rotation rotation;
    private boolean isSneaking;

    protected void tickEntity(Entity entityId) {
        this.entityId = entityId;
        heading = entityId.get(Heading.class);
        position = entityId.get(Position.class);
        physics = entityId.get(Physics.class);
        size = entityId.get(Size.class);
        aabb = entityId.get(AABB.class);
        isSneaking = entityId.has(Sneaking.class);
        collisionState = entityId.get(CollisionState.class);
        rotation = entityId.get(Rotation.class);

        tick();
    }

    public void resetPositionToBB() {
        position.position.x = (aabb.aabb.minX + aabb.aabb.maxX) / 2.0f;
        position.position.y = aabb.aabb.minY;
        position.position.z = (aabb.aabb.minZ + aabb.aabb.maxZ) / 2.0f;
    }

    public void setPosition(float x, float y, float z) {
        position.position.x = x;
        position.position.y = y;
        position.position.z = z;
        float halfWidth = size.width / 2.0F;
        aabb.aabb.set(x - halfWidth, y, z - halfWidth, x + halfWidth, y + size.height, z + halfWidth);
    }

    public void setPosition(Entity entityId, float x, float y, float z) {
        entityId.get(Position.class).position.set(x, y, z);
        float halfWidth = entityId.get(Size.class).width / 2.0F;
        entityId.get(AABB.class).aabb.set(x - halfWidth, y, z - halfWidth, x + halfWidth, y + entityId.get(Size.class).height, z + halfWidth);
    }

    public void tick() {
        physics.jumpMovementFactor = physics.speedInAir;

        position.prevPosition.set(position.position);
        rotation.prevRotationPitch = rotation.rotationPitch;
        rotation.prevRotationYaw = rotation.rotationYaw;

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

        rotation.rotationYaw = (float) Math.atan2(heading.heading.x, heading.heading.z) * -MathHelper.RAD_TO_DEG;

        physics.moveStrafing *= 0.98f;
        physics.moveForward *= 0.98f;
        moveEntityWithHeading(physics.moveStrafing, physics.moveForward);
    }

    public List<AxisAlignedBB> getCollisionBoxes(AxisAlignedBB aabb) {
        return getCollisionBoxes(aabb, -1);
    }

    public List<AxisAlignedBB> getCollisionBoxes(AxisAlignedBB aabb, int entityId) {
        List<AxisAlignedBB> list = new ArrayList<>();
        int minX = MathHelper.floor_double(aabb.minX) - 1;
        int maxX = MathHelper.ceiling_double_int(aabb.maxX) + 1;
        int minY = MathHelper.floor_double(aabb.minY) - 1;
        int maxY = MathHelper.ceiling_double_int(aabb.maxY) + 1;
        int minZ = MathHelper.floor_double(aabb.minZ) - 1;
        int maxZ = MathHelper.ceiling_double_int(aabb.maxZ) + 1;
        TempVars tempVars = TempVars.get();

        for (int x = minX; x < maxX; ++x) {
            for (int z = minZ; z < maxZ; ++z) {
                int i2 = (x != minX && x != maxX - 1 ? 0 : 1) + (z != minZ && z != maxZ - 1 ? 0 : 1);
                if (i2 != 2) {
                    for (int y = minY; y < maxY; ++y) {
                        if (i2 <= 0 || y != minY && y != maxY - 1) {
                            tempVars.blockPos0.set(x, y, z);

                            terrain.getBlockState(tempVars.blockPos0)
                                   .getAttributes()
                                   .addCollisionBoxToList(terrain, tempVars.blockPos0, aabb, list, entityId, false);
                        }
                    }
                }
            }
        }

        tempVars.release();

        // blockpos$pooledmutableblockpos.release();
        // if(entityIn != null) {
        // List<Entity> list1 =
        // this.getEntitiesWithinAABBExcludingEntity(entityIn,
        // aabb.expandXyz(0.25D));
        //
        // for(int k2 = 0; k2 < list1.size(); ++k2) {
        // Entity entity = (Entity)list1.get(k2);
        // if(!entityIn.isRidingSameEntity(entity)) {
        // AxisAlignedBB axisalignedbb = entity.getCollisionBoundingBox();
        // if(axisalignedbb != null && axisalignedbb.intersectsWith(aabb)) {
        // list.add(axisalignedbb);
        // }
        //
        // axisalignedbb = entityIn.getCollisionBox(entity);
        // if(axisalignedbb != null && axisalignedbb.intersectsWith(aabb)) {
        // list.add(axisalignedbb);
        // }
        // }
        // }
        // }

        return list;
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

            boolean shouldApplySneak = collisionState.onGround && isSneaking;
            shouldApplySneak = false;
            if (shouldApplySneak) {
                for (; motion.x != 0.0f && getCollisionBoxes(aabb.aabb.offset(motion.x, -1.0f, 0.0f)).isEmpty(); initialMotion.x = motion.x) {
                    if (motion.x < 0.05f && motion.x >= -0.05f) {
                        motion.x = 0.0f;
                    }
                    else if (motion.x > 0.0f) {
                        motion.x -= 0.05f;
                    }
                    else {
                        motion.x += 0.05f;
                    }
                }

                for (; motion.z != 0.0f && getCollisionBoxes(aabb.aabb.offset(0.0f, -1.0f, motion.z)).isEmpty(); initialMotion.z = motion.z) {
                    if (motion.z < 0.05f && motion.z >= -0.05f) {
                        motion.z = 0.0f;
                    }
                    else if (motion.z > 0.0f) {
                        motion.z -= 0.05f;
                    }
                    else {
                        motion.z += 0.05f;
                    }
                }

                for (; motion.x != 0.0f && motion.z != 0.0f && getCollisionBoxes(aabb.aabb.offset(motion.x, -1.0f, motion.z)).isEmpty(); initialMotion.z = motion.z) {
                    if (motion.x < 0.05f && motion.x >= -0.05f) {
                        motion.x = 0.0f;
                    }
                    else if (motion.x > 0.0f) {
                        motion.x -= 0.05f;
                    }
                    else {
                        motion.x += 0.05f;
                    }

                    initialMotion.x = motion.x;
                    if (motion.z < 0.05D && motion.z >= -0.05f) {
                        motion.z = 0.0f;
                    }
                    else if (motion.z > 0.0f) {
                        motion.z -= 0.05f;
                    }
                    else {
                        motion.z += 0.05f;
                    }
                }
            }

            List<AxisAlignedBB> collisionBoxes = getCollisionBoxes(aabb.aabb.addCoord(motion.x, motion.y, motion.z));

            for (int i = 0; i < collisionBoxes.size(); i++) {
                motion.y = collisionBoxes.get(i)
                                         .calculateYOffset(aabb.aabb, motion.y);
            }
            AxisAlignedBB aabbMovedOnlyOnY = aabb.aabb.copy();
            boolean flag = collisionState.onGround || initialMotion.y != motion.y && initialMotion.y < 0.0f;
            aabb.aabb.offsetLocal(0.0f, motion.y, 0.0f);
            for (int i = 0; i < collisionBoxes.size(); i++) {
                motion.x = collisionBoxes.get(i)
                                         .calculateXOffset(aabb.aabb, motion.x);
            }

            aabb.aabb.offsetLocal(motion.x, 0.0f, 0.0f);
            for (int i = 0; i < collisionBoxes.size(); i++) {
                motion.z = collisionBoxes.get(i)
                                         .calculateZOffset(aabb.aabb, motion.z);
            }

            aabb.aabb.offsetLocal(0.0f, 0.0f, motion.z);

            if (physics.stepHeight > 0.0F && flag && (initialMotion.x != motion.x || initialMotion.z != motion.z)) {
                float d11 = motion.x;
                float d7 = motion.y;
                float d8 = motion.z;
                AxisAlignedBB bbBeforeStep = aabb.aabb.copy();
                aabb.aabb.set(aabbMovedOnlyOnY);
                motion.y = (float) physics.stepHeight;
                List<AxisAlignedBB> list = getCollisionBoxes(aabb.aabb.addCoord(initialMotion.x, motion.y, initialMotion.z));
                AxisAlignedBB axisalignedbb2 = aabb.aabb.copy();
                AxisAlignedBB axisalignedbb3 = axisalignedbb2.addCoord(initialMotion.x, 0.0f, initialMotion.z);
                float d9 = motion.y;
                int l = 0;

                for (int i1 = list.size(); l < i1; ++l) {
                    d9 = (list.get(l)).calculateYOffset(axisalignedbb3, d9);
                }

                axisalignedbb2.offsetLocal(0.0f, d9, 0.0f);
                float d15 = initialMotion.x;
                int j1 = 0;

                for (int k1 = list.size(); j1 < k1; ++j1) {
                    d15 = (list.get(j1)).calculateXOffset(axisalignedbb2, d15);
                }

                axisalignedbb2.offsetLocal(d15, 0.0f, 0.0f);
                float d16 = initialMotion.z;
                int l1 = 0;

                for (int i2 = list.size(); l1 < i2; ++l1) {
                    d16 = (list.get(l1)).calculateZOffset(axisalignedbb2, d16);
                }

                axisalignedbb2.offsetLocal(0.0f, 0.0f, d16);
                AxisAlignedBB axisalignedbb4 = aabb.aabb.copy();
                float d17 = motion.y;
                int j2 = 0;

                for (int k2 = list.size(); j2 < k2; ++j2) {
                    d17 = (list.get(j2)).calculateYOffset(axisalignedbb4, d17);
                }

                axisalignedbb4.offsetLocal(0.0f, d17, 0.0f);
                float d18 = initialMotion.x;
                int l2 = 0;

                for (int i3 = list.size(); l2 < i3; ++l2) {
                    d18 = (list.get(l2)).calculateXOffset(axisalignedbb4, d18);
                }

                axisalignedbb4.offsetLocal(d18, 0.0f, 0.0f);
                float d19 = initialMotion.z;
                int j3 = 0;

                for (int k3 = list.size(); j3 < k3; ++j3) {
                    d19 = (list.get(j3)).calculateZOffset(axisalignedbb4, d19);
                }

                axisalignedbb4.offsetLocal(0.0f, 0.0f, d19);
                float d20 = d15 * d15 + d16 * d16;
                float d10 = d18 * d18 + d19 * d19;
                if (d20 > d10) {
                    motion.x = d15;
                    motion.z = d16;
                    motion.y = -d9;
                    aabb.aabb.set(axisalignedbb2);
                }
                else {
                    motion.x = d18;
                    motion.z = d19;
                    motion.y = -d17;
                    aabb.aabb.set(axisalignedbb4);
                }

                int l3 = 0;

                for (int i4 = list.size(); l3 < i4; ++l3) {
                    motion.y = (list.get(l3)).calculateYOffset(aabb.aabb, motion.y);
                }

                aabb.aabb.offsetLocal(0.0f, motion.y, 0.0f);
                // this.setEntityBoundingBox(this.getEntityBoundingBox()
                // .offset(0.0f, y, 0.0f));
                if (d11 * d11 + d8 * d8 >= motion.x * motion.x + motion.z * motion.z) {
                    motion.x = d11;
                    motion.y = d7;
                    motion.z = d8;
                    aabb.aabb.set(bbBeforeStep);
                }
            }

            resetPositionToBB();
            collisionState.isCollidedHorizontally = initialMotion.x != motion.x || initialMotion.z != motion.z;
            collisionState.isCollidedVertically = initialMotion.y != motion.y;

            collisionState.onGround = collisionState.isCollidedVertically && initialMotion.y < 0.0f;
            collisionState.isCollided = collisionState.isCollidedHorizontally || collisionState.isCollidedVertically;
            int j4 = MathHelper.floor_float(position.position.x);
            int l4 = MathHelper.floor_float(position.position.y - 0.20000000298023224f);
            int i5 = MathHelper.floor_float(position.position.z);
            BlockPos blockpos = new BlockPos(j4, l4, i5);
            BlockState iblockstate = terrain.getBlockState(blockpos);
            if (iblockstate.getAttributes().getMaterial() == Material.AIR) {
//                BlockPos blockpos1 = blockpos.down();
//                BlockState iblockstate1 = terrain.getBlockState(blockpos1);
//                BlockType block1 = iblockstate1.getBlockType();
                // if (block1 instanceof BlockFence || block1 instanceof
                // BlockWall || block1 instanceof BlockFenceGate) {
                // iblockstate = iblockstate1;
                // blockpos = blockpos1;
                // }
            }

            this.updateFallState(motion.y, collisionState.onGround, iblockstate, blockpos);
            if (initialMotion.x != motion.x) {
                physics.motion.x = 0.0f;
            }

            if (initialMotion.z != motion.z) {
                physics.motion.z = 0.0f;
            }

            if (initialMotion.y != motion.y) {
                iblockstate.getBehaviour().onLanded(world, entityId);
                physics.motion.y = 0.0f;
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

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        if (onGroundIn) {
            if (physics.fallDistance > 0.0F) {
                state.getBlockType()
                     .onFallenUpon(world, pos, entityId, physics.fallDistance);
            }

            physics.fallDistance = 0.0F;
        }
        else if (y < 0.0D) {
            physics.fallDistance = (float) ((double) physics.fallDistance - y);
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
            float f1 = MathHelper.sin(rotation.rotationYaw * MathHelper.DEG_TO_RAD);
            float f2 = MathHelper.cos(rotation.rotationYaw * MathHelper.DEG_TO_RAD);
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

    public static interface MotionModifier {
        boolean apply(int entityId, Vector3f initialMotion, Vector3f modified);
    }
}
