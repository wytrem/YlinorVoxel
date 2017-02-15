package com.ylinor.client.render.model.block;

import java.util.function.Function;

import org.joml.Vector2f;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ylinor.client.render.model.Icon;
import com.ylinor.library.util.ArrayUtils;
import com.ylinor.library.util.spring.Assert;


public class UVMapping {
    private Vector2f[] mapping;

    public UVMapping(Vector2f[] mapping) {
        Assert.state(mapping.length == 4);
        this.mapping = mapping;
    }
    
    public Vector2f getCoords(int i) {
        return mapping[i];
    }

    public static final UVMapping fromIcon(Icon icon) {
        return new UVMapping(mappingFromIcon(icon));
    }

    private static Vector2f[] mappingFromIcon(Icon icon) {
        return new Vector2f[] {new Vector2f(icon.getMinU(), icon.getMinV()), new Vector2f(icon.getMaxU(), icon.getMinV()), new Vector2f(icon.getMaxU(), icon.getMaxV()), new Vector2f(icon.getMinU(), icon.getMaxV())};
    }

    private static Vector2f[] mappingFromIcon(Icon icon, int[] uv) {
        Icon reduced = icon.copy().reduce(uv);
        return mappingFromIcon(reduced);
    }

    public static final void applyRotation(Vector2f[] mapping, int rotation) {
        ArrayUtils.round(mapping, rotation / 90);
    }

    public static int[] uvFromJson(ArrayNode uvNode) {
        int[] mapping = new int[4];

        for (int i = 0; i < mapping.length; i++) {
            mapping[i] = uvNode.get(i).asInt();
        }

        return mapping;
    }

    public static UVMapping fromJson(JsonNode root, Function<String, Icon> registeredIcons) {

        if (root.isTextual()) {
            return fromIcon(registeredIcons.apply(root.textValue()));
        }

        int[] mapping = uvFromJson((ArrayNode) root.get("uv"));
        String texture = root.get("texture").textValue();
        int rotation = root.get("rotation").asInt();

        Vector2f[] vertices = mappingFromIcon(registeredIcons.apply(texture), mapping);
        applyRotation(vertices, rotation);

        return new UVMapping(vertices);
    }
}
