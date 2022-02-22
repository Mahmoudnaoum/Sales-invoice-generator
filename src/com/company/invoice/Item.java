package com.company.invoice;

public class Item {
    private String itemName;
    private int itemPrice;
    private int count;

    public Item() {}

    public String getItemName() {
        return itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public int getCount() {
        return count;
    }

    public int getItemTotal() {
        return itemPrice * count;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
