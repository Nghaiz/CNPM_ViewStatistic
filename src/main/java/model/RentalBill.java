package model;

import java.io.Serializable;
import java.time.*;
import java.util.*;

public class RentalBill implements Serializable {
    private int id;
    private LocalDateTime createdAt;
    private float saleoff;
    private String note;
    private Client client;
    private User seller;
    private List<RentedCostume> listRentedCostume = new ArrayList<>();
    private List<RentalCollateral> listRentalCollateral = new ArrayList<>();

    public RentalBill(){
        super();
    }

    public RentalBill(int id, LocalDateTime createdAt, float saleoff, String note, Client client, User seller, List<RentedCostume> listRentedCostume, List<RentalCollateral> listRentalCollateral) {
        this.id = id;
        this.createdAt = createdAt;
        this.saleoff = saleoff;
        this.note = note;
        this.client = client;
        this.seller = seller;
        this.listRentedCostume = listRentedCostume;
        this.listRentalCollateral = listRentalCollateral;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public float getSaleoff() {
        return saleoff;
    }

    public void setSaleoff(float saleoff) {
        this.saleoff = saleoff;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public List<RentedCostume> getListRentedCostume() {
        return listRentedCostume;
    }

    public void setListRentedCostume(List<RentedCostume> listRentedCostume) {
        this.listRentedCostume = listRentedCostume;
    }

    public List<RentalCollateral> getListRentalCollateral() {
        return listRentalCollateral;
    }

    public void setListRentalCollateral(List<RentalCollateral> listRentalCollateral) {
        this.listRentalCollateral = listRentalCollateral;
    }
}
