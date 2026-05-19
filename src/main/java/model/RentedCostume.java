package model;

import java.io.Serializable;
import java.util.*;
import java.time.*;

public class RentedCostume implements Serializable {
    private int id;
    private float rentalPrice;
    private int rentalQuantity;
    private LocalDateTime rentedAt;
    private LocalDateTime dateToReturn;
    private Costume costume;
    private List<RentedDamage> listRentedDamage = new ArrayList<>();

    public RentedCostume() {
        super();
    }

    public RentedCostume(int id, float rentalPrice, int rentalQuantity, LocalDateTime rentedAt, LocalDateTime dateToReturn, Costume costume, List<RentedDamage> listRentedDamage) {
        this.id = id;
        this.rentalPrice = rentalPrice;
        this.rentalQuantity = rentalQuantity;
        this.rentedAt = rentedAt;
        this.dateToReturn = dateToReturn;
        this.costume = costume;
        this.listRentedDamage = listRentedDamage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(float rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public int getRentalQuantity() {
        return rentalQuantity;
    }

    public void setRentalQuantity(int rentalQuantity) {
        this.rentalQuantity = rentalQuantity;
    }

    public LocalDateTime getRentedAt() {
        return rentedAt;
    }

    public void setRentedAt(LocalDateTime rentedAt) {
        this.rentedAt = rentedAt;
    }

    public LocalDateTime getDateToReturn() {
        return dateToReturn;
    }

    public void setDateToReturn(LocalDateTime dateToReturn) {
        this.dateToReturn = dateToReturn;
    }

    public Costume getCostume() {
        return costume;
    }

    public void setCostume(Costume costume) {
        this.costume = costume;
    }

    public List<RentedDamage> getListRentedDamage() {
        return listRentedDamage;
    }

    public void setListRentedDamage(List<RentedDamage> listRentedDamage) {
        this.listRentedDamage = listRentedDamage;
    }
}
