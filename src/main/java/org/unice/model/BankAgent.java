package org.unice.model;

import java.time.LocalDate;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BankAgent extends JsonSerializable{
    private int id;
    private String name;
    private String hiringDate;  // This should be LocalDate but I can not make it work so far...

    public static BankAgent fromJson(String json){
        return fromJson(json, BankAgent.class);
    }
}
