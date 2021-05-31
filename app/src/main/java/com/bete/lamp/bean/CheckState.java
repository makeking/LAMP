package com.bete.lamp.bean;

import java.util.LinkedList;

import static com.myutils.GlobalDate.BLOCKNUM;
import static com.myutils.GlobalDate.BLOCKSIZE;
import static com.myutils.GlobalDate.CAIJICOUNT;
import static com.myutils.GlobalDate.GUANGNUM;

public class CheckState {
    public int version = 1;
    public String checktime="";
    public int blockno=0;
    public int checkcnt=0;
    public int checkcount=0;
    public int caijicount=CAIJICOUNT;
    public double[][][] raredatas = new double[GUANGNUM][BLOCKSIZE][caijicount];
    public double[][][] lvbodatas = new double[GUANGNUM][BLOCKSIZE][caijicount];
    public double[][][] guiyidatas = new double[GUANGNUM][BLOCKSIZE][caijicount];
    public double[][] cts = new double[GUANGNUM][BLOCKSIZE];
    public boolean qcEnable = false;
    public float[] thresValues =new float[GUANGNUM];
    public String[] sampleNos = new String[BLOCKSIZE];
    public PCRProject pcrProject = new PCRProject();
    public LampResultItem[][] lampResultItems = new LampResultItem[GUANGNUM][BLOCKSIZE];
    public LampQcData lampQcData= new LampQcData();

    public CheckState() {
        for(int i=0;i<BLOCKSIZE;i++)
        {
            sampleNos[i]="";
        }
        for(int i=0;i<GUANGNUM;i++) {
            thresValues[i]= (float) 1.037;
            for (int j = 0; j < BLOCKSIZE; j++)
            {
                cts[i][j]=0;
                lampResultItems[i][j]=new LampResultItem(blockno*GUANGNUM*BLOCKSIZE+i * BLOCKSIZE + j, (float) blockno*GUANGNUM*BLOCKSIZE+i * BLOCKSIZE + j + 1);
                for(int z=0;z<caijicount;z++)
                {
                    raredatas[i][j][z] = i * BLOCKNUM * BLOCKSIZE * CAIJICOUNT + j * CAIJICOUNT + z;
                    lvbodatas[i][j][z] = i * BLOCKNUM * BLOCKSIZE * CAIJICOUNT + j * CAIJICOUNT + z;
                    guiyidatas[i][j][z] = i * BLOCKNUM * BLOCKSIZE * CAIJICOUNT + j * CAIJICOUNT + z;
                }
            }
        }
    }

    public CheckState(int version, String checktime, int blockno, int checkcnt, int checkcount, int caijicount, double[][][] raredatas, double[][][] lvbodatas, double[][][] guiyidatas, double[][] cts, boolean qcEnable, float[] thresValues, String[] sampleNos, PCRProject pcrProject, LampResultItem[][] lampResultItems, LampQcData lampQcData) {
        this.version = version;
        this.checktime = checktime;
        this.blockno = blockno;
        this.checkcnt = checkcnt;
        this.checkcount = checkcount;
        this.caijicount = caijicount;
        this.raredatas = raredatas;
        this.lvbodatas = lvbodatas;
        this.guiyidatas = guiyidatas;
        this.cts = cts;
        this.qcEnable = qcEnable;
        this.thresValues = thresValues;
        this.sampleNos = sampleNos;
        this.pcrProject = pcrProject;
        this.lampResultItems = lampResultItems;
        this.lampQcData = lampQcData;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getChecktime() {
        return checktime;
    }

    public void setChecktime(String checktime) {
        this.checktime = checktime;
    }

    public int getBlockno() {
        return blockno;
    }

    public void setBlockno(int blockno) {
        this.blockno = blockno;
    }

    public int getCheckcnt() {
        return checkcnt;
    }

    public void setCheckcnt(int checkcnt) {
        this.checkcnt = checkcnt;
    }

    public int getCheckcount() {
        return checkcount;
    }

    public void setCheckcount(int checkcount) {
        this.checkcount = checkcount;
    }

    public int getCaijicount() {
        return caijicount;
    }

    public void setCaijicount(int caijicount) {
        this.caijicount = caijicount;
    }

    public double[][][] getRaredatas() {
        return raredatas;
    }

    public void setRaredatas(double[][][] raredatas) {
        this.raredatas = raredatas;
    }

    public double[][][] getLvbodatas() {
        return lvbodatas;
    }

    public void setLvbodatas(double[][][] lvbodatas) {
        this.lvbodatas = lvbodatas;
    }

    public double[][][] getGuiyidatas() {
        return guiyidatas;
    }

    public void setGuiyidatas(double[][][] guiyidatas) {
        this.guiyidatas = guiyidatas;
    }

    public double[][] getCts() {
        return cts;
    }

    public void setCts(double[][] cts) {
        this.cts = cts;
    }

    public boolean isQcEnable() {
        return qcEnable;
    }

    public void setQcEnable(boolean qcEnable) {
        this.qcEnable = qcEnable;
    }

    public float[] getThresValues() {
        return thresValues;
    }

    public void setThresValues(float[] thresValues) {
        this.thresValues = thresValues;
    }

    public String[] getSampleNos() {
        return sampleNos;
    }

    public void setSampleNos(String[] sampleNos) {
        this.sampleNos = sampleNos;
    }

    public PCRProject getPcrProject() {
        return pcrProject;
    }

    public void setPcrProject(PCRProject pcrProject) {
        this.pcrProject = pcrProject;
    }

    public LampResultItem[][] getLampResultItems() {
        return lampResultItems;
    }

    public void setLampResultItems(LampResultItem[][] lampResultItems) {
        this.lampResultItems = lampResultItems;
    }

    public LampQcData getLampQcData() {
        return lampQcData;
    }

    public void setLampQcData(LampQcData lampQcData) {
        this.lampQcData = lampQcData;
    }
}
