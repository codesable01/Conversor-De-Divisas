package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Historial {
    private final List<Registro> registros = new ArrayList<>();

    // Método para agregar un nuevo registro
    public void agregarRegistro(String baseCurrency, String targetCurrency, double amount, double convertedAmount) {
        registros.add(new Registro(baseCurrency, targetCurrency, amount, convertedAmount));
    }

    // Usamos streams para devolver una lista de strings con el formato deseado
    public List<String> obtenerHistorialFormateado() {
        return registros.isEmpty() 
            ? List.of("No hay conversiones en el historial.") 
            : registros.stream()
                       .map(Registro::toString)  // Usar el método toString de Registro
                       .collect(Collectors.toList());
    }
}
