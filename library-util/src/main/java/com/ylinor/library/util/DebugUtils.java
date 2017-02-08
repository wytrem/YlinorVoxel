package com.ylinor.library.util;

public class DebugUtils {

    static long start;

    public static void timeStart() {
        start = System.currentTimeMillis();
    }

    public static void printTime(String process) {
        System.out.println(process + " took " + (System.currentTimeMillis() - start) + "ms");
    }
}
