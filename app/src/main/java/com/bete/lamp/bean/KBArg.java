package com.bete.lamp.bean;

public class KBArg {
    public String name;
    public int itemtype;
    public int inputX;
    public int inputY;
    public int jiaoGuangXIndex=1;
    public double K =1;
    public double B =0;
    public double refmin =0;
    public double refmax = 0;
    public int methodType =1;
    public int kongwei =1;
    public int ARG0 =0;
    public int ARG1 =0;
    public int ARG2 =0;
    public int ARG3 =0;
    public int hangsFilter =0;
    public String danwei="";
    public int guanglu=1;

    public KBArg() {
    }

    public KBArg(String name, int itemtype, int inputX, int inputY, int jiaoGuangXIndex, double k, double b, double refmin, double refmax, int methodType, int kongwei, int ARG0, int ARG1, int ARG2, int ARG3, int hangsFilter, String danwei, int guanglu) {
        this.name = name;
        this.itemtype = itemtype;
        this.inputX = inputX;
        this.inputY = inputY;
        this.jiaoGuangXIndex = jiaoGuangXIndex;
        K = k;
        B = b;
        this.refmin = refmin;
        this.refmax = refmax;
        this.methodType = methodType;
        this.kongwei = kongwei;
        this.ARG0 = ARG0;
        this.ARG1 = ARG1;
        this.ARG2 = ARG2;
        this.ARG3 = ARG3;
        this.hangsFilter = hangsFilter;
        this.danwei = danwei;
        this.guanglu = guanglu;
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

    public int getInputX() {
        return inputX;
    }

    public void setInputX(int inputX) {
        this.inputX = inputX;
    }

    public int getInputY() {
        return inputY;
    }

    public void setInputY(int inputY) {
        this.inputY = inputY;
    }

    public int getJiaoGuangXIndex() {
        return jiaoGuangXIndex;
    }

    public void setJiaoGuangXIndex(int jiaoGuangXIndex) {
        this.jiaoGuangXIndex = jiaoGuangXIndex;
    }

    public double getK() {
        return K;
    }

    public void setK(double k) {
        K = k;
    }

    public double getB() {
        return B;
    }

    public void setB(double b) {
        B = b;
    }

    public double getRefmin() {
        return refmin;
    }

    public void setRefmin(double refmin) {
        this.refmin = refmin;
    }

    public double getRefmax() {
        return refmax;
    }

    public void setRefmax(double refmax) {
        this.refmax = refmax;
    }

    public int getMethodType() {
        return methodType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    public int getKongwei() {
        return kongwei;
    }

    public void setKongwei(int kongwei) {
        this.kongwei = kongwei;
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

    public int getHangsFilter() {
        return hangsFilter;
    }

    public void setHangsFilter(int hangsFilter) {
        this.hangsFilter = hangsFilter;
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }

    public int getGuanglu() {
        return guanglu;
    }

    public void setGuanglu(int guanglu) {
        this.guanglu = guanglu;
    }

    @Override
    public String toString() {
        return "KBArg{" +
                "name='" + name + '\'' +
                ", itemtype=" + itemtype +
                ", inputX=" + inputX +
                ", inputY=" + inputY +
                ", jiaoGuangXIndex=" + jiaoGuangXIndex +
                ", K=" + K +
                ", B=" + B +
                ", refmin=" + refmin +
                ", refmax=" + refmax +
                ", methodType=" + methodType +
                ", kongwei=" + kongwei +
                ", ARG0=" + ARG0 +
                ", ARG1=" + ARG1 +
                ", ARG2=" + ARG2 +
                ", ARG3=" + ARG3 +
                ", hangsFilter=" + hangsFilter +
                ", danwei='" + danwei + '\'' +
                ", guanglu=" + guanglu +
                '}';
    }
}
