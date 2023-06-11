package org.unice;

import org.unice.model.Customer;
import org.unice.model.submodel.Address;
import org.unice.service.CustomerService;

import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "STARTING THE APPLICATION" );

        testClient();

        System.out.println( "APPLICATION FINISHED" );
    }

    public static void testClient(){
        // Create a new customer
        Customer customer = new Customer();
        customer.setId(1);
        customer.setLastname("AMEDOMEY");
        customer.setFirstname(List.of("Roméo", "David"));
        customer.setAddress(new Address(11L, "Avenue ", "06200", "Nice", "France"));

        try {
            CustomerService.delete(customer.getId());
            CustomerService.save(customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Get a customer from database by id
        System.out.println(CustomerService.getById(1));

        // Get all document
        System.out.println("Récupération de la liste de tous les clients");
        System.out.println(CustomerService.getAll());

        // Get all document with lastname = AMEDOMEY
        System.out.println("\n\nRécupération de la liste des clients avec le nom de famille AMEDOMEY");
        System.out.println(CustomerService.getByLastname("AMEDOMEY"));
    }
}
