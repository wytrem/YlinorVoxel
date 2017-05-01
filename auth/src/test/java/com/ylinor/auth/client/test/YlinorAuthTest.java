package com.ylinor.auth.client.test;

import com.ylinor.auth.client.YlinorUser;
import com.ylinor.auth.client.model.AuthException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class YlinorAuthTest
{
    private static YlinorUser user = new YlinorUser();

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Type 'help' for help, and 'quit' to exit\n> ");

        while (scanner.hasNextLine())
        {
            update(scanner.nextLine());
            System.out.print("> ");
        }
        
        scanner.close();
    }

    private static void update(String line)
    {
        String[] split = line.split(" ");

        if (split.length == 0)
        {
            return;
        }

        String[] args = new String[0];

        if (split.length > 1)
        {
            args = Arrays.asList(split).subList(1, split.length).toArray(new String[split.length - 1]);
        }

        switch (split[0].toLowerCase())
        {
            case "login":
                login(args);
                break;
            case "user":
                user();
                break;
            case "logout":
                logout();
                break;
            case "help":
                help();
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                System.err.println("Unknown command '" + split[0] + "'");
        }
    }

    private static void login(String[] args)
    {
        if (args.length != 2)
        {
            System.err.println("Syntax : login <username> <password>");
            return;
        }

        if (user.isLogged())
        {
            System.err.println("You're already logged, use 'logout' command to logout");
            return;
        }

        System.out.println("Logging in " + args[0] + "...");

        try
        {
            user.login(args[0], args[1]);
        }
        catch (AuthException | IOException e)
        {
            e.printStackTrace();
            return;
        }

        System.out.println("Success ! Token : " + user.getToken());
    }

    private static void user()
    {
        if (!user.isLogged())
        {
            System.err.println("You're not logged, use 'login' command to login");
            return;
        }

        System.out.println("User#" + user.getToken() + " {" + "\n    email = '" + user.user().getEmail() + "'\n    username = '" + user.user().getUsername() + "'\n    birthday = " + user.user().getBirthdayDate() + "\n}");
    }

    private static void logout()
    {
        if (!user.isLogged())
        {
            System.err.println("You're not logged, use 'login' command to login");
            return;
        }

        try
        {
            user.logout();
        }
        catch (AuthException | IOException e)
        {
            e.printStackTrace();
            return;
        }

        System.out.println("Logged out");
    }

    private static void help()
    {
        System.out.println("login <username> <password>\n    Login a user\nuser\n    Prints the logged user infos\nlogout\n    Log out the logged user\nhelp\n    Prints this message\nquit\n    Exit this test");
    }
}
