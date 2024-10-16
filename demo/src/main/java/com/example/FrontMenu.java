package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class FrontMenu extends JFrame {

    private Api currencyAPI;  // Instancia de la API
    private Historial historial;  // Instancia del historial
    private JComboBox<NameItem> fromCurrencyComboBox;
    private JComboBox<NameItem> toCurrencyComboBox;
    private JTextField amountField;
    private JTextField resultField;

    // Monedas soportadas
    private final NameItem[] nameItems = {
        new NameItem("Argentina", "Peso argentino", "ARS"),
        new NameItem("Perú", "Sol peruano", "PEN"),
        new NameItem("Brasil", "Real brasileño", "BRL"),
        new NameItem("Colombia", "Peso colombiano", "COP"),
        new NameItem("Rusia", "Rublo ruso", "RUB"),
        new NameItem("Estados Unidos", "Dólar estadounidense", "USD"),
        new NameItem("Canadá", "Dólar canadiense", "CAD"),
        new NameItem("Chile", "Peso chileno", "CLP"),
        new NameItem("China", "Yuan chino", "CNY"),
        new NameItem("Vietnam", "Dong vietnamita", "VND"),
        new NameItem("Corea del Sur", "Won surcoreano", "KRW"),
        new NameItem("Venezuela", "Bolívar venezolano", "VES"),
        new NameItem("México", "Peso Mexicano", "MXN"),
        new NameItem("Japón", "Yen japonés", "JPY")
    };

    public FrontMenu() {
        currencyAPI = new Api();  // Inicializa la API
        historial = new Historial();  // Inicializa el historial

        // Configurar el layout de la ventana principal
        setTitle("Conversor de Monedas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Crear los componentes de la UI
        fromCurrencyComboBox = new JComboBox<>(nameItems);
        toCurrencyComboBox = new JComboBox<>(nameItems);
        amountField = new JTextField(10);
        resultField = new JTextField(10);
        resultField.setEditable(false);  // Solo mostrar resultados

        JButton convertButton = new JButton("Convertir");
        convertButton.addActionListener(this::convertCurrency);  // Usar expresión lambda

        JButton historialButton = new JButton("Ver Historial");
        historialButton.addActionListener(e -> mostrarHistorial());

        // Añadir componentes al panel con GridBagLayout
        gbc.fill = GridBagConstraints.HORIZONTAL; // Asegura que los componentes se expandan horizontalmente
        gbc.weightx = 1.0; // Los componentes pueden expandirse en ancho
        gbc.weighty = 0.1; // Control vertical
        
        // Componentes del formulario
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("De:"), gbc);

        gbc.gridx = 1;
        add(fromCurrencyComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("A:"), gbc);

        gbc.gridx = 1;
        add(toCurrencyComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Monto:"), gbc);

        gbc.gridx = 1;
        add(amountField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(convertButton, gbc);

        gbc.gridy = 4;
        add(historialButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Resultado:"), gbc);

        gbc.gridx = 1;
        add(resultField, gbc);

        // Ajustes para asegurar que el panel escale cuando la ventana cambie de tamaño
        gbc.weighty = 1.0; // Para que los componentes ocupen espacio vertical
    }

    // Método para convertir la moneda
    private void convertCurrency(ActionEvent e) {
        try {
            NameItem fromCurrencyItem = (NameItem) fromCurrencyComboBox.getSelectedItem();
            NameItem toCurrencyItem = (NameItem) toCurrencyComboBox.getSelectedItem();

            if (fromCurrencyItem == null || toCurrencyItem == null) {
                showAlert("Seleccione las monedas.");
                return;
            }

            String fromCurrencyCode = fromCurrencyItem.getCurrencyCode();
            String toCurrencyCode = toCurrencyItem.getCurrencyCode();

            double amount = Double.parseDouble(amountField.getText());

            // Obtener las tasas de conversión
            Map<String, Double> rates = currencyAPI.getExchangeRates(fromCurrencyCode);

            // Si existe la tasa de cambio, realiza la conversión
            if (rates.containsKey(toCurrencyCode)) {
                double conversionRate = rates.get(toCurrencyCode);
                double convertedAmount = amount * conversionRate;

                String resultado = createConversionMessage(amount, fromCurrencyItem, conversionRate, toCurrencyItem);
                showAlert(resultado);

                // Agregar el registro al historial
                historial.agregarRegistro(fromCurrencyCode, toCurrencyCode, amount, convertedAmount);
            } else {
                showAlert("No se encontró la tasa de cambio para " + toCurrencyItem.getCurrencyName());
            }
        } catch (NumberFormatException ex) {
            showAlert("Ingrese una cantidad válida.");
        } catch (Exception ex) {
            showAlert("Error: " + ex.getMessage());
        }
    }

    // Método para crear un mensaje de conversión
    private String createConversionMessage(double amount, NameItem fromCurrencyItem, double conversionRate, NameItem toCurrencyItem) {
        double result = amount * conversionRate;
        resultField.setText(result < 1 ? String.format("%.5f", result) : String.format("%.2f", result));

        return String.format("%.2f %s equivalen a %s %s",
                amount,
                fromCurrencyItem.getCurrencyName(),
                result < 1 ? String.format("%.5f", result) : String.format("%.2f", result),
                toCurrencyItem.getCurrencyName());
    }

    // Método para mostrar alertas
    private void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    // Método para mostrar el historial
    private void mostrarHistorial() {
        JTextArea historialArea = new JTextArea(10, 30);
        historialArea.setEditable(false);

        // Obtener el historial formateado
        historial.obtenerHistorialFormateado().forEach(entry -> historialArea.append(entry + "\n"));

        JScrollPane scrollPane = new JScrollPane(historialArea);
        JFrame historialFrame = new JFrame("Historial de Conversiones");
        historialFrame.add(scrollPane);
        historialFrame.setSize(400, 300);
        historialFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrontMenu().setVisible(true));  // Usar lambda en vez de Runnable
    }
}
