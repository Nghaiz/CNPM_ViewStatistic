package model;

import java.io.Serializable;

public class Costume implements Serializable {
    private int id;
    private String name;
    private String type;
    private String category;
    private String description;
    private float price;

    public Costume(){
        super();
    }

    public Costume(int id, String name, String type, String category, String description, float price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.description = description;
        this.price = price;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
