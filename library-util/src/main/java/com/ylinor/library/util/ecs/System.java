package com.ylinor.library.util.ecs;

public abstract class System {
    /**
     * The world this system belongs to.
     */
    protected World world;

    public System() {}

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
    protected void initialize() {
    }

    protected void setWorld(World world) {
        this.world = world;
    }

    protected World getWorld() {
        return world;
    }

    protected void dispose() {
    }
}
