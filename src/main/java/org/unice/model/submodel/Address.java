package org.unice.model.submodel;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private Long number;
    private String street;
    private String postalCode;
    private String town;
    private String country;
}
