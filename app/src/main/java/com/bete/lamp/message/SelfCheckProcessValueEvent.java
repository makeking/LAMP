package com.bete.lamp.message;

public class SelfCheckProcessValueEvent {
    private int processValue;

    public int getProcessValue() {
        return processValue;
    }

    public void setProcessValue(int processValue) {
        this.processValue = processValue;
    }

    @Override
    public String toString() {
        return "SelfCheckProcessValueEvent{" +
                "processValue='" + String.valueOf(processValue) + '\'' +
                '}';
    }
}