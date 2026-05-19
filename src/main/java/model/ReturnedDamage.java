package model;

import java.io.Serializable;

public class ReturnedDamage implements Serializable {
    private int id;
    private int quantity;
    private float fee;
    private String note;
    private Damage damage;

    public ReturnedDamage() {
        super();
    }

    public ReturnedDamage(int id, int quantity, float fee, String note, Damage damage) {
        this.id = id;
        this.quantity = quantity;
        this.fee = fee;
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

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
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
