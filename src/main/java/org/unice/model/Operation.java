package org.unice.model;

import java.time.LocalDate;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Operation extends JsonSerializable{
    private int id;
    private LocalDate operationTime;
    private String title;
    private int operationProfit;
    private int operationDebt;
    
    public static Operation fromJson(String json){
        return fromJson(json, Operation.class);
    }
}
