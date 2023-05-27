package org.unice.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String id;
    private String lastname;
    private String firstname;
    private String phone;
    private String address;

    @Override
    public String toString(){
        try {
            return (new ObjectMapper()).writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Failed to parse the client as string %s", e));
        }
    }

    public static Client fromString(String clientString){
        try {
            return (new ObjectMapper()).readValue(clientString, Client.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Failed to create client from given string %s \n%s", clientString, e));
        }
    }
}
