package com.ylinor.auth.client.model;

import com.ylinor.auth.client.YlinorAuthClient;

/**
 * The Login Response<br><br>
 *
 * The response given by the {@link YlinorAuthClient#login(String, String)}
 * request.<br>
 * Contains the user session token.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public class LoginResponse
{
    private String token;

    /**
     * The Login Response
     *
     * @param token The user session token
     */
    public LoginResponse(String token)
    {
        this.token = token;
    }

    /**
     * @return The user session token
     */
    public String getToken()
    {
        return token;
    }
}
