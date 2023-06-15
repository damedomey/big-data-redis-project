package org.unice.model;

import java.time.Instant;
import java.util.Date;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Operation extends JsonSerializable{
    private int id;
    private Date operationDate = Date.from(Instant.now());
    private String title;
    private int operationDebit;
    private int operationProfit;
    
    public static Operation fromJson(String json){
        return fromJson(json, Operation.class);
    }

    public Operation(int id, String title, int operationDebit, int operationProfit){
        this.id = id;
        this.title = title;
        this.operationDebit = operationDebit;
        this.operationProfit = operationProfit;
    }
}
