package com.bete.lamp.bean;
public class ItemQc {
    public String name;
    public int itemtype;
    public int daYuShiNeng;
    public int errHandle;
    public int kongweino0;
    public int kongweino1;
    public int guangluno;
    public int refvalue;

    public ItemQc() {
        this.name="";
        this.itemtype=0;
        this.daYuShiNeng=0;
        this.errHandle=0;
        this.kongweino0=0;
        this.kongweino1=0;
        this.guangluno=0;
        this.refvalue=0;
    }

    public ItemQc(String name, int itemtype, int daYuShiNeng, int errHandle, int kongweino0, int kongweino1, int guangluno, int refvalue) {
        this.name = name;
        this.itemtype = itemtype;
        this.daYuShiNeng = daYuShiNeng;
        this.errHandle = errHandle;
        this.kongweino0 = kongweino0;
        this.kongweino1 = kongweino1;
        this.guangluno = guangluno;
        this.refvalue = refvalue;
    }

    public ItemQc(ItemQc temp)
    {
        this.name = temp.name;
        this.itemtype = temp.itemtype;
        this.daYuShiNeng = temp.daYuShiNeng;
        this.errHandle = temp.errHandle;
        this.kongweino0 = temp.kongweino0;
        this.kongweino1 = temp.kongweino1;
        this.guangluno = temp.guangluno;
        this.refvalue = temp.refvalue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemtype() {
        return itemtype;
    }

    public void setItemtype(int itemtype) {
        this.itemtype = itemtype;
    }

    public int getDaYuShiNeng() {
        return daYuShiNeng;
    }

    public void setDaYuShiNeng(int daYuShiNeng) {
        this.daYuShiNeng = daYuShiNeng;
    }

    public int getErrHandle() {
        return errHandle;
    }

    public void setErrHandle(int errHandle) {
        this.errHandle = errHandle;
    }

    public int getKongweino0() {
        return kongweino0;
    }

    public void setKongweino0(int kongweino0) {
        this.kongweino0 = kongweino0;
    }

    public int getKongweino1() {
        return kongweino1;
    }

    public void setKongweino1(int kongweino1) {
        this.kongweino1 = kongweino1;
    }

    public int getGuangluno() {
        return guangluno;
    }

    public void setGuangluno(int guangluno) {
        this.guangluno = guangluno;
    }

    public int getRefvalue() {
        return refvalue;
    }

    public void setRefvalue(int refvalue) {
        this.refvalue = refvalue;
    }

    @Override
    public String toString() {
        return "ItemQc{" +
                "name='" + name + '\'' +
                ", itemtype=" + itemtype +
                ", daYuShiNeng=" + daYuShiNeng +
                ", errHandle=" + errHandle +
                ", kongweino0=" + kongweino0 +
                ", kongweino1=" + kongweino1 +
                ", guangluno=" + guangluno +
                ", refvalue=" + refvalue +
                '}';
    }
}
