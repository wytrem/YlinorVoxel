package com.ylinor.library.network.kryo;

import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * @author pierre
 * @since 1.0.0
 */

public class KryoEncoder extends MessageToByteEncoder<Object>
{

    private final Kryo kryo;

    public KryoEncoder(Kryo kryo)
    {
        this.kryo = kryo;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Output output = new Output(outStream, 4096);

        kryo.writeClassAndObject(output, in);
        output.flush();

        byte[] outArray = outStream.toByteArray();
        out.writeShort(outArray.length);
        out.writeBytes(outArray);
    }

}