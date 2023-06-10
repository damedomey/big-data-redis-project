package org.unice.service;

import org.unice.config.RedisClient;
import org.unice.model.Operation;
import redis.clients.jedis.UnifiedJedis;



public class OperationService {
    private static final UnifiedJedis database = RedisClient.getInstance().getResource();
    private static final String commonName = "operation:";
    private static final String indexName = "idx_customer";

    public static Operation save(Operation operation) throws Exception{
        String operationId = commonName + operation.getId();
        if (database.exists(operationId)){
            throw new Exception("An operation with id " + operationId + " already exists.");
        } else {
            database.jsonSet(operationId, operation.toJson());
            return operation;
        }
    }

    /**
     * Delete an opeartion by id
     * @param id
     */
    public static void delete(int id){
        database.jsonDel(commonName + id);
    }

}
