package com.ylinor.library.api.entity;

import com.ylinor.library.api.world.WorldUniqueId;

public class EntityId implements WorldUniqueId
{
    private final int entityId;

    public EntityId(int entityId)
    {
        this.entityId = entityId;
    }

    public int getEntityId()
    {
        return entityId;
    }
}
