package org.unice.model;

import java.time.LocalDate;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Operation extends JsonSerializable{
    private int id;
    private String operationDate; // This should be LocalDate but I can not make it work so far...
    private String title;
    private int operationDebit;
    private int operationProfit;
    
    public static Operation fromJson(String json){
        return fromJson(json, Operation.class);
    }
}
