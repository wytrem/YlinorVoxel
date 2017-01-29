package com.ylinor.client.render.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ylinor.client.render.model.block.BlockModel;
import com.ylinor.client.render.model.block.Cube;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelDeserializer extends JsonDeserializer<BlockModel> {
    @Override
    public BlockModel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode tree = p.readValueAsTree();

        String name = tree.get("name").asToken().asString();
        String parent = tree.get("extends").asToken().asString();

        TreeNode model = tree.get("model");
        TreeNode textures = tree.get("textures");
        TreeNode variants = tree.get("variants");



        return null;
    }

    public List<Cube> readModel(TreeNode model, TreeNode textures) {
        List<Cube> parts = new ArrayList<>();

        model.fieldNames().forEachRemaining((part) -> {
            TreeNode cube = model.get(part);

            int x = Integer.valueOf(cube.get("x").asToken().asString());
            int y = Integer.valueOf(cube.get("y").asToken().asString());
            int z = Integer.valueOf(cube.get("z").asToken().asString());

            TreeNode children = model.get("children");

            int sizeX = 0, sizeY = 0, sizeZ = 0;
            List<Cube> childrenParts = null;

            if (children.isMissingNode())
            {
                sizeX = Integer.valueOf(cube.get("sizeX").asToken().asString());
                sizeY = Integer.valueOf(cube.get("sizeY").asToken().asString());
                sizeZ = Integer.valueOf(cube.get("sizeZ").asToken().asString());
            }
            else
            {
                childrenParts = readModel(children, textures);
            }

            parts.add(new Cube());
        });
    }

    public TreeNode getNode(TreeNode parent, TreeNode tree, String path) {
        TreeNode node = parent.path(path);

        if (node.isMissingNode()) {
            node = tree.path(path);
        }

        return node;
    }
}
