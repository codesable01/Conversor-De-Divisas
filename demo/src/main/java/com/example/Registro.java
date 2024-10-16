package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Registro(String baseCurrency, String targetCurrency, double amount, double convertedAmount, String timestamp) {
    
    // Constructor que incluye la creación de la marca de tiempo al momento de la conversión
    public Registro(String baseCurrency, String targetCurrency, double amount, double convertedAmount) {
        this(baseCurrency, targetCurrency, amount, convertedAmount, 
             LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    // Método toString 
    @Override
    public String toString() {
        return "[%s] %.5f %s -> %.5f %s".formatted(timestamp, amount, baseCurrency, convertedAmount, targetCurrency);
    }
}
