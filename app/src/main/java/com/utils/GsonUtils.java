package com.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils
{
    private static Gson gson;

    private GsonUtils()
    {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

//    public static Gson getInstance()
//    {
//        if (gson == null) {
//            gson = new Gson();
//        }
//        return gson;
//    }

    public static Gson getInstance()
    {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.serializeSpecialFloatingPointValues();
            gson = gsonBuilder.create() ;
//                gson = new Gson();
        }
        return gson;
    }

}
