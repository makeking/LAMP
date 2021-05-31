package com.bete.lamp.bean;
public class ItemTest {
    public int itemtype;
    public int K;
    public int B;
    public int fangfa;
    public int kongweino;
    public int guangluno;
    public int ARG0;
    public int ARG1;
    public int ARG2;
    public int ARG3;
    public int refvalue0;
    public int refvalue1;
    public int danwei;

    public ItemTest() {
        this.itemtype=0;
        this.K=0;
        this.B=0;
        this.fangfa=0;
        this.kongweino=0;
        this.guangluno=0;
        this.ARG0=0;
        this.ARG1=0;
        this.ARG2=0;
        this.ARG3=0;
        this.refvalue0=0;
        this.refvalue1=0;
        this.danwei=0;

    }

    public ItemTest(int itemtype, int k, int b, int fangfa, int kongweino, int guangluno, int ARG0, int ARG1, int ARG2, int ARG3, int refvalue0, int refvalue1, int danwei) {
        this.itemtype = itemtype;
        K = k;
        B = b;
        this.fangfa = fangfa;
        this.kongweino = kongweino;
        this.guangluno = guangluno;
        this.ARG0 = ARG0;
        this.ARG1 = ARG1;
        this.ARG2 = ARG2;
        this.ARG3 = ARG3;
        this.refvalue0 = refvalue0;
        this.refvalue1 = refvalue1;
        this.danwei = danwei;
    }

    public ItemTest(ItemTest temp) {
        this.itemtype = temp.itemtype;
        this.K = temp.K;
        this.B = temp.B;
        this.fangfa = temp.fangfa;
        this.kongweino = temp.kongweino;
        this.guangluno = temp.guangluno;
        this.ARG0 = temp.ARG0;
        this.ARG1 = temp.ARG1;
        this.ARG2 = temp.ARG2;
        this.ARG3 = temp.ARG3;
        this.refvalue0 = temp.refvalue0;
        this.refvalue1 = temp.refvalue1;
        this.danwei = temp.danwei;
    }

    public int getItemtype() {
        return itemtype;
    }

    public void setItemtype(int itemtype) {
        this.itemtype = itemtype;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public int getFangfa() {
        return fangfa;
    }

    public void setFangfa(int fangfa) {
        this.fangfa = fangfa;
    }

    public int getKongweino() {
        return kongweino;
    }

    public void setKongweino(int kongweino) {
        this.kongweino = kongweino;
    }

    public int getGuangluno() {
        return guangluno;
    }

    public void setGuangluno(int guangluno) {
        this.guangluno = guangluno;
    }

    public int getARG0() {
        return ARG0;
    }

    public void setARG0(int ARG0) {
        this.ARG0 = ARG0;
    }

    public int getARG1() {
        return ARG1;
    }

    public void setARG1(int ARG1) {
        this.ARG1 = ARG1;
    }

    public int getARG2() {
        return ARG2;
    }

    public void setARG2(int ARG2) {
        this.ARG2 = ARG2;
    }

    public int getARG3() {
        return ARG3;
    }

    public void setARG3(int ARG3) {
        this.ARG3 = ARG3;
    }

    public int getRefvalue0() {
        return refvalue0;
    }

    public void setRefvalue0(int refvalue0) {
        this.refvalue0 = refvalue0;
    }

    public int getRefvalue1() {
        return refvalue1;
    }

    public void setRefvalue1(int refvalue1) {
        this.refvalue1 = refvalue1;
    }

    public int getDanwei() {
        return danwei;
    }

    public void setDanwei(int danwei) {
        this.danwei = danwei;
    }

    @Override
    public String toString() {
        return "ItemTest{" +
                "itemtype=" + itemtype +
                ", K=" + K +
                ", B=" + B +
                ", fangfa=" + fangfa +
                ", kongweino=" + kongweino +
                ", guangluno=" + guangluno +
                ", ARG0=" + ARG0 +
                ", ARG1=" + ARG1 +
                ", ARG2=" + ARG2 +
                ", ARG3=" + ARG3 +
                ", refvalue0=" + refvalue0 +
                ", refvalue1=" + refvalue1 +
                ", danwei=" + danwei +
                '}';
    }
}
