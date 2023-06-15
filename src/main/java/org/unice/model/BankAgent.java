package org.unice.model;

import java.time.Instant;
import java.util.Date;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BankAgent extends JsonSerializable{
    private int id;
    private String name;
    private int bankId;
    private Date hiringDate = Date.from(Instant.now());

    public static BankAgent fromJson(String json){
        return fromJson(json, BankAgent.class);
    }
}
