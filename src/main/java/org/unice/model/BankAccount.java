package org.unice.model;

import java.util.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BankAccount extends JsonSerializable{
    private int id;
    private String accountNumber;
    private int customerId;
    private int accountBalance;
    private String accountType;
    private Date creationDate;
    private Date closeDate;

    public static BankAccount fromJson(String json){
        return fromJson(json, BankAccount.class);
    }
}
