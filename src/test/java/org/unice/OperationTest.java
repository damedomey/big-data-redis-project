package org.unice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import junit.framework.TestCase;
import org.unice.model.Operation;
import org.unice.service.OperationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

public class OperationTest extends TestCase{
    
    public void testCreateOperation(){
        ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.registerModule(new JavaTimeModule());
        // SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        // objectMapper.setDateFormat(df);
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

    public void testGetAllOperations() throws RuntimeException{
        try{
            List<Operation> operations = OperationService.getAll();
            assertEquals(100, operations.size());
        }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
                throw new RuntimeException();
            }
    }

    public void testGetByID() throws RuntimeException{
        try {
            Operation operation = OperationService.getByID(68);
            assertEquals( "Coffee Decaf Colombian", operation.getTitle());
        }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
                throw new RuntimeException();
            }
    }

    public void testGetByTitle() throws RuntimeException{
        try{
            List<Operation> operations = OperationService.getByTitle("Potato Dill Pickle");
            assertEquals(1, operations.size() );
            assertEquals(4, operations.get(0).getId());
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
