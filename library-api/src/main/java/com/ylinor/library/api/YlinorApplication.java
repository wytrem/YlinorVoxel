package com.ylinor.library.api;

import com.fasterxml.jackson.databind.ObjectMapper;

public class YlinorApplication
{
    private ObjectMapper mapper = new ObjectMapper();
    protected static YlinorApplication instance;

    public static YlinorApplication getYlinorApplication()
    {
        return instance;
    }

    public ObjectMapper getMapper()
    {
        return mapper;
    }
}
