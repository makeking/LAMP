package com.bete.lamp.bean;

public class BlockItem {
    public int ID=0;
    public int fromPosition = 0;
    public int endPosition = 0;


    public BlockItem(int ID) {
        this.ID = ID;
    }

    public BlockItem(int ID, int fromPosition, int endPosition) {
        this.ID = ID;
        this.fromPosition = fromPosition;
        this.endPosition = endPosition;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(int fromPosition) {
        this.fromPosition = fromPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }
}
