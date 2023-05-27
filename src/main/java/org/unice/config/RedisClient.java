package org.unice.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Manage the connection to the database
 */
public class RedisClient {
    private static RedisClient instance;
    private final JedisPool jedisPool;

    private RedisClient() {
        jedisPool = new JedisPool("localhost", 6379);
    }

    public static synchronized RedisClient getInstance() {
        if (instance == null) {
            instance = new RedisClient();
        }
        return instance;
    }

    public Jedis getResource(){
        return jedisPool.getResource();
    }
}
