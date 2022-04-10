package com.company.InvoiceController;

import com.company.invoice.Invoice;
import com.company.invoice.Item;
import com.company.frame.InvoiceFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class FileOperations {

    JTable invoices;
    Component thisComponent;
    String currentDataDirectory;


    public FileOperations (JTable invoices, Component thisComponent, String currentDataDirectory) {
        this.invoices = invoices;
        this.thisComponent = thisComponent;
        this.currentDataDirectory = currentDataDirectory;
    }
    public void updateInvoices ( JTable invoices ) {
        this.invoices = invoices;
    }


    public Invoice[] loadFile(Invoice[] invoicesArray) {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        int result = fc.showOpenDialog(thisComponent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = fc.getSelectedFiles();
            File invoiceHeader;
            File invoiceLine;
            try {
                String file0 = files[0].getName();
                String file1 = files[1].getName();

                if (file0.equals("InvoiceHeader.csv")) {
                    invoiceHeader = files[0];
                    invoiceLine = files[1];
                } else if (file1.equals("InvoiceHeader.csv")) {
                    invoiceHeader = files[1];
                    invoiceLine = files[0];
                } else {
                    throw new IOException();
                }
                String[][] invoiceHeaderCsv = loadInvoiceHeader(invoiceHeader.getPath());
                String[][] invoiceLineCsv = loadInvoiceLine(invoiceLine.getPath());
                invoicesArray = updateInvoicesArray(invoiceHeaderCsv, invoiceLineCsv);
                currentDataDirectory = invoiceHeader.getParent() + File.separatorChar;
                Invoice[] finalInvoicesArray = invoicesArray;
                return finalInvoicesArray;
//                renderInvoiceTable(invoicesArray);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(thisComponent, "Please select InvoiceHeader.csv and InvoiceLine.csv files", "Error",
                        JOptionPane.ERROR_MESSAGE);

            }



        }
        return null;
    }

    public void saveFile(Invoice[] invoicesArray) {

        String invoiceHeaderCSV = "";
        for (Invoice value : invoicesArray) {
            String[] invoice = value.getInvoiceArray();

            invoiceHeaderCSV = invoiceHeaderCSV + invoice[0];
            invoiceHeaderCSV = invoiceHeaderCSV + ",";
            invoiceHeaderCSV = invoiceHeaderCSV + invoice[1];
            invoiceHeaderCSV = invoiceHeaderCSV + ",";
            invoiceHeaderCSV = invoiceHeaderCSV + invoice[2];
            invoiceHeaderCSV = invoiceHeaderCSV + "\r\n";

        }
        File invoiceHeader = new File(currentDataDirectory  + "InvoiceHeader.csv");
        try {
            FileWriter writer = new FileWriter(invoiceHeader);
            writer.write(invoiceHeaderCSV);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String invoiceLineCSV = "";
        for (int i = 0; i < invoicesArray.length; i++) {
            String[][] items = invoicesArray[i].getItemsArray();

            for (int j = 0; j < items.length; j++) {
                invoiceLineCSV = invoiceLineCSV + invoicesArray[i].getInvoiceNumber();
                invoiceLineCSV = invoiceLineCSV + ",";
                invoiceLineCSV = invoiceLineCSV + items[j][1];
                invoiceLineCSV = invoiceLineCSV + ",";
                invoiceLineCSV = invoiceLineCSV + items[j][2];
                invoiceLineCSV = invoiceLineCSV + ",";
                invoiceLineCSV = invoiceLineCSV + items[j][3];
                invoiceLineCSV = invoiceLineCSV + "\r\n";
            }
        }
        File invoiceLine = new File(currentDataDirectory  + "InvoiceLine.csv");
        try {
            FileWriter writer = new FileWriter(invoiceLine);
            writer.write(invoiceLineCSV);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String[][] loadInvoiceHeader(String path) {
        if (path.equals("")) {
            path = "./data/InvoiceHeader.csv";
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int size = 0;
        try {
            size = fis.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[size];
        try {
            fis.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileString = new String(b);
        String [] fileLines = fileString.split("\\r?\\n");
        String [][] invoicesCSV = new String[fileLines.length][3];
        for (int i = 0; i < fileLines.length; i++){
            String [] row = fileLines[i].split(",");
            System.arraycopy(row, 0, invoicesCSV[i], 0, row.length);
        }
        return  invoicesCSV;
    }
    public String[][] loadInvoiceLine(String path) {
        if (path.equals("")) {
            path = "./data/InvoiceLine.csv";
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int size = 0;
        try {
            size = fis.available();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[size];
        try {
            fis.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileString = new String(b);
        String [] fileLines = fileString.split("\\r?\\n");
        String [][] invoicesItemsCSV = new String[fileLines.length][4];
        for (int i = 0; i < fileLines.length; i++){
            String [] row = fileLines[i].split(",");
            System.arraycopy(row, 0, invoicesItemsCSV[i], 0, row.length);
        }
        return  invoicesItemsCSV;
    }

    public Invoice[] updateInvoicesArray(String[][] invoiceHeader, String[][] invoiceLine) {
        Invoice[] invoicesArray = new Invoice[ invoiceHeader.length ];
        for (int i = 0; i < invoicesArray.length; i++) {

            int itemsCount = 0;
            for (int j = 0; j < invoiceLine.length; j++) {
                if (Integer.parseInt(invoiceLine[j][0]) == i + 1) {
                    itemsCount++;
                } else if (Integer.parseInt(invoiceLine[j][0]) > i + 1) {
                    break;
                }
            }
            Item[] items = new Item[itemsCount];
            int itemIndex = 0;
            for (int j = 0; j < invoiceLine.length; j++) {
                if (Integer.parseInt(invoiceLine[j][0]) == i + 1) {
                    items[itemIndex] = new Item();
                    items[itemIndex].setItemName(invoiceLine[j][1]);
                    items[itemIndex].setItemPrice(Integer.parseInt(invoiceLine[j][2]));
                    items[itemIndex].setCount(Integer.parseInt(invoiceLine[j][3]));
                    itemIndex++;
                } else if (Integer.parseInt(invoiceLine[j][0]) > i + 1) {
                    break;
                }
            }
            invoicesArray[i] = new Invoice();
            invoicesArray[i].setInvoiceNumber(i+1);
            invoicesArray[i].setInvoiceDate(invoiceHeader[i][1]);
            invoicesArray[i].setCustomerName(invoiceHeader[i][2]);
            invoicesArray[i].setItems(items);

        }

        return invoicesArray;
    }

    private void renderInvoiceTable(Invoice[] invoicesArray) {
        String[][] invoicesData = new String[invoicesArray.length][4];
        for (int i = 0; i < invoicesArray.length; i++) {
            invoicesData[i] = invoicesArray[i].getInvoiceArray();
        }

        String[] invoicesColumn ={"No.","Date","Customer","Total"};
        DefaultTableModel model = new DefaultTableModel(invoicesData, invoicesColumn);
        this.invoices.setModel( model );
    }



}
