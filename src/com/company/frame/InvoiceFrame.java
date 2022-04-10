package com.company.frame;

import com.company.InvoiceController.FileOperations;
import com.company.invoice.Invoice;
import com.company.invoice.Item;

import javax.naming.directory.NoSuchAttributeException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

public class InvoiceFrame extends JFrame implements ActionListener {
    // Menu bar
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem loadItem;
    private JMenuItem saveItem;

    // Left panel
    private JPanel leftPanel;
    private JButton create;
    private JButton delete;
    private JTable invoices;



    // Right panel
    private JPanel rightPanel;
    private JLabel invoiceNum;
    private JButton save;
    private JButton cancel;

    private JLabel invoiceNumValue;
    private JTextField invoiceDateValue;
    private JTextField customerNameValue;
    private JLabel invoiceTotalValue;
    private JTable invoiceItemsTable;


    private String currentDataDirectory = "./data/";

    private FileOperations fileOperations = new FileOperations( invoices , this, currentDataDirectory);

    // Invoices
    private Invoice[] invoicesArray = fileOperations.updateInvoicesArray( fileOperations.loadInvoiceHeader(""), fileOperations.loadInvoiceLine("")); // Load the data from the default directory



    public InvoiceFrame() throws HeadlessException {
        super("SIG");
        setLayout(new FlowLayout());

            // Menu bar
            loadItem = new JMenuItem("Load files");
            saveItem = new JMenuItem("Save files");
            menuBar = new JMenuBar();
            fileMenu = new JMenu("File");

            loadItem.addActionListener(this);
            saveItem.addActionListener(this);
            loadItem.setActionCommand("L");
            saveItem.setActionCommand("S");


            menuBar.add(fileMenu);
            fileMenu.add(loadItem);
            fileMenu.add(saveItem);
            setJMenuBar(menuBar);

            // Left panel
            leftPanel = new JPanel();
            leftPanel.setBorder(new EmptyBorder(10,10,10,10));
            leftPanel.setLayout(new BoxLayout( leftPanel, BoxLayout.Y_AXIS));

                // Table panel
                JPanel invoicesTablePanel = new JPanel();
                invoicesTablePanel.setLayout( new FlowLayout());

                    // Table label
                    JLabel invoicesTable = new JLabel("Invoices Table");
                    invoicesTablePanel.add(invoicesTable);


                    // Invoices Table
                    invoices = new JTable();
                    renderInvoiceTable();

                    // Get the selected row from the table and show it in the right panel
                    invoices.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            if (e.getClickCount() == 1) {
                                JTable target = (JTable)e.getSource();
                                int row = target.getSelectedRow();
                                Invoice selectedInvoice = invoicesArray[row];
                                invoiceNumValue.setText( String.valueOf( selectedInvoice.getInvoiceNumber() ) );
                                if ( selectedInvoice.getInvoiceDate() == null) {
                                    invoiceDateValue.setText("");
                                }else {
                                    invoiceDateValue.setText(selectedInvoice.getInvoiceDate());
                                }
                                if ( selectedInvoice.getCustomerName() == null) {
                                    customerNameValue.setText("");
                                } else {
                                    customerNameValue.setText(selectedInvoice.getCustomerName());
                                }
                                if(selectedInvoice.getItemsArray() == null ) {
                                    invoiceTotalValue.setText("0");
                                    String[] columnNames = {
                                            "No.", "Item Name", "Item Price", "Count", "Item Total"};

                                    // empty rows to add new items in it
                                    String[][] data = {
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""},
                                            {"","","","",""}
                                    };
                                    DefaultTableModel model = new DefaultTableModel(data, columnNames);
                                    invoiceItemsTable.setModel( model);
                                } else {
                                    invoiceTotalValue.setText( String.valueOf( selectedInvoice.getTotalAmount() ) );
                                    String[] columnNames = {
                                            "No.", "Item Name", "Item Price", "Count", "Item Total"};
                                    String[][] data = selectedInvoice.getItemsArray();
                                    DefaultTableModel model = new DefaultTableModel(data, columnNames);
                                    invoiceItemsTable.setModel( model );
                                }
                            }
                        }
                    });
                    JScrollPane invoices_sp = new JScrollPane(invoices);
                    invoicesTablePanel.add(invoices_sp);

                // Left Panel Buttons
                JPanel leftPanelButtons = new JPanel();
                leftPanelButtons.setLayout(new FlowLayout());

                create = new JButton("Create New Invoice");
                create.addActionListener(this);
                create.setActionCommand("C");

                delete = new JButton("Delete Invoice");
                delete.addActionListener(this);
                delete.setActionCommand("D");

                leftPanelButtons.add(create);
                leftPanelButtons.add(delete);

            leftPanel.add(invoicesTablePanel);
            leftPanel.add(leftPanelButtons);

        add(leftPanel);

            // Right panel
            rightPanel = new JPanel();
            rightPanel.setBorder(new EmptyBorder(10,10,10,10));
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

                // Invoice Data
                JPanel invoiceData = new JPanel();
                invoiceData.setBorder(new EmptyBorder(10,10,10,10));
                invoiceData.setLayout(new GridLayout(4, 2, 30, 5));

                    // Invoice Number Label
                    invoiceNum = new JLabel("Invoice Number");
                    invoiceData.add(invoiceNum);

                    // Invoice Number Value
                    invoiceNumValue = new JLabel(""); // SHOW CONST
                    invoiceData.add(invoiceNumValue);

                    // Invoice Date Label
                    JLabel invoiceDate = new JLabel("Invoice Date");
                    invoiceData.add(invoiceDate);

                    // Invoice Date Value
                    invoiceDateValue = new JTextField(); // VAR CONST
                    invoiceData.add(invoiceDateValue);

                    // Customer Name Label
                    JLabel customerName = new JLabel("Customer Name");
                    invoiceData.add(customerName);

                    // Customer Name Value
                    customerNameValue = new JTextField(); // VAR CONST
                    invoiceData.add(customerNameValue);

                    // Invoice Total Label
                    JLabel invoiceTotal = new JLabel("Invoice Total");
                    invoiceData.add(invoiceTotal);

                    // Invoice Total Value
                    invoiceTotalValue = new JLabel(""); // SHOW CONST
                    invoiceData.add(invoiceTotalValue);


                // Invoice Items
                JPanel invoiceItems = new JPanel();

                    // Table label
                    JLabel invoiceItemsLabel = new JLabel("Invoice Items");
                    invoiceItems.add(invoiceItemsLabel);


                    // Invoices Items Table

                    String[][] invoiceItemsData ={};
                    String[] invoiceItemsColumn ={"No.","Item Name","Item Price","Count","Item Total"};
                    DefaultTableModel model = new DefaultTableModel(invoiceItemsData, invoiceItemsColumn);
                    invoiceItemsTable = new JTable(model);
                    JScrollPane invoiceItems_sp = new JScrollPane(invoiceItemsTable);

                invoiceItems.add(invoiceItemsLabel);
                invoiceItems.add(invoiceItems_sp);



                // Right Panel Buttons
                save = new JButton("Save");
                save.addActionListener(this);
                save.setActionCommand("SE");
                cancel = new JButton("Cancel");
                cancel.addActionListener(this);
                cancel.setActionCommand("CE");

                JPanel buttonsPanel = new JPanel();
                buttonsPanel.add(save);
                buttonsPanel.add(cancel);

            rightPanel.add(invoiceData);
            rightPanel.add(invoiceItems);
            rightPanel.add(buttonsPanel);

        add(rightPanel);

        setLayout(new GridLayout(1, 2, 10, 15));
        setSize(1000, 750);
        setLocation(100,50);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "L":

                fileOperations.updateInvoices(invoices);
                invoicesArray = fileOperations.loadFile(invoicesArray); // Load both InvoiceHeader.csv and InvoiceLine.csv files with the instructed format
                renderInvoiceTable();
                break;
            case "S":
                fileOperations.updateInvoices(invoices);
                fileOperations.saveFile(invoicesArray); // Save the current changes to the loaded InvoiceHeader.csv and InvoiceLine.csv files
                break;
            case "C":
                // Add new empty row to the invoices' table where it can be edited later
                DefaultTableModel model = (DefaultTableModel) invoices.getModel();
                model.addRow(new Object[]{String.valueOf( model.getRowCount() + 1 ), "", "", ""});
                invoices.setModel(model);
                Invoice[] newInvoicesArray = Arrays.copyOf( invoicesArray, invoicesArray.length + 1);
                Invoice newInvoice = new Invoice();
                newInvoice.setInvoiceNumber(model.getRowCount());
                invoicesArray = newInvoicesArray;
                invoicesArray[ invoicesArray.length - 1] = newInvoice;
                break;
            case "D":
                // Delete the selected row
                model = (DefaultTableModel) invoices.getModel();
                int row = invoices.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(new JFrame(), "Select an invoice", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    model.removeRow( row );
                    invoices.setModel(model);
                    Invoice[] DeleteInvoicesArray = new Invoice[ invoicesArray.length - 1];
                    for (int i = 0; i < DeleteInvoicesArray.length; i++) {
                        if (i < row) {
                            DeleteInvoicesArray[i] = invoicesArray[i];
                        } else if (i >= row){
                            invoicesArray[i+1].setInvoiceNumber( invoicesArray[i+1].getInvoiceNumber() - 1 );
                            DeleteInvoicesArray[i] = invoicesArray[i+1];
                        }

                    }
                    invoicesArray = DeleteInvoicesArray;
                    renderInvoiceTable();
                }

                break;
            case "SE":

                // Save the changes to the invoice and update the invoices' table

                // Unselect the row before pressing save
                try {
                    if ( invoiceNumValue.getText().equals("")) {
                        throw new NoSuchAttributeException() ;
                    }
                    int invoiceNumber = Integer.parseInt( invoiceNumValue.getText() );
                    invoicesArray[invoiceNumber - 1].setInvoiceDate( invoiceDateValue.getText() );
                    invoicesArray[invoiceNumber - 1].setCustomerName( customerNameValue.getText() );
                    model = (DefaultTableModel) invoiceItemsTable.getModel();
                    int itemsCount = 0;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if( model.getValueAt(i, 0) == "" ) { break;}
                        itemsCount++;
                    }
                    Item[] items = new Item[itemsCount];

                    for (int i = 0; i < itemsCount; i++) {
                        Item item = new Item();
                        item.setItemName( (String) model.getValueAt(i, 1) );
                        item.setItemPrice(Integer.parseInt((String) model.getValueAt(i, 2)));
                        item.setCount(Integer.parseInt((String) model.getValueAt(i, 3)));
                        model.setValueAt( String.valueOf(item.getItemTotal()) , i, 4);
                        items[i] = item;
                    }
                    invoicesArray[invoiceNumber - 1].setItems(items);
                    invoiceTotalValue.setText(String.valueOf(invoicesArray[invoiceNumber - 1].getTotalAmount()));
                    renderInvoiceTable();

                } catch (NoSuchAttributeException saveButton) {
                    JOptionPane.showMessageDialog(new JFrame(), "Select an invoice", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "CE":

                // Cancel the changes and show the old invoice
                int selectedInvoice;
                try {
                    selectedInvoice = Integer.parseInt(invoiceNumValue.getText());
                    invoiceDateValue.setText("");
                    customerNameValue.setText("");
                    String[] columnNames = {
                            "No.", "Item Name", "Item Price", "Count", "Item Total"};
                    String [][] originalData = invoicesArray[selectedInvoice - 1].getItemsArray();
                    DefaultTableModel originalModel = new DefaultTableModel(originalData, columnNames);
                    invoiceItemsTable.setModel(originalModel);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(new JFrame(), "Select an invoice", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                break;

        }
    }

    private void renderInvoiceTable() {
        String[][] invoicesData = new String[invoicesArray.length][4];
        for (int i = 0; i < invoicesArray.length; i++) {
            invoicesData[i] = invoicesArray[i].getInvoiceArray();
        }

        String[] invoicesColumn ={"No.","Date","Customer","Total"};
        DefaultTableModel model = new DefaultTableModel(invoicesData, invoicesColumn);
        invoices.setModel( model );
    }

}
