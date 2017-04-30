package com.ylinor.auth.client.model;

/**
 * The Login Request<br><br>
 *
 *
 * The login request to send as json.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public class LoginRequest
{
    private String email;
    private String password;

    /**
     * The login request
     *
     * @param email The user E-Mail
     * @param password The user password
     */
    public LoginRequest(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    /**
     * @return The user E-Mail
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @return The user password
     */
    public String getPassword()
    {
        return password;
    }
}
