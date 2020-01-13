package com.example.customer.client;

import com.example.customer.dto.CustomerDto;
import com.example.customer.queue.RetryingRabbitListener;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

@Log4j2
@Service
public class CustomerRestClient {

    @Autowired
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(CustomerRestClient.class);


    public Optional<CustomerDto> findById(String id) {
        logger.info("Entering findById with id:" + id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer 1234567890qwertyuiopasdfghjklzxcvbnm");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> result = restTemplate.exchange("https://increase-transactions.herokuapp.com/clients/" + id, HttpMethod.GET, entity, String.class);

        CustomerDto customerDto = "null".equalsIgnoreCase(result.getBody()) ? null : new CustomerDto(result.getBody());

        return Optional.ofNullable(customerDto);

    }

}
