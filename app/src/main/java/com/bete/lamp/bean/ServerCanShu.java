package com.bete.lamp.bean;

public class ServerCanShu {
    private String ipaddr="172.16.16.181";
    private String port="8080";
    private int timeout=20000;
    private String updatedir="/lamp-web/update/";
    private String downloaddir="/lamp-web/download/";

    public ServerCanShu() {
    }

    public ServerCanShu(String ipaddr, String port, int timeout, String updatedir, String downloaddir) {
        this.ipaddr = ipaddr;
        this.port = port;
        this.timeout = timeout;
        this.updatedir = updatedir;
        this.downloaddir = downloaddir;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getUpdatedir() {
        return updatedir;
    }

    public void setUpdatedir(String updatedir) {
        this.updatedir = updatedir;
    }

    public String getDownloaddir() {
        return downloaddir;
    }

    public void setDownloaddir(String downloaddir) {
        this.downloaddir = downloaddir;
    }
}
