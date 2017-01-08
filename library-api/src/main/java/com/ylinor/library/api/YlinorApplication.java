package com.ylinor.library.api;

import com.fasterxml.jackson.databind.ObjectMapper;

public class YlinorApplication
{

    private ObjectMapper mapper = new ObjectMapper();
    private static YlinorApplication instance;

    public static YlinorApplication getYlinorApplication()
    {
        if(instance == null)
        {
            instance = new YlinorApplication();
        }
        return instance;
    }

    public ObjectMapper getMapper()
    {
        return mapper;
    }
}
