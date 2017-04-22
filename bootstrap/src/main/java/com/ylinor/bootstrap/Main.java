package com.ylinor.bootstrap;

import static com.ylinor.bootstrap.Resources.RESOURCE_PATH;

import fr.theshark34.swinger.Swinger;
import net.wytrem.wylog.BasicLogger;
import net.wytrem.wylog.LoggerFactory;

public final class Main {
    private static BasicLogger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info(YlinorBootstrap.TITLE + " Bootstrap v" + YlinorBootstrap.VERSION);
        logger.info("Par la team Ylinor (Wytrem, Litarvan, Alexis. L)");

        logger.info("Préparation des resources et définition du thème du système");

        Swinger.setSystemLookNFeel();
        Swinger.setResourcePath(RESOURCE_PATH);

        logger.info("Affichage du Splash");
        YlinorBootstrap.splash();

        logger.info("Mise à jour du launcher");
        YlinorBootstrap.update();

        logger.info("Lancement du jeu");
        YlinorBootstrap.launch();
    }
}
