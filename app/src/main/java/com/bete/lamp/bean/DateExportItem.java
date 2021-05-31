package com.bete.lamp.bean;

import java.io.File;

public class DateExportItem {
    private int ID;
    private String FileName;
    private File file;

    public DateExportItem(int ID, File file) {
        this.ID = ID;
        this.file = file;
        this.FileName = file.getName();
    }

    public DateExportItem(File file) {
        this.ID = 0;
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
}
