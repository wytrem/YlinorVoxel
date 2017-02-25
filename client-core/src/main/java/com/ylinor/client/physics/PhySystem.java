package com.ylinor.client.physics;

import java.util.ArrayList;
import java.util.List;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.ylinor.client.YlinorClient;
import com.ylinor.library.api.ecs.systems.TickingIteratingSystem;
import com.ylinor.library.api.ecs.systems.Timer;
import com.ylinor.library.api.terrain.BlockExtraData;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.TempVars;
import com.ylinor.library.util.math.BlockPos;
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

	public static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);

	public PhySystem() {
		super(Aspect.all(Physics.class, Position.class, Heading.class, Size.class, AABB.class, CollisionState.class));
	}

	@Override
	protected void begin() {
		tempVars = TempVars.get();
	}

	private Heading heading;
	private Position position;
	private Physics physics;
	private Size size;
	private AABB aabb;
	private CollisionState collisionState;
	private boolean isSneaking;

	protected void tickEntity(int entityId) {
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

	public void setPosition(float x, float y, float z) {
		position.position.x = x;
		position.position.y = y;
		position.position.z = z;
		float halfWidth = size.width / 2.0F;
		aabb.aabb.set(x - halfWidth, y, z - halfWidth, x + halfWidth, y + size.height, z + halfWidth);
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

		if (Math.abs(physics.motionX) < 0.003f) {
			physics.motionX = 0.0f;
		}

		if (Math.abs(physics.motionY) < 0.003f) {
			physics.motionY = 0.0f;
		}

		if (Math.abs(physics.motionZ) < 0.003f) {
			physics.motionZ = 0.0f;
		}

		if (physics.jumpTicks > 0) {
			physics.jumpTicks--;
		}

		if (physics.isJumping) {
			if (collisionState.onGround && physics.jumpTicks == 0) {
				physics.jump();
				physics.jumpTicks = 10;
			}
		} else {
			physics.jumpTicks = 0;
		}

		physics.rotationYaw = (float) Math.atan2(heading.heading.x, heading.heading.z) * -MathHelper.RAD_TO_DEG;

		physics.moveStrafing *= 0.98f;
		physics.moveForward *= 0.98f;
		moveEntityWithHeading(terrain, physics.moveStrafing, physics.moveForward);
	}

	public List<AxisAlignedBB> getCollisionBoxes(AxisAlignedBB aabb) {
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

							// IBlockState iblockstate1 = iblockstate;
							// if(worldborder.contains(blockpos$pooledmutableblockpos)
							// || !flag1) {
							// iblockstate1 =
							// this.getBlockState(blockpos$pooledmutableblockpos);
							// }
							//
							// iblockstate1.addCollisionBoxToList(this,
							// blockpos$pooledmutableblockpos, aabb, list,
							// entityIn);

							if (terrain.getBlockType(tempVars.blockPos0).isCollidable()) {

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

	public void moveEntity(float x, float y, float z, Terrain terrain) {
		if (collisionState.noClip) {
			aabb.aabb.offsetLocal(x, y, z);
			resetPositionToBB();
		} else {
			// float d0 = position.position.x;
			// float d1 = position.position.y;
			// float d2 = position.position.z;

			float initialX = x;
			float initialY = y;
			float initialZ = z;
			boolean shouldApplySneak = collisionState.onGround && isSneaking;
			shouldApplySneak = false;
			if (shouldApplySneak) {
				for (; x != 0.0f && getCollisionBoxes(aabb.aabb.offset(x, -1.0f, 0.0f))
						.isEmpty(); initialX = x) {
					if (x < 0.05f && x >= -0.05f) {
						x = 0.0f;
					} else if (x > 0.0f) {
						x -= 0.05f;
					} else {
						x += 0.05f;
					}
				}

				for (; z != 0.0f && getCollisionBoxes(aabb.aabb.offset(0.0f, -1.0f, z))
						.isEmpty(); initialZ = z) {
					if (z < 0.05f && z >= -0.05f) {
						z = 0.0f;
					} else if (z > 0.0f) {
						z -= 0.05f;
					} else {
						z += 0.05f;
					}
				}

				for (; x != 0.0f && z != 0.0f && getCollisionBoxes(aabb.aabb.offset(x, -1.0f, z))
						.isEmpty(); initialZ = z) {
					if (x < 0.05f && x >= -0.05f) {
						x = 0.0f;
					} else if (x > 0.0f) {
						x -= 0.05f;
					} else {
						x += 0.05f;
					}

					initialX = x;
					if (z < 0.05D && z >= -0.05f) {
						z = 0.0f;
					} else if (z > 0.0f) {
						z -= 0.05f;
					} else {
						z += 0.05f;
					}
				}
			}

			List<AxisAlignedBB> collisionBoxes = getCollisionBoxes(aabb.aabb.addCoord(x, y, z));

			for (int i = 0; i < collisionBoxes.size(); i++) {
				y = collisionBoxes.get(i).calculateYOffset(aabb.aabb, y);
			}
			AxisAlignedBB aabbMovedOnlyOnY = aabb.aabb.copy();
			boolean flag = collisionState.onGround || initialY != y && initialY < 0.0f;
			aabb.aabb.offsetLocal(0.0f, y, 0.0f);
			for (int i = 0; i < collisionBoxes.size(); i++) {
				x = collisionBoxes.get(i).calculateXOffset(aabb.aabb, x);
			}

			aabb.aabb.offsetLocal(x, 0.0f, 0.0f);
			for (int i = 0; i < collisionBoxes.size(); i++) {
				z = collisionBoxes.get(i).calculateZOffset(aabb.aabb, z);
			}

			aabb.aabb.offsetLocal(0.0f, 0.0f, z);

			if (physics.stepHeight > 0.0F && flag && (initialX != x || initialZ != z)) {
				float d11 = x;
				float d7 = y;
				float d8 = z;
				AxisAlignedBB bbBeforeStep = aabb.aabb.copy();
				aabb.aabb.set(aabbMovedOnlyOnY);
				y = (float) physics.stepHeight;
				List<AxisAlignedBB> list = getCollisionBoxes(
						aabb.aabb.addCoord(initialX, y, initialZ));
				AxisAlignedBB axisalignedbb2 = aabb.aabb.copy();
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
				AxisAlignedBB axisalignedbb4 = aabb.aabb.copy();
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
					aabb.aabb.set(axisalignedbb2);
				} else {
					x = d18;
					z = d19;
					y = -d17;
					aabb.aabb.set(axisalignedbb4);
				}

				int l3 = 0;

				for (int i4 = list.size(); l3 < i4; ++l3) {
					y = (list.get(l3)).calculateYOffset(aabb.aabb, y);
				}

				aabb.aabb.offsetLocal(0.0f, y, 0.0f);
				// this.setEntityBoundingBox(this.getEntityBoundingBox()
				// .offset(0.0f, y, 0.0f));
				if (d11 * d11 + d8 * d8 >= x * x + z * z) {
					x = d11;
					y = d7;
					z = d8;
					aabb.aabb.set(bbBeforeStep);
				}
			}

			resetPositionToBB();
			collisionState.isCollidedHorizontally = initialX != x || initialZ != z;
			collisionState.isCollidedVertically = initialY != y;

			collisionState.onGround = collisionState.isCollidedVertically && initialY < 0.0f;
			collisionState.isCollided = collisionState.isCollidedHorizontally || collisionState.isCollidedVertically;
			// j4 = MathHelper.floor_float(position.position.x);
			// int l4 = MathHelper.floor_float(position.position.y -
			// 0.20000000298023224f);
			// int i5 = MathHelper.floor_float(position.position.z);
			// BlockPos blockpos = new BlockPos(j4, l4, i5);
			// BlockExtraData iblockstate = terrain.getBlockData(blockpos);
			// if (iblockstate.getMaterial() == Material.AIR) {
			// BlockPos blockpos1 = blockpos.down();
			// IBlockState iblockstate1 = worldObj.getBlockState(blockpos1);
			// Block block1 = iblockstate1.getBlock();
			// if (block1 instanceof BlockFence || block1 instanceof BlockWall
			// || block1 instanceof BlockFenceGate) {
			// iblockstate = iblockstate1;
			// blockpos = blockpos1;
			// }
			// }

			// this.updateFallState(terrain, y, this.onGround, iblockstate,
			// blockpos);
			if (initialX != x) {
				physics.motionX = 0.0f;
			}

			if (initialZ != z) {
				physics.motionZ = 0.0f;
			}

			// Block block = iblockstate.getBlock();
			if (initialY != y) {
				// block.onLanded(worldObj, this);
				physics.motionY = 0.0f;
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
			// float f = MathHelper.sqrt_float(this.motionX * this.motionX *
			// 0.20000000298023224D + this.motionY * this.motionY + this.motionZ
			// * this.motionZ * 0.20000000298023224D) * 0.35F;
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

	protected void updateFallState(Physics physics, Terrain terrain, double y, boolean onGroundIn, BlockExtraData state,
			BlockPos pos) {
		if (onGroundIn) {
			if (physics.fallDistance > 0.0F) {
				// state.getBlock().onFallenUpon(terrain, pos, this,
				// this.fallDistance);
			}

			physics.fallDistance = 0.0F;
		} else if (y < 0.0D) {
			physics.fallDistance = (float) ((double) physics.fallDistance - y);
		}
	}

	public void moveEntityWithHeading(Terrain terrain, float strafe, float forward) {
		// VIEUX
		// double d0 = position.position.y;
		// float f1 = this.func_189749_co();
		// float f2 = 0.02F;
		// float f3 = 3.0f;
		//
		// if (!this.onGround) {
		// f3 *= 0.5F;
		// }
		//
		// if (f3 > 0.0F) {
		// f1 += (0.54600006F - f1) * f3 / 3.0F;
		// f2 += (this.getAIMoveSpeed() - f2) * f3 / 3.0F;
		// }
		//
		// this.moveRelative(strafe, forward, f2);
		// this.moveEntity(this.motionX, this.motionY, this.motionZ, terrain);
		// this.motionX *= f1;
		// this.motionY *= 0.800000011920929f;
		// this.motionZ *= f1;
		// // Gravity
		// this.motionY -= 0.08f;
		//
		// // if (this.isCollidedHorizontally &&
		// this.isOffsetPositionInLiquid(this.motionX, this.motionY +
		// 0.6000000238418579D - position.position.y + d0, this.motionZ)) {
		// // this.motionY = 0.30000001192092896f;
		// // }

		// NOUVEAU

		float f6 = 0.91F;
		// BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos =
		// BlockPos.PooledMutableBlockPos.retain(position.position.x,
		// this.getEntityBoundingBox().minY - 1.0D, position.position.z);
		// if(this.onGround) {
		// f6 =
		// this.worldObj.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness
		// * 0.91F;
		// }

		f6 = 0.6f * 0.91f;

		float f7 = 0.16277136F / (f6 * f6 * f6);
		float f8;
		if (collisionState.onGround) {
			f8 = physics.getAIMoveSpeed() * f7;
		} else {
			f8 = physics.jumpMovementFactor;
		}

		moveRelative(strafe, forward, f8);

		f6 = 0.91F;
		// if(this.onGround) {
		// f6 =
		// this.worldObj.getBlockState(blockpos$pooledmutableblockpos.func_189532_c(position.position.x,
		// this.getEntityBoundingBox().minY - 1.0D,
		// position.position.z)).getBlock().slipperiness * 0.91F;
		// }

		f6 = 0.91f;

		if (collisionState.onGround) {
			f6 *= 0.6f;
		}

		// if(this.isOnLadder()) {
		// float f9 = 0.15F;
		// this.motionX = MathHelper.clamp_double(this.motionX,
		// -0.15000000596046448D, 0.15000000596046448D);
		// this.motionZ = MathHelper.clamp_double(this.motionZ,
		// -0.15000000596046448D, 0.15000000596046448D);
		// this.fallDistance = 0.0F;
		// if(this.motionY < -0.15D) {
		// this.motionY = -0.15D;
		// }
		//
		// boolean flag = this.isSneaking() && this instanceof EntityPlayer;
		// if(flag && this.motionY < 0.0D) {
		// this.motionY = 0.0D;
		// }
		// }

		moveEntity(physics.motionX, physics.motionY, physics.motionZ, terrain);
		// if(this.isCollidedHorizontally && this.isOnLadder()) {
		// this.motionY = 0.2D;
		// }

		physics.motionY -= 0.08D;

		physics.motionY *= 0.9800000190734863D;
		physics.motionX *= (double) f6;
		physics.motionZ *= (double) f6;
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
			physics.motionX += (strafe * f2 - forward * f1);
			physics.motionZ += (forward * f2 + strafe * f1);
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
