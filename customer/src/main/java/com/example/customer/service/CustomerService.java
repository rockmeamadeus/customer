package com.example.customer.service;

import com.example.customer.client.CustomerRestClient;
import com.example.customer.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRestClient customerRestClient;

    public Optional<CustomerDto> findById(String id) {

        return customerRestClient.findById(id);
    }

}
