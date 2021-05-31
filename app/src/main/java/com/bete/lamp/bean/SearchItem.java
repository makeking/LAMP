package com.bete.lamp.bean;

public class SearchItem {
    private int ID;
    private String sampleNO;
    private String checkDateTime;
    private String abState;
    public SearchItem(int ID, String sampleNO, String checkDateTime, String abState) {
        this.ID = ID;
        this.sampleNO = sampleNO;
        this.checkDateTime = checkDateTime;
        this.abState = abState;
    }

    public String getAbState() {
        return abState;
    }

    public void setAbState(String abState) {
        this.abState = abState;
    }

    public SearchItem(String sampleNO , String checkDateTime, String abState) {
        this.ID = 0;
        this.sampleNO = sampleNO;
        this.checkDateTime = checkDateTime;
        this.abState = abState;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setSampleNO(String sampleNO) {
        this.sampleNO = sampleNO;
    }

    public void setCheckDateTime(String checkDateTime) {
        this.checkDateTime = checkDateTime;
    }

    public int getID() {
        return ID;
    }

    public String getSampleNO() {
        return sampleNO;
    }

    public String getCheckDateTime() {
        return checkDateTime;
    }
}
