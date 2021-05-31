package com.bete.lamp.message;

public class StateCodeEvent {
    private int codeState;

    public int getCodeState() {
        return codeState;
    }

    public void setCodeState(int codeState) {
        this.codeState = codeState;
    }

    @Override
    public String toString() {
        return "StateCodeEvent{" +
                "codeState='" + String.valueOf(codeState) + '\'' +
                '}';
    }
}
