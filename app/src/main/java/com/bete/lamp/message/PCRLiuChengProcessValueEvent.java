package com.bete.lamp.message;

import com.myutils.GlobalDate;

import org.greenrobot.eventbus.EventBus;

public class PCRLiuChengProcessValueEvent {
    private int index;
    private GlobalDate.EventType type;//0:状态改变，1：data改变，2：剩余时间改变

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public GlobalDate.EventType getType() {
        return type;
    }

    public void setType(GlobalDate.EventType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PCRLiuChengProcessValueEvent{" +
                "index='" + String.valueOf(index) + '\'' +
                "type='" + String.valueOf(type) + '\'' +
                '}';
    }
}
