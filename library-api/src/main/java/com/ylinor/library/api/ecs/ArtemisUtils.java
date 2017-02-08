package com.ylinor.library.api.ecs;

import com.artemis.World;

import net.mostlyoriginal.api.event.common.Event;
import net.mostlyoriginal.api.event.common.EventSystem;


public class ArtemisUtils {
    public static void dispatchEvent(World world, Event event) {
        world.getSystem(EventSystem.class).dispatch(event);
    }

    public static <T extends Event> void dispatchEvent(World world, Class<T> event) {
        world.getSystem(EventSystem.class).dispatch(event);
    }
}
