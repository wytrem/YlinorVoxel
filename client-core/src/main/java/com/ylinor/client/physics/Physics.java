package com.ylinor.client.physics;

import java.util.ArrayList;
import java.util.List;

import com.artemis.Component;
import com.ylinor.library.api.terrain.BlockExtraData;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.TempVars;
import com.ylinor.library.util.math.BlockPos;
import com.ylinor.library.util.math.MathHelper;


public class Physics extends Component {
    public float moveStrafing;
    public float moveForward;
    public float prevPosX;
    public float prevPosY;
    public float prevPosZ;
    public float posX;
    public float posY;
    public float posZ;
    public float motionX;
    public float motionY;
    public float motionZ;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    public AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean isCollidedHorizontally;
    public boolean isCollidedVertically;
    public boolean isCollided;
    public boolean noClip;
    public float stepHeight;
    public float fallDistance;
    public float landMovementFactor = 0.1f;
    public boolean isJumping;
    public int jumpTicks;
    public float width;
    public float height;
    public float jumpMovementFactor = 0.02F;
    public float speedInAir = 0.02F;
    //    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

    public static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);

    public Physics() {
        this.stepHeight = -1.f;
        this.boundingBox = new AxisAlignedBB(0,0,0,1,1,1);
        this.width = 0.6F;
        this.height = 1.8F;
        setPosition(0, 260, 0);
    }

    public void setPosition(float x, float y, float z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float halfWidth = this.width / 2.0F;
        this.getEntityBoundingBox().set(x - halfWidth, y, z - halfWidth, x + halfWidth, y + this.height, z + halfWidth);
    }

    public void tick(Heading heading, Terrain terrain) {
        this.jumpMovementFactor = this.speedInAir;

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        if (Math.abs(this.motionX) < 0.003f) {
            this.motionX = 0.0f;
        }

        if (Math.abs(this.motionY) < 0.003f) {
            this.motionY = 0.0f;
        }

        if (Math.abs(this.motionZ) < 0.003f) {
            this.motionZ = 0.0f;
        }

        if (this.jumpTicks > 0) {
            this.jumpTicks--;
        }

        if (this.isJumping) {
            if (this.onGround && this.jumpTicks == 0) {
                this.jump();
                this.jumpTicks = 10;
            }
        }
        else {
            this.jumpTicks = 0;
        }

        this.rotationYaw = (float) Math.atan2(heading.heading.x, heading.heading.z) * -MathHelper.RAD_TO_DEG;

        this.moveStrafing *= 0.98f;
        this.moveForward *= 0.98f;
        this.moveEntityWithHeading(terrain, this.moveStrafing, this.moveForward);
    }

    protected float getJumpUpwardsMotion() {
        return 0.42F;
    }

    protected void jump() {
        this.motionY = this.getJumpUpwardsMotion();
    }

    public List<AxisAlignedBB> getCollisionBoxes(Terrain terrain, AxisAlignedBB aabb) {
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

                            //                       IBlockState iblockstate1 = iblockstate;
                            //                       if(worldborder.contains(blockpos$pooledmutableblockpos) || !flag1) {
                            //                          iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos);
                            //                       }
                            //
                            //                       iblockstate1.addCollisionBoxToList(this, blockpos$pooledmutableblockpos, aabb, list, entityIn);

                            if (terrain.getBlockType(tempVars.blockPos0)
                                       .isCollidable()) {

                                AxisAlignedBB blockBb = FULL_BLOCK_AABB.copy().offsetLocal(tempVars.blockPos0);
                                if (aabb.intersectsWith(blockBb)) {
                                    list.add(blockBb);
                                }
                            }
                        }
                    }
                }
            }
        }

        tempVars.release();

        //        blockpos$pooledmutableblockpos.release();
        //        if(entityIn != null) {
        //           List<Entity> list1 = this.getEntitiesWithinAABBExcludingEntity(entityIn, aabb.expandXyz(0.25D));
        //
        //           for(int k2 = 0; k2 < list1.size(); ++k2) {
        //              Entity entity = (Entity)list1.get(k2);
        //              if(!entityIn.isRidingSameEntity(entity)) {
        //                 AxisAlignedBB axisalignedbb = entity.getCollisionBoundingBox();
        //                 if(axisalignedbb != null && axisalignedbb.intersectsWith(aabb)) {
        //                    list.add(axisalignedbb);
        //                 }
        //
        //                 axisalignedbb = entityIn.getCollisionBox(entity);
        //                 if(axisalignedbb != null && axisalignedbb.intersectsWith(aabb)) {
        //                    list.add(axisalignedbb);
        //                 }
        //              }
        //           }
        //        }


        return list;
    }

    public void moveEntity(float x, float y, float z, Terrain terrain) {
        if (this.noClip) {
            this.getEntityBoundingBox().offsetLocal(x, y, z);
            this.resetPositionToBB();
        }
        else {
            //            float d0 = this.posX;
            //            float d1 = this.posY;
            //            float d2 = this.posZ;

            float initialX = x;
            float initialY = y;
            float initialZ = z;
            boolean shouldApplySneak = this.onGround/*
                                                     * && this.isSneaking() &&
                                                     * this instanceof
                                                     * EntityPlayer
                                                     */;

            shouldApplySneak = false;
            if (shouldApplySneak) {
                for (; x != 0.0f && getCollisionBoxes(terrain, this.getEntityBoundingBox()
                                                                   .offset(x, -1.0f, 0.0f)).isEmpty(); initialX = x) {
                    if (x < 0.05f && x >= -0.05f) {
                        x = 0.0f;
                    }
                    else if (x > 0.0f) {
                        x -= 0.05f;
                    }
                    else {
                        x += 0.05f;
                    }
                }

                for (; z != 0.0f && getCollisionBoxes(terrain, this.getEntityBoundingBox()
                                                                   .offset(0.0f, -1.0f, z)).isEmpty(); initialZ = z) {
                    if (z < 0.05f && z >= -0.05f) {
                        z = 0.0f;
                    }
                    else if (z > 0.0f) {
                        z -= 0.05f;
                    }
                    else {
                        z += 0.05f;
                    }
                }

                for (; x != 0.0f && z != 0.0f && getCollisionBoxes(terrain, this.getEntityBoundingBox()
                                                                                .offset(x, -1.0f, z)).isEmpty(); initialZ = z) {
                    if (x < 0.05f && x >= -0.05f) {
                        x = 0.0f;
                    }
                    else if (x > 0.0f) {
                        x -= 0.05f;
                    }
                    else {
                        x += 0.05f;
                    }

                    initialX = x;
                    if (z < 0.05D && z >= -0.05f) {
                        z = 0.0f;
                    }
                    else if (z > 0.0f) {
                        z -= 0.05f;
                    }
                    else {
                        z += 0.05f;
                    }
                }
            }

            List<AxisAlignedBB> collisionBoxes = getCollisionBoxes(terrain, this.getEntityBoundingBox()
                                                                                .addCoord(x, y, z));

            for (int i = 0; i < collisionBoxes.size(); i++) {
                y = collisionBoxes.get(i)
                                  .calculateYOffset(this.getEntityBoundingBox(), y);
            }
            AxisAlignedBB aabbMovedOnlyOnY = this.getEntityBoundingBox().copy();
            boolean flag = this.onGround || initialY != y && initialY < 0.0f;
            this.getEntityBoundingBox().offsetLocal(0.0f, y, 0.0f);
            for (int i = 0; i < collisionBoxes.size(); i++) {
                x = collisionBoxes.get(i)
                                  .calculateXOffset(this.getEntityBoundingBox(), x);
            }

            this.getEntityBoundingBox().offsetLocal(x, 0.0f, 0.0f);
            for (int i = 0; i < collisionBoxes.size(); i++) {
                z = collisionBoxes.get(i)
                                  .calculateZOffset(this.getEntityBoundingBox(), z);
            }

            this.getEntityBoundingBox().offsetLocal(0.0f, 0.0f, z);

            if (this.stepHeight > 0.0F && flag && (initialX != x || initialZ != z)) {
                float d11 = x;
                float d7 = y;
                float d8 = z;
                AxisAlignedBB bbBeforeStep = this.getEntityBoundingBox().copy();
                this.getEntityBoundingBox().set(aabbMovedOnlyOnY);
                y = (float) this.stepHeight;
                List<AxisAlignedBB> list = getCollisionBoxes(terrain, this.getEntityBoundingBox()
                                                                          .addCoord(initialX, y, initialZ));
                AxisAlignedBB axisalignedbb2 = this.getEntityBoundingBox().copy();
                AxisAlignedBB axisalignedbb3 = axisalignedbb2.addCoord(initialX, 0.0f, initialZ);
                float d9 = y;
                int l = 0;

                for (int i1 = list.size(); l < i1; ++l) {
                    d9 = (list.get(l)).calculateYOffset(axisalignedbb3, d9);
                }

                axisalignedbb2.offsetLocal(0.0f, d9, 0.0f);
                float d15 = initialX;
                int j1 = 0;

                for (int k1 = list.size(); j1 < k1; ++j1) {
                    d15 = (list.get(j1)).calculateXOffset(axisalignedbb2, d15);
                }

                axisalignedbb2.offsetLocal(d15, 0.0f, 0.0f);
                float d16 = initialZ;
                int l1 = 0;

                for (int i2 = list.size(); l1 < i2; ++l1) {
                    d16 = (list.get(l1)).calculateZOffset(axisalignedbb2, d16);
                }

                axisalignedbb2.offsetLocal(0.0f, 0.0f, d16);
                AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox().copy();
                float d17 = y;
                int j2 = 0;

                for (int k2 = list.size(); j2 < k2; ++j2) {
                    d17 = (list.get(j2)).calculateYOffset(axisalignedbb4, d17);
                }

                axisalignedbb4.offsetLocal(0.0f, d17, 0.0f);
                float d18 = initialX;
                int l2 = 0;

                for (int i3 = list.size(); l2 < i3; ++l2) {
                    d18 = (list.get(l2)).calculateXOffset(axisalignedbb4, d18);
                }

                axisalignedbb4.offsetLocal(d18, 0.0f, 0.0f);
                float d19 = initialZ;
                int j3 = 0;

                for (int k3 = list.size(); j3 < k3; ++j3) {
                    d19 = (list.get(j3)).calculateZOffset(axisalignedbb4, d19);
                }

                axisalignedbb4.offsetLocal(0.0f, 0.0f, d19);
                float d20 = d15 * d15 + d16 * d16;
                float d10 = d18 * d18 + d19 * d19;
                if (d20 > d10) {
                    x = d15;
                    z = d16;
                    y = -d9;
                    this.getEntityBoundingBox().set(axisalignedbb2);
                }
                else {
                    x = d18;
                    z = d19;
                    y = -d17;
                    this.getEntityBoundingBox().set(axisalignedbb4);
                }

                int l3 = 0;

                for (int i4 = list.size(); l3 < i4; ++l3) {
                    y = (list.get(l3)).calculateYOffset(this.getEntityBoundingBox(), y);
                }


                this.getEntityBoundingBox().offsetLocal(0.0f, y, 0.0f);
//                this.setEntityBoundingBox(this.getEntityBoundingBox()
//                                              .offset(0.0f, y, 0.0f));
                if (d11 * d11 + d8 * d8 >= x * x + z * z) {
                    x = d11;
                    y = d7;
                    z = d8;
                    this.getEntityBoundingBox().set(bbBeforeStep);
                }
            }

            this.resetPositionToBB();
            this.isCollidedHorizontally = initialX != x || initialZ != z;
            this.isCollidedVertically = initialY != y;

            this.onGround = this.isCollidedVertically && initialY < 0.0f;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            //            j4 = MathHelper.floor_float(this.posX);
            //            int l4 = MathHelper.floor_float(this.posY - 0.20000000298023224f);
            //            int i5 = MathHelper.floor_float(this.posZ);
            //            BlockPos blockpos = new BlockPos(j4, l4, i5);
            //            BlockExtraData iblockstate = terrain.getBlockData(blockpos);
            //            if (iblockstate.getMaterial() == Material.AIR) {
            //                BlockPos blockpos1 = blockpos.down();
            //                IBlockState iblockstate1 = worldObj.getBlockState(blockpos1);
            //                Block block1 = iblockstate1.getBlock();
            //                if (block1 instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate) {
            //                    iblockstate = iblockstate1;
            //                    blockpos = blockpos1;
            //                }
            //            }

            //            this.updateFallState(terrain, y, this.onGround, iblockstate, blockpos);
            if (initialX != x) {
                this.motionX = 0.0f;
            }

            if (initialZ != z) {
                this.motionZ = 0.0f;
            }

            //            Block block = iblockstate.getBlock();
            if (initialY != y) {
                //                block.onLanded(worldObj, this);
                this.motionY = 0.0f;
            }
            //
            //            if (this.canTriggerWalking() && !flag && !this.isRiding()) {
            //                float d12 = this.posX - d0;
            //                float d13 = this.posY - d1;
            //                float d14 = this.posZ - d2;
            //                if (block != Blocks.LADDER) {
            //                    d13 = 0.0f;
            //                }
            //
            //                if (block != null && this.onGround) {
            //                    block.onEntityWalk(worldObj, blockpos, this);
            //                }
            //
            //                this.distanceWalkedModified = (float) ((float) this.distanceWalkedModified + (float) MathHelper.sqrt_float(d12 * d12 + d14 * d14) * 0.6D);
            //                this.distanceWalkedOnStepModified = (float) ((float) this.distanceWalkedOnStepModified + (float) MathHelper.sqrt_float(d12 * d12 + d13 * d13 + d14 * d14) * 0.6D);
            //                if (this.distanceWalkedOnStepModified > (float) this.nextStepDistance && iblockstate.getMaterial() != Material.AIR) {
            //                    this.nextStepDistance = (int) this.distanceWalkedOnStepModified + 1;
            //                    if (this.isInWater()) {
            //                        float f = MathHelper.sqrt_float(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;
            //                        if (f > 1.0F) {
            //                            f = 1.0F;
            //                        }
            //
            //                        this.playSound(this.getSwimSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            //                    }
            //
            //                    this.playStepSound(blockpos, block);
            //                }
            //            }

            //            try {
            //                this.doBlockCollisions();
            //            }
            //            catch (Throwable throwable) {
            //                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
            //                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
            //                this.addEntityCrashInfo(crashreportcategory);
            //                throw new ReportedException(crashreport);
            //            }
        }
    }

    public void debug(String str, Object... objects) {
        if (this.jumpTicks > 0) {
            System.out.println(String.format(str, objects));
        }
    }

    protected void updateFallState(Terrain terrain, double y, boolean onGroundIn, BlockExtraData state, BlockPos pos) {
        if (onGroundIn) {
            if (this.fallDistance > 0.0F) {
                //              state.getBlock().onFallenUpon(terrain, pos, this, this.fallDistance);
            }

            this.fallDistance = 0.0F;
        }
        else if (y < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - y);
        }
    }

    public void moveEntityWithHeading(Terrain terrain, float strafe, float forward) {

        //VIEUX
        //        double d0 = this.posY;
        //        float f1 = this.func_189749_co();
        //        float f2 = 0.02F;
        //        float f3 = 3.0f;
        //
        //        if (!this.onGround) {
        //            f3 *= 0.5F;
        //        }
        //
        //        if (f3 > 0.0F) {
        //            f1 += (0.54600006F - f1) * f3 / 3.0F;
        //            f2 += (this.getAIMoveSpeed() - f2) * f3 / 3.0F;
        //        }
        //        
        //        this.moveRelative(strafe, forward, f2);
        //        this.moveEntity(this.motionX, this.motionY, this.motionZ, terrain);
        //        this.motionX *= f1;
        //        this.motionY *= 0.800000011920929f;
        //        this.motionZ *= f1;
        //        // Gravity
        //        this.motionY -= 0.08f;
        //        
        //        //        if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ)) {
        //        //            this.motionY = 0.30000001192092896f;
        //        //        }

        // NOUVEAU

        float f6 = 0.91F;
        //        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ);
        //        if(this.onGround) {
        //           f6 = this.worldObj.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91F;
        //        }

        f6 = 0.6f * 0.91f;

        float f7 = 0.16277136F / (f6 * f6 * f6);
        float f8;
        if (this.onGround) {
            f8 = this.getAIMoveSpeed() * f7;
        }
        else {
            f8 = this.jumpMovementFactor;
        }

        this.moveRelative(strafe, forward, f8);

        f6 = 0.91F;
        //        if(this.onGround) {
        //           f6 = this.worldObj.getBlockState(blockpos$pooledmutableblockpos.func_189532_c(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ)).getBlock().slipperiness * 0.91F;
        //        }

        f6 = 0.91f;

        if (this.onGround) {
            f6 *= 0.6f;
        }

        //        if(this.isOnLadder()) {
        //           float f9 = 0.15F;
        //           this.motionX = MathHelper.clamp_double(this.motionX, -0.15000000596046448D, 0.15000000596046448D);
        //           this.motionZ = MathHelper.clamp_double(this.motionZ, -0.15000000596046448D, 0.15000000596046448D);
        //           this.fallDistance = 0.0F;
        //           if(this.motionY < -0.15D) {
        //              this.motionY = -0.15D;
        //           }
        //
        //           boolean flag = this.isSneaking() && this instanceof EntityPlayer;
        //           if(flag && this.motionY < 0.0D) {
        //              this.motionY = 0.0D;
        //           }
        //        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ, terrain);
        //        if(this.isCollidedHorizontally && this.isOnLadder()) {
        //           this.motionY = 0.2D;
        //        }

        this.motionY -= 0.08D;

        this.motionY *= 0.9800000190734863D;
        this.motionX *= (double) f6;
        this.motionZ *= (double) f6;
        //        blockpos$pooledmutableblockpos.release();
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
            float f1 = MathHelper.sin(this.rotationYaw * MathHelper.DEG_TO_RAD);
            float f2 = MathHelper.cos(this.rotationYaw * MathHelper.DEG_TO_RAD);
            this.motionX += (strafe * f2 - forward * f1);
            this.motionZ += (forward * f2 + strafe * f1);
        }
    }

    public float getAIMoveSpeed() {
        return this.landMovementFactor;
    }

    protected float func_189749_co() {
        return 0.8F;
    }

    public void resetPositionToBB() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0f;
        this.posY = axisalignedbb.minY;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0f;
    }

    public AxisAlignedBB getEntityBoundingBox() {
        return this.boundingBox;
    }

    public AxisAlignedBB getRenderBoundingBox() {
        return this.getEntityBoundingBox();
    }
}
