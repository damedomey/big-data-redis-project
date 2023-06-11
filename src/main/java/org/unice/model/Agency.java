package org.unice.model;

import org.unice.model.submodel.Address;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
/**
     * This model represents a bank agency.
     */
public class Agency extends JsonSerializable{
    private int id;
    private String name;
    private Address address;

    public static Agency fromJson(String json){
        return fromJson(json, Agency.class);
    }
}
