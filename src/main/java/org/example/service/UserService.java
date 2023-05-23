package org.example.service;

import org.example.config.RedisClient;
import org.example.model.User;

public class UserService {
    public static void save(User user){
        RedisClient.getInstance().getResource().set("david", "test de regression");
    }
}
