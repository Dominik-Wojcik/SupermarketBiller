package com.codecool;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

public class Biller {
    private final Map<String, TreeMap<Integer, BigDecimal>> productsPrices;
    private final Map<String, Integer> productsInBasket;

    public Biller() {
        productsPrices = new HashMap<>();
        productsInBasket = new HashMap<>();
    }


    public void setPrices(String arg) {
        List<String> file = getFileAsArray(arg);
        for (int line = 4; line < file.size(); ) {
            String barcode = file.get(line++);
            int amount = Integer.parseInt(file.get(++line));
            BigDecimal price = getBigDecimalFromString(file.get(++line));
            line++;
            if (productsPrices.get(barcode) == null) {
                TreeMap<Integer, BigDecimal> treeMap = new TreeMap<>(Collections.reverseOrder());
                treeMap.put(amount, price);
                productsPrices.put(barcode, treeMap);
            } else {
                productsPrices.get(barcode).put(amount, price);
            }
        }
    }

    private BigDecimal getBigDecimalFromString(String s) {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);
        try {
            return (BigDecimal) decimalFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public void setBasket(String arg) {
        List<String> file = getFileAsArray(arg);
        for (String item : file) {
            if (productsInBasket.get(item) == null) {
                productsInBasket.put(item, 1);
            } else {
                int amount = productsInBasket.get(item);
                productsInBasket.put(item, ++amount);
            }
        }
    }

    public String getBill() {
        System.out.println(productsPrices);
        System.out.println(productsInBasket);
        BigDecimal sum = BigDecimal.ZERO;
        for (String item : productsInBasket.keySet()) {
            for (String product : productsPrices.keySet()) {
                if (item.equals(product)) {
                    int amountInBasket = productsInBasket.get(item);
                    for (int amountForDiscount : productsPrices.get(product).keySet()) {
                        int amountOnSpecificPrice = amountInBasket / amountForDiscount;
                        BigDecimal price;
                        if (amountInBasket % amountForDiscount == 0){
                            price = productsPrices.get(product).get(amountForDiscount);
                            amountInBasket = 0;
                        }else{
                            amountInBasket -= amountOnSpecificPrice;
                            price = productsPrices.get(product).get(amountForDiscount);
                        }
//                        BigDecimal price = productsPrices.get(product).get(amountForPrice);
                        sum = sum.add(price.multiply(BigDecimal.valueOf(amountOnSpecificPrice)));

                    }

                }
            }
        }
        return "Total price is: " + sum.toString();
    }

    private List<String> getFileAsArray(String arg) {
        File file = new File(arg);
        List<String> fileList = new ArrayList<>();
        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNext()) {
                fileList.add(fileScanner.next().replace(",", ""));
            }
            return fileList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileList;
    }
}
