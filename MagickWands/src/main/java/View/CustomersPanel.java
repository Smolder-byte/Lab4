package view;

import model.Customer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomersPanel extends JPanel {
    private JTable customersTable;
    private JButton registerButton;
    private JTextField firstNameField;
    private JTextField lastNameField;

    public CustomersPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        customersTable = new JTable();
        add(new JScrollPane(customersTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        registerButton = new JButton("Зарегистрировать клиента");
        
        inputPanel.add(new JLabel("Имя:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Фамилия:"));
        inputPanel.add(lastNameField);
        inputPanel.add(registerButton);
        
        add(inputPanel, BorderLayout.SOUTH);
    }

    public void displayCustomers(List<Customer> customers) {
        String[] columnNames = {"ID", "Имя", "Фамилия"};
        Object[][] data = new Object[customers.size()][3];
        
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getFirstName();
            data[i][2] = c.getLastName();
        }
        
        customersTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    public CustomerData getCustomerData() {
        return new CustomerData(
            firstNameField.getText().trim(),
            lastNameField.getText().trim()
        );
    }

    public void addRegistrationListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void clearInputFields() {
        firstNameField.setText("");
        lastNameField.setText("");
    }
}