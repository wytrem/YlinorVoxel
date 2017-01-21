package com.ylinor.client.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;


public class VertexFormat {
    private final List<VertexFormatElement> elements;
    private final TIntList offsets;
    private int stride;

    public VertexFormat(VertexFormatElement... elementsIn) {
        List<VertexFormatElement> temp = new ArrayList<>(elementsIn.length);
        offsets = new TIntArrayList(elementsIn.length);

        for (int i = 0; i < elementsIn.length; i++) {
            addElement(elementsIn[i], temp);
        }

        elements = Collections.unmodifiableList(temp);
    }

    private void addElement(VertexFormatElement vertexFormatElement, List<VertexFormatElement> list) {
        list.add(vertexFormatElement);
        offsets.add(stride);
        stride += vertexFormatElement.getSize();
    }

    public List<VertexFormatElement> getElements() {
        return elements;
    }

    public int getOffset(int index) {
        return offsets.get(index);
    }

    public int getStride() {
        return stride;
    }

    public VertexAttributes toGdx() {
        return new VertexAttributes(elements.stream().map(VertexFormatElement::toGdx).collect(Collectors.toList()).toArray(new VertexAttribute[elements.size()]));
    }
}
