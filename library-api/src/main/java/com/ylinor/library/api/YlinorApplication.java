package com.ylinor.library.api;

import com.fasterxml.jackson.databind.ObjectMapper;

public class YlinorApplication
{
    private ObjectMapper objectMapper = new ObjectMapper();
    private static YlinorApplication instance;

    public static YlinorApplication getInstance(Class<? extends YlinorApplication> clazz) throws IllegalAccessException, InstantiationException
    {
        if(instance == null)
        {
            instance = clazz.newInstance();
        }
        return instance;
    }

    public static ObjectMapper getJacksonMapper()
    {
        try
        {
            return getInstance(YlinorApplication.class).objectMapper;
        }
        catch(IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
