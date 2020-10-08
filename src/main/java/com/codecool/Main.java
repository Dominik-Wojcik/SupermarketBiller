package com.codecool;

public class Main
{
    public static void main( String[] args )
    {
        Biller biller = new Biller();
        biller.setPrices("src/main/resources/product_prices.csv");
        biller.setBasket("src/main/resources/basket.txt");
        System.out.println(biller.getBill());
    }
}
