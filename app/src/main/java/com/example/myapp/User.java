package com.example.myapp;

public class User {
    String usr;
    String pas;
    public User(String usr, String pas) {
        this.usr = usr;
        this.pas = pas;
    }

    public String getUsr() {
        return usr;
    }

    public String getPas() {
        return pas;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public void setPas(String pas) {
        this.pas = pas;
    }
}
/*
public class User {
    String id;
    String Name;

    public User(String id, String name) {
        this.id = id;
        Name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.Name;
    }
}
*/