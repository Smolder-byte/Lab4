package main;

import controller.ShopController;
import model.DatabaseManager;
import view.ShopView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseManager dbManager = new DatabaseManager();
                ShopView view = new ShopView();
                new ShopController(dbManager, view);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Ошибка инициализации приложения: " + e.getMessage(), 
                    "Фатальная ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}