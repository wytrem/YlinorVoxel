package com.ylinor.library.api.ecs;

import com.ylinor.library.util.ecs.Event;
import com.ylinor.library.util.ecs.EventSystem;
import com.ylinor.library.util.ecs.World;


public class ArtemisUtils {
    public static void dispatchEvent(World world, Event event) {
        world.getSystem(EventSystem.class).dispatch(event);
    }

    public static <T extends Event> void dispatchEvent(World world, Class<T> event) {
        world.getSystem(EventSystem.class).dispatch(event);
    }
}
