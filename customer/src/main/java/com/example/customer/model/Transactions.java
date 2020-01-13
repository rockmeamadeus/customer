package com.example.customer.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Builder
@Data
public class Transactions {

    private String idTransaccion;
    private Long monto;
    private String tipo;
    private LocalDate fechaPago;
    private String moneda;

}
