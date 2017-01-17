package com.ylinor.library.util.io;

import com.github.luben.zstd.Zstd;

public final class Compresser
{
    /**
     * Max compression level (from Zstd C sources)
     */
    public static final int ZSTD_MAX_CLEVEL = 22;

    public static byte[] compress(byte[] bytes)
    {
        return Zstd.compress(bytes, ZSTD_MAX_CLEVEL);
    }

    public static byte[] decompress(byte[] bytes)
    {
        byte[] buffer = new byte[(int) Zstd.decompressedSize(bytes)];
        Zstd.decompress(bytes, buffer);

        return buffer;
    }
}
