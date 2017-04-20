package com.ylinor.launcher;

import fr.theshark34.swinger.Swinger;
import net.wytrem.wylog.BasicLogger;
import net.wytrem.wylog.LoggerFactory;

public final class Main {
    private static BasicLogger logger = LoggerFactory.getLogger(Main.class);
    private static LauncherFrame frame;

    public static void main(String[] args) {
        logger.info(YlinorLauncher.TITLE + " v" + YlinorLauncher.VERSION);
        logger.info("Par la team Ylinor (Wytrem, Litarvan, Alexis L.)");

        logger.info("Préparation des resources et définition du thème du système");

        YlinorLauncher.init();

        Swinger.setResourcePath(Resources.RESOURCE_PATH);
        Swinger.setSystemLookNFeel();

        logger.info("Affichage du launcher\n");

        frame = new LauncherFrame();
        frame.setVisible(true);
    }

    public static void hide() {
        frame.setVisible(false);
    }
}
