package com.ylinor.client.render.model.block;

import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ylinor.client.render.model.Icon;

public class UVMapping {
    private int[] mapping;
    private Icon icon;

    public UVMapping(int[] mapping, Icon icon) {
        this.mapping = mapping;
        this.icon = icon;
    }

    public static UVMapping fromJson(JsonNode root, String defaultTexture, Function<String, Icon> registeredIcons) {

        if (root.isTextual()) {
            return new UVMapping(new int[] {0,0,32,32}, registeredIcons.apply(root.asText()));
        }
        
        JsonNode uv = root.at("/uv");

        if (uv.isArray())
        {
            UVMapping uvMapping = new UVMapping(null, null);

            ArrayNode uvNode = (ArrayNode) uv;
            int[] mapping ={uvNode.get(0).asInt(), uvNode.get(1).asInt(), uvNode.get(2).asInt(), uvNode.get(3).asInt()};
            JsonNode tex = root.at("/texture");
            String texture = tex.isMissingNode() ? defaultTexture : tex.asText();

            uvMapping.mapping = mapping;
            uvMapping.icon = registeredIcons.apply(texture);
        }

        // TODO: CULLFACE & ROTATION

        throw new RuntimeException("Invalid uv node.");
    }
}
