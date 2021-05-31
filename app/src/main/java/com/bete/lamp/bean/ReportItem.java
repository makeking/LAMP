package com.bete.lamp.bean;

public class ReportItem {
    private String name;
    private String resultTip;
    private String result;
    private String ref;
    private String danwei;

    public ReportItem(String resultTip, String result, String ref, String danwei) {
        this.name = "";
        this.resultTip = resultTip;
        this.result = result;
        this.ref = ref;
        this.danwei = danwei;
    }

    public ReportItem(String name, String resultTip, String result, String ref, String danwei) {
        this.name = name;
        this.resultTip = resultTip;
        this.result = result;
        this.ref = ref;
        this.danwei = danwei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResultTip() {
        return resultTip;
    }

    public void setResultTip(String resultTip) {
        this.resultTip = resultTip;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei;
    }
}
