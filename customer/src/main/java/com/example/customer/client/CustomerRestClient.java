package com.example.customer.client;

import com.example.customer.dto.CustomerDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CustomerRestClient {

    @Autowired
    private RestTemplate restTemplate;

    public Optional<CustomerDto> findById(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer 1234567890qwertyuiopasdfghjklzxcvbnm");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> result = restTemplate.exchange("https://increase-transactions.herokuapp.com/clients/" + id, HttpMethod.GET, entity, String.class);

        CustomerDto customerDto = StringUtils.isBlank(result.getBody()) ? null : new CustomerDto(result.getBody());

        return Optional.of(customerDto);

    }

}
