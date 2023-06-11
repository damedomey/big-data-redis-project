package org.unice.service;

import org.unice.config.RedisClient;
import org.unice.model.Agency;

import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.*;
import redis.clients.jedis.search.schemafields.SchemaField;
import redis.clients.jedis.search.schemafields.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AgencyService {
    private static final UnifiedJedis database = RedisClient.getInstance().getResource();
    private static final String commonName = "agency:";
    private static final String indexName = "idx_agency";
    
    public static Agency save(Agency agency) throws Exception {
        String agencyId = commonName + agency.getId();
        if (database.exists(agencyId)) {
            throw new Exception("An agency with id " + agencyId + " already exist.");
        } else {
            database.jsonSet(agencyId, agency.toJson());
            return agency;
        }
        
    }

    public static void delete(int id){
        database.jsonDel(commonName + id);
    }

    public static List<Agency> getAll(){
        createIndexIfNotExists();
        Query q = new Query("*").limit(0, 10000);
        SearchResult result = database.ftSearch(indexName, q);
        return extractAgenciesFromResult(result);
    }

    public static Agency getByID(int id){
        return database.jsonGet(commonName + id, Agency.class);
    }

    /**
     * Get all the operations that contain subTitle string parameter in their title.
     * TO DO: Currently does not support special characters like '-' symbol
     * @param id
     */
    public static List<Agency> getByName(String subTitle){
        createIndexIfNotExists();
        Query q = new Query("@name:" + "\"" + subTitle + "\"");
        SearchResult result = database.ftSearch(indexName, q); 
        return extractAgenciesFromResult(result);
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
                    .of("$.name")
                    .as("name")
                    .sortable()
            );

            String res = database.ftCreate(indexName, params, fields);
            System.out.println("--> Index creation : " + res);
        }
    }

    private static List<Agency> extractAgenciesFromResult(SearchResult result) {
        List<Agency> agencies = new ArrayList<>();
        for (Document document: result.getDocuments()){
            Iterable<Map.Entry<String, Object>> properties = document.getProperties();

            if (properties.iterator().hasNext()){
                Object json = properties.iterator().next().getValue();
                agencies.add(Agency.fromJson(json.toString()));
            }
        }

        return agencies;
    }

}
