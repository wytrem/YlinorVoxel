package com.ylinor.library.api.entity;

import org.joml.Vector3f;

public abstract class AbstractEntity
{

    /**
     * Current position.
     */
    private Vector3f position;

    /**
     * Speed for all 3 dimensions.
     */
    private Vector3f speed;




    public void setSpeedX(float speedX)
    {
        speed.set(speedX, speed.y, speed.z);
    }

    public void setSpeedY(float speedY)
    {
        speed.set(speed.x, speedY, speed.z);
    }

    public void setSpeedZ(float speedZ)
    {
        speed.set(speed.x, speed.y, speedZ);
    }

    public void setSpeed(float x, float y, float z)
    {
        speed.set(x, y, z);
    }

    public float getSpeedX()
    {
        return speed.x;
    }

    public float getSpeedY()
    {
        return speed.y;
    }

    public float getSpeedZ()
    {
        return speed.z;
    }

    public Vector3f getSpeed()
    {
        return speed;
    }

    public void setSpeed(Vector3f speed)
    {
        this.speed = speed;
    }

    public void setPositionX(float positionX)
    {
        position.set(positionX, position.y, position.z);
    }

    public void setPositionY(float positionY)
    {
        position.set(position.x, positionY, position.z);
    }

    public void setPositionZ(float positionZ)
    {
        position.set(position.x, position.y, positionZ);
    }

    public void setPosition(float x, float y, float z)
    {
        position.set(x, y, z);
    }

    public float getPositionX()
    {
        return position.x;
    }

    public float getPositionY()
    {
        return position.y;
    }

    public float getPositionZ()
    {
        return position.z;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public void setPosition(Vector3f position)
    {
        this.position = position;
    }
}
