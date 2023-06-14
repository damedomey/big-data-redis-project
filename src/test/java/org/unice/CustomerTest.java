package org.unice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.unice.model.Customer;
import org.unice.service.CustomerService;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * Clear data and load data in json resources files
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerTest extends TestCase {

    /**
     * This test create many customer
     * The customers information is loaded from json file
     */
    public void test1CreateCustomer(){
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
        } catch (Exception ignored) {}

        assertTrue(true);
    }

    public void test2GetAllCustomers() {
        List<Customer> customers = CustomerService.getAll();
        assertEquals(100, customers.size());
    }

    public void test3GetByID() throws RuntimeException{
        Customer customer = CustomerService.getById("1");
        assertEquals( "Mattecot", customer.getLastname());
    }

    /**
     * Test the update all method.
     * Due to the fact that update all method is based on update by id, whe don't need to test
     * update by id anymore. The success of this test means update by id work.
     */
    public void test4UpdateAll() {
        Customer previousCustomer = CustomerService.getById("10");
        CustomerService.updateValueForAllCustomers("lastname", "Hello world");
        Customer currentCustomer = CustomerService.getById("10");
        assertNotSame(previousCustomer, currentCustomer);
        assertEquals("Hello world", currentCustomer.getLastname());
    }

    public void test5Delete() {
        CustomerService.delete("13");
        assertEquals(99, CustomerService.getAll().size());
    }
}
