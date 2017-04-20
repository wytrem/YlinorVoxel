package com.ylinor.launcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.internal.InternalLaunchProfile;
import fr.theshark34.openlauncherlib.internal.InternalLauncher;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.wytrem.wylog.BasicLogger;
import net.wytrem.wylog.LoggerFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;


import static com.ylinor.launcher.YlinorLauncher.*;

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

        if (object.has("error"))
        {
            throw new AuthException(object.get("message").getAsString());
        }

        String token = object.get("token").getAsString();
        saver.set("token", token);
        saver.set("email", username);

        logger.info("Session créée, token : " + token);
    }

    public static String getUsername(String def)
    {
        return saver.get("email", def);
    }

    public static User getUser() throws IOException {
        String token = saver.get("token");

        if (token == null)
        {
            return null;
        }

        HttpGet post = new HttpGet(AUTH_URL + "api/user?token=" + token);
        String rep = http(post, null);
        JsonObject object = parser.parse(rep).getAsJsonObject();

        if (object.has("error"))
        {
            logger.warning("Erreur lors du refresh : " + object.get("message").getAsString());
            return null;
        }

        User user = gson.fromJson(object, User.class);

        logger.info("Utilisateur connecté : " + user.getUsername());

        System.setProperty("ylinor.user.token", token);
        System.setProperty("ylinor.user.username", user.getUsername());

        return user;
    }

    private static String http(HttpRequestBase req, List<NameValuePair> params) throws IOException {
        if (req instanceof HttpPost) {
            ((HttpPost) req).setEntity(new UrlEncodedFormEntity(params));
        }

        req.addHeader("Accept", "application/json;charset=UTF-8");
        HttpResponse response = http.execute(req);

        return new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().collect(Collectors.joining("\n"));
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

    public static void launch() throws Exception {
        logger.info("Lancement du jeu");

        InternalLaunchProfile profile = new InternalLaunchProfile(MAIN_CLASS);
        profile.setClasspath(Explorer.dir(new File(GAME_FOLDER, LIB_FOLDER)).allRecursive().match("^(.*\\.((jar)$))*$").files().get());
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
