package com.ylinor.client.renderlib.format;

import com.ylinor.client.renderlib.PrimitiveType;
import com.ylinor.client.renderlib.format.VertexFormatElement.Usage;


public class VertexFormats
{
    public static final VertexFormatElement POS_2F = new VertexFormatElement(PrimitiveType.FLOAT, Usage.POSITION, 2);
    public static final VertexFormatElement POS_3F = new VertexFormatElement(PrimitiveType.FLOAT, Usage.POSITION, 3);
    public static final VertexFormatElement NORMAL_3F = new VertexFormatElement(PrimitiveType.FLOAT, Usage.NORMAL, 3);
    public static final VertexFormatElement COLOR_4F = new VertexFormatElement(PrimitiveType.FLOAT, Usage.COLOR, 4);
    public static final VertexFormatElement TEXCOORDS_2F = new VertexFormatElement(PrimitiveType.FLOAT, Usage.TEX_COORDS, 2);
    public static final VertexFormatElement TEXTURE_ID = new VertexFormatElement(PrimitiveType.UBYTE, Usage.OTHER, 1);

    public static final VertexFormat BLOCKS = new VertexFormat(POS_3F, NORMAL_3F, TEXCOORDS_2F);
}