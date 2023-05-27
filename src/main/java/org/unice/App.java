package org.unice;

import org.unice.model.Client;

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "STARTING THE APPLICATION" );

        test();

        System.out.println( "APPLICATION FINISHED" );
    }

    public static void test(){
        Client client = new Client();
        client.setId("1");
        client.setLastname("AMEDOMEY");

        //ClientService.save(client);
        System.out.println(client);
        Client client2 = Client.fromString(client.toString());
        System.out.println(client2);
    }
}
