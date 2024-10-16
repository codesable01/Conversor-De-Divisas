package com.example;

public class NameItem {
    private String countryName;
    private String currencyName;
    private String currencyCode;

    public NameItem(String countryName, String currencyName, String currencyCode) {
        this.countryName = countryName;
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public String toString() {
        return countryName + " - " + currencyName;
    }
}
