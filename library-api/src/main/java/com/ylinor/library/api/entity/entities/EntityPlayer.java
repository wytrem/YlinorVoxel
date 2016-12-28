package com.ylinor.library.api.entity.entities;

import org.joml.Vector3f;

import com.ylinor.library.api.entity.AbstractEntity;


public class EntityPlayer extends AbstractEntity {

    public EntityPlayer(Vector3f position, Vector3f speed) {
        super(position, speed);
    }

    public void strafeLeft(float delta) {
        setPositionX(getPositionX() - (getSpeedX() * (delta / 1000)));
        System.out.println(getPositionX());
    }

    public void strafeRight(float delta) {
        setPositionX(getPositionX() + (getSpeedX() * (delta / 1000)));
        System.out.println(getPositionX());
    }

    public void walkForward(float delta) {
        setPositionZ(getPositionZ() + (getSpeedZ() / (delta / 1000)));
        System.out.println(getPositionZ());
    }

    public void walkBackward(float delta) {
        setPositionZ(getPositionZ() - (getSpeedZ() / (delta / 1000)));
        System.out.println(getPositionZ());
    }

    public void jump(float delta) {
        setPositionY(getPositionY() + (getSpeedY() / (delta / 1000)));
        System.out.println(getPositionY());
    }

}
