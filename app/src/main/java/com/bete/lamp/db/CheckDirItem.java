package com.bete.lamp.db;

import org.litepal.crud.LitePalSupport;

public class CheckDirItem extends LitePalSupport {
    //    @Column(unique = true, defaultValue = "unknown")
    public String samplenumber;
    public String checkdatetime;
    public String dir;

    public CheckDirItem(String samplenumber, String checkdatetime, String dir) {
        this.samplenumber = samplenumber;
        this.checkdatetime = checkdatetime;
        this.dir = dir;
    }

    public String getSamplenumber() {
        return samplenumber;
    }

    public void setSamplenumber(String samplenumber) {
        this.samplenumber = samplenumber;
    }

    public String getCheckdatetime() {
        return checkdatetime;
    }

    public void setCheckdatetime(String checkdatetime) {
        this.checkdatetime = checkdatetime;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return samplenumber+","+checkdatetime+","+dir+"\n";
    }
}
