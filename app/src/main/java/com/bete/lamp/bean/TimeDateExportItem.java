package com.bete.lamp.bean;

import com.utils.FileCommonUtil;

import java.io.File;

public class TimeDateExportItem {
    private int ID;
    private String CheckTime;
    private String FileName;
    private File file;

    public TimeDateExportItem(int ID, String CheckTime, String FileName) {
        this.ID = ID;
        this.CheckTime = CheckTime;
        this.file = FileCommonUtil.getFileByPath(FileName);
        this.FileName = file.getName();
    }

    public TimeDateExportItem(int ID, String CheckTime, File file) {
        this.ID = ID;
        this.CheckTime = CheckTime;
        this.file = file;
        this.FileName = file.getName();
    }

    public TimeDateExportItem(String CheckTime, File file) {
        this.ID = 0;
        this.CheckTime = CheckTime;
        this.file = file;
        this.FileName = file.getName();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getCheckTime() {
        return CheckTime;
    }

    public void setCheckTime(String checkTime) {
        CheckTime = checkTime;
    }
}
