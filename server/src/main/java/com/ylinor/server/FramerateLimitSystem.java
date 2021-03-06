package com.ylinor.server;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ylinor.library.api.ecs.systems.Timer;
import com.ylinor.library.util.ecs.system.BaseSystem;


@Singleton
public class FramerateLimitSystem extends BaseSystem {

    @Inject
    Timer timer;

    @Override
    protected void processSystem() {
        if (timer.elapsedTicks == 0) {
            try {
                Thread.sleep(10l);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
