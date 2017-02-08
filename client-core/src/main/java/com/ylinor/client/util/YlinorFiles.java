package com.ylinor.client.util;

import java.io.File;


public class YlinorFiles {

    /**
     * Folder {home}/.ylinor/
     */
    private static File dotYlinor;

    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            dotYlinor = new File(System.getProperty("user.home") + "\\AppData\\Roaming\\.ylinor");
        }
        else if (os.contains("mac")) {
            dotYlinor = new File(System.getProperty("user.home") + "/Library/Application Support/.ylinor");
        }
        else {
            dotYlinor = new File(System.getProperty("user.home") + "/.ylinor");
        }
        if (!dotYlinor.exists()) {
            dotYlinor.mkdir();
        }
    }

    public static File getGameFolder() {
        return dotYlinor;
    }

    public static void setDotYlinor(File path) {
        dotYlinor = path;
    }
}
