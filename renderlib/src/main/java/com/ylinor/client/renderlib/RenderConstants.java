package com.ylinor.client.renderlib;

import com.badlogic.gdx.graphics.Mesh;
import com.ylinor.client.renderlib.format.VertexFormats;


public class RenderConstants {
    public static final int MAX_INDICES_PER_MESH = 65536;
    public static final int MAX_QUADS_VERTICES = RenderConstants.MAX_INDICES_PER_MESH * 4 / 6;

    public static final Mesh newBiggestQuadsMesh() {
        return new Mesh(true, RenderConstants.MAX_QUADS_VERTICES, RenderConstants.MAX_INDICES_PER_MESH, VertexFormats.BLOCKS.toGdx());
    }
}
