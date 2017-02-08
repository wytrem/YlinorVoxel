package com.ylinor.client.physics;

import com.artemis.Component;


/**
 * Marks entity for which gravity applies.
 *
 * @author wytrem
 */
public class Gravitable extends Component {
    public static final float DEFAULT_GRAVITY_FACTOR = 30.0f;

    public float g;

    public Gravitable() {
        this(DEFAULT_GRAVITY_FACTOR);
    }

    public Gravitable(float g) {
        this.g = g;
    }
}
