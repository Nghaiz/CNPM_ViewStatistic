package model;

import java.io.Serializable;

public class RentedDamage implements Serializable {
    private int id;
    private int quantity;
    private String note;
    private Damage damage;

    public RentedDamage(){
        super();
    }

    public RentedDamage(int id, int quantity, String note, Damage damage) {
        this.id = id;
        this.quantity = quantity;
        this.note = note;
        this.damage = damage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Damage getDamage() {
        return damage;
    }

    public void setDamage(Damage damage) {
        this.damage = damage;
    }
}
