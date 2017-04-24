package com.ylinor.launcher;

import static com.ylinor.launcher.YlinorLauncher.AUTH_URL;
import static com.ylinor.launcher.YlinorLauncher.GAME_FOLDER;
import static com.ylinor.launcher.YlinorLauncher.LIB_FOLDER;
import static com.ylinor.launcher.YlinorLauncher.MAIN_CLASS;
import static com.ylinor.launcher.YlinorLauncher.UPDATE_URL;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
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

    private static HttpClient http = HttpClientBuilder.create().build();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static JsonParser parser = new JsonParser();
    private static Saver saver = new Saver(new File(GAME_FOLDER, "launcher.properties"));

    public static void auth(String username, String password) throws Exception {
        HttpPost post = new HttpPost(AUTH_URL + "auth/login");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", username));
        params.add(new BasicNameValuePair("password", password));

        String rep = http(post, params);
        JsonObject object = parser.parse(rep).getAsJsonObject();

        if (object.has("error")) {
            throw new AuthException(object.get("message").getAsString());
        }

        String token = object.get("token").getAsString();
        saver.set("token", token);
        saver.set("email", username);

        logger.info("Session créée, token : " + token);
    }

    public static String getUsername(String def) {
        return saver.get("email", def);
    }

    public static User getUser() throws IOException {
        String token = saver.get("token");

        if (token == null) {
            return null;
        }

        HttpGet post = new HttpGet(AUTH_URL + "api/user?token=" + token);
        String rep = http(post, null);
        JsonObject object = parser.parse(rep).getAsJsonObject();

        if (object.has("error")) {
            logger.warning("Erreur lors du refresh : " + object.get("message")
                                                               .getAsString());
            return null;
        }

        User user = gson.fromJson(object, User.class);

        logger.info("Utilisateur connecté : " + user.getUsername());

        return user;
    }

    private static String http(HttpRequestBase req, List<NameValuePair> params) throws IOException {
        if (req instanceof HttpPost) {
            ((HttpPost) req).setEntity(new UrlEncodedFormEntity(params));
        }

        req.addHeader("Accept", "application/json;charset=UTF-8");
        HttpResponse response = http.execute(req);

        String rep = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().collect(Collectors.joining("\n"));

        while (rep.charAt(0) != '{')
        {
            rep = rep.substring(1);
        }

        return rep;
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
