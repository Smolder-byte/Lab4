package controller;

import model.*;
import view.*;
import java.sql.SQLException;
import java.util.List;

public class ShopController {
    private final DatabaseManager dbManager;
    private final ShopView view;

    public ShopController(DatabaseManager dbManager, ShopView view) {
        this.dbManager = dbManager;
        this.view = view;
        
        initializeView();
        setupEventHandlers();
        loadInitialData();
    }

    private void initializeView() {
        view.setVisible(true);
    }

    private void setupEventHandlers() {
        view.getMaterialsPanel().addSupplyListener(e -> handleMaterialSupply());
        view.getWandsPanel().addCreationListener(e -> handleWandCreation());
        view.getCustomersPanel().addRegistrationListener(e -> handleCustomerRegistration());
        view.getSalesPanel().addSaleListener(e -> handleWandSale());
        view.addDataClearListener(e -> handleDataClear());
    }

    private void loadInitialData() {
        refreshData();
    }

    private void refreshData() {
        try {
            List<Material> materials = dbManager.getAllMaterials();
            view.getMaterialsPanel().displayMaterials(materials);
            
            List<Wand> wands = dbManager.getAllWands();
            view.getWandsPanel().displayWands(wands);
            
            List<Customer> customers = dbManager.getAllCustomers();
            view.getCustomersPanel().displayCustomers(customers);
            
            List<Sale> sales = dbManager.getAllSales();
            view.getSalesPanel().displaySales(sales);
            
            updateComboBoxes(materials, wands, customers);
            
        } catch (SQLException ex) {
            view.showError("Ошибка загрузки данных: " + ex.getMessage());
        }
    }

    private void updateComboBoxes(List<Material> materials, List<Wand> wands, List<Customer> customers) {
        List<Material> cores = materials.stream().filter(m -> "core".equals(m.getType())).toList();
        
        List<Material> woods = materials.stream().filter(m -> "wood".equals(m.getType())).toList();
        
        view.getWandsPanel().setCoreMaterials(cores);
        view.getWandsPanel().setWoodMaterials(woods);
        
        view.getSalesPanel().setAvailableWands(
            wands.stream().filter(w -> "available".equals(w.getStatus())).toList()
        );
        view.getSalesPanel().setCustomers(customers);
    }

    private void handleMaterialSupply() {
        try {
            MaterialSupplyData data = view.getMaterialsPanel().getSupplyData();
            
            if (data.getName().isEmpty()) {
                throw new IllegalArgumentException("Название материала не может быть пустым");
            }
            if (data.getQuantity() <= 0) {
                throw new IllegalArgumentException("Количество должно быть положительным");
            }
            
            dbManager.addMaterial(data.getType(), data.getName(), data.getQuantity());
            refreshData();
            view.getMaterialsPanel().clearInputFields();
            view.showSuccess("Материалы успешно добавлены!");
            
        } catch (Exception ex) {
            view.showError(ex.getMessage());
        }
    }

    private void handleWandCreation() {
    try {
        WandComponents components = view.getWandsPanel().getComponentsData();
        
        if (!dbManager.areMaterialsAvailable(components.getCoreId(), components.getWoodId())) {
            throw new IllegalArgumentException("Недостаточно материалов для создания палочки");
        }
        
        dbManager.createWand(components.getCoreId(), components.getWoodId());
        refreshData();
        view.showSuccess("Палочка успешно создана!");
        
    } catch (Exception ex) {
        view.showError(ex.getMessage());
    }
}

    private void handleCustomerRegistration() {
        try {
            CustomerData data = view.getCustomersPanel().getCustomerData();
            
            if (data.getFirstName().isEmpty() || data.getLastName().isEmpty()) {
                throw new IllegalArgumentException("Имя и фамилия не могут быть пустыми");
            }
            
            dbManager.addCustomer(data.getFirstName(), data.getLastName());
            refreshData();
            view.getCustomersPanel().clearInputFields();
            view.showSuccess("Клиент успешно зарегистрирован!");
            
        } catch (Exception ex) {
            view.showError(ex.getMessage());
        }
    }

    private void handleWandSale() {
        try {
            SaleData data = view.getSalesPanel().getSaleData();
            dbManager.recordSale(data.getWandId(), data.getCustomerId());
            refreshData();
            view.showSuccess("Продажа успешно зарегистрирована!");
            
        } catch (Exception ex) {
            view.showError(ex.getMessage());
        }
    }

    private void handleDataClear() {
        if (view.confirmAction("Вы уверены, что хотите очистить все данные?")) {
            try {
                dbManager.clearAllData();
                refreshData();
                view.showSuccess("Все данные успешно очищены!");
            } catch (SQLException ex) {
                view.showError("Ошибка очистки данных: " + ex.getMessage());
            }
        }
    }
}