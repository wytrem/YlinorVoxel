package com.ylinor.client.render.model.block;

import java.util.List;
import java.util.Map;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.ylinor.client.render.Renderable;
import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.library.util.Facing;
import com.ylinor.library.util.TempVars;


public class Cube implements Renderable {
    private String id;
    private Vector3f position, size;
    private Quaternionf rotation;
    private List<Cube> children;
    private Map<Facing, TexturedFace> textures;

    public void render(VertexBuffer vertexBuffer) {

        TempVars tempVars = TempVars.get();

        tempVars.tempMat4.setTranslation(position).rotate(rotation).scale(size);

        for (Facing face : Facing.values()) {
            putFace(face, vertexBuffer, tempVars);
        }

        tempVars.release();

        children.forEach(child -> child.render(vertexBuffer));
    }

    private void putFace(Facing face, VertexBuffer vertexBuffer, TempVars tempVars) {

    }

    public void createTop(VertexBuffer buffer, TextureRegion region, TempVars tempVars) {
        // normal
        tempVars.vect1.set(0, 1, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV())
              .endVertex();

        tempVars.vect0.set(1, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV())
              .endVertex();

        tempVars.vect0.set(1, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();

        tempVars.vect0.set(0, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV2())
              .endVertex();
    }

    public void createBottom(VertexBuffer buffer, TextureRegion region, TempVars tempVars) {

        // normal
        tempVars.vect1.set(0, -1, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV())
              .endVertex();

        tempVars.vect0.set(0, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV2())
              .endVertex();

        tempVars.vect0.set(1, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();

        tempVars.vect0.set(1, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV())
              .endVertex();
    }

    public void createLeft(VertexBuffer buffer, TextureRegion region, TempVars tempVars) {

        // normal
        tempVars.vect1.set(-1, 0, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV2())
              .endVertex();

        tempVars.vect0.set(0, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV())
              .endVertex();

        tempVars.vect0.set(0, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV())
              .endVertex();

        tempVars.vect0.set(0, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();
    }

    public void createRight(VertexBuffer buffer, TextureRegion region, TempVars tempVars) {
        // normal
        tempVars.vect1.set(1, 0, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(1, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV2())
              .endVertex();

        tempVars.vect0.set(1, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();

        tempVars.vect0.set(1, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV())
              .endVertex();

        tempVars.vect0.set(1, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV())
              .endVertex();
    }

    public void createFront(VertexBuffer buffer, TextureRegion region, TempVars tempVars) {
        // normal
        tempVars.vect1.set(0, 0, 1).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();

        tempVars.vect0.set(1, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV2())
              .endVertex();

        tempVars.vect0.set(1, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV())
              .endVertex();

        tempVars.vect0.set(0, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV())
              .endVertex();
    }

    public void createBack(VertexBuffer buffer, TextureRegion region, TempVars tempVars) {

        // normal
        tempVars.vect1.set(0, 0, -1).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();

        tempVars.vect0.set(0, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU2(), region.getV())
              .endVertex();

        tempVars.vect0.set(1, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV())
              .endVertex();

        tempVars.vect0.set(1, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(region.getU(), region.getV2())
              .endVertex();
    }
}
