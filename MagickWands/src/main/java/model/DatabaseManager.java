package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() throws SQLException {
        connect();
        initializeDatabase();
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:magic_shop.db");
    }

    private void initializeDatabase() throws SQLException {
        String[] createTables = {
            "CREATE TABLE IF NOT EXISTS materials (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "type TEXT NOT NULL CHECK(type IN ('core', 'wood'))," +
            "name TEXT NOT NULL," +
            "quantity INTEGER NOT NULL DEFAULT 0)",
            
            "CREATE TABLE IF NOT EXISTS wands (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "core_id INTEGER NOT NULL REFERENCES materials(id)," +
            "wood_id INTEGER NOT NULL REFERENCES materials(id)," +
            "status TEXT NOT NULL DEFAULT 'available' CHECK(status IN ('available', 'sold')))",
            
            "CREATE TABLE IF NOT EXISTS customers (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "first_name TEXT NOT NULL," +
            "last_name TEXT NOT NULL)",
            
            "CREATE TABLE IF NOT EXISTS sales (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "wand_id INTEGER NOT NULL REFERENCES wands(id)," +
            "customer_id INTEGER NOT NULL REFERENCES customers(id)," +
            "sale_date DATE NOT NULL)"
        };

        try (Statement stmt = connection.createStatement()) {
            for (String sql : createTables) {
                stmt.execute(sql);
            }
        }
    }

    public List<Material> getAllMaterials() throws SQLException {
        List<Material> materials = new ArrayList<>();
        String sql = "SELECT * FROM materials";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                materials.add(new Material(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getString("name"),
                    rs.getInt("quantity")
                ));
            }
        }
        return materials;
    }

    public void addMaterial(String type, String name, int quantity) throws SQLException {
    String checkSql = "SELECT id, quantity FROM materials WHERE type = ? AND name = ?";
    
    try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
        checkStmt.setString(1, type);
        checkStmt.setString(2, name);
        ResultSet rs = checkStmt.executeQuery();
        
        if (rs.next()) {
            int existingId = rs.getInt("id");
            int existingQuantity = rs.getInt("quantity");
            
            String updateSql = "UPDATE materials SET quantity = ? WHERE id = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setInt(1, existingQuantity + quantity);
                updateStmt.setInt(2, existingId);
                updateStmt.executeUpdate();
            }
        } else {
            String insertSql = "INSERT INTO materials (type, name, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setString(1, type);
                insertStmt.setString(2, name);
                insertStmt.setInt(3, quantity);
                insertStmt.executeUpdate();
            }
        }
    }
}

    public List<Wand> getAllWands() throws SQLException {
    List<Wand> wands = new ArrayList<>();
    String sql = """
        SELECT w.id, 
               mc.name AS core_name, 
               mw.name AS wood_name, 
               w.status
        FROM wands w
        LEFT JOIN materials mc ON w.core_id = mc.id
        LEFT JOIN materials mw ON w.wood_id = mw.id
        """;
    
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            wands.add(new Wand(
                rs.getInt("id"),
                rs.getString("core_name"),
                rs.getString("wood_name"),
                rs.getString("status")
            ));
        }
    }
    return wands;
}

    public void createWand(int coreId, int woodId) throws SQLException {
    connection.setAutoCommit(false);
    
    try {
        if (!areMaterialsAvailable(coreId, woodId)) {
            throw new SQLException("Недостаточно материалов для создания палочки");
        }

        updateMaterialQuantity(coreId, -1);
        updateMaterialQuantity(woodId, -1);

        String sql = "INSERT INTO wands (core_id, wood_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, coreId);
            stmt.setInt(2, woodId);
            stmt.executeUpdate();
        }

        connection.commit();
        
    } catch (SQLException e) {
        connection.rollback();
        throw e;
    } finally {
        connection.setAutoCommit(true);
    }
}

private void updateMaterialQuantity(int materialId, int delta) throws SQLException {
    String sql = "UPDATE materials SET quantity = quantity + ? WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, delta);
        stmt.setInt(2, materialId);
        stmt.executeUpdate();
    }
}

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                ));
            }
        }
        return customers;
    }

    public int addCustomer(String firstName, String lastName) throws SQLException {
        String sql = "INSERT INTO customers (first_name, last_name) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to add customer");
    }

    public List<Sale> getAllSales() throws SQLException {
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("id"),
                    rs.getInt("wand_id"),
                    rs.getInt("customer_id"),
                    LocalDate.parse(rs.getString("sale_date"))
                ));
            }
        }
        return sales;
    }

    public void recordSale(int wandId, int customerId) throws SQLException {
        connection.setAutoCommit(false);
        try {
            String updateWand = "UPDATE wands SET status = 'sold' WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(updateWand)) {
                pstmt.setInt(1, wandId);
                pstmt.executeUpdate();
            }
            
            String insertSale = "INSERT INTO sales (wand_id, customer_id, sale_date) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertSale)) {
                pstmt.setInt(1, wandId);
                pstmt.setInt(2, customerId);
                pstmt.setString(3, LocalDate.now().toString());
                pstmt.executeUpdate();
            }
            
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void clearAllData() throws SQLException {
    String[] tables = {"sales", "wands", "customers", "materials"};
    
    try (Statement stmt = connection.createStatement()) {
        stmt.execute("PRAGMA foreign_keys = OFF");
        
        for (String table : tables) {
            stmt.execute("DELETE FROM " + table);
        }
        
        stmt.execute("DELETE FROM sqlite_sequence");
        
        stmt.execute("PRAGMA foreign_keys = ON");
        }
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
    
    
    public boolean areMaterialsAvailable(int coreId, int woodId) throws SQLException {
    String sql = """
        SELECT 
            (SELECT quantity FROM materials WHERE id = ?) >= 1 AS has_core,
            (SELECT quantity FROM materials WHERE id = ?) >= 1 AS has_wood
        """;
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, coreId);
        stmt.setInt(2, woodId);
        
        ResultSet rs = stmt.executeQuery();
        return rs.next() && rs.getBoolean("has_core") && rs.getBoolean("has_wood");
    }
}
}