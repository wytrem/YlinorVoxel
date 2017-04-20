package com.ylinor.launcher;

public class User {
    private String username;
    private String email;
    private int birthday;

    public User(String username, String email, int birthday)
    {
        this.username = username;
        this.email = email;
        this.birthday = birthday;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public int getBirthday()
    {
        return birthday;
    }

    public void setBirthday(int birthday)
    {
        this.birthday = birthday;
    }
}
