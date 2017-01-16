package com.ylinor.library.util;

import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;
import de.ruedigermoeller.serialization.util.FSTInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Serializer
{
    public static <T> T read(InputStream in) throws IOException, ClassNotFoundException
    {
        FSTObjectInput input = new FSTObjectInput(in);
        T object = (T) input.readObject();

        input.close();

        return object;
    }

    public static <T> T read(File file) throws IOException, ClassNotFoundException
    {
        return read(new FileInputStream(file));
    }

    public static <T> void write(OutputStream out, T object) throws IOException
    {
        FSTObjectOutput output = new FSTObjectOutput(out);
        output.writeObject(object);

        output.close();
    }

    public static <T> void write(File file, T object) throws IOException
    {
        write(new FileOutputStream(file), object);
    }
}
