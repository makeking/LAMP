package com.bete.lamp.message;

import java.util.Arrays;

public class ScanDateValueEvent {
    private byte[] scandataBytes;
    private int length;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte[] getScandataBytes() {
        return scandataBytes;
    }

    public void setScandataBytes(byte[] scandataBytes) {
        this.scandataBytes = scandataBytes;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ScanDateValueEvent(byte[] scandataBytes, int length, int type) {
        this.scandataBytes = scandataBytes;
        this.length = length;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ScanDateValueEvent{" +
                "scandataBytes=" + Arrays.toString(scandataBytes) +
                ", length=" + length +
                ", type=" + type +
                '}';
    }

}
