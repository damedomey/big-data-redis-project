package org.unice;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.unice.model.BankAgent;
import org.unice.model.Operation;
import org.unice.service.BankAgentService;
import org.unice.service.OperationService;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BankAgentTest extends TestCase{

    public void test1CreateBankAgent(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = Paths.get("src", "main", "resources", "bankagents.json").toFile();
            List<BankAgent> bankAgents = objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, BankAgent.class));
            for (BankAgent bankAgent : bankAgents){
                BankAgentService.delete(bankAgent.getId());
                BankAgentService.save(bankAgent);
                }
            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }

            assertTrue(true);
        }

    public void test2GetAllBankAgents(){
        try{
            List<BankAgent> bankAgents = BankAgentService.getAll();
            assertEquals(200, bankAgents.size());
        }catch(Exception e){
        System.out.println(e);
        e.printStackTrace();
        throw new RuntimeException();
        }
    }
    public void test3GetByID() throws RuntimeException{
        try {
            BankAgent bankAgent = BankAgentService.getById(47);
            assertEquals( "Matilde Fendlow", bankAgent.getName());
        }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }
    }

    public void test4GetByName() throws RuntimeException{
        try{
            List<BankAgent> bankAgents = BankAgentService.getByName("Julie Piwell");
            assertEquals(1, bankAgents.size() );
            assertEquals(111, bankAgents.get(0).getId());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    
    public void test5GetByBankId() throws RuntimeException{
        try{
            List<BankAgent> bankAgents = BankAgentService.getByBankId(35);
            assertEquals(8, bankAgents.size());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void test6UpdateAll(){
        BankAgent previousAgent = BankAgentService.getById(17);
        BankAgentService.updateValueForAllBankAgents("name", "Marshall Bruce Mathers");
        BankAgent currentBankAgent = BankAgentService.getById(17);
        assertNotSame(previousAgent, currentBankAgent);
        assertEquals("Marshall Bruce Mathers", currentBankAgent.getName());
    }

    public void test7Delte(){
        BankAgentService.delete(23);
        assertEquals(199, BankAgentService.getAll().size());
    }

}
