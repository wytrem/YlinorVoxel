package com.ylinor.server.systems;

import java.util.Scanner;

import javax.inject.Inject;

import com.ylinor.library.util.ecs.system.NonProcessingSystem;
import com.ylinor.server.main.YlinorServer;


public class CommandLineSystem extends NonProcessingSystem {

    @Inject
    CommandLineThread commandLineThread;

    @Override
    public void initialize() {
        commandLineThread.start();
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
