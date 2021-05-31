package com.bete.lamp.bean;

public class EepromItem {
    //public int ID;
    public String type="";//1 Int=2； 2 Float=4； 3 Double=8； 4 Short= 2；5 Char =1 ； 6 Unsigned int=2； 7 Unsigned short=2
    public String addr="";//int
    public String value="";//int
    public String len="";//int
    public String name="";
    public String prop="";
    public boolean isAble= false;

    public EepromItem() {
    }

    public EepromItem(String type, String addr, String value, String len, String name, String prop, boolean isAble) {
        this.type = type;
        this.addr = addr;
        this.value = value;
        this.len = len;
        this.name = name;
        this.prop = prop;
        this.isAble = isAble;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLen() {
        return len;
    }

    public void setLen(String len) {
        this.len = len;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAble() {
        return isAble;
    }

    public void setAble(boolean able) {
        isAble = able;
    }
}
