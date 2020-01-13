package com.example.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
public class BalanceDto {

     private Map<String, Long> disponible = new HashMap() {{
        put("ARS", 0L);
        put("USD", 0L);
    }};

     private Map<String, Long> noDisponible = new HashMap() {{
        put("ARS", 0L);
        put("USD", 0L);
    }};

}
