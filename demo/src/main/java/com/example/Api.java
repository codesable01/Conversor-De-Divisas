package com.example;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Api {
    private static final String API_KEY = "85dca733808e2e1d4edad98b";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public Map<String, Double> getExchangeRates(String fromCurrency) throws Exception {
        String urlStr = API_URL + fromCurrency;
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Error: " + connection.getResponseCode());
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            JsonObject jsonResponse = JsonParser.parseString(responseBuilder.toString()).getAsJsonObject();
            if (!jsonResponse.get("result").getAsString().equals("success")) {
                throw new RuntimeException("Error en la respuesta de la API");
            }

            JsonObject ratesJson = jsonResponse.getAsJsonObject("conversion_rates");

            // Usa TypeToken para especificar el tipo exacto del Map
            Type type = new TypeToken<Map<String, Double>>(){}.getType();
            return new Gson().fromJson(ratesJson, type);
        } finally {
            connection.disconnect();
        }
    }
}

