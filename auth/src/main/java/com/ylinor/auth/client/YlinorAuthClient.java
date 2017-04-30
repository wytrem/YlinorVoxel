package com.ylinor.auth.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ylinor.auth.client.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

/**
 * The Auth Client<br><br>
 *
 *
 * HTTP request maker for the API.<br><br>
 *
 * <b>Requests :</b>
 * <ul>
 *     <li>/auth/login -> {@link #login(String, String)}</li>
 *     <li>/api/user -> {@link #user(String)}</li>
 *     <li>/auth/logout -> {@link #logout(String)}</li>
 * </ul><br>
 *
 * It throws {@link AuthException} when the server thrown an exception.<br>
 * And it throws {@link IOException} when the HTTP client thrown one.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public class YlinorAuthClient
{
    private String url;
    private HttpClient client;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * The Auth client
     *
     * @param url The URL of the auth API
     * @param client The HTTP client to use
     */
    public YlinorAuthClient(String url, HttpClient client)
    {
        this.url = url;
        this.client = client;
    }

    /**
     * Login a user using its credentials ( /auth/login )
     *
     * @param email The email of the user to login
     * @param password The password of the user to login
     *
     * @return The generated to token returned by the server
     *
     * @throws AuthException If the server thrown one
     * @throws IOException If the HTTP client thrown one
     */
    public LoginResponse login(String email, String password) throws AuthException, IOException
    {
        HttpPost request = new HttpPost(url + "auth/login");
        request.setEntity(new StringEntity(gson.toJson(new LoginRequest(email, password))));

        return request(request, LoginResponse.class);
    }

    /**
     * Fetch the user infos (that was previously logged) ( /api/user )
     *
     * @param token The user token obtained when logging him
     *
     * @return The user
     *
     * @throws AuthException If the server thrown one
     * @throws IOException If the HTTP client thrown one
     */
    public User user(String token) throws AuthException, IOException
    {
        return request(new HttpGet(url + "api/user?token=" + token), User.class);
    }

    /**
     * Logout a user ( /auth/user )
     *
     * @param token The user token obtained when logging him
     *
     * @throws AuthException If the server thrown one
     * @throws IOException If the HTTP client thrown one
     */
    public void logout(String token) throws AuthException, IOException
    {
        request(new HttpPost(url + "auth/logout?token=" + token), null);
    }

    /**
     * Make a JSON request
     *
     * @param request The request to send
     * @param type The type of the model of the JSON response (nullable)
     * @param <T> The type of the model of the JSON response
     *
     * @return An instance of the given model class filled with the response.
     *          Null if the type given is null
     *
     * @throws AuthException If the server thrown one
     * @throws IOException If the HTTP client thrown one
     */
    private <T> T request(HttpUriRequest request, Class<T> type) throws AuthException, IOException
    {
        request.setHeader("Accept", "application/json");

        HttpResponse response = this.client.execute(request);
        String entity = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).lines().collect(Collectors.joining("\n"));

        if (entity.contains("\"error\""))
        {
            throw gson.fromJson(entity, AuthException.class);
        }

        if (type == null)
        {
            return null;
        }

        return gson.fromJson(entity, type);
    }

    /**
     * @return The given API url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @return The given HTTP client
     */
    public HttpClient getClient()
    {
        return client;
    }
}
