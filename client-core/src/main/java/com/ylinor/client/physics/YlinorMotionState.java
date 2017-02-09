package com.ylinor.client.physics;

import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;

public class YlinorMotionState extends MotionState {
    public Transform worldTransform;
    public YlinorMotionState() 
    {
        worldTransform = new Transform();
        worldTransform.setIdentity();
    }

    @Override
    public Transform getWorldTransform(Transform worldTrans) 
    {
        worldTrans.set(worldTransform);
        return worldTrans;
    }

    @Override
    public void setWorldTransform(Transform worldTrans) 
    {
        worldTransform.set(worldTrans);
    }
}
