package org.example;

import org.example.model.User;
import org.example.service.UserService;

public class App
{
    public static void main( String[] args )
    {
        System.out.println( "STARTING THE APPLICATION" );

        test();

        System.out.println( "APPLICATION FINISHED" );
    }

    public static void test(){
        User user = new User();
        user.setId("1");
        user.setLastname("AMEDOMEY");

        UserService.save(user);
    }
}
