package com.ylinor.library.api.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;

public class TimerUpdateSystem extends BaseSystem {
    @Wire
    private Timer timer;
    
    @Override
    protected void processSystem() {
        timer.updateTimer();
    }
}
