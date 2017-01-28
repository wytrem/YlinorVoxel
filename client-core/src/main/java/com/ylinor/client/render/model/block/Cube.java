package com.ylinor.client.render.model.block;

import java.util.List;
import java.util.Map;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.library.util.Facing;


public class Cube {
    private String id;
    private Vector3f position, size, scale;
    private Quaternionf rotation;
    private List<Cube> children;
    private Map<Facing, TexturedFace> textures;

    public void renderAt(VertexBuffer vertexBuffer, int x, int y, int z) {

    }

    private void putFace(Facing face, VertexBuffer vertexBuffer, int x, int y, int z) {

    }
}
