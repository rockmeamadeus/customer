package com.example.customer.service;

import com.example.customer.client.CustomerRestClient;
import com.example.customer.dto.BalanceDto;
import com.example.customer.model.Customer;
import com.example.customer.dto.CustomerDto;
import com.example.customer.dto.TransactionsDto;
import com.example.customer.model.Transactions;
import com.example.customer.repository.CustomerRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.groupingBy;

@Service
public class CustomerService {

    @Autowired
    private CustomerRestClient customerRestClient;

    @Autowired
    private CustomerRepository customerRepository;

    public Optional<CustomerDto> findById(String id) {

        Optional<Customer> customerRetrieved = customerRepository.findById(id);

        CustomerDto customerDto = null;

        if (customerRetrieved.isPresent()) {

            Customer customer = customerRetrieved.get();

            customerDto = CustomerDto.builder()
                    .id(customer.getId())
                    .address(customer.getAddress())
                    .country(customer.getCountry())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .job(customer.getJob())
                    .email(customer.getEmail())
                    .zipCode(customer.getZipCode())
                    .phone(customer.getPhone()).build();
        }

        return Optional.ofNullable(customerDto);
    }


    public void updateCustomer(String customer) {


        JSONObject jsonObject = new JSONObject(customer);

        String customerId = jsonObject.getJSONObject("footer").get("idCliente").toString();

        Optional<Customer> customerRetrieved = customerRepository.findById(customerId);

        Customer theCustomer;

        if (customerRetrieved.isPresent()) {

            theCustomer = customerRetrieved.get();

        } else {
            Optional<CustomerDto> result = customerRestClient.findById(customerId);

            if (result.isPresent()) {
                CustomerDto customerDto = result.get();

                theCustomer = Customer.builder()
                        .address(customerDto.getAddress())
                        .country(customerDto.getCountry())
                        .email(customerDto.getEmail())
                        .firstName(customerDto.getFirstName())
                        .job(customerDto.getJob())
                        .lastName(customerDto.getLastName())
                        .zipCode(customerDto.getZipCode())
                        .phone(customerDto.getPhone())
                        .id(customerDto.getId())
                        .build();
            } else {
                throw new RuntimeException("Customer not found: " + customerId);
            }

        }

        JSONArray transactions = jsonObject.getJSONArray("transaccions");
        LocalDate fechaPago = LocalDate.parse(jsonObject.getJSONObject("footer").getString("fechaPago"),
                DateTimeFormatter.ofPattern("yyyyMMdd"));

        String moneda = jsonObject.getJSONObject("header").getString("moneda");

        for (int i = 0; i < transactions.length(); ++i) {
            JSONObject rec = transactions.getJSONObject(i);
            Transactions transaction = Transactions.builder()
                    .idTransaccion(rec.getString("idTransaccion"))
                    .monto(rec.getLong("monto"))
                    .tipo(rec.getString("tipo"))
                    .fechaPago(fechaPago)
                    .moneda(moneda)
                    .build();
            theCustomer.getTransactions().add(transaction);
        }

        customerRepository.save(theCustomer);

    }

    public Optional<List<TransactionsDto>> fetchAllTransactions(String id) {

        Optional<Customer> customer = customerRepository.findById(id);

        List<TransactionsDto> transactionsDtos = new ArrayList<>();

        if (customer.isPresent()) {
            transactionsDtos = customer.get()
                    .getTransactions()
                    .stream()
                    .map(transaction ->
                            TransactionsDto.builder()
                                    .idTransaccion(transaction.getIdTransaccion())
                                    .monto(transaction.getMonto())
                                    .tipo(transaction.getTipo())
                                    .fechaPago(transaction.getFechaPago())
                                    .build())
                    .collect(Collectors.toList());
        }


        return Optional.ofNullable(transactionsDtos);
    }

    public Optional<BalanceDto> getCustomerBalance(String id) {

        Optional<Customer> customer = customerRepository.findById(id);

        BalanceDto balanceDto = new BalanceDto();

        if (customer.isPresent()) {
            List<Transactions> transactions = customer.get().getTransactions().stream()
                    .filter(t -> "APROBADO".equals(t.getTipo()))
                    .collect(Collectors.toList());

            for (Transactions t : transactions) {

                if (!t.getFechaPago().isAfter(LocalDate.now())) {
                    if ("ARS".equals(t.getMoneda())) {
                        balanceDto.getDisponible().put("ARS", balanceDto.getDisponible().get("ARS") + t.getMonto());
                    } else {
                        balanceDto.getDisponible().put("USD", balanceDto.getDisponible().get("USD") + t.getMonto());
                    }

                } else {
                    if ("ARS".equals(t.getMoneda())) {
                        balanceDto.getNoDisponible().put("ARS", balanceDto.getDisponible().get("ARS") + t.getMonto());
                    } else {
                        balanceDto.getNoDisponible().put("USD", balanceDto.getDisponible().get("USD") + t.getMonto());
                    }
                }
            }

        }
        return Optional.ofNullable(balanceDto);

    }

}
