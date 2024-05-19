package com.example.myapp;

public class Data {
    String usr;
    String gen;
    String email;
    String phno;
    String dob;
    String imageUrl;
    String id;

    public Data(String usr, String gen, String email, String phno, String dob, String imageUrl,String id) {
        this.usr = usr;
        this.gen = gen;
        this.email = email;
        this.phno = phno;
        this.dob = dob;
        this.imageUrl = imageUrl;
        this.id=id;
    }

    public Data()
    {

    }
    public String getEmail() {
        return email;
    }

    public String getPhno() {
        return phno;
    }

    public String getDob() {
        return dob;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUsr() {
        return usr;
    }

    public String getGen() {
        return gen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public void setGen(String pas) {
        this.gen = pas;
    }
}

/*
public class Data {
    String name;
    String id;

    public Data(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

}*/