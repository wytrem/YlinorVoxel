package com.ylinor.client.render.model.block;

import java.util.List;
import java.util.Map;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.ylinor.client.render.Renderable;
import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.library.util.Facing;


public class Cube implements Renderable {
    private String id;
    private Vector3f position, size, scale;
    private Quaternionf rotation;
    private List<Cube> children;
    private Map<Facing, TexturedFace> textures;

    public void render(VertexBuffer vertexBuffer) {

    }

    private void putFace(Facing face, VertexBuffer vertexBuffer) {
        
    }
}
