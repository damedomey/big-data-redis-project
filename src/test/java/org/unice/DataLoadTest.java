package org.unice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.unice.model.Customer;
import org.unice.service.CustomerService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Clear data and load data in json resources files
 */
public class DataLoadTest extends TestCase {

    public void testLoadClient(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = Paths.get("src", "main", "resources", "customers.json").toFile();
            List<Customer> customers = objectMapper
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Customer.class));
            for (Customer customer : customers) {
                CustomerService.delete(customer.getId());
                CustomerService.save(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        assertTrue(true);
    }
}
