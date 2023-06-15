package org.unice.service;

import org.unice.config.RedisClient;
import org.unice.model.Customer;
import org.unice.model.submodel.Address;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.*;
import redis.clients.jedis.search.schemafields.SchemaField;
import redis.clients.jedis.search.schemafields.TagField;
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

        Query q = new Query("*").limit(0, 10000);

        SearchResult result = database.ftSearch(indexName, q);

        return extractClientsFromResult(result);
    }

    public static Customer getById(int id){
        return database.jsonGet(commonName + id, Customer.class);
    }

    public static List<Customer> getByLastname(String lastname){
        createIndexIfNotExists();

        SearchResult result = database.ftSearch(indexName, "@lastname:" + lastname);

        return extractClientsFromResult(result);
    }

    /**
     * Update a value for all clients.
     * @param key The key value define what should be updated.
     *        The possible values for the key are
     *            - lastname
     *            - firstname
     *            - phone
     *            - address
     * @param newValue
     */
    public static void updateValueForAllCustomers(String key, String newValue) {
        List<Customer> customers = getAll();
        for (Customer customer : customers) {
            updateValueById(customer.getId(), key, newValue);
        }
    }

    /**
     * Update a value for the client by id.
     * @param id
     * @param key The key value define what should be updated.
     *        The possible values for the key are
     *            - id
     *            - lastname
     *            - firstname
     *            - phone
     *            - address
     * @param newValue
     */
    public static void updateValueById(int id, String key, Object newValue) {
        Customer customer = getById(id);
        if (customer != null) {
            switch (key) {
                case "id":
                    CustomerService.delete(customer.getId());
                    customer.setId((int) newValue);
                    break;
                case "lastname":
                    customer.setLastname(newValue.toString());
                    break;
                case "firstname":
                    customer.setFirstname((List<String>) newValue);
                    break;
                case "phone":
                    customer.setPhone(newValue.toString());
                    break;
                case "address":
                    customer.setAddress((Address) newValue);
                    break;
                default:
                    System.err.println("Unrecognized key to update");
            }
            String customerId = commonName + customer.getId();
            database.jsonSet(customerId, customer.toJson());
        }
    }

    /**
     * Delete a customer by id
     * @param id
     */
    public static void delete(int id){
        database.jsonDel(commonName + id);
    }

    /**
     * Create the index used by redis search
     */
    private static void createIndexIfNotExists(){
        boolean indexExists = false;

        try {
            var info = database.ftInfo(indexName);

            if (info != null && info.size() > 0) {
                indexExists = true;
            }
        } catch (JedisDataException exception) {
            // Ignore the exception if the index does not exist
        }

        if (!indexExists){
            FTCreateParams params = new FTCreateParams();
            params.prefix(commonName);
            params.on(IndexDataType.JSON);

            List<SchemaField> fields = new ArrayList<>();

            fields.add(TextField
                    .of("$.lastname")
                    .as("lastname")
                    .sortable()
            );

            fields.add(TagField
                    .of("$.firstname.*")
                    .as("firstname")
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
