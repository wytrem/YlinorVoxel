package com.ylinor.library.api.ecs.systems;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ylinor.library.util.ecs.system.BaseSystem;


@Singleton
public class TimerUpdateSystem extends BaseSystem {
    @Inject
    private Timer timer;

    @Override
    protected void processSystem() {
        timer.updateTimer();
    }
}
