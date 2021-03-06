package com.ylinor.library.api.ecs.components;

import com.ylinor.library.util.ecs.component.Component;
import com.ylinor.library.util.math.AxisAlignedBB;


/**
 * Stands for an entity AABB.
 * 
 * @author wytrem
 */
public class AABB extends Component {
    public final AxisAlignedBB aabb = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
}
