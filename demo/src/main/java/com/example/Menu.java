package com.example;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private Api api = new Api();
    private Historial historial = new Historial(); // Instancia del historial

    // Lista de NameItem para manejar los nombres de países y monedas
    private List<NameItem> nameItems = List.of(
        new NameItem("Argentina", "Peso", "ARS"),
        new NameItem("Peru", "Sol", "PEN"),
        new NameItem("Vietnam", "Dong", "VND"),
        new NameItem("Corea del Sur", "Won", "KRW"),
        new NameItem("Japón", "Yen", "JPY"),
        new NameItem("China", "Yuan", "CNY"),
        new NameItem("Canadá", "Dólar", "CAD"),
        new NameItem("Estados Unidos", "Dólar", "USD"),
        new NameItem("Venezuela", "Bolívar", "VES"),
        new NameItem("México", "Peso", "MXN"),
        new NameItem("Unión Europea", "Euro", "EUR")
    );

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean continueConversion = true; // Inicializar la variable

        do {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Realizar una conversión");
            System.out.println("2. Ver historial de conversiones");
            System.out.println("3. Salir");

            String option = scanner.next();
            switch (option) {
                case "1":
                    realizarConversion(scanner);
                    break;
                case "2":
                    mostrarHistorial();
                    break;
                case "3":
                    continueConversion = false; // Salir del bucle cuando selecciona "Salir"
                    System.out.println("Gracias por usar el convertidor de monedas.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (continueConversion); // Usar la variable para controlar el ciclo
    }

    private void realizarConversion(Scanner scanner) {
        // Mostrar las monedas disponibles
        System.out.println("Monedas y países disponibles:");
        nameItems.forEach(item -> System.out.printf("%d. %s%n", nameItems.indexOf(item) + 1, item));

        // Obtener monedas base y destino con operadores ternarios
        String baseCurrency = getCurrencyInput(scanner, "base");
        String targetCurrency = getCurrencyInput(scanner, "convertir");

        // Obtener la cantidad a convertir
        double amount = getValidAmount(scanner);

        // Realizar la conversión y mostrar el resultado
        performConversion(baseCurrency, targetCurrency, amount);
    }

    private void mostrarHistorial() {
        System.out.println("Historial de conversiones:");
        historial.obtenerHistorialFormateado().forEach(System.out::println);
    }

    private String getCurrencyInput(Scanner scanner, String type) {
        return promptUntilValid(scanner, "Seleccione la moneda o país " + type + " (número): ",
            input -> isValidCurrencyInput(input) ? nameItems.get(Integer.parseInt(input) - 1).getCurrencyCode() : null);
    }

    private boolean isValidCurrencyInput(String input) {
        try {
            int currencyIndex = Integer.parseInt(input);
            return currencyIndex >= 1 && currencyIndex <= nameItems.size();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double getValidAmount(Scanner scanner) {
        return Double.parseDouble(promptUntilValid(scanner,
            "Introduzca la cantidad de dinero que quiere convertir: ",
            input -> isValidAmount(input) ? input : null));
    }

    private boolean isValidAmount(String input) {
        try {
            return Double.parseDouble(input) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void performConversion(String baseCurrency, String targetCurrency, double amount) {
        try {
            double exchangeRate = api.getExchangeRates(baseCurrency).getOrDefault(targetCurrency, 0.0);
            if (exchangeRate > 0) {
                double convertedAmount = amount * exchangeRate;
                System.out.printf("El resultado final es: %.2f %s (%s) equals %.5f %s (%s)%n",
                    amount, getCurrencyName(baseCurrency), baseCurrency,
                    convertedAmount, getCurrencyName(targetCurrency), targetCurrency);

                // Agregar la conversión al historial
                historial.agregarRegistro(baseCurrency, targetCurrency, amount, convertedAmount);
            } else {
                System.err.println("No se encontró la tasa de cambio.");
            }
        } catch (Exception e) {
            System.err.println("Error al obtener la tasa de cambio: " + e.getMessage());
        }
    }

    private String getCurrencyName(String currencyCode) {
        return nameItems.stream()
            .filter(item -> item.getCurrencyCode().equals(currencyCode))
            .map(NameItem::getCurrencyName)
            .findFirst()
            .orElse("Desconocido");
    }

    private String promptUntilValid(Scanner scanner, String prompt, Validator validator) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.next();
        } while (validator.validate(input) == null);
        return validator.validate(input);
    }

    @FunctionalInterface
    private interface Validator {
        String validate(String input);
    }

    public static void main(String[] args) {
        new Menu().start();
    }
}
