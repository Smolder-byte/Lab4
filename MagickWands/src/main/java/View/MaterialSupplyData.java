package view;

public class MaterialSupplyData {
    private final String type;
    private final String name;
    private final int quantity;

    public MaterialSupplyData(String type, String name, int quantity) {
        this.type = type;
        this.name = name;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "MaterialSupplyData{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}