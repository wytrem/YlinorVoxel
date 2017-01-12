package com.ylinor.client.render;

import java.util.ArrayList;
import java.util.List;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

public class VertexFormat {
    private final List<VertexFormatElement> elements;
    private final TIntList offsets;
    private int stride;
    
    public VertexFormat(VertexFormatElement...elementsIn) {
        elements = new ArrayList<>(elementsIn.length);
        offsets = new TIntArrayList(elementsIn.length);
        
        for (int i = 0; i < elementsIn.length; i++) {
            addElement(elementsIn[i]);
        }
    }

    private void addElement(VertexFormatElement vertexFormatElement) {
        elements.add(vertexFormatElement);
        offsets.add(stride);
        stride += vertexFormatElement.getSize();
    }
}
