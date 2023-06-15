package org.unice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;

import org.unice.config.RedisClient;
import org.unice.model.BankAgent;

import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.*;
import redis.clients.jedis.search.schemafields.NumericField;
import redis.clients.jedis.search.schemafields.SchemaField;
import redis.clients.jedis.search.schemafields.TextField;

public class BankAgentService {
    private static final UnifiedJedis database = RedisClient.getInstance().getResource();
    private static final String commonName = "bankAgent:";
    private static final String indexName = "idx_bankAgent";

    public static BankAgent save(BankAgent bankAgent) throws Exception{
        String bankAgentId = commonName + bankAgent.getId();
        if (database.exists(bankAgentId)){
            throw new Exception("An operation with id " + bankAgentId + " already exists.");
        } else {
            database.jsonSet(bankAgentId, bankAgent.toJson());
            return bankAgent;
        }
    }

    public static List<BankAgent> getAll(){
        createIndexIfNotExists();
        Query q = new Query("*").limit(0, 10000);
        SearchResult result = database.ftSearch(indexName, q);
        return extractBankAgentsFromResult(result);
    }

    public static BankAgent getById(int id){
        return database.jsonGet(commonName + id, BankAgent.class);
    }

    public static List<BankAgent> getByName(String name){
        Query q = new Query("@name:" + "\"" + name + "\"");
        SearchResult result = database.ftSearch(indexName, q); 
        return extractBankAgentsFromResult(result);
    }

    public static List<BankAgent> getByBankId(int bankId){
        Query q = new Query("@bankId:["+ bankId +"," + bankId + "]");
        SearchResult result = database.ftSearch(indexName, q); 
        return extractBankAgentsFromResult(result);
    }

    /**
     * Delete a bankAgent by id
     * @param id
     */
    public static void delete(int id){
        database.jsonDel(commonName + id);
    }

    /**
     * Update a value for the operation by id.
     * @param id
     * @param key The key value define what should be updated.
     *        The possible values for the key are
     *            - id
     *            - name
     *            - bankId
     *            - hiringDate
     * @param newValue
     */
    public static void updateValueById(int id, String key, Object newValue){
        BankAgent bankAgent = getById(id);
        if (bankAgent != null){
            switch (key) {
                case "id":
                    BankAgentService.delete(bankAgent.getId());
                    bankAgent.setId((int) newValue);
                    break;
                case "name":
                    bankAgent.setName((String) newValue);
                    break;
                case "bankId":
                    bankAgent.setBankId((int) newValue);
                    break;
                case "hiringDate":
                    bankAgent.setHiringDate((Date) newValue);
                    break;
                default:
                    System.err.println("Unrecognized key to update");
            }
        }
        String bankAgentId = commonName + bankAgent.getId();
        database.jsonSet(bankAgentId, bankAgent.toJson());
    }

    /**
     * Update a value for all bankAgents.
     * @param key The key value define what should be updated.
     *        The possible values for the key are
     *            - id
     *            - name
     *            - bankId
     *            - hiringDate
     * @param newValue
     */
    public static void updateValueForAllBankAgents(String key, String newValue) {
        List<BankAgent> bankAgents = getAll();
        for (BankAgent bankAgent : bankAgents) {
            updateValueById(bankAgent.getId(), key, newValue);
        }
    }

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
                    .of("$.name")
                    .as("name")
                    .sortable()
            );

            fields.add(NumericField
                    .of("$.bankId")
                    .as("bankId")
                    .sortable()
            );


            String res = database.ftCreate(indexName, params, fields);
            System.out.println("--> Index creation : " + res);
        }
    }
    private static List<BankAgent> extractBankAgentsFromResult(SearchResult result){
        List<BankAgent> bankAgents  = new ArrayList<>();
        for (Document document: result.getDocuments()){
            Iterable<Map.Entry<String, Object>> properties = document.getProperties();

            if(properties.iterator().hasNext()){
                Object json = properties.iterator().next().getValue();
                bankAgents.add(BankAgent.fromJson(json.toString()));
            }
        }
        return bankAgents;
        };
}
