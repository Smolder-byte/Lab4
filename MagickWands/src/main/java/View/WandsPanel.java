package view;

import model.Wand;
import model.Material;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class WandsPanel extends JPanel {
    private JTable wandsTable;
    private JButton createButton;
    private JComboBox<String> coreCombo;
    private JComboBox<String> woodCombo;

    public WandsPanel() {
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        wandsTable = new JTable();
        add(new JScrollPane(wandsTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        
        coreCombo = new JComboBox<>();
        woodCombo = new JComboBox<>();
        createButton = new JButton("Создать палочку");
        
        inputPanel.add(new JLabel("Сердцевина:"));
        inputPanel.add(coreCombo);
        inputPanel.add(new JLabel("Древесина:"));
        inputPanel.add(woodCombo);
        inputPanel.add(createButton);
        
        add(inputPanel, BorderLayout.SOUTH);
    }

    public void displayWands(List<Wand> wands) {
        String[] columnNames = {"ID", "ID сердцевины", "ID древесины", "Статус"};
        Object[][] data = new Object[wands.size()][4];
        
        for (int i = 0; i < wands.size(); i++) {
            Wand w = wands.get(i);
            data[i][0] = w.getId();
            data[i][1] = w.getCore();
            data[i][2] = w.getWood();
            data[i][3] = w.getStatus();
        }
        
        wandsTable.setModel(new javax.swing.table.DefaultTableModel(
            data, columnNames
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    public void setCoreMaterials(List<Material> cores) {
        coreCombo.removeAllItems();
        for (Material core : cores) {
            coreCombo.addItem(core.getId() + ": " + core.getName());
        }
    }

    public void setWoodMaterials(List<Material> woods) {
        woodCombo.removeAllItems();
        for (Material wood : woods) {
            woodCombo.addItem(wood.getId() + ": " + wood.getName());
        }
    }

    public WandComponents getComponentsData() {
        if (coreCombo.getSelectedItem() == null || woodCombo.getSelectedItem() == null) {
            throw new IllegalStateException("Выберите сердцевину и древесину");
        }
        
        String coreSelection = (String) coreCombo.getSelectedItem();
        String woodSelection = (String) woodCombo.getSelectedItem();
        
        try {
            int coreId = Integer.parseInt(coreSelection.split(":")[0].trim());
            int woodId = Integer.parseInt(woodSelection.split(":")[0].trim());
            
            return new WandComponents(coreId, woodId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Неверный формат ID материала");
        }
    }

    public void addCreationListener(ActionListener listener) {
        createButton.addActionListener(listener);
    }
}