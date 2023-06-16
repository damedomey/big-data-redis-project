package org.unice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.unice.model.Operation;
import org.unice.service.OperationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OperationTest extends TestCase{
    
    public void test1CreateOperation(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = Paths.get("src", "main", "resources", "operations.json").toFile();
            List<Operation> operations = objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Operation.class));
            for (Operation operation : operations){
                OperationService.delete(operation.getId());
                OperationService.save(operation);
                }
            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }

            assertTrue(true);
        }

    public void test2GetAllOperations() throws RuntimeException{
        try{
            List<Operation> operations = OperationService.getAll();
            assertEquals(200, operations.size());
        }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
                throw new RuntimeException();
            }
    }

    public void test3GetByID() throws RuntimeException{
        try {
            Operation operation = OperationService.getById(68);
            assertEquals( "Tortillas - Flour, 8", operation.getTitle());
        }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }
    }

    public void test4GetByTitle() throws RuntimeException{
        try{
            List<Operation> operations = OperationService.getByTitle("Whmis Spray Bottle Graduated");
            assertEquals(1, operations.size() );
            assertEquals(69, operations.get(0).getId());
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    public void test5GetByBeneficiary() throws RuntimeException{
        try{
            List<Operation> operations = OperationService.getByBeneficiary("PL83 2635 2984 1984 1033 2505 4781");
            assertEquals(1, operations.size() );
            assertEquals(60, operations.get(0).getId());
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void test6GetByPayer() throws RuntimeException{
        try{
            List<Operation> operations = OperationService.getByPayer("AE59 5507 5487 9702 2651 839");
            assertEquals(1, operations.size() );
            assertEquals(64, operations.get(0).getId());
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void test7UpdateAll() {
        Operation previousOperation = OperationService.getById(10);
        OperationService.updateValueForAllOperations("title", "Hello world");
        Operation currentOperation = OperationService.getById(10);
        assertNotSame(previousOperation, currentOperation);
        assertEquals("Hello world", currentOperation.getTitle());
    }

    public void test8Delete() {
        OperationService.delete(13);
        assertEquals(199, OperationService.getAll().size());
    }
}
