package org.unice.service;

import org.unice.config.RedisClient;
import org.unice.model.Client;
import redis.clients.jedis.Jedis;

import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;

/**
 * Manage the persistence of data.
 * The client information are stored as Redis hash name like this client:id.
 * To go faster in search, the additional list can be stored to map a property
 * with the user key in database.
 */
public class ClientService {
    private static Jedis database = RedisClient.getInstance().getResource();
    private static String commonName = "client:";

    /**
     * Save the client in database.
     * Throw [Exception] if a user with the same id already exist.
     * @param client
     */
    public static Client save(Client client) throws Exception {
        String clientId = commonName + client.getId();
        if (database.exists(clientId)) {
            throw new Exception("A client with id " + clientId + " already exist.");
        } else {
            database.hset(clientId, client.toMap());
            return client;
        }
    }

    public static Optional getById(String id){
        Optional<Client> client = Optional.empty();
        Map<String, String> response = database.hgetAll(commonName + id);

        if (response.size() > 0){
            client = Optional.of(Client.fromMap(response));
        }

        return client;
    }

    /**
     * Delete a client by id
     * @param id
     */
    public static void delete(String id){
        database.del(commonName + id);
    }
}
