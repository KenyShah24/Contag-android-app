package com.example.myapp.Model;

public class SearchUser {

    public String usr;
    public String gen;
    public String email;
    public String phno;
    public String dob;
    public String imageUrl;

    public SearchUser()
    {

    }

    public SearchUser(String usr, String gen, String email, String phno, String dob, String imageUrl) {
        this.usr = usr;
        this.gen = gen;
        this.email = email;
        this.phno = phno;
        this.dob = dob;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

}
