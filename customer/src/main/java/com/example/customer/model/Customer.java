package com.example.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String job;
    private String country;
    private String address;
    private String zipCode;
    private String phone;
    @Builder.Default
    private List<Transactions> transactions = new ArrayList<>();

}
