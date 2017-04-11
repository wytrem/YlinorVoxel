package com.ylinor.launcher;

import fr.theshark34.openlauncherlib.internal.InternalLaunchProfile;
import fr.theshark34.openlauncherlib.internal.InternalLauncher;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import java.io.File;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.wytrem.wylog.BasicLogger;
import net.wytrem.wylog.LoggerFactory;


import static com.ylinor.launcher.YlinorLauncher.*;

public class Launcher {
    private static final BasicLogger logger = LoggerFactory.getLogger(Launcher.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void update(final Runnable callback) throws Exception {
        logger.info("Lancement de la mise a jour...");

        SUpdate su = new SUpdate(UPDATE_URL, GAME_FOLDER);
        su.addApplication(new FileDeleter());
        su.getServerRequester().setRewriteEnabled(true);

        scheduler.scheduleAtFixedRate(callback, 1, 1, TimeUnit.MILLISECONDS);

        su.start();

        logger.info("Mise a jour terminee");

        shutdown();
    }

    public static void launch() throws Exception {
        logger.info("Lancement du jeu");

        InternalLaunchProfile profile = new InternalLaunchProfile(MAIN_CLASS);
        profile.setClasspath(Collections.singletonList(new File(GAME_FOLDER, MAIN_JAR)));
        InternalLauncher launcher = new InternalLauncher(profile);

        Main.hide();

        logger.info("Jeu lance !");
        launcher.launch();
    }

    /**
     * Termine la mise à jour de la barre
     */
    public static void shutdown()
    {
        scheduler.shutdown();
    }
}
