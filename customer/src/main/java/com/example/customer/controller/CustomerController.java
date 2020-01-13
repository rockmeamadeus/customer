package com.example.customer.controller;

import com.example.customer.dto.BalanceDto;
import com.example.customer.dto.CustomerDto;
import com.example.customer.dto.TransactionsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.customer.service.CustomerService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable String id) {
        logger.info("entering getCustomer with id: " + id);
        try {
            Optional<CustomerDto> customerDto = customerService.findById(id);

            if (customerDto.isPresent()) {
                return new ResponseEntity<>(customerDto.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("an error was ocurred while finding the customer " + id + ex.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionsDto>> getCustomerTransactions(@PathVariable String id) {
        logger.info("entering getCustomerTransactions with id: " + id);
        try {

            Optional<List<TransactionsDto>> transactionsDto = customerService.fetchAllTransactions(id);

            if (transactionsDto.isPresent()) {
                return new ResponseEntity<>(transactionsDto.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("an error was ocurred while finding the transactions " + id + ex.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/balance-transactions")
    public ResponseEntity<BalanceDto> getCustomerBalance(@PathVariable String id) {
        logger.info("entering getCustomerTransactions with id: " + id);
        try {

            Optional<BalanceDto> balanceDto = customerService.getCustomerBalance(id);

            if (balanceDto.isPresent()) {
                return new ResponseEntity<>(balanceDto.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("an error was ocurred while finding the transactions " + id + ex.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
