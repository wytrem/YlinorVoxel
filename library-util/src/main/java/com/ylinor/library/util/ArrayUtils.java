package com.ylinor.library.util;

import java.nio.ByteBuffer;


public class ArrayUtils {
    public static final <T> void round(T[] array, int steps) {
        for (int i = 0; i < steps; i++) {
            round(array);
        }
    }

    public static final <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static <T> void round(T[] array) {
        T temp = array[0];
        for (int i = 0; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        array[array.length - 1] = temp;
    }

    public static final void round(int[] array, int steps) {
        int[] temp = new int[steps];
        System.arraycopy(array, 0, temp, 0, steps);

        for (int i = 0; i < array.length - steps; i++) {
            array[i] = array[i + steps];
        }

        System.arraycopy(temp, 0, array, array.length - steps, steps);
    }

    public static final void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static final void mult(int[] array, int number) {
        for (int i = 0; i < array.length - 1; i++) {
            array[i] = array[i] * number;
        }
    }

    public static final void toByteArray(short[] src, byte[] dest) {
        ByteBuffer.wrap(dest).asShortBuffer().put(src);
    }

    public static final byte[] toByteArray(short[] src) {
        byte[] result = new byte[src.length << 1];
        toByteArray(src, result);
        return result;
    }

    public static final void toShortArray(byte[] src, short[] dest) {
        ByteBuffer.wrap(src).asShortBuffer().get(dest);
    }

    public static final short[] toShortArray(byte[] src) {
        short[] result = new short[src.length >> 1];
        toShortArray(src, result);
        return result;
    }
}
