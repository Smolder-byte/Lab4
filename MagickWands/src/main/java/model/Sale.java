package model;

import java.time.LocalDate;

public class Sale {
    private int id;
    private int wandId;
    private int customerId;
    private LocalDate saleDate;

    public Sale(int id, int wandId, int customerId, LocalDate saleDate) {
        this.id = id;
        this.wandId = wandId;
        this.customerId = customerId;
        this.saleDate = saleDate;
    }

    public int getId() { return id; }
    public int getWandId() { return wandId; }
    public int getCustomerId() { return customerId; }
    public LocalDate getSaleDate() { return saleDate; }
}
