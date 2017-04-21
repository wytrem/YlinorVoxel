package com.ylinor.bootstrap;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.ProcessLogManager;
import fr.theshark34.openlauncherlib.util.SplashScreen;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import java.io.File;

import static com.ylinor.bootstrap.Resources.*;

import static fr.theshark34.swinger.Swinger.*;

public class YlinorBootstrap {
    public static final String NAME = "Ylinor";
    public static final String TITLE = "Ylinor";
    public static final String VERSION = "1.0.0";

    private static final String UPDATE_URL = "http://api.ylinor.com:9572/";
    private static final File FOLDER = new File(GameDirGenerator.createGameDir(NAME.toLowerCase()), "launcher");
    private static final File LOGS_FILE = new File(FOLDER, "logs.txt");

    private static final String LIBS_FOLDER = "libs/";
    private static final String LAUNCHER_FILE = "launcher.jar";
    private static final String MAIN_CLASS = "com.ylinor.launcher.Main";

    private static SplashScreen splash;
    private static CrashReporter reporter = new CrashReporter(TITLE, new File(FOLDER, "bootstrap-crashes"));

    public static void splash() {
        splash = new SplashScreen(TITLE, getResource(SPLASH));

        splash.setIconImage(getResource(ICON));
        splash.setResizable(false);

        splash.setVisible(true);
    }

    public static void update() {
        SUpdate su = new SUpdate(UPDATE_URL, FOLDER);
        su.addApplication(new FileDeleter());

        try {
            su.start();
        } catch (Exception e) {
            reporter.catchError(e, "Erreur lors de la mise à jour du launcher !");
        }
    }

    public static void launch() {
        ClasspathConstructor constructor = new ClasspathConstructor();
        constructor.add(Explorer.dir(FOLDER).sub(LIBS_FOLDER).files());
        constructor.add(Explorer.dir(FOLDER).get(LAUNCHER_FILE));

        ExternalLaunchProfile profile = new ExternalLaunchProfile(MAIN_CLASS, constructor.make());
        
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            profile.getVmArgs().add("-XstartOnFirstThread");
        }
        
        profile.setRedirectErrorStream(true);
        ExternalLauncher launcher = new ExternalLauncher(profile);

        splash.setVisible(false);

        Process process = null;
        try {
            process = launcher.launch();
        } catch (LaunchException e) {
            reporter.catchError(e, "Erreur lors du lancement du launcher !");
        }

        ProcessLogManager manager = new ProcessLogManager(process.getInputStream());
        manager.setToWrite(LOGS_FILE);
        manager.start();

        try {
            process.waitFor();
        } catch (InterruptedException ignored) {
        }

        System.exit(0);
    }
}