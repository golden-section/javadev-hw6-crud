package org.gs.models;

import java.math.BigDecimal;

public class ProjectPrices {
    private final String name;
    private final BigDecimal price;

    public ProjectPrices(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProjectPrices{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}