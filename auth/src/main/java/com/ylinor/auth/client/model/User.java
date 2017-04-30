package com.ylinor.auth.client.model;

import com.ylinor.auth.client.YlinorAuthClient;
import com.ylinor.auth.client.YlinorUser;
import java.util.Date;

/**
 * An User object<br><br>
 *
 *
 * The user infos. Retrieved from {@link YlinorAuthClient#user(String)} or
 * {@link YlinorUser#user()}.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public class User
{
    private String email;
    private String username;
    private int birthday;

    private Date birthdayDate;

    /**
     * An User
     *
     * @param email The E-Mail of the user
     * @param username The username of the user
     * @param birthday The birthday date of the user
     */
    public User(String email, String username, int birthday)
    {
        this.email = email;
        this.username = username;
        this.birthday = birthday;
    }

    /**
     * @return The E-Mail of the user
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @return The username of the user
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @return The birthday date of the user (timestamp)
     */
    public int getBirthday()
    {
        return birthday;
    }

    /**
     * @return The birthday date of the user (date object)
     */
    public Date getBirthdayDate()
    {
        if (this.birthdayDate == null)
        {
            this.birthdayDate = new Date(this.birthday);
        }

        return this.birthdayDate;
    }
}
