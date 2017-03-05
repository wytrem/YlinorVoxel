package com.ylinor.client.render.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.ylinor.client.render.Renderable;
import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.library.api.terrain.IBlockContainer;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.util.Facing;
import com.ylinor.library.util.TempVars;


public class Cube implements Renderable {
    private String id = null;
    public final Vector3f position, size;
    public final Quaternionf rotation;
    private List<Cube> children = new ArrayList<>();
    private Map<Facing, FaceRenderInfo> faces;
    private Map<Facing, Facing> cullfaces;

    public Cube(String id, Vector3f position, Vector3f size, Quaternionf rotation) {
        this.id = id;
        this.position = position;
        this.size = size;
        this.rotation = rotation;
    }

    public void setCullfaces(Map<Facing, Facing> cullfaces) {
        this.cullfaces = cullfaces;
    }

    public void setFaces(Map<Facing, FaceRenderInfo> textures) {
        this.faces = textures;
    }

    public void setChildren(List<Cube> children) {
        this.children = children;
    }

    public void render(VertexBuffer vertexBuffer, IBlockContainer neighbours, int x, int y, int z) {
        TempVars tempVars = TempVars.get();

        tempVars.tempMat4.identity()
                         .translate(position)
                         .rotate(rotation)
                         .scale(size);

        tempVars.blockPos0.set(x, y, z);
        BlockState state = neighbours.getBlockState(tempVars.blockPos0);
        for (Facing facing : faces.keySet()) {
            if (cullfaces.containsKey(facing)) {
                tempVars.blockPos0.set(x, y, z).offset(cullfaces.get(facing));

                if (neighbours.getBlockState(tempVars.blockPos0)
                              .getAttributes()
                              .isOpaqueCube()) {
                    continue;
                }
            }
            
            FaceRenderInfo infos = faces.get(facing);

            putFace(facing, vertexBuffer, infos.useColorMultiplier ? state.getAttributes()
                            .getColorMultiplier() : 0xffffff, tempVars);
        }

        tempVars.release();

        children.forEach(child -> child.render(vertexBuffer));
    }

    public void render(VertexBuffer vertexBuffer) {

        TempVars tempVars = TempVars.get();

        tempVars.tempMat4.setTranslation(position).rotate(rotation).scale(size);
        for (Facing face : Facing.values()) {
            putFace(face, vertexBuffer, 0xffffff, tempVars);
        }

        tempVars.release();

        children.forEach(child -> child.render(vertexBuffer));
    }

    private void putFace(Facing face, VertexBuffer buffer, int color, TempVars tempVars) {
        FaceRenderInfo texture = faces.get(face);

        if (face.equals(Facing.UP)) {
            createTop(buffer, tempVars, color, texture);
        }
        else if (face.equals(Facing.DOWN)) {
            createBottom(buffer, tempVars, color, texture);
        }
        else if (face.equals(Facing.NORTH)) {
            createFront(buffer, tempVars, color, texture);
        }
        else if (face.equals(Facing.SOUTH)) {
            createBack(buffer, tempVars, color, texture);
        }
        else if (face.equals(Facing.EAST)) {
            createLeft(buffer, tempVars, color, texture);
        }
        else if (face.equals(Facing.WEST)) {
            createRight(buffer, tempVars, color, texture);
        }

    }

    public void initRendering(TempVars tempVars) {
        tempVars.tempMat4.setTranslation(position).rotate(rotation).scale(size);
    }

    public void createTop(VertexBuffer buffer, TempVars tempVars, int color, FaceRenderInfo texture) {
        // normal
        tempVars.vect1.set(0, 1, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(0))
              .endVertex();

        tempVars.vect0.set(1, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(1))
              .endVertex();

        tempVars.vect0.set(1, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(2))
              .endVertex();

        tempVars.vect0.set(0, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(3))
              .endVertex();
    }

    public void createBottom(VertexBuffer buffer, TempVars tempVars, int color, FaceRenderInfo texture) {

        // normal
        tempVars.vect1.set(0, -1, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(0))
              .endVertex();

        tempVars.vect0.set(1, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(1))
              .endVertex();

        tempVars.vect0.set(1, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(2))
              .endVertex();

        tempVars.vect0.set(0, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(3))
              .endVertex();
    }

    public void createLeft(VertexBuffer buffer, TempVars tempVars, int color, FaceRenderInfo texture) {

        // normal
        tempVars.vect1.set(-1, 0, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(3))
              .endVertex();

        tempVars.vect0.set(0, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(0))
              .endVertex();

        tempVars.vect0.set(0, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(1))
              .endVertex();

        tempVars.vect0.set(0, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(2))
              .endVertex();
    }

    public void createRight(VertexBuffer buffer, TempVars tempVars, int color, FaceRenderInfo texture) {
        // normal
        tempVars.vect1.set(1, 0, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(1, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(3))
              .endVertex();

        tempVars.vect0.set(1, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(2))
              .endVertex();

        tempVars.vect0.set(1, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(1))
              .endVertex();

        tempVars.vect0.set(1, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(0))
              .endVertex();
    }

    public void createFront(VertexBuffer buffer, TempVars tempVars, int color, FaceRenderInfo texture) {
        // normal
        tempVars.vect1.set(0, 0, 1).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(2))
              .endVertex();

        tempVars.vect0.set(1, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(3))
              .endVertex();

        tempVars.vect0.set(1, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(0))
              .endVertex();

        tempVars.vect0.set(0, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(1))
              .endVertex();
    }

    public void createBack(VertexBuffer buffer, TempVars tempVars, int color, FaceRenderInfo texture) {

        // normal
        tempVars.vect1.set(0, 0, -1).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(2))
              .endVertex();

        tempVars.vect0.set(0, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(1))
              .endVertex();

        tempVars.vect0.set(1, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(0))
              .endVertex();

        tempVars.vect0.set(1, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .color(color).normal(tempVars.vect1)
              .texCoords(texture.getTexCoords(3))
              .endVertex();
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Cube [id=" + id + ", position=" + position + ", size=" + size + ", rotation=" + rotation + ", children=" + children + "]";
    }

}
