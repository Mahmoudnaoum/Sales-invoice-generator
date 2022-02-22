package com.company.invoice;

public class Invoice {
    private int invoiceNumber;
    private String invoiceDate;
    private String customerName;
    private Item [] items;

    public Invoice(){}

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getTotalAmount() {
        int totalAmount = 0;
        for (int i = 0; i < this.items.length; i++){
            if( items[i] == null) {break;}
            totalAmount += this.items[i].getItemTotal();
        }
        return totalAmount;
    }

    public Item[] getItems() {
        return items;
    }

    public String[][] getItemsArray() {
        if (items == null) {
            return null;
        }
        String [][] itemsArray = new String[this.items.length][5];
        for (int i = 0; i < this.items.length; i++) {
            itemsArray[i][0] = String.valueOf( i + 1 );
            itemsArray[i][1] = this.items[i].getItemName();
            itemsArray[i][2] = String.valueOf( this.items[i].getItemPrice() );
            itemsArray[i][3] = String.valueOf( this.items[i].getCount() );
            itemsArray[i][4] = String.valueOf( this.items[i].getItemTotal() );
        }
        return itemsArray;
    }
    public String[] getInvoiceArray() {
        String[] invoiceArray = new String[4];
        invoiceArray[0] = String.valueOf( getInvoiceNumber() );
        invoiceArray[1] = String.valueOf( getInvoiceDate() );
        invoiceArray[2] = String.valueOf( getCustomerName() );
        invoiceArray[3] = String.valueOf( getTotalAmount() );
        return invoiceArray;
    }

}
