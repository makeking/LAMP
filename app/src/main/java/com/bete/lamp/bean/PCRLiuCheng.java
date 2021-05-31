package com.bete.lamp.bean;

import java.util.LinkedList;

public class PCRLiuCheng {
    private LinkedList<SegmentEntity> segmentEntityLinkedList = new LinkedList<>();

    public PCRLiuCheng() {
    }

    public PCRLiuCheng(LinkedList<SegmentEntity> segmentEntityLinkedList) {
        this.segmentEntityLinkedList = segmentEntityLinkedList;
    }

    public LinkedList<SegmentEntity> getSegmentEntityLinkedList() {
        return segmentEntityLinkedList;
    }

    public void setSegmentEntityLinkedList(LinkedList<SegmentEntity> segmentEntityLinkedList) {
        this.segmentEntityLinkedList = segmentEntityLinkedList;
    }
}
