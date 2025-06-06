package view;

public class SaleData {
    private final int wandId;
    private final int customerId;

    public SaleData(int wandId, int customerId) {
        this.wandId = wandId;
        this.customerId = customerId;
    }

    public int getWandId() {
        return wandId;
    }

    public int getCustomerId() {
        return customerId;
    }

    @Override
    public String toString() {
        return "SaleData{" +
                "wandId=" + wandId +
                ", customerId=" + customerId +
                '}';
    }
}