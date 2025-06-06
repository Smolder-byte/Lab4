package view;

import model.Material;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class MaterialsPanel extends JPanel {
    private JTable materialsTable;
    private JButton supplyButton;
    private JComboBox<String> typeCombo;
    private JTextField nameField;
    private JSpinner quantitySpinner;

    public MaterialsPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        materialsTable = new JTable();
        add(new JScrollPane(materialsTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(1, 5, 5, 5));
        
        typeCombo = new JComboBox<>(new String[]{"core", "wood"});
        nameField = new JTextField();
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        supplyButton = new JButton("Добавить поставку");
        
        inputPanel.add(new JLabel("Тип:"));
        inputPanel.add(typeCombo);
        inputPanel.add(new JLabel("Название:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Количество:"));
        inputPanel.add(quantitySpinner);
        inputPanel.add(supplyButton);
        
        add(inputPanel, BorderLayout.SOUTH);
    }

    public void displayMaterials(List<Material> materials) {
        String[] columnNames = {"ID", "Тип", "Название", "Количество"};
        Object[][] data = new Object[materials.size()][4];
        
        for (int i = 0; i < materials.size(); i++) {
            Material m = materials.get(i);
            data[i][0] = m.getId();
            data[i][1] = m.getType();
            data[i][2] = m.getName();
            data[i][3] = m.getQuantity();
        }
        
        materialsTable.setModel(new javax.swing.table.DefaultTableModel(
            data, columnNames
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    public MaterialSupplyData getSupplyData() {
        return new MaterialSupplyData(
            (String) typeCombo.getSelectedItem(),
            nameField.getText().trim(),
            (Integer) quantitySpinner.getValue()
        );
    }

    public void addSupplyListener(ActionListener listener) {
        supplyButton.addActionListener(listener);
    }

    public void clearInputFields() {
        nameField.setText("");
        quantitySpinner.setValue(1);
    }
}