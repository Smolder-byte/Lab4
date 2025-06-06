package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import model.*;


public class ShopView extends JFrame {
    private JTabbedPane tabbedPane;
    private MaterialsPanel materialsPanel;
    private WandsPanel wandsPanel;
    private CustomersPanel customersPanel;
    private SalesPanel salesPanel;
    
    public ShopView() {
        super("Магазин волшебных палочек Олливандера");
        initializeUI();
    }
    
    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        materialsPanel = new MaterialsPanel();
        wandsPanel = new WandsPanel();
        customersPanel = new CustomersPanel();
        salesPanel = new SalesPanel();
        
        tabbedPane.addTab("Материалы", materialsPanel);
        tabbedPane.addTab("Палочки", wandsPanel);
        tabbedPane.addTab("Клиенты", customersPanel);
        tabbedPane.addTab("Продажи", salesPanel);
        
        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton clearButton = new JButton("Очистить всё");
        
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        clearButton.addActionListener(e -> fireDataClearEvent());
    }
    
    public void displayMaterials(List<Material> materials) {
        materialsPanel.displayMaterials(materials);
    }
    
    public void displayWands(List<Wand> wands) {
        wandsPanel.displayWands(wands);
    }
    
    public void displayCustomers(List<Customer> customers) {
        customersPanel.displayCustomers(customers);
    }
    
    public void displaySales(List<Sale> sales) {
        salesPanel.displaySales(sales);
    }
    
    public MaterialSupplyData getMaterialSupplyData() {
        return materialsPanel.getSupplyData();
    }
    
    public WandComponents getWandComponents() {
        return wandsPanel.getComponentsData();
    }
    
    public CustomerData getCustomerData() {
        return customersPanel.getCustomerData();
    }
    
    public SaleData getSaleData() {
        return salesPanel.getSaleData();
    }
    
    public boolean confirmAction(String message) {
        return JOptionPane.showConfirmDialog(
            this, message, "Подтверждение", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Успех", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    
    public void addMaterialSupplyListener(ActionListener listener) {
        materialsPanel.addSupplyListener(listener);
    }
    
    public void addWandCreationListener(ActionListener listener) {
        wandsPanel.addCreationListener(listener);
    }
    
    public void addCustomerRegistrationListener(ActionListener listener) {
        customersPanel.addRegistrationListener(listener);
    }
    
    public void addWandSaleListener(ActionListener listener) {
        salesPanel.addSaleListener(listener);
    }
    
    public void addDataClearListener(ActionListener listener) {
        this.dataClearListener = listener;
    }
    
    private ActionListener dataClearListener;
    
    private void fireDataClearEvent() {
        if (dataClearListener != null) {
            dataClearListener.actionPerformed(
                new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "clear"));
        }
    }
    
    public MaterialsPanel getMaterialsPanel() {
        return materialsPanel;
    }
    
    public WandsPanel getWandsPanel() {
        return wandsPanel;
    }
    
    public CustomersPanel getCustomersPanel() {
        return customersPanel;
    }
    
    public SalesPanel getSalesPanel() {
        return salesPanel;
    }
}