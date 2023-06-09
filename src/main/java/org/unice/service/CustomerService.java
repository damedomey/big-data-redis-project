package org.unice.service;

import org.unice.config.RedisClient;
import org.unice.model.Customer;
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
 * The customer information are stored as Json name like this: customer:id.
 *
 * The search are performed with RedisSearch. The index name is idx_customer.
 */
public class CustomerService {
    private static final UnifiedJedis database = RedisClient.getInstance().getResource();
    private static final String commonName = "customer:";
    private static final String indexName = "idx_customer";

    /**
     * Save the customer in database.
     * Throw [Exception] if a user with the same id already exist.
     * @param customer
     */
    public static Customer save(Customer customer) throws Exception {
        String customerId = commonName + customer.getId();
        if (database.exists(customerId)) {
            throw new Exception("A customer with id " + customerId + " already exist.");
        } else {
            database.jsonSet(customerId, customer.toJson());
            return customer;
        }
    }

    public static List<Customer> getAll(){
        createIndexIfNotExists();

        SearchResult result = database.ftSearch(indexName, "*");

        return extractClientsFromResult(result);
    }

    public static Customer getById(String id){
        return database.jsonGet(commonName + id, Customer.class);
    }

    public static List<Customer> getByLastname(String lastname){
        createIndexIfNotExists();

        SearchResult result = database.ftSearch(indexName, "@lastname:" + lastname);

        return extractClientsFromResult(result);
    }

    /**
     * Delete a customer by id
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

    private static List<Customer> extractClientsFromResult(SearchResult result) {
        List<Customer> customers = new ArrayList<>();
        for (Document document: result.getDocuments()){
            Iterable<Map.Entry<String, Object>> properties = document.getProperties();

            if (properties.iterator().hasNext()){
                Object json = properties.iterator().next().getValue();
                customers.add(Customer.fromJson(json.toString()));
            }
        }

        return customers;
    }
}
