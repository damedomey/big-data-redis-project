package org.unice.service;

import org.unice.config.RedisClient;
import org.unice.model.Client;
import redis.clients.jedis.Jedis;

public class ClientService {
    public static void save(Client client){
        Jedis connector = RedisClient.getInstance().getResource();
        RedisClient.getInstance().getResource().set("david", "test de regression");
    }
}
