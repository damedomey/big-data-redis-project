package org.unice.service;

import org.unice.config.RedisClient;
import org.unice.model.BankAccount;
import org.unice.model.Customer;
import org.unice.model.submodel.Address;

import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy.BaseNameValidator;

import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.search.*;
import redis.clients.jedis.search.schemafields.NumericField;
import redis.clients.jedis.search.schemafields.SchemaField;
import redis.clients.jedis.search.schemafields.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Date;


public class BankAccountService {
    private static final UnifiedJedis database = RedisClient.getInstance().getResource();
    private static final String commonName = "bankAccount:";
    private static final String indexName = "idx_bankAccount";
    
     /**
     * Save the account in database.
     * Throw [Exception] if a user with the same id already exist.
     * @param bankAccount
     */
    public static BankAccount save(BankAccount bankAccount) throws Exception {
        String bankAccountId = commonName + bankAccount.getId();
        if (database.exists(bankAccountId)) {
            throw new Exception("An account with id " + bankAccountId + " already exist.");
        } else {
            database.jsonSet(bankAccountId, bankAccount.toJson());
            return bankAccount;
        }
    }

    public static List<BankAccount> getAll(){
        createIndexIfNotExists();
        Query q = new Query("*").limit(0, 10000);
        SearchResult result = database.ftSearch(indexName, q);
        return extractBankAccountsFromResult(result);
    }

    public static BankAccount getById(int id){
            return database.jsonGet(commonName + id, BankAccount.class);
        }
    
    public static List<BankAccount> getByAccountNumber(String accountNumber){
        createIndexIfNotExists();
        Query q = new Query("@accountNumber:"+ "\"" + accountNumber + "\"");
        SearchResult result = database.ftSearch(indexName, q);
        return extractBankAccountsFromResult(result);

    }

    /**
     * Delete an account by id
     * @param id
     */
    public static void delete(int id){
        database.jsonDel(commonName + id);
    }

    /**
     * Update a value for all accounts.
     * @param key The key value define what should be updated.
     *        The possible values for the key are
     *            - id
     *            - accountNumber
     *            - customerId
     *            - accountBalance
     *            - accountType
     *            - creationDate
     *            - closeDate
     * @param newValue
     */
    public static void updateValueForAllCustomers(String key, String newValue) {
        List<BankAccount> bankAccounts = getAll();
        for (BankAccount bankAccount : bankAccounts) {
            updateValueById(bankAccount.getId(), key, newValue);
        }
    }

    /**
     * Update a value for the account by id.
     * @param id
     * @param key The key value define what should be updated.
     *        The possible values for the key are
     *            - id
     *            - accountNumber
     *            - customerId
     *            - accountBalance
     *            - accountType
     *            - creationDate
     *            - closeDate
     * @param newValue
     */
    public static void updateValueById(int id, String key, Object newValue) {
        BankAccount bankAccount = getById(id);
        if (bankAccount != null) {
            switch (key) {
                case "id":
                    BankAccountService.delete(bankAccount.getId());
                    bankAccount.setId((int) newValue);
                    break;
                case "accountNumber":
                    bankAccount.setAccountNumber(newValue.toString());
                    break;
                case "customerId":
                    bankAccount.setCustomerId((int) newValue);
                    break;
                case "accountBalance":
                    bankAccount.setAccountBalance((int) newValue);
                    break;
                case "accountType":
                    bankAccount.setAccountType((String) newValue);
                    break;
                case "creationDate":
                    bankAccount.setCreationDate((Date) newValue);
                case "closeDate":
                    bankAccount.setCloseDate((Date) newValue);
                default:
                    System.err.println("Unrecognized key to update");
            }
            String customerId = commonName + bankAccount.getId();
            database.jsonSet(customerId, bankAccount.toJson());
        }
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

            fields.add(NumericField
                    .of("$.customerId")
                    .as("customerId")
                    .sortable()
            );

            fields.add(TextField
                    .of("$.accountNumber")
                    .as("accountNumber")
            );

            fields.add(TextField
                    .of("$.accountType")
                    .as("accountType")
            );

            String res = database.ftCreate(indexName, params, fields);
            System.out.println("--> Index creation : " + res);
        }
    }

    private static List<BankAccount> extractBankAccountsFromResult(SearchResult result) {
        List<BankAccount> bankAccounts = new ArrayList<>();
        for (Document document: result.getDocuments()){
            Iterable<Map.Entry<String, Object>> properties = document.getProperties();

            if (properties.iterator().hasNext()){
                Object json = properties.iterator().next().getValue();
                bankAccounts.add(BankAccount.fromJson(json.toString()));
            }
        }

        return bankAccounts;
    }
}
