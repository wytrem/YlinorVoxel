package com.ylinor.library.network.kryo;

import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


/**
 * @author pierre
 * @since 1.0.0
 */
public class KryoDecoder extends ByteToMessageDecoder
{

    private final Kryo kryo;

    public KryoDecoder(Kryo kryo)
    {
        this.kryo = kryo;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {

        if (in.readableBytes() < 2)
            return;

        in.markReaderIndex();

        int len = in.readUnsignedShort();

        if (in.readableBytes() < len)
        {
            in.resetReaderIndex();
            return;
        }

        byte[] buf = new byte[len];
        in.readBytes(buf);
        Input input = new Input(buf);
        Object object = kryo.readClassAndObject(input);
        out.add(object);

    }
}