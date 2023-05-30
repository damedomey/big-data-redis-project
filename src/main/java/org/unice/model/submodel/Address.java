package org.unice.model.submodel;

import lombok.*;

@Getter
@Setter
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
