package com.ylinor.library.utils;

import org.joml.AxisAngle4f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.ylinor.api.world.objects.BlockPos;


/**
 * Temporary variables assigned to each thread. Engine classes may access these
 * temp variables with TempVars.get(), all retrieved TempVars instances must be
 * returned via TempVars.release(). This returns an available instance of the
 * TempVar class ensuring this particular instance is never used elsewhere in
 * the mean time.
 */
public class TempVars
{
    /**
     * Allow X instances of TempVars in a single thread.
     */
    private static final int STACK_SIZE = 5;

    /**
     * <code>TempVarsStack</code> contains a stack of TempVars. Every time
     * TempVars.get() is called, a new entry is added to the stack, and the
     * index incremented. When TempVars.release() is called, the entry is
     * checked against the current instance and then the index is decremented.
     */
    private static class TempVarsStack
    {
        int index = 0;
        TempVars[] tempVars = new TempVars[STACK_SIZE];
    }

    /**
     * ThreadLocal to store a TempVarsStack for each thread. This ensures each
     * thread has a single TempVarsStack that is used only in method calls in
     * that thread.
     */
    private static final ThreadLocal<TempVarsStack> varsLocal = new ThreadLocal<TempVarsStack>() {

        @Override
        public TempVarsStack initialValue()
        {
            return new TempVarsStack();
        }
    };
    /**
     * This instance of TempVars has been retrieved but not released yet.
     */
    private boolean isUsed = false;

    private TempVars()
    {
    }

    /**
     * Acquire an instance of the TempVar class. You have to release the
     * instance after use by calling the release() method. If more than
     * STACK_SIZE (currently 5) instances are requested in a single thread then
     * an ArrayIndexOutOfBoundsException will be thrown.
     *
     * @return A TempVar instance
     */
    public static TempVars get()
    {
        TempVarsStack stack = varsLocal.get();

        TempVars instance = stack.tempVars[stack.index];

        if (instance == null)
        {
            // Create new
            instance = new TempVars();

            // Put it in there
            stack.tempVars[stack.index] = instance;
        }

        stack.index++;

        instance.isUsed = true;

        return instance;
    }

    /**
     * Releases this instance of TempVars. Once released, the contents of the
     * TempVars are undefined. The TempVars must be released in the opposite
     * order that they are retrieved, e.g. Acquiring vars1, then acquiring
     * vars2, vars2 MUST be released first otherwise an exception will be
     * thrown.
     */
    public void release()
    {
        if (!isUsed)
        {
            throw new IllegalStateException("This instance of TempVars was already released!");
        }

        isUsed = false;

        TempVarsStack stack = varsLocal.get();

        // Return it to the stack
        stack.index--;

        // Check if it is actually there
        if (stack.tempVars[stack.index] != this)
        {
            throw new IllegalStateException("An instance of TempVars has not been released in a called method!");
        }
    }

    /**
     * General vectors.
     */
    public final Vector3f vect0 = new Vector3f();
    public final Vector3f vect1 = new Vector3f();
    public final Vector3f vect2 = new Vector3f();
    public final Vector3f vect3 = new Vector3f();
    public final Vector3f vect4 = new Vector3f();
    public final Vector3f vect5 = new Vector3f();
    public final Vector3f vect6 = new Vector3f();
    public final Vector3f vect7 = new Vector3f();
    public final Vector3f vect8 = new Vector3f();
    public final Vector3f vect9 = new Vector3f();
    public final Vector3f vect10 = new Vector3f();
    public final Vector3f vect11 = new Vector3f();

    public final Vector4f vect4f1 = new Vector4f();
    public final Vector4f vect4f2 = new Vector4f();
    public final Vector3f[] tri = {new Vector3f(), new Vector3f(), new Vector3f()};
    /**
     * 2D vector
     */
    public final Vector2f vect2d = new Vector2f();
    public final Vector2f vect2d2 = new Vector2f();
    /**
     * General matrices.
     */
    public final Matrix3f tempMat3 = new Matrix3f();
    public final Matrix4f tempMat4 = new Matrix4f();
    public final Matrix4f tempMat42 = new Matrix4f();
    /**
     * General quaternions.
     */
    public final Quaternionf quat1 = new Quaternionf();
    public final Quaternionf quat2 = new Quaternionf();

    /**
     * Angles
     */
    public final AxisAngle4f axisAngle4f = new AxisAngle4f();
    /**
     * BlockPos
     */
    public final BlockPos blockPos0 = new BlockPos(0, 0, 0);
    public final BlockPos blockPos1 = new BlockPos(0, 0, 0);
    public final BlockPos blockPos2 = new BlockPos(0, 0, 0);
    public final BlockPos blockPos3 = new BlockPos(0, 0, 0);
    public final BlockPos blockPos4 = new BlockPos(0, 0, 0);
    public final BlockPos blockPos5 = new BlockPos(0, 0, 0);

    public final Vector3f vector3f(int id)
    {
        return id == 0 ? vect0 : (id == 1 ? vect1 : (id == 2 ? vect2 : (id == 3 ? vect3 : (id == 4 ? vect4 : (id == 5 ? vect5 : (id == 6 ? vect6 : (id == 7 ? vect7 : (id == 8 ? vect8 : (id == 9 ? vect9 : (id == 10 ? vect10 : (id == 11 ? vect11 : null)))))))))));
    }

    public final BlockPos blockPos(int id)
    {
        return id == 0 ? blockPos0 : (id == 1 ? blockPos1 : (id == 2 ? blockPos2 : (id == 3 ? blockPos3 : (id == 4 ? blockPos4 : (id == 5 ? blockPos5 : null)))));
    }
}
