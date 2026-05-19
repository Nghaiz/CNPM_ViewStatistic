package model;

import java.io.Serializable;

public class Damage implements Serializable {
    private int id;
    private String name;
    private String description;
    private float fee;

    public Damage(){
        super();
    }

    public Damage(int id, String name, String description, float fee) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fee = fee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }
}
