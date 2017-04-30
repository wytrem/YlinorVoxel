package com.ylinor.launcher;

import static com.ylinor.launcher.YlinorLauncher.*;


import com.ylinor.auth.client.model.User;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.theshark34.openlauncherlib.external.*;
import fr.theshark34.openlauncherlib.util.ProcessLogManager;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import net.wytrem.wylog.BasicLogger;
import net.wytrem.wylog.LoggerFactory;


public class Launcher {
    private static final BasicLogger logger = LoggerFactory.getLogger(Launcher.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static Saver saver = new Saver(new File(GAME_FOLDER, "launcher.properties"));

    public static String getUsername(String def) {
        return saver.get("email", def);
    }

    public static String getToken() {
        return saver.get("token");
    }

    public static void update(final Runnable callback) throws Exception {
        logger.info("Lancement de la mise à jour...");

        SUpdate su = new SUpdate(UPDATE_URL, GAME_FOLDER);
        su.addApplication(new FileDeleter());
        su.getServerRequester().setRewriteEnabled(true);

        scheduler.scheduleAtFixedRate(callback, 1, 1, TimeUnit.MILLISECONDS);

        su.start();

        logger.info("Mise a jour terminée");

        shutdown();
    }

    public static void launch(User user) throws Exception {
        logger.info("Lancement du jeu");

        ClasspathConstructor classpath = new ClasspathConstructor();
        classpath.add(Explorer.dir(new File(GAME_FOLDER, LIB_FOLDER)).allRecursive().match("^(.*\\.((jar)$))*$").files().get());

        List<String> vmArgs = new ArrayList<>();
        vmArgs.addAll(Arrays.asList("-Dylinor.user.token=" + saver.get("token"),
                                                        "-Dylinor.user.username=" + user.getUsername()));
        
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            vmArgs.add("-XstartOnFirstThread");
        }
        
        ExternalLaunchProfile profile =
                new ExternalLaunchProfile(MAIN_CLASS,
                                          classpath.make(),
                                          vmArgs,
                                          Collections.emptyList(),
                                          true,
                                          "Ylinor Epsilon",
                                          GAME_FOLDER);
        ExternalLauncher launcher = new ExternalLauncher(profile);
        
        Process p = launcher.launch();
        logger.info("Jeu lance !");

        ProcessLogManager manager = new ProcessLogManager(p.getInputStream());
        manager.start();

        Main.hide();
        p.waitFor();

        System.exit(0);
    }

    /**
     * Termine la mise à jour de la barre
     */
    public static void shutdown() {
        scheduler.shutdown();
    }
}
