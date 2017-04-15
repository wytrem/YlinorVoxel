package com.ylinor.library.util.ecs.system;

import com.ylinor.library.util.ecs.World;

public abstract class BaseSystem {
    /**
     * The world this system belongs to.
     */
    protected World world;

    public BaseSystem() {}

    protected void begin() {
    }

    public final void process() {
        if (checkProcessing()) {
            begin();
            processSystem();
            end();
        }
    }

    protected abstract void processSystem();

    protected void end() {
    }

    @SuppressWarnings("static-method")
    protected boolean checkProcessing() {
        return true;
    }
    public void initialize() {
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void dispose() {
    }
}
