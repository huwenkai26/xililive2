package com.example.xililive;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.util.Map;

public class SDJsonUtil {
    private SDJsonUtil() {
    }

    public static <T> T json2Object(String json, Class<T> clazz) {
        return  new Gson().fromJson(json,  clazz);
    }

    public static String object2Json(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T map2Object(Map map, Class<T> clazz) {
        if (map != null) {
            return json2Object(object2Json(map), clazz);
        }
        return null;
    }
}