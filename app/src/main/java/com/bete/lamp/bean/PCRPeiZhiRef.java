package com.bete.lamp.bean;

import java.util.LinkedList;

import static com.myutils.GlobalDate.BLOCKSIZE;
import static com.myutils.GlobalDate.BLOCKNUM;
import static com.myutils.GlobalDate.GUANGNUM;
import static com.myutils.GlobalDate.LIUCHENGNUM;

public class PCRPeiZhiRef {
    public int fileType = 11;//XY，X代表设备类型，1快速pcr，;Y代表文件，1代表全配置文件
    public int projectType = 0;//1是ct，2是定性，3是定量，默认ct
    public LinkedList<Boolean>  guangchannels = new LinkedList<>();
    public LinkedList<Boolean>  blockchannels = new LinkedList<>();
    public LinkedList<PCRLiuChengCanShuItem> pcrLiuChengCanShuItemLinkedList = new LinkedList<>();
    public LinkedList<SampleItem> sampleItemLinkedList = new LinkedList<>();

    public PCRPeiZhiRef() {
        for(int i=0;i<GUANGNUM;i++)
        {
            guangchannels.add(false);
        }
        for(int i = 0; i< BLOCKSIZE; i++)
        {
            blockchannels.add(false);
        }
        for(int i=1;i<=LIUCHENGNUM;i++) {
                pcrLiuChengCanShuItemLinkedList.add(new PCRLiuChengCanShuItem(0));
        }
        for(int i = 1; i<= BLOCKSIZE * BLOCKNUM; i++) {
            sampleItemLinkedList.add(new SampleItem(i));
        }
    }

    public PCRPeiZhiRef(int fileType, int projectType, LinkedList<Boolean> guangchannels, LinkedList<Boolean> blockchannels, LinkedList<PCRLiuChengCanShuItem> pcrLiuChengCanShuItemLinkedList, LinkedList<SampleItem> sampleItemLinkedList) {
        this.fileType = fileType;
        this.projectType = projectType;
        this.guangchannels = guangchannels;
        this.blockchannels = blockchannels;
        this.pcrLiuChengCanShuItemLinkedList = pcrLiuChengCanShuItemLinkedList;
        this.sampleItemLinkedList = sampleItemLinkedList;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getProjectType() {
        return projectType;
    }

    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }

    public LinkedList<Boolean> getGuangchannels() {
        return guangchannels;
    }

    public void setGuangchannels(LinkedList<Boolean> guangchannels) {
        this.guangchannels = guangchannels;
    }

    public LinkedList<Boolean> getBlockchannels() {
        return blockchannels;
    }

    public void setBlockchannels(LinkedList<Boolean> blockchannels) {
        this.blockchannels = blockchannels;
    }

    public LinkedList<PCRLiuChengCanShuItem> getPcrLiuChengCanShuItemLinkedList() {
        return pcrLiuChengCanShuItemLinkedList;
    }

    public void setPcrLiuChengCanShuItemLinkedList(LinkedList<PCRLiuChengCanShuItem> pcrLiuChengCanShuItemLinkedList) {
        this.pcrLiuChengCanShuItemLinkedList = pcrLiuChengCanShuItemLinkedList;
    }

    public LinkedList<SampleItem> getSampleItemLinkedList() {
        return sampleItemLinkedList;
    }

    public void setSampleItemLinkedList(LinkedList<SampleItem> sampleItemLinkedList) {
        this.sampleItemLinkedList = sampleItemLinkedList;
    }
}
