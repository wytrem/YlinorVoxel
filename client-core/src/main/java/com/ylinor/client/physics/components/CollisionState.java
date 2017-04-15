package com.ylinor.client.physics.components;

import com.ylinor.library.util.ecs.component.Component;

public class CollisionState extends Component {
    public boolean onGround;
    public boolean isCollidedHorizontally;
    public boolean isCollidedVertically;
    public boolean isCollided;
    public boolean noClip;
}
