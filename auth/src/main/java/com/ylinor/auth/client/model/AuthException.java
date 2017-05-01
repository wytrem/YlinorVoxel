package com.ylinor.auth.client.model;

/**
 * The Auth Exception<br><br>
 *
 *
 * An exception thrown by the auth server.
 *
 * @author Litarvan
 * @version 1.0.0
 */
public class AuthException extends Exception
{
    private static final long serialVersionUID = -3335580292281431458L;
    private String error;
    private String message;

    /**
     * The Auth Exception
     *
     * @param error The error class
     * @param message The error message
     */
    public AuthException(String error, String message)
    {
        super(message);

        this.error = error;
        this.message = message;
    }

    /**
     * @return The error class
     */
    public String getError()
    {
        return error;
    }

    /**
     * @return The error message
     */
    public String getMessage()
    {
        return message;
    }
}
