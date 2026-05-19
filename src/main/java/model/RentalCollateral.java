package model;

import java.io.Serializable;

public class RentalCollateral implements Serializable {
    private int id;
    private float value;
    private String note;
    private Collateral collateral;

    public RentalCollateral() {
        super();
    }

    public RentalCollateral(int id, float value, String note, Collateral collateral) {
        this.id = id;
        this.value = value;
        this.note = note;
        this.collateral = collateral;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Collateral getCollateral() {
        return collateral;
    }

    public void setCollateral(Collateral collateral) {
        this.collateral = collateral;
    }
}
