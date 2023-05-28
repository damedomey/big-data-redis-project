package org.unice;

import org.unice.model.Client;
import org.unice.model.submodel.Address;
import org.unice.service.ClientService;

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
        // Create a new client
        Client client = new Client();
        client.setId("1");
        client.setLastname("AMEDOMEY");
        client.setFirstname(List.of("Roméo", "David"));
        client.setAddress(new Address(11L, "Avenue ", "06200", "Nice", "France"));

        try {
            ClientService.delete(client.getId());
            ClientService.save(client);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Get a client from database
        System.out.println(ClientService.getById("1"));;
    }
}
