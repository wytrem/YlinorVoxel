package com.ylinor.client.render;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.CubemapAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.management.RuntimeMBeanException;

/**
 * Created by Alexis Lavaud on 28/01/2017.
 */
public final class Skybox implements Disposable {
    private Cubemap skyboxCubemap;
    private Mesh mesh;
    private ShaderProgram shader;

    public Skybox(Cubemap skyboxCubemap) {
        this.skyboxCubemap = skyboxCubemap;

        MeshBuilder meshBuilder = new MeshBuilder();

        meshBuilder.begin(VertexAttributes.Usage.Position, GL20.GL_TRIANGLES);
        BoxShapeBuilder.build(meshBuilder, 1.0f, 1.0f, 1.0f);

        this.mesh = meshBuilder.end();

        this.shader = new ShaderProgram(
                Gdx.files.getFileHandle("skybox/skybox_vs.glsl", Files.FileType.Internal),
                Gdx.files.getFileHandle("skybox/skybox_fs.glsl", Files.FileType.Internal)
        );

        if (!shader.isCompiled() && ShaderProgram.pedantic) {
            throw new RuntimeException(shader.getLog());
        }
    }

    public void render(PerspectiveCamera camera) {
        Matrix4 camView = new Matrix4(camera.view);
        camView.setTranslation(0.0f, 0.0f, 0.0f);

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);

        shader.begin();
        shader.setUniformMatrix("u_camView", camView);
        shader.setUniformMatrix("u_camProj", camera.projection);
        skyboxCubemap.bind(0);
        shader.setUniformi("u_cubemap", 0);
        mesh.render(shader, GL20.GL_TRIANGLES);
        shader.end();

        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }

    public Cubemap getSkyboxCubemap() {
        return skyboxCubemap;
    }

    public void setSkyboxCubemap(Cubemap skyboxCubemap) {
        if (skyboxCubemap != null) {
            skyboxCubemap.dispose();
        }

        this.skyboxCubemap = skyboxCubemap;
    }

    @Override
    public void dispose() {
        shader.dispose();
        mesh.dispose();

        if (skyboxCubemap != null) {
            skyboxCubemap.dispose();
        }
    }
}
