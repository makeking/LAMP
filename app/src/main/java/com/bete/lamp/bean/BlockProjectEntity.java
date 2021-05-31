package com.bete.lamp.bean;

import java.util.LinkedList;

public class BlockProjectEntity {
    public int id=0;
    public LinkedList<BlockItem> blockItemLinkedList = new LinkedList<>();

    public BlockProjectEntity() {
    }

    public BlockProjectEntity(LinkedList<BlockItem> blockItemLinkedList) {
        this.blockItemLinkedList = blockItemLinkedList;
    }

    public BlockProjectEntity(int id, LinkedList<BlockItem> blockItemLinkedList) {
        this.id = id;
        this.blockItemLinkedList = blockItemLinkedList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LinkedList<BlockItem> getBlockItemLinkedList() {
        return blockItemLinkedList;
    }

    public void setBlockItemLinkedList(LinkedList<BlockItem> blockItemLinkedList) {
        this.blockItemLinkedList = blockItemLinkedList;
    }
}
