package com.bete.lamp.bean;

public class PCRLiuChengCanShuItem implements Cloneable {
    public int ID = 0;
    public boolean isExe = false;
    public double targetTemp = 35;
    public int holdTime = 0;
    public boolean isRead = false;
    public int intervalTime = 5;

    public PCRLiuChengCanShuItem() {
    }


    public PCRLiuChengCanShuItem(int ID) {
        this.ID = ID;
    }

    public PCRLiuChengCanShuItem(int ID, boolean isExe, double targetTemp, int holdTime, boolean isRead, int intervalTime) {
        this.ID = ID;
        this.isExe = isExe;
        this.targetTemp = targetTemp;
        this.holdTime = holdTime;
        this.isRead = isRead;
        this.intervalTime = intervalTime;
    }

    public PCRLiuChengCanShuItem(boolean isExe, double targetTemp, int holdTime, boolean isRead, int intervalTime) {
        this.isExe = isExe;
        this.targetTemp = targetTemp;
        this.holdTime = holdTime;
        this.isRead = isRead;
        this.intervalTime = intervalTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isExe() {
        return isExe;
    }

    public void setExe(boolean exe) {
        isExe = exe;
    }


    public double getTargetTemp() {
        return targetTemp;
    }

    public void setTargetTemp(double targetTemp) {
        this.targetTemp = targetTemp;
    }

    public int getHoldTime() {
        return holdTime;
    }

    public void setHoldTime(int holdTime) {
        this.holdTime = holdTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    @Override
    public Object clone() {
        PCRLiuChengCanShuItem pcrLiuChengCanShuItem = new PCRLiuChengCanShuItem(ID,isExe, targetTemp, holdTime, isRead,intervalTime);
        return pcrLiuChengCanShuItem;
    }
}
