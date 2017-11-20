package com.researchs.pdi.utils;

/**
 * Created by kristian.pereira on 17/04/2017.
 */
public class MostPopular {

    private int value;
    private int quantity;

    public MostPopular(int value, int quantity) {
        this.value = value;
        this.quantity = quantity;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
