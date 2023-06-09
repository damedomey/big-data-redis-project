package org.unice.model;

import lombok.*;
import org.unice.model.submodel.Address;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Customer extends JsonSerializable {
    private String id;
    private String lastname;
    private List<String> firstname;
    private String phone;
    private Address address;

    public static Customer fromJson(String json){
        return fromJson(json, Customer.class);
    }
}
