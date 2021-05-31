package com.bete.lamp.bean;

public class UpdateVersion {
    private String versionName;
    private int versionCode;
    private String content;
    private String url;
    private String fileName;

    public UpdateVersion(String versionName, int versionCode, String content, String url, String fileName) {
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.content = content;
        this.url = url;
        this.fileName = fileName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return fileName;
    }

    public void setFilename(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "UpdateVersion{" +
                "versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
