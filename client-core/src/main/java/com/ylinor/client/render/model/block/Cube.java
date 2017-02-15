package com.ylinor.client.render.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ylinor.client.render.Renderable;
import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.library.api.world.IBlockContainer;
import com.ylinor.library.util.Facing;
import com.ylinor.library.util.TempVars;


public class Cube implements Renderable {
    private String id = null;
    public final Vector3f position, size;
    public final Quaternionf rotation;
    private List<Cube> children = new ArrayList<>();
    private Map<Facing, UVMapping> textures;
    private Map<Facing, Facing> cullfaces;
    public TextureRegion region;

    public Cube(String id, Vector3f position, Vector3f size, Quaternionf rotation) {
        this.id = id;
        this.position = position;
        this.size = size;
        this.rotation = rotation;
    }

    public void setCullfaces(Map<Facing, Facing> cullfaces) {
        this.cullfaces = cullfaces;
    }
    
    public void setTextures(Map<Facing, UVMapping> textures) {
        this.textures = textures;
    }
    
    public void setChildren(List<Cube> children) {
        this.children = children;
    }

    public void render(VertexBuffer vertexBuffer, IBlockContainer neighbours, int x, int y, int z) {
        TempVars tempVars = TempVars.get();

        tempVars.tempMat4.identity().translate(position).rotate(rotation).scale(size);
        for (Facing face : Facing.values()) {
            if (cullfaces.containsKey(face)) {
                tempVars.blockPos0.set(x, y, z).offset(cullfaces.get(face));
                
                if (neighbours.getBlockType(tempVars.blockPos0).isOpaque()) {
                    continue;
                }
            }
            
            putFace(face, vertexBuffer, tempVars);
        }

        tempVars.release();

        children.forEach(child -> child.render(vertexBuffer));
    }

    public void render(VertexBuffer vertexBuffer) {

        TempVars tempVars = TempVars.get();

        tempVars.tempMat4.setTranslation(position).rotate(rotation).scale(size);
        for (Facing face : Facing.values()) {
            putFace(face, vertexBuffer, tempVars);
        }

        tempVars.release();

        children.forEach(child -> child.render(vertexBuffer));
    }

    private void putFace(Facing face, VertexBuffer buffer, TempVars tempVars) {
        
        UVMapping texture = textures.get(face);
        
        if (face.equals(Facing.UP)) {
            createTop(buffer, tempVars, texture);
        }
        else if (face.equals(Facing.DOWN)) {
            createBottom(buffer, tempVars, texture);
        }
        else if (face.equals(Facing.NORTH)) {
            createFront(buffer, tempVars, texture);
        }
        else if (face.equals(Facing.SOUTH)) {
            createBack(buffer, tempVars, texture);
        }
        else if (face.equals(Facing.EAST)) {
            createLeft(buffer, tempVars, texture);
        }
        else if (face.equals(Facing.WEST)) {
            createRight(buffer, tempVars, texture);
        }
        
    }

    public void initRendering(TempVars tempVars) {
        tempVars.tempMat4.setTranslation(position).rotate(rotation).scale(size);
    }

    public void createTop(VertexBuffer buffer, TempVars tempVars, UVMapping texture) {
        // normal
        tempVars.vect1.set(0, 1, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(0))
              .endVertex();

        tempVars.vect0.set(1, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(1))
              .endVertex();

        tempVars.vect0.set(1, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(2))
              .endVertex();

        tempVars.vect0.set(0, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(3))
              .endVertex();
    }

    public void createBottom(VertexBuffer buffer, TempVars tempVars, UVMapping texture) {

        // normal
        tempVars.vect1.set(0, -1, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(0))
              .endVertex();

        tempVars.vect0.set(1, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(1))
              .endVertex();

        tempVars.vect0.set(1, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(2))
              .endVertex();

        tempVars.vect0.set(0, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(3))
              .endVertex();
    }

    public void createLeft(VertexBuffer buffer, TempVars tempVars, UVMapping texture) {

        // normal
        tempVars.vect1.set(-1, 0, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(3))
              .endVertex();

        tempVars.vect0.set(0, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(0))
              .endVertex();

        tempVars.vect0.set(0, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(1))
              .endVertex();

        tempVars.vect0.set(0, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(2))
              .endVertex();
    }

    public void createRight(VertexBuffer buffer, TempVars tempVars, UVMapping texture) {
        // normal
        tempVars.vect1.set(1, 0, 0).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(1, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(3))
              .endVertex();

        tempVars.vect0.set(1, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(2))
              .endVertex();

        tempVars.vect0.set(1, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(1))
              .endVertex();

        tempVars.vect0.set(1, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(0))
              .endVertex();
    }

    public void createFront(VertexBuffer buffer, TempVars tempVars, UVMapping texture) {
        // normal
        tempVars.vect1.set(0, 0, 1).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(2))
              .endVertex();

        tempVars.vect0.set(1, 0, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(3))
              .endVertex();

        tempVars.vect0.set(1, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(0))
              .endVertex();

        tempVars.vect0.set(0, 1, 0).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(1))
              .endVertex();
    }

    public void createBack(VertexBuffer buffer, TempVars tempVars, UVMapping texture) {

        // normal
        tempVars.vect1.set(0, 0, -1).mulProject(tempVars.tempMat4);

        // pos
        tempVars.vect0.set(0, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(2))
              .endVertex();

        tempVars.vect0.set(0, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(1))
              .endVertex();

        tempVars.vect0.set(1, 1, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(0))
              .endVertex();

        tempVars.vect0.set(1, 0, 1).mulProject(tempVars.tempMat4);
        buffer.pos(tempVars.vect0)
              .normal(tempVars.vect1)
              .texCoords(texture.getCoords(3))
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
