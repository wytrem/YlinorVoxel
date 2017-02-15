package com.ylinor.client.physics;

import com.artemis.Component;


/**
 * Stands for an entity AABB.
 * 
 * @author wytrem
 */
public class AABB extends Component {
    public final AxisAlignedBB aabb = new AxisAlignedBB(0,0,0,1,1,1);
}
