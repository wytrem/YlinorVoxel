package com.ylinor.client.renderlib;

import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Temporary variables assigned to each thread. Engine classes may access these
 * temp variables with TempVars.get(), all retrieved TempVars instances must be
 * returned via TempVars.release(). This returns an available instance of the
 * TempVar class ensuring this particular instance is never used elsewhere in
 * the mean time.
 */
public class GdxTempVars {
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
    private static class TempVarsStack {
        int index = 0;
        GdxTempVars[] tempVars = new GdxTempVars[STACK_SIZE];
    }

    /**
     * ThreadLocal to store a TempVarsStack for each thread. This ensures each
     * thread has a single TempVarsStack that is used only in method calls in
     * that thread.
     */
    private static final ThreadLocal<TempVarsStack> varsLocal = new ThreadLocal<TempVarsStack>() {

        @Override
        public TempVarsStack initialValue() {
            return new TempVarsStack();
        }
    };
    /**
     * This instance of TempVars has been retrieved but not released yet.
     */
    private boolean isUsed = false;

    private GdxTempVars() {
    }

    /**
     * Acquire an instance of the TempVar class. You have to release the
     * instance after use by calling the release() method. If more than
     * STACK_SIZE (currently 5) instances are requested in a single thread then
     * an ArrayIndexOutOfBoundsException will be thrown.
     *
     * @return A TempVar instance
     */
    public static GdxTempVars get() {
        TempVarsStack stack = varsLocal.get();

        GdxTempVars instance = stack.tempVars[stack.index];

        if (instance == null) {
            // Create new
            instance = new GdxTempVars();

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
    public void release() {
        if (!isUsed) {
            throw new IllegalStateException("This instance of TempVars was already released!");
        }

        isUsed = false;

        TempVarsStack stack = varsLocal.get();

        // Return it to the stack
        stack.index--;

        // Check if it is actually there
        if (stack.tempVars[stack.index] != this) {
            throw new IllegalStateException("An instance of TempVars has not been released in a called method!");
        }
    }

    /**
     * General vectors.
     */
    public final Vector3 vect0 = new Vector3();
    public final Vector3 vect1 = new Vector3();
    public final Vector3 vect2 = new Vector3();
    public final Vector3 vect3 = new Vector3();
    public final Vector3 vect4 = new Vector3();
    public final Vector3 vect5 = new Vector3();
    public final Vector3 vect6 = new Vector3();
    public final Vector3 vect7 = new Vector3();
    public final Vector3 vect8 = new Vector3();
    public final Vector3 vect9 = new Vector3();
    public final Vector3 vect10 = new Vector3();
    public final Vector3 vect11 = new Vector3();

    public final Vector3[] tri = {new Vector3(), new Vector3(), new Vector3()};
    /**
     * 2D vector
     */
    public final Vector2 vect2d = new Vector2();
    public final Vector2 vect2d2 = new Vector2();
    /**
     * General matrices.
     */
    public final Matrix3 mat3 = new Matrix3();
    public final Matrix4 mat4a = new Matrix4();
    public final Matrix4 mat4b = new Matrix4();
    /**
     * General quaternions.
     */
    public final Quaternion quat1 = new Quaternion();
    public final Quaternion quat2 = new Quaternion();
    
    public final Frustum frustum = new Frustum();
    public final BoundingBox bb1 = new BoundingBox();
    public final BoundingBox bb2 = new BoundingBox();
    public final BoundingBox bb3 = new BoundingBox();

}
