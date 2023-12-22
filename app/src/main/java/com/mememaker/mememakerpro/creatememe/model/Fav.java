package com.mememaker.mememakerpro.creatememe.model;

public class Fav {

    private String value;

    public Fav(String value, int id) {
        this.value = value;
        this.id = id;
    }

    public Fav() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
}
