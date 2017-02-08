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

    public static UVMapping fromJson(JsonNode node, Function<String, Icon> registeredIcons) {

        JsonNode uv = node.at("/uv");

        if (uv.isArray()) {
            UVMapping uvMapping = new UVMapping(null, null);

            ArrayNode uvNode = (ArrayNode) uv;
            int[] mapping = {uvNode.get(0)
                                   .asInt(), uvNode.get(1)
                                                   .asInt(), uvNode.get(2)
                                                                   .asInt(), uvNode.get(3)
                                                                                   .asInt()};
            String texture = node.at("/texture").asText();

            uvMapping.mapping = mapping;
            uvMapping.icon = registeredIcons.apply(texture);
        }
        throw new RuntimeException("Invalid uv node.");
    }
}
