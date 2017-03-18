package com.example.sujay.stocks;

/**
 * Created by Sujay on 3/11/2017.
 */

public class Stock {
    private String stock_name;
    private String stock_symbol;
    private double stock_value;
    private double stock_percentage;
    private double stock_change;

    public Stock(String stock_name,String stock_symbol,double stock_value,double stock_percentage,double stock_change) {
        this.stock_name = stock_name;
        this.stock_symbol = stock_symbol;
        this.stock_value = stock_value;
        this.stock_percentage = stock_percentage;
        this.stock_change = stock_change;
    }

    public String getstock_name() {
        return stock_name;
    }

    public String getstock_symbol() {
        return stock_symbol;
    }

    public double getstock_value() {
        return stock_value;
    }

    public double getstock_percentage() {
        return stock_percentage;
    }

    public double getstock_change() {
        return stock_change;
    }

    @Override
    public String toString() {
        return stock_name + " (" + stock_symbol+ "), " + stock_value + stock_percentage + stock_change;
    }
}
