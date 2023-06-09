package org.unice;

import org.unice.model.Operation;

import junit.framework.TestCase;

public class OperationTest extends TestCase{
    
    public void testCreateClient(){

        Operation o = new Operation();
        System.out.println(o);
        assertTrue(true);
    }
}
