package com.ylinor.server;

import com.ylinor.library.api.YlinorApplication;
public class YlinorServer extends YlinorApplication {
    private static YlinorServer server;

    public YlinorServer() {
        instance = this;
        server = this;
    }

    public static void main(String[] args) {
        
    }

    public static YlinorServer server() {
        return server;
    }
}
