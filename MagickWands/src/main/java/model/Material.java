package model;

public class Material {
    private int id;
    private String type;
    private String name;
    private int quantity;

    public Material(int id, String type, String name, int quantity) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
    }
}