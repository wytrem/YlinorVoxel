package com.ylinor.auth.client;

import com.ylinor.auth.client.model.AuthException;
import com.ylinor.auth.client.model.User;
import java.io.IOException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * The Ylinor User (Main API class)<br><br>
 *
 *
 * This class is a wrapper for a user. It manages its session (token) and
 * contains the HTTP client.<br><br>
 *
 * <b>How to use it :</b><br><br>
 *
 * First create an instance of YlinorUser. Then, use the login function to
 * fetch the user.<br>
 * You can then access the user entity using {@link YlinorUser#user()}.<br><br>
 *
 * Don't forget to do logout() then.<br><br>
 *
 * <b>Example :</b>
 * <pre>
 *     YlinorUser user = new YlinorUser();
 *     user.login("me@email.com", "password");
 *
 *     System.out.println("User : " + user.user().getUsername());
 *
 *     user.logout();
 * </pre>
 *
 * It throws {@link AuthException} when the server thrown an exception.<br>
 * And it throws {@link IOException} when the HTTP client thrown one.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public class YlinorUser
{
    public static final String DEFAULT_URL = "http://users.ylinor.com/";

    private YlinorAuthClient client;

    private String token;
    private User user;

    /**
     * Initialize the API using the {@link #DEFAULT_URL} and a simple HTTP
     * client
     */
    public YlinorUser()
    {
        this(DEFAULT_URL);
    }

    /**
     * Initialize the API using a custom URL
     *
     * @param url The URL of the API to use
     */
    public YlinorUser(String url)
    {
        this(url, HttpClientBuilder.create().build());
    }

    /**
     * Initialize the API using the {@link #DEFAULT_URL} and a custom
     * HTTP client
     *
     * @param client The HTTP client to use
     */
    public YlinorUser(HttpClient client)
    {
        this(DEFAULT_URL, client);
    }

    /**
     * Initialize the API using a custom URL and HTTP client
     *
     * @param url The URL of the API to use
     * @param client The HTTP client to use
     */
    public YlinorUser(String url, HttpClient client)
    {
        this(new YlinorAuthClient(url, client));
    }

    /**
     * Initialize the API using a custom {@link YlinorAuthClient}
     *
     * @param client The auth client to use
     */
    public YlinorUser(YlinorAuthClient client)
    {
        this.client = client;
    }

    /**
     * Login a user using its email and password
     *
     * @param email The E-Mail of the user to login
     * @param password The password of the user to login
     *
     * @throws AuthException If the server dropped an error
     * @throws IOException If the HTTP client dropped an error
     */
    public void login(String email, String password) throws AuthException, IOException
    {
        this.token = client.login(email, password).getToken();
        this.fetch();
    }

    /**
     * Fetch the user infos (automatically called by {@link #login(String, String)}
     *
     * @throws AuthException If the server dropped an error
     * @throws IOException If the HTTP client dropped an error
     */
    public void fetch() throws AuthException, IOException
    {
        if (!isLogged())
        {
            return;
        }

        fetch(this.token);
    }

    /**
     * Fetch the user infos without login, using a session token.
     *
     * @param token The user session token to use
     *
     * @throws AuthException If the server dropped an error
     * @throws IOException If the HTTP client dropped an error
     */
    public void fetch(String token) throws AuthException, IOException
    {
        this.user = client.user(token);
    }

    /**
     * Fetch the user infos (automatically called by {@link #login(String, String)}
     *
     * @throws AuthException If the server dropped an error
     * @throws IOException If the HTTP client dropped an error
     */
    public void logout() throws AuthException, IOException
    {
        client.logout(this.token);

        this.token = null;
        this.user = null;
    }

    /**
     * @return The user (if logged)
     */
    public User user()
    {
        return this.user;
    }

    /**
     * @return The user session token (if logged)
     */
    public String getToken()
    {
        return token;
    }

    /**
     * @return If the user is logged (using {@link #login(String, String)})
     */
    public boolean isLogged()
    {
        return this.user != null;
    }

    /**
     * @return The auth client used
     */
    public YlinorAuthClient getClient()
    {
        return client;
    }
}
