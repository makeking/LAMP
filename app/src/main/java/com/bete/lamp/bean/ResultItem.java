package com.bete.lamp.bean;

public class ResultItem {
    public int ID;
    public int type=0;
    public float weizhi=0;
    public double nongdu=0;
    public double ct=0;
    public int yiyang = 0;

    public ResultItem(int ID, float weizhi, double ct) {
        this.ID = ID;
        this.weizhi = weizhi;
        this.ct = ct;
    }

    public ResultItem(int ID) {
        this.ID = ID;
    }

    public ResultItem(int ID, int type) {
        this.ID = ID;
        this.type = type;
    }

    public ResultItem(int ID, float weizhi,int type, double nongdu) {
        this.ID = ID;
        this.weizhi=weizhi;
        this.type = type;
        this.nongdu = nongdu;
    }

    public ResultItem(int ID,float weizhi, int type, double nongdu, double ct) {
        this.ID = ID;
        this.weizhi=weizhi;
        this.type = type;
        this.nongdu = nongdu;
        this.ct = ct;
    }

    public ResultItem(int ID, float weizhi,int type, double nongdu, double ct, int yiyang) {
        this.ID = ID;
        this.weizhi=weizhi;
        this.type = type;
        this.nongdu = nongdu;
        this.ct = ct;
        this.yiyang = yiyang;
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

    public int getYiyang() {
        return yiyang;
    }

    public void setYiyang(int yiyang) {
        this.yiyang = yiyang;
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


    public float getWeizhi() {
        return weizhi;
    }

    public void setWeizhi(float weizhi) {
        this.weizhi = weizhi;
    }


}
