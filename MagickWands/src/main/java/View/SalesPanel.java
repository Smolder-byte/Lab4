package view;

import model.Sale;
import model.Wand;
import model.Customer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class SalesPanel extends JPanel {
    private JTable salesTable;
    private JButton sellButton;
    private JComboBox<String> wandCombo;
    private JComboBox<String> customerCombo;

    public SalesPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        salesTable = new JTable();
        add(new JScrollPane(salesTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        
        wandCombo = new JComboBox<>();
        customerCombo = new JComboBox<>();
        sellButton = new JButton("Зарегистрировать продажу");
        
        inputPanel.add(new JLabel("Палочка:"));
        inputPanel.add(wandCombo);
        inputPanel.add(new JLabel("Клиент:"));
        inputPanel.add(customerCombo);
        inputPanel.add(sellButton);
        
        add(inputPanel, BorderLayout.SOUTH);
    }

    public void displaySales(List<Sale> sales) {
        String[] columnNames = {"ID продажи", "ID палочки", "ID клиента", "Дата продажи"};
        Object[][] data = new Object[sales.size()][4];
        
        for (int i = 0; i < sales.size(); i++) {
            Sale s = sales.get(i);
            data[i][0] = s.getId();
            data[i][1] = s.getWandId();
            data[i][2] = s.getCustomerId();
            data[i][3] = s.getSaleDate().toString();
        }
        
        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            data, columnNames
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    public void setAvailableWands(List<Wand> wands) {
        wandCombo.removeAllItems();
        for (Wand wand : wands) {
            if ("available".equals(wand.getStatus())) {
                wandCombo.addItem(wand.getId() + ": " + wand.getCore() + "/" + wand.getWood());
            }
        }
    }

    public void setCustomers(List<Customer> customers) {
        customerCombo.removeAllItems();
        for (Customer customer : customers) {
            customerCombo.addItem(customer.getId() + ": " + 
                customer.getFirstName() + " " + customer.getLastName());
        }
    }

    public SaleData getSaleData() {
        String wandSelection = (String) wandCombo.getSelectedItem();
        String customerSelection = (String) customerCombo.getSelectedItem();
        
        int wandId = Integer.parseInt(wandSelection.split(":")[0].trim());
        int customerId = Integer.parseInt(customerSelection.split(":")[0].trim());
        
        return new SaleData(wandId, customerId);
    }

    public void addSaleListener(ActionListener listener) {
        sellButton.addActionListener(listener);
    }
}