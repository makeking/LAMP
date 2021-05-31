package com.bete.lamp.bean;
public class ItemStd {
    public int itemtype = 0;
    public int kongweino =0;

    public ItemStd() {
        this.itemtype = itemtype;
        this.kongweino =itemtype;
    }

    public ItemStd(int itemtype, int kongweino) {
        this.itemtype = itemtype;
        this.kongweino = kongweino;
    }

    public ItemStd(ItemStd temp) {
        this.itemtype = temp.itemtype;
        this.kongweino = temp.kongweino;
    }

    public int getItemtype() {
        return itemtype;
    }

    public void setItemtype(int itemtype) {
        this.itemtype = itemtype;
    }

    public int getKongweino() {
        return kongweino;
    }

    public void setKongweino(int kongweino) {
        this.kongweino = kongweino;
    }

    @Override
    public String toString() {
        return "ItemStd{" +
                "itemtype=" + itemtype +
                ", kongweino=" + kongweino +
                '}';
    }
}
