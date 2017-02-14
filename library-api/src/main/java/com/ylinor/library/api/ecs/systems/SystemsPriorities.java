package com.ylinor.library.api.ecs.systems;

import com.artemis.WorldConfigurationBuilder.Priority;

public class SystemsPriorities {
    public static final int RENDER_PRIORITY = Priority.NORMAL;
    
    public static final class Update {
        public static final int UPDATE_PRIORITY = Priority.HIGH;

        public static final int TIMER_UPDATE = 200 + UPDATE_PRIORITY;
        public static final int WORLD_UPDATE = 100 + UPDATE_PRIORITY;
        public static final int EVENT_DISPATCH = -200 + UPDATE_PRIORITY;

    }
}
