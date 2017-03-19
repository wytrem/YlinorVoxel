package com.ylinor.client.physics.systems.modifiers;

import org.joml.Vector3f;


public abstract class MotionModifier {
    public abstract Object apply(int entityId, Vector3f motion, Vector3f initialMotion, Object previousOuput);

    public abstract boolean shouldApply(int entityId);
}
