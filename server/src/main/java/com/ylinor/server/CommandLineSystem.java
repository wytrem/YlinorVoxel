package com.ylinor.server;

import java.rmi.server.SocketSecurityException;
import java.util.Scanner;

import javax.inject.Inject;

import com.ylinor.library.util.ecs.system.BaseSystem;
import com.ylinor.server.main.YlinorServer;


public class CommandLineSystem extends BaseSystem {

    @Inject
    CommandLineThread commandLineThread;
    
    @Override
    public void initialize() {
        commandLineThread.start();
    }

    @Override
    protected void processSystem() {
        System.out.println("hey command");
    }

    public static final class CommandLineThread extends Thread {
        @Inject
        private YlinorServer server;

        public CommandLineThread() {
            super("Command line thread");
            setDaemon(true);
        }

        private void parseLine(String line) {
            if (line.equals("help")) {
                System.out.println("Not implemented yet, be patient.");
            }
            if (line.equals("stop")) {
                server.stop();
            }
            else {
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

            sc.close();
        }
    }

}
