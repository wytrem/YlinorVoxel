package com.ylinor.library.util.ecs.system;

public abstract class NonProcessingSystem extends BaseSystem {
    @Override
    protected final void processSystem() {
        
    }
    
    @Override
    protected final boolean checkProcessing() {
        return false;
    }
}
