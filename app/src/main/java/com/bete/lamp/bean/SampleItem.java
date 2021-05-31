package com.bete.lamp.bean;

public class SampleItem {
    public int ID;
    public int type = 0;
    public double ct = 0;
    public double nongdu = 0;
    public int gluanglu = 0;
    public int suanfa = 0;

    public SampleItem(int ID) {
        this.ID = ID;
    }

    public SampleItem(int ID, int type) {
        this.ID = ID;
        this.type = type;
    }

    public SampleItem(int ID, int type, double nongdu) {
        this.ID = ID;
        this.type = type;
        this.nongdu = nongdu;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getNongdu() {
        return nongdu;
    }

    public void setNongdu(double nongdu) {
        this.nongdu = nongdu;
    }

    public double getCt() {
        return ct;
    }

    public void setCt(double ct) {
        this.ct = ct;
    }

}
