package com.ylinor.library.api;

import com.ylinor.library.api.entity.entities.EntityPlayer;
import org.joml.Vector3f;

public class EntityTest
{

    public static void main(String[] args) throws InterruptedException
    {
        EntityPlayer player = new EntityPlayer(new Vector3f(), new Vector3f(1, 1, 1));
        while (true)
        {
            System.out.println("time: " + System.currentTimeMillis());
            player.strafeLeft(33);
            Thread.sleep(1000 / 30);
        }
    }
}
