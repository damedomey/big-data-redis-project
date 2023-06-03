package org.unice.service;

import org.unice.config.RedisClient;
import org.unice.model.Client;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.*;
import redis.clients.jedis.search.schemafields.SchemaField;
import redis.clients.jedis.search.schemafields.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manage the persistence of data.
 * The client information are stored as Json name like this: client:id.
 *
 * The search are performed with RedisSearch. The index name is idx_client.
 */
public class ClientService {
    private static final UnifiedJedis database = RedisClient.getInstance().getResource();
    private static final String commonName = "client:";
    private static final String indexName = "idx_client";

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
            database.jsonSet(clientId, client.toJson());
            return client;
        }
    }

    public static List<Client> getAll(){
        createIndexIfNotExists();

        SearchResult result = database.ftSearch(indexName, "*");

        return extractClientsFromResult(result);
    }

    public static Client getById(String id){
        return database.jsonGet(commonName + id, Client.class);
    }

    public static List<Client> getByLastname(String lastname){
        createIndexIfNotExists();

        SearchResult result = database.ftSearch(indexName, "@lastname:" + lastname);

        return extractClientsFromResult(result);
    }

    /**
     * Delete a client by id
     * @param id
     */
    public static void delete(String id){
        database.jsonDel(commonName + id);
    }

    /**
     * Create the index used by redis search
     */
    private static void createIndexIfNotExists(){
        try {
            database.ftInfo(indexName);
        } catch (JedisDataException exception) {
            FTCreateParams params = new FTCreateParams();
            params.prefix(commonName);
            params.on(IndexDataType.JSON);

            List<SchemaField> fields = new ArrayList<>();

            fields.add(TextField
                    .of("$.lastname")
                    .as("lastname")
                    .sortable()
            );

            fields.add(TextField
                    .of("$.firstname")
                    .as("firstname")
                    .sortable()
            );

            String res = database.ftCreate(indexName, params, fields);
            System.out.println("--> Index creation : " + res);
        }
    }

    private static List<Client> extractClientsFromResult(SearchResult result) {
        List<Client> clients = new ArrayList<>();
        for (Document document: result.getDocuments()){
            Iterable<Map.Entry<String, Object>> properties = document.getProperties();

            if (properties.iterator().hasNext()){
                Object json = properties.iterator().next().getValue();
                clients.add(Client.fromJson(json.toString()));
            }
        }

        return clients;
    }
}
