package org.unice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.unice.model.Agency;
import org.unice.service.AgencyService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AgencyTest extends TestCase {
    
    public void test1CreateAgencies(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = Paths.get("src", "main", "resources", "agencies.json").toFile();
            List<Agency> agencies = objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Agency.class));
            for (Agency agency : agencies){
                AgencyService.delete(agency.getId());
                AgencyService.save(agency);
                }
            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }

            assertTrue(true);
    }

    public void test2GetAllAgencies() throws RuntimeException{
        try{
            List<Agency> agencies = AgencyService.getAll();
            assertEquals(50, agencies.size());
        }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
                throw new RuntimeException();
            }
    }

    public void test3GetByID() throws RuntimeException{
        try {
            Agency agency = AgencyService.getByID(4);
            assertEquals( "Peoples Bancorp Inc.", agency.getName());
        }catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
                throw new RuntimeException();
            }
    }

    public void test4GetByName() throws RuntimeException{
        try{
            List<Agency> agencies = AgencyService.getByName("Potbelly Corporation");
            assertEquals(1, agencies.size() );
            assertEquals(8, agencies.get(0).getId());
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
