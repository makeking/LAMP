package com.bete.lamp.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class UserPermission extends LitePalSupport {
    @Column(unique = true, defaultValue = "unknown")
    private String name;
    @Column(defaultValue = "000000")
    private String mima;
    private int level;

    public void setName(String name) {
        this.name = name;
    }

    public void setMima(String mima) {
        this.mima = mima;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getMima() {
        return mima;
    }

    public int getLevel() {
        return level;
    }
}
