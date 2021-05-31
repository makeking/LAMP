package com.bete.lamp.message;

public class LiuChengProcessValueEvent {
    private int processValue;

    public int getProcessValue() {
        return processValue;
    }

    public void setProcessValue(int processValue) {
        this.processValue = processValue;
    }

    @Override
    public String toString() {
        return "LiuChengProcessValueEvent{" +
                "processValue='" + String.valueOf(processValue) + '\'' +
                '}';
    }
}
