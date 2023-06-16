package org.unice.model;

import lombok.*;
import org.unice.model.submodel.Address;
import org.unice.model.BankAccount;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Customer extends JsonSerializable {
    private int id;
    private String lastname;
    private List<String> firstname;
    private String phone;
    private Address address;
    private List<BankAccount> accounts;

    public static Customer fromJson(String json){
        return fromJson(json, Customer.class);
    }
}
