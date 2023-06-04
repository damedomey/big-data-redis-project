package org.unice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.unice.model.Client;
import org.unice.service.ClientService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Clear data and load data in json resources files
 */
public class DataLoadTest extends TestCase {

    public void testLoadClient(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = Paths.get("src", "main", "resources", "clients.json").toFile();
            List<Client> clients = objectMapper
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Client.class));
            for (Client client : clients) {
                ClientService.delete(client.getId());
                ClientService.save(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertTrue(true);
    }
}
