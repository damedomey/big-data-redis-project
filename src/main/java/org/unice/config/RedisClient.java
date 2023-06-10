package org.unice.config;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.providers.PooledConnectionProvider;

/**
 * Manage the connection to the database
 */
public class RedisClient {
    private static RedisClient instance;
    private final UnifiedJedis jedis;

    private RedisClient() {
        HostAndPort config = new HostAndPort("localhost", 6379);
        PooledConnectionProvider provider = new PooledConnectionProvider(config);
        jedis = new UnifiedJedis(provider);
    }

    public static synchronized RedisClient getInstance() {
        if (instance == null) {
            instance = new RedisClient();
        }
        return instance;
    }

    /**
     * @return The database client
     */
    public UnifiedJedis getResource(){
        return jedis;
    }
}
