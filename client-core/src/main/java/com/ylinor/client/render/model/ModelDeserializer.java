package com.ylinor.client.render.model;

import com.ylinor.client.resource.TextureAtlas;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Stream;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylinor.client.render.model.block.BlockModel;
import com.ylinor.client.render.model.block.Cube;
import com.ylinor.client.render.model.block.UVMapping;
import com.ylinor.library.util.Facing;


import static com.ylinor.library.util.JsonUtil.*;


public class ModelDeserializer {
    private ObjectMapper mapper = new ObjectMapper();
    private TextureAtlas atlas;
    private ModelRegistry registry;

    private String name;
    private TreeNode model;
    private List<Cube> cubes;
    private List<BlockModel> variants;

    public ModelDeserializer(String name, ModelRegistry registry, TextureAtlas atlas, TreeNode tree) {
        this.name = name;
        this.registry = registry;
        this.atlas = atlas;
        this.model = tree;
    }

    public void deserialize() {
        // TODO: VARIANTS

        TreeNode parentNode = model.get("parent");

        if (!parentNode.isMissingNode()) {
            File parentFile = new File((File) null /* TODO: THIS */, parentNode.asToken().asString() + ".json");
            TreeNode parent;

            try
            {
                parent = makeTree(parentFile);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to read model '" + parentFile + "' (parent of " + name + ")", e);
            }

            this.model = merge((JsonNode) parent, (JsonNode) this.model);
        }

        this.cubes = readPart(model.get("elements"));
    }

    public List<Cube> readPart(TreeNode model) {
        List<Cube> parts = new ArrayList<>();

        walkTree(model, (part, cube) -> {
            TreeNode children = model.get("children");
            TreeNode faces = model.get("faces");

            List<Cube> childrenParts = null;

            Vector3f position = new Vector3f(), size = new Vector3f();
            Quaternionf rotation = new Quaternionf();

            if (children.isMissingNode()) {
                try {
                    position = mapper.treeToValue(cube.get("position"), Vector3f.class);
                    size = mapper.treeToValue(cube.get("size"), Vector3f.class);
                    rotation = mapper.treeToValue(cube.get("rotation"), Quaternionf.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Unable to read json model " + name, e);
                }
            }
            else {
                childrenParts = readPart(children);
            }

            Map<Facing, UVMapping> mapping = new HashMap<>();

            if (faces.isObject()) {
                walkTree(faces, (face, obj) -> {
                    if (face.equals("allfaces")) {
                        Stream.of(Facing.values()).filter((f) -> !mapping.containsKey(f)).forEach((f) -> mapping.put(f, new UVMapping(new int[] {0, 0, 32, 32}, atlas.getUVFor(obj.asToken().asString()))));
                        return;
                    }

                    Facing facing = Facing.valueOf(face);
                    UVMapping uv = UVMapping.fromJson((JsonNode) obj, (s) -> atlas.getUVFor(s));

                    mapping.put(facing, uv);
                });
            } else {
                String texture = faces.asToken().asString();
                Stream.of(Facing.values()).forEach((f) -> mapping.put(f, new UVMapping(new int[] {0, 0, 32, 32}, atlas.getUVFor(texture))));
            }

            parts.add(new Cube(part, position, size, rotation, childrenParts, mapping));
        });

        return parts;
    }

    public BlockModel[] getModel() {
        if (cubes == null) {
            deserialize();
        }

        ArrayList<BlockModel> models = new ArrayList<>();
        models.add(new BlockModel(name, cubes));
        models.addAll(variants);

        return models.toArray(new BlockModel[models.size()]);
    }

    public static ModelDeserializer read(File file, ModelRegistry registry, TextureAtlas atlas) throws IOException {
        String name = file.getName();
        return new ModelDeserializer(name.substring(0, name.lastIndexOf(".json")), registry, atlas, makeTree(file));
    }
}
