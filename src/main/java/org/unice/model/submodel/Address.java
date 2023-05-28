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

    /**
     * This constructor is used by jackson package to convert a string into Address
     *
     * When the data is serialized and stored in database, the default constructor cannot
     * revert the process to get Address() from the string. This constructor is specially
     * adapt from this case.
     * This is a possible solution to solve this issue instead of custom Serializer/Deserializer
     * @param address
     */
    public Address(String address){
        // Remove leading and trailing braces
        String cleanedString = address.substring(1, address.length() - 1);

        String[] keyValuePairs = cleanedString.split(",");

        for (String keyValuePair : keyValuePairs) {
            String[] keyValue = keyValuePair.split("=");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            switch (key){
                case "number":
                    this.number = Long.parseLong(value);
                    break;
                case "street":
                    this.street = value.trim();
                    break;
                case "postalCode":
                    this.postalCode = value.trim();
                    break;
                case "town":
                    this.town = value.trim();
                    break;
                case "country":
                    this.country = value.trim();
                    break;
            }
        }
    }
}
