package com.ylinor.launcher;

import java.awt.Dimension;
import java.io.File;

import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import net.wytrem.wylog.BasicLogger;
import net.wytrem.wylog.LoggerFactory;


public final class YlinorLauncher {
    public static final String SERVER = "Ylinor";
    public static final String TITLE = "Ylinor Launcher";
    public static final String VERSION = "1.0.1";
    public static final Dimension SIZE = new Dimension(625, 625);

    public static final File GAME_FOLDER = GameDirGenerator.createGameDir(SERVER.toLowerCase());
    public static final File LAUNCHER_FOLDER = new File(GAME_FOLDER, "launcher/");

    public static final String UPDATE_URL = "http://api.ylinor.com:9571/";

    public static final String LIB_FOLDER = "lib/";
    public static final String MAIN_CLASS = "com.ylinor.client.desktop.DesktopLauncher";

    private static final BasicLogger logger = LoggerFactory.getLogger(YlinorLauncher.class);

    private static final CrashReporter reporter = new CrashReporter(TITLE, new File(LAUNCHER_FOLDER, "crashes/"));

    public static void init() {
        if (!LAUNCHER_FOLDER.exists()) {
            logger.info("Création du dossier Ylinor pour la première fois");
            LAUNCHER_FOLDER.mkdirs();
        }

        logger.info("Launcher Ylinor initialisé");
    }

    public static void handleCrash(String message, Exception e) {
        reporter.catchError(e, message);
    }
}
