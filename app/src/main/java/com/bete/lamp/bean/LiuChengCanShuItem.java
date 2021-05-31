package com.bete.lamp.bean;

public class LiuChengCanShuItem {
    private String name;
    private int type=0;
    private int dir0=0;
    private int speed0=0;
    private int time0=0;
    private int cnt=0;
    private int dir1=0;
    private int speed1=0;
    private int time1=0;
    private int length=0;

    public LiuChengCanShuItem() {
    }

    public LiuChengCanShuItem(String name, int type, int dir0, int speed0, int time0, int cnt, int dir1, int speed1, int time1,  int length) {
        this.name = name;
        this.type = type;
        this.dir0 = dir0;
        this.speed0 = speed0;
        this.time0 = time0;
        this.cnt = cnt;
        this.dir1 = dir1;
        this.speed1 = speed1;
        this.time1 = time1;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDir0() {
        return dir0;
    }

    public void setDir0(int dir0) {
        this.dir0 = dir0;
    }

    public int getSpeed0() {
        return speed0;
    }

    public void setSpeed0(int speed0) {
        this.speed0 = speed0;
    }

    public int getTime0() {
        return time0;
    }

    public void setTime0(int time0) {
        this.time0 = time0;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getDir1() {
        return dir1;
    }

    public void setDir1(int dir1) {
        this.dir1 = dir1;
    }

    public int getSpeed1() {
        return speed1;
    }

    public void setSpeed1(int speed1) {
        this.speed1 = speed1;
    }

    public int getTime1() {
        return time1;
    }

    public void setTime1(int time1) {
        this.time1 = time1;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

//    int writeCanShu(LiuChengCanShuItem canshu,QSettings *m_IniFile)
//    {
//
//
//    }
}
