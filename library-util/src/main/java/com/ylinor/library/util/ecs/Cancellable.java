package com.ylinor.library.util.ecs;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean flag);
}
