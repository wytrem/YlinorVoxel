package com.ylinor.client.physics.components;

import org.joml.Vector3f;

import com.artemis.Component;
import com.ylinor.library.util.math.AxisAlignedBB;


public class Physics extends Component {
    public float moveStrafing;
    public float moveForward;
    public Vector3f motion = new Vector3f();
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    public float stepHeight;
    public float fallDistance;
    public float landMovementFactor = 0.1f;
    public boolean isJumping;
    public int jumpTicks;
    public float jumpMovementFactor = 0.02F;
    public float speedInAir = 0.02F;
    //    private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

    public static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);

    public Physics() {
        this.stepHeight = -1.f;
    }

    protected float getJumpUpwardsMotion() {
        return 0.42F;
    }

    public void jump() {
        this.motion.y = this.getJumpUpwardsMotion();
    }

    public float getAIMoveSpeed() {
        return this.landMovementFactor;
    }

    protected float func_189749_co() {
        return 0.8F;
    }
}
