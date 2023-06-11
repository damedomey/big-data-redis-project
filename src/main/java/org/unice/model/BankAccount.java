package org.unice.model;

import java.time.LocalDate;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BankAccount extends JsonSerializable{
    private int id;
    private int customerId;
    private int accountBalance;
    private String accountType;
    private String creationDate; // Those should be LocalDate but I can not make it work so far...
    private String closeDate;

    public static BankAccount fromJson(String json){
        return fromJson(json, BankAccount.class);
    }
}
