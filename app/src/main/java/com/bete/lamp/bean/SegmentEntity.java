package com.bete.lamp.bean;

import java.util.LinkedList;

public class SegmentEntity {

    // cycle
    public int cycleCnt=1;

    // list
    public LinkedList<StepEntity> stepEntityLinkedList = new LinkedList<>();

    public SegmentEntity() {
        cycleCnt=1;
        if(stepEntityLinkedList.size()==0)
        stepEntityLinkedList.add(new StepEntity());
    }

    public SegmentEntity(int cycleCnt, LinkedList<StepEntity> stepEntityLinkedList) {
        this.cycleCnt = cycleCnt;
        this.stepEntityLinkedList = stepEntityLinkedList;
    }

    public int getCycleCnt() {
        return cycleCnt;
    }

    public void setCycleCnt(int cycleCnt) {
        this.cycleCnt = cycleCnt;
    }

    public LinkedList<StepEntity> getStepEntityLinkedList() {
        return stepEntityLinkedList;
    }

    public void setStepEntityLinkedList(LinkedList<StepEntity> stepEntityLinkedList) {
        this.stepEntityLinkedList = stepEntityLinkedList;
    }
}
