package model;

import java.io.Serializable;

public class ReturnedCollateral implements Serializable {
    private int id;
    private float returnedValue;

    public ReturnedCollateral(){
        super();
    }

    public ReturnedCollateral(int id, float returnedValue) {
        this.id = id;
        this.returnedValue = returnedValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getReturnedValue() {
        return returnedValue;
    }

    public void setReturnedValue(float returnedValue) {
        this.returnedValue = returnedValue;
    }
}
