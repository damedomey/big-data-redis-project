package org.unice.service;

import org.unice.config.RedisClient;
import org.unice.model.Operation;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.*;
import redis.clients.jedis.search.schemafields.SchemaField;
import redis.clients.jedis.search.schemafields.TextField;
import redis.clients.jedis.search.schemafields.NumericField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OperationService {
    private static final UnifiedJedis database = RedisClient.getInstance().getResource();
    private static final String commonName = "operation:";
    private static final String indexName = "idx_operation";

    public static Operation save(Operation operation) throws Exception{
        String operationId = commonName + operation.getId();
        if (database.exists(operationId)){
            throw new Exception("An operation with id " + operationId + " already exists.");
        } else {
            database.jsonSet(operationId, operation.toJson());
            return operation;
        }
    }

    public static List<Operation> getAll(){
        createIndexIfNotExists();
        Query q = new Query("*").limit(0, 10000);
        SearchResult result = database.ftSearch(indexName, q);
        return extractOperationsFromResult(result);
    }

    public static Operation getByID(int id){
        return database.jsonGet(commonName + id, Operation.class);
    }

    /**
     * Get all the operations that contain subTitle string parameter in their title.
     * TO DO: Currently does not support special characters like '-' symbol
     * @param id
     */
    public static List<Operation> getByTitle(String subTitle){
        createIndexIfNotExists();
        Query q = new Query("@title:" + "\"" + subTitle + "\"");
        SearchResult result = database.ftSearch(indexName, q); 
        return extractOperationsFromResult(result);
    }

    /**
     * Delete an opeartion by id
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
                    .of("$.title")
                    .as("title")
                    .sortable()
            );

            fields.add(NumericField
                    .of("$.operationDebit")
                    .as("operationDebit")
            );

             fields.add(NumericField
                    .of("$.operationProfit")
                    .as("operationProfit")
            );


            String res = database.ftCreate(indexName, params, fields);
            System.out.println("--> Index creation : " + res);
        }
    }

    private static List<Operation> extractOperationsFromResult(SearchResult result){
        List<Operation> operations  = new ArrayList<>();
        for (Document document: result.getDocuments()){
            Iterable<Map.Entry<String, Object>> properties = document.getProperties();

            if(properties.iterator().hasNext()){
                Object json = properties.iterator().next().getValue();
                operations.add(Operation.fromJson(json.toString()));
            }
        }
        return operations;
        };
    }

