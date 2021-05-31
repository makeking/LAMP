package com.bete.lamp.bean;

public class ThermalItem {
    private int ID;
    private String info;

    public ThermalItem(int ID, String info) {
        this.ID = ID;
        this.info = info;
    }

    public ThermalItem(String info) {
        this.ID = 0;
        this.info = info;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
