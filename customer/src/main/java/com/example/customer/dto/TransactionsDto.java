package com.example.customer.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class TransactionsDto {

    private String idTransaccion;
    private Long monto;
    private String tipo;
    private LocalDate fechaPago;
    private String moneda;

}
