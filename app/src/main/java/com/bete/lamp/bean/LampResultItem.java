package com.bete.lamp.bean;

public class LampResultItem {
    public int ID;
    public float weizhi=0;
    public String bianhao="";
    public String projectname="";
    public String jieguo="";
    public double ct=0;

    public LampResultItem(int ID, float weizhi, double ct) {
        this.ID = ID;
        this.weizhi = weizhi;
        this.ct = ct;
    }

    public LampResultItem(int ID, float weizhi) {
        this.ID = ID;
        this.weizhi = weizhi;
    }

    public LampResultItem(int ID) {
        this.ID = ID;
    }

    public LampResultItem(int ID, float weizhi, String bianhao, String projectname, String jieguo, double ct) {
        this.ID = ID;
        this.weizhi = weizhi;
        this.bianhao = bianhao;
        this.projectname = projectname;
        this.jieguo = jieguo;
        this.ct = ct;
    }

    public void setLampResultItem(int ID, float weizhi, String bianhao, String projectname, String jieguo, double ct) {
        this.ID = ID;
        this.weizhi = weizhi;
        this.bianhao = bianhao;
        this.projectname = projectname;
        this.jieguo = jieguo;
        this.ct = ct;
    }

    public void setLampResultItem(LampResultItem lampResultItem) {
        this.ID = lampResultItem.ID;
        this.weizhi = lampResultItem.weizhi;
        this.bianhao = lampResultItem.bianhao;
        this.projectname = lampResultItem.projectname;
        this.jieguo = lampResultItem.jieguo;
        this.ct = lampResultItem.ct;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getJieguo() {
        return jieguo;
    }

    public void setJieguo(String jieguo) {
        this.jieguo = jieguo;
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
