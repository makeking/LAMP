package com.bete.lamp.bean;

public class HexRec {
    private long addr;
    private long len;
    private String buf;

    public HexRec() {

    }

    public long getAddr() {
        return addr;
    }

    public void setAddr(long addr) {
        this.addr = addr;
    }

    public long getLen() {
        return len;
    }

    public void setLen(long len) {
        this.len = len;
    }

    public String getBuf() {
        return buf;
    }

    public void setBuf(String buf) {
        this.buf = buf;
    }

}