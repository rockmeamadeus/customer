package com.example.customer.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String job;
    private String country;
    private String address;
    private String zipCode;
    private String phone;

    public CustomerDto(String customer) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(customer);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("An error was ocurred while parsing the json response: " + e.getMessage());
        }
        id = root.path("id").asText();
        email = root.path("email").asText();
        firstName = root.path("first_name").asText();
        lastName = root.path("last_name").asText();
        job = root.path("job").asText();
        country = root.path("country").asText();
        address = root.path("address").asText();
        zipCode = root.path("zip_code").asText();
        phone = root.path("phone").asText();

    }

}
