package com.bete.lamp.bean;

public class StepEntity {

    // 变温前
    public float tempBefore=55;

    // 变温后
    public float tempAfter=55;

    // 延时
    public int timemsec=1;

    // 采样
    public boolean readEnable=false;

    // 加速度
    public float changeSpeed=0;


    public StepEntity() {
    }

    public StepEntity(float tempBefore, float tempAfter, int timemsec, boolean readEnable, float changeSpeed) {
        this.tempBefore = tempBefore;
        this.tempAfter = tempAfter;
        this.timemsec = timemsec;
        this.readEnable = readEnable;
        this.changeSpeed = changeSpeed;
    }

    public float getTempBefore() {
        return tempBefore;
    }

    public void setTempBefore(float tempBefore) {
        this.tempBefore = tempBefore;
    }

    public float getTempAfter() {
        return tempAfter;
    }

    public void setTempAfter(float tempAfter) {
        this.tempAfter = tempAfter;
    }

    public int getTimemsec() {
        return timemsec;
    }

    public void setTimemsec(int timemsec) {
        this.timemsec = timemsec;
    }

    public boolean isReadEnable() {
        return readEnable;
    }

    public void setReadEnable(boolean readEnable) {
        this.readEnable = readEnable;
    }

    public float getChangeSpeed() {
        return changeSpeed;
    }

    public void setChangeSpeed(float changeSpeed) {
        this.changeSpeed = changeSpeed;
    }
}
