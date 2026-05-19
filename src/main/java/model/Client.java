package model;

import java.io.Serializable;

public class Client implements Serializable {
    private int id;
    private String fullname;
    private String address;
    private String tel;
    private String email;
    private String note;

    public Client(){
        super();
    }

    public Client(int id, String fullname, String address, String tel, String email, String note) {
        this.id = id;
        this.fullname = fullname;
        this.address = address;
        this.tel = tel;
        this.email = email;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
