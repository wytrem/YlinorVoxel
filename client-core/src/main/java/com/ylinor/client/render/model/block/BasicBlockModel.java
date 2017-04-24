package com.ylinor.client.render.model.block;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class BasicBlockModel extends BlockModel {
    public BasicBlockModel() {
        super();
    }

    public BasicBlockModel(@NotNull List<Cube> cubes) {
        super(cubes);
    }

    public BasicBlockModel(@Nullable String name, @NotNull List<Cube> cubes) {
        super(name, cubes);
    }
}
