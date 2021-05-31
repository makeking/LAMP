package com.bete.lamp.bean;

import java.util.Arrays;

public class BarRareData {
    public int bartype;
    public int productyear;
    public int productmonth;
    public int productday;
    public int num;
    public int limityear;
    public int limitmonth;
    public int limitday;
    public int isi;
    public ItemStd itemstd[];
    public ItemQc itemqc[];
    public ItemTest itemtest[];
    public ItemTest0 itemtest0[];
    public BarRareData() {
        this.bartype=1;
        this.productyear=2019;
        this.productmonth=1;
        this.productday=1;
        this.num=1;
        this.limityear=2019;
        this.limitmonth=12;
        this.limitday=31;
        isi = 0;
        itemstd = new ItemStd[5];
        for(int i=0;i<5;i++)
            this.itemstd[i] = new ItemStd();
        itemqc = new ItemQc[8];
        for(int i=0;i<8;i++)
            this.itemqc[i] = new ItemQc();
        itemtest0 = new ItemTest0[3];
        for(int i=0;i<3;i++)
            this.itemtest0[i] = new ItemTest0();
        itemtest = new ItemTest[10];
        for(int i=0;i<10;i++)
            this.itemtest[i] = new ItemTest();
    }

    public BarRareData(BarRareData temp) {
        this.bartype=temp.bartype;
        this.productyear=temp.productyear;
        this.productmonth=temp.productmonth;
        this.productday=temp.productday;
        this.num=temp.num;
        this.limityear=temp.limityear;
        this.limitmonth=temp.limitmonth;
        this.limitday=temp.limitday;
        this.isi=temp.isi;
        itemstd = new ItemStd[5];
        for(int i=0;i<5;i++) {
            this.itemstd[i] = new ItemStd(temp.itemstd[i]);
        }
        itemqc = new ItemQc[8];
        for(int i=0;i<8;i++)
            this.itemqc[i] = new ItemQc(temp.itemqc[i]);

        itemtest0 = new ItemTest0[3];
        for(int i=0;i<3;i++)
            this.itemtest0[i] = new ItemTest0(temp.itemtest0[i]);

        itemtest = new ItemTest[10];
        for(int i=0;i<10;i++)
            this.itemtest[i] = new ItemTest(temp.itemtest[i]);
    }

    public int getBartype() {
        return bartype;
    }

    public void setBartype(int bartype) {
        this.bartype = bartype;
    }

    public int getProductyear() {
        return productyear;
    }

    public void setProductyear(int productyear) {
        this.productyear = productyear;
    }

    public int getProductmonth() {
        return productmonth;
    }

    public void setProductmonth(int productmonth) {
        this.productmonth = productmonth;
    }

    public int getProductday() {
        return productday;
    }

    public void setProductday(int productday) {
        this.productday = productday;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getLimityear() {
        return limityear;
    }

    public void setLimityear(int limityear) {
        this.limityear = limityear;
    }

    public int getLimitmonth() {
        return limitmonth;
    }

    public void setLimitmonth(int limitmonth) {
        this.limitmonth = limitmonth;
    }

    public int getLimitday() {
        return limitday;
    }

    public void setLimitday(int limitday) {
        this.limitday = limitday;
    }

    public int getIsi() {
        return isi;
    }

    public void setIsi(int isi) {
        this.isi = isi;
    }

    public ItemStd[] getItemstd() {
        return itemstd;
    }

    public void setItemstd(ItemStd[] itemstd) {
        this.itemstd = itemstd;
    }

    public ItemQc[] getItemqc() {
        return itemqc;
    }

    public void setItemqc(ItemQc[] itemqc) {
        this.itemqc = itemqc;
    }

    public ItemTest[] getItemtest() {
        return itemtest;
    }

    public void setItemtest(ItemTest[] itemtest) {
        this.itemtest = itemtest;
    }

    public ItemTest0[] getItemtest0() {
        return itemtest0;
    }

    public void setItemtest0(ItemTest0[] itemtest0) {
        this.itemtest0 = itemtest0;
    }

    @Override
    public String toString() {
        return "BarRareData{" +
                "bartype=" + bartype +
                ", productyear=" + productyear +
                ", productmonth=" + productmonth +
                ", productday=" + productday +
                ", num=" + num +
                ", limityear=" + limityear +
                ", limitmonth=" + limitmonth +
                ", limitday=" + limitday +
                ", isi=" + isi +
                ", itemstd=" + Arrays.toString(itemstd) +
                ", itemqc=" + Arrays.toString(itemqc) +
                ", itemtest0=" + Arrays.toString(itemtest0) +
                ", itemtest=" + Arrays.toString(itemtest) +
                '}';
    }
}
