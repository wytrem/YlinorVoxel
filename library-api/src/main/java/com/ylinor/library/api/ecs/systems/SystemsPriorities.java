package com.ylinor.library.api.ecs.systems;

public class SystemsPriorities {
    
    public static final class Render {
        public static final int RENDER_PRIORITY = 1000;
        
        public static final int TERRAIN_RENDER = RENDER_PRIORITY;
        public static final int HUD_RENDER = -100 + RENDER_PRIORITY;
        public static final int SCREEN_RENDER = -200 + RENDER_PRIORITY;
    }
    

    public static final class Update {
        public static final int UPDATE_PRIORITY = 10000;

        public static final int TIMER_UPDATE = 200 + UPDATE_PRIORITY;
        public static final int WORLD_UPDATE = 100 + UPDATE_PRIORITY;
        public static final int EVENT_DISPATCH = -200 + UPDATE_PRIORITY;

    }
}
