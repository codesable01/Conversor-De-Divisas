package com.example;

import java.util.HashMap;
import java.util.Map;

public class Cache {
    private Map<String, Map<String, Double>> cache;

    public Cache() {
        cache = new HashMap<>();
    }

    public void storeRates(String baseCurrency, Map<String, Double> rates) {
        cache.put(baseCurrency, rates);
    }

    public Map<String, Double> getRates(String baseCurrency) {
        return cache.get(baseCurrency);
    }
}
