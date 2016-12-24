package com.ylinor.library.util.math;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3i;
import org.joml.Vector3ic;


public class Immutables
{
    private static final class ImmutableProxy2i implements Vector2ic
    {
        private final Vector2ic vec;

        private ImmutableProxy2i(Vector2ic vec)
        {
            this.vec = vec;
        }

        public int x()
        {
            return vec.x();
        }

        public int y()
        {
            return vec.y();
        }

        public ByteBuffer get(ByteBuffer buffer)
        {
            return vec.get(buffer);
        }

        public ByteBuffer get(int index, ByteBuffer buffer)
        {
            return vec.get(index, buffer);
        }

        public IntBuffer get(IntBuffer buffer)
        {
            return vec.get(buffer);
        }

        public IntBuffer get(int index, IntBuffer buffer)
        {
            return vec.get(index, buffer);
        }

        public Vector2i sub(Vector2ic v, Vector2i dest)
        {
            return vec.sub(v, dest);
        }

        public Vector2i sub(int x, int y, Vector2i dest)
        {
            return vec.sub(x, y, dest);
        }

        public long lengthSquared()
        {
            return vec.lengthSquared();
        }

        public double length()
        {
            return vec.length();
        }

        public double distance(Vector2ic v)
        {
            return vec.distance(v);
        }

        public double distance(int x, int y)
        {
            return vec.distance(x, y);
        }

        public long distanceSquared(Vector2ic v)
        {
            return vec.distanceSquared(v);
        }

        public long distanceSquared(int x, int y)
        {
            return vec.distanceSquared(x, y);
        }

        public Vector2i add(Vector2ic v, Vector2i dest)
        {
            return vec.add(v, dest);
        }

        public Vector2i add(int x, int y, Vector2i dest)
        {
            return vec.add(x, y, dest);
        }

        public Vector2i mul(int scalar, Vector2i dest)
        {
            return vec.mul(scalar, dest);
        }

        public Vector2i mul(Vector2ic v, Vector2i dest)
        {
            return vec.mul(v, dest);
        }

        public Vector2i mul(int x, int y, Vector2i dest)
        {
            return vec.mul(x, y, dest);
        }

        public Vector2i negate(Vector2i dest)
        {
            return vec.negate(dest);
        }
    }

    public static Vector2ic of(Vector2ic vec)
    {
        return new ImmutableProxy2i(vec);
    }

    private static final class ImmutableProxy3i implements Vector3ic
    {
        private final Vector3ic vec;

        private ImmutableProxy3i(Vector3ic vec)
        {
            super();
            this.vec = vec;
        }

        public int x()
        {
            return vec.x();
        }

        public int y()
        {
            return vec.y();
        }

        public int z()
        {
            return vec.z();
        }

        public IntBuffer get(IntBuffer buffer)
        {
            return vec.get(buffer);
        }

        public IntBuffer get(int index, IntBuffer buffer)
        {
            return vec.get(index, buffer);
        }

        public ByteBuffer get(ByteBuffer buffer)
        {
            return vec.get(buffer);
        }

        public ByteBuffer get(int index, ByteBuffer buffer)
        {
            return vec.get(index, buffer);
        }

        public Vector3i sub(Vector3ic v, Vector3i dest)
        {
            return vec.sub(v, dest);
        }

        public Vector3i sub(int x, int y, int z, Vector3i dest)
        {
            return vec.sub(x, y, z, dest);
        }

        public Vector3i add(Vector3ic v, Vector3i dest)
        {
            return vec.add(v, dest);
        }

        public Vector3i add(int x, int y, int z, Vector3i dest)
        {
            return vec.add(x, y, z, dest);
        }

        public Vector3i mul(int scalar, Vector3i dest)
        {
            return vec.mul(scalar, dest);
        }

        public Vector3i mul(Vector3ic v, Vector3i dest)
        {
            return vec.mul(v, dest);
        }

        public Vector3i mul(int x, int y, int z, Vector3i dest)
        {
            return vec.mul(x, y, z, dest);
        }

        public long lengthSquared()
        {
            return vec.lengthSquared();
        }

        public double length()
        {
            return vec.length();
        }

        public double distance(Vector3ic v)
        {
            return vec.distance(v);
        }

        public double distance(int x, int y, int z)
        {
            return vec.distance(x, y, z);
        }

        public long distanceSquared(Vector3ic v)
        {
            return vec.distanceSquared(v);
        }

        public long distanceSquared(int x, int y, int z)
        {
            return vec.distanceSquared(x, y, z);
        }

        public Vector3i negate(Vector3i dest)
        {
            return vec.negate(dest);
        }
    }

    public static final Vector3ic of(Vector3ic vec)
    {
        return new ImmutableProxy3i(vec);
    }
}
