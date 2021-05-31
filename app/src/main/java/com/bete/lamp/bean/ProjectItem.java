package com.bete.lamp.bean;

public class ProjectItem {
    public int id=0;
    public String Name = "";
    public String fangShi = "";
    public String lot ="";
    public String limit="";

    public ProjectItem(String Name) {
        this.Name = Name;
    }

    public ProjectItem(String name, String fangShi, String lot, String limit) {
        Name = name;
        this.fangShi = fangShi;
        this.lot = lot;
        this.limit = limit;
    }

    public ProjectItem(int id, String name, String fangShi, String lot, String limit) {
        this.id = id;
        Name = name;
        this.fangShi = fangShi;
        this.lot = lot;
        this.limit = limit;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFangShi() {
        return fangShi;
    }

    public void setFangShi(String fangShi) {
        this.fangShi = fangShi;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }
}
