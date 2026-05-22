package model;

import java.io.Serializable;
import java.util.*;
import java.time.*;

public class ReturnedCostume implements Serializable {
    private int id;
    private int returnedQuantity;
    private LocalDateTime returnedAt;
    private List<ReturnedDamage> listReturnedDamage = new ArrayList<>();
    private RentedCostume rentedCostume;
    private float totalAmount;

    public ReturnedCostume() {
        super();
    }

    public ReturnedCostume(int id, int returnedQuantity, LocalDateTime returnedAt, List<ReturnedDamage> listReturnedDamage) {
        this.id = id;
        this.returnedQuantity = returnedQuantity;
        this.returnedAt = returnedAt;
        this.listReturnedDamage = listReturnedDamage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReturnedQuantity() {
        return returnedQuantity;
    }

    public void setReturnedQuantity(int returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }

    public List<ReturnedDamage> getListReturnedDamage() {
        return listReturnedDamage;
    }

    public void setListReturnedDamage(List<ReturnedDamage> listReturnedDamage) {
        this.listReturnedDamage = listReturnedDamage;
    }

    public RentedCostume getRentedCostume() {
        return rentedCostume;
    }

    public void setRentedCostume(RentedCostume rentedCostume) {
        this.rentedCostume = rentedCostume;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
}
