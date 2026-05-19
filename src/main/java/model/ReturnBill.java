package model;

import java.io.Serializable;
import java.time.*;
import java.util.*;

public class ReturnBill implements Serializable {
    private int id;
    private LocalDateTime returnedAt;
    private float additionalFee;
    private String note;
    private Client client;
    private User seller;
    private List<ReturnedCostume> listReturnedCostume = new ArrayList<>();
    private List<ReturnedCollateral> listReturnedCollateral = new ArrayList<>();

    public ReturnBill(){
        super();
    }

    public ReturnBill(int id, LocalDateTime returnedAt, float additionalFee, String note, Client client, User seller, List<ReturnedCostume> listReturnedCostume, List<ReturnedCollateral> listReturnedCollateral) {
        this.id = id;
        this.returnedAt = returnedAt;
        this.additionalFee = additionalFee;
        this.note = note;
        this.client = client;
        this.seller = seller;
        this.listReturnedCostume = listReturnedCostume;
        this.listReturnedCollateral = listReturnedCollateral;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }

    public float getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(float additionalFee) {
        this.additionalFee = additionalFee;
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

    public List<ReturnedCostume> getListReturnedCostume() {
        return listReturnedCostume;
    }

    public void setListReturnedCostume(List<ReturnedCostume> listReturnedCostume) {
        this.listReturnedCostume = listReturnedCostume;
    }

    public List<ReturnedCollateral> getListReturnedCollateral() {
        return listReturnedCollateral;
    }

    public void setListReturnedCollateral(List<ReturnedCollateral> listReturnedCollateral) {
        this.listReturnedCollateral = listReturnedCollateral;
    }
}
