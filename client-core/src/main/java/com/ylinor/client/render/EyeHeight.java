package com.ylinor.client.render;

import org.joml.Vector3f;

import com.ylinor.library.util.ecs.component.Component;


/**
 * Marks entities having their eyes higher (or lower, but that's a weird
 * concept) than their bounding box position.
 *
 * @author wytrem
 */
public class EyeHeight extends Component {
    public Vector3f eyePadding;

    public EyeHeight() {
        this.eyePadding = new Vector3f();
    }

    public EyeHeight(float eyeHeight) {
        this.eyePadding = new Vector3f(0, eyeHeight, 0);
    }
}
