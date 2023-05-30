package org.unice.model;

import com.google.gson.Gson;

public abstract class JsonSerializable {
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static <T extends JsonSerializable> T fromJson(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }
}
