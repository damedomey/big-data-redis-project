package org.unice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.unice.model.submodel.Address;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String id;
    private String lastname;
    private List<String> firstname;
    private String phone;
    private Address address;

    public Map<String, String> toMap() {
        try {
            ObjectMapper objectMapper = new ObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String jsonString = objectMapper.writeValueAsString(this);
            return objectMapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert Client object to Map", e);
        }
    }

    public static Client fromMap(Map<String, String> clientMap){
        try {
            Optional<List<String>> firstname = Optional.empty();
            if (clientMap.containsKey("firstname")){
                String f = clientMap.get("firstname");
                String cleanedString = f.substring(1, f.length() - 1);

                firstname = Optional.of(List.of(cleanedString.split(",")));
                clientMap.remove("firstname");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String jsonString = objectMapper.writeValueAsString(clientMap);
            Client client = objectMapper.readValue(jsonString, Client.class);

            firstname.ifPresent(client::setFirstname);

            return client;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create Client object from Map", e);
        }
    }
}
