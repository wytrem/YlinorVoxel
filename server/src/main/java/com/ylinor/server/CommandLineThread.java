package com.ylinor.server;

import com.ylinor.server.main.YlinorServer;

import java.util.Scanner;

public final class CommandLineThread extends Thread {
    private final YlinorServer server;

    public CommandLineThread(YlinorServer server) {
        super("Command line thread");

        this.server = server;
        setDaemon(true);
    }

    private void parseLine(String line) {
        if (line.equals("help")) {
            System.out.println("Not implemented yet, be patient.");
        } if (line.equals("stop")) {
            server.stop();
        } else {
            System.out.println("Unrecognized command, type 'help' for help.");
        }
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        String line;

        while ((line = sc.nextLine()) != null) {
            parseLine(line);
        }
    }
}
