package com.bete.lamp.bean;

public class CheckBoxItem {
    public String Name="1";
    public boolean isCheck=false;
    public boolean isShow=true;
    public CheckBoxItem() {
        this.isShow = true;
    }

    public CheckBoxItem(String name, boolean isCheck) {
        Name = name;
        this.isCheck = isCheck;
        this.isShow = true;
    }

    public CheckBoxItem(String name, boolean isCheck, boolean isShow) {
        Name = name;
        this.isCheck = isCheck;
        this.isShow = isShow;
    }

    public CheckBoxItem(String name) {
        Name = name;
        this.isCheck = false;
        this.isShow = true;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
