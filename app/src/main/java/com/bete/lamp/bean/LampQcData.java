package com.bete.lamp.bean;

import com.bete.lamp.thread.LampLiuChengThread;

import java.util.LinkedList;

import static com.myutils.GlobalDate.BLOCKNUM;
import static com.myutils.GlobalDate.BLOCKSIZE;
import static com.myutils.GlobalDate.CAIJICOUNT;
import static com.myutils.GlobalDate.GUANGNUM;

public class LampQcData {
    public String time ="无";
    public static int QCCAIJICOUNT=CAIJICOUNT;
    public int qc_index = 0;//实际采集次数
    public double[][][] qc_raredatas =new double [GUANGNUM][3][QCCAIJICOUNT];
    public double[][][] qc_lvbodatas =new double [GUANGNUM][3][QCCAIJICOUNT];
    public double[][][] qc_guiyidatas =new double [GUANGNUM][3][QCCAIJICOUNT];
    public float[] qc_thresvalues = new float[GUANGNUM];
    public double[][] qc_ct = new double[GUANGNUM][3];
    public double kadjustref;
    public LampQcData() {
        time = "无";
        qc_index=0;
        for (int i = 0; i < GUANGNUM; i++) {
            qc_thresvalues[i]= (float) 1.037;
            kadjustref = 0.2;
            for (int j = 0; j < 3; j++){
                qc_ct[i][j]=0;
                for (int z = 0; z < QCCAIJICOUNT; z++) {
                    qc_raredatas[i][j][z] = i*3*QCCAIJICOUNT+j*QCCAIJICOUNT+z+1;
                    qc_lvbodatas[i][j][z] = i*3*QCCAIJICOUNT+j*QCCAIJICOUNT+z+1;
                    qc_guiyidatas[i][j][z] = i*3*QCCAIJICOUNT+j*QCCAIJICOUNT+z+1;
                }
            }
        }
    }

    public LampQcData(String time, int QCCAIJICOUNT, int qc_index, double[][][] qc_raredatas, double[][][] qc_lvbodatas, double[][][] qc_guiyidatas, float[] qc_thresvalues, double[][] qc_ct, double kadjustref) {
        this.time = time;
        this.QCCAIJICOUNT = QCCAIJICOUNT;
        this.qc_index = qc_index;
        this.qc_raredatas = qc_raredatas;
        this.qc_lvbodatas = qc_lvbodatas;
        this.qc_guiyidatas = qc_guiyidatas;
        this.qc_thresvalues = qc_thresvalues;
        this.qc_ct = qc_ct;
        this.kadjustref = kadjustref;
    }

    public double getKadjustref() {
        return kadjustref;
    }

    public void setKadjustref(double kadjustref) {
        this.kadjustref = kadjustref;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQCCAIJICOUNT() {
        return QCCAIJICOUNT;
    }

    public void setQCCAIJICOUNT(int QCCAIJICOUNT) {
        this.QCCAIJICOUNT = QCCAIJICOUNT;
    }

    public int getQc_index() {
        return qc_index;
    }

    public void setQc_index(int qc_index) {
        this.qc_index = qc_index;
    }

    public double[][][] getQc_raredatas() {
        return qc_raredatas;
    }

    public void setQc_raredatas(double[][][] qc_raredatas) {
        this.qc_raredatas = qc_raredatas;
    }

    public double[][][] getQc_lvbodatas() {
        return qc_lvbodatas;
    }

    public void setQc_lvbodatas(double[][][] qc_lvbodatas) {
        this.qc_lvbodatas = qc_lvbodatas;
    }

    public double[][][] getQc_guiyidatas() {
        return qc_guiyidatas;
    }

    public void setQc_guiyidatas(double[][][] qc_guiyidatas) {
        this.qc_guiyidatas = qc_guiyidatas;
    }

    public float[] getQc_thresvalues() {
        return qc_thresvalues;
    }

    public void setQc_thresvalues(float[] qc_thresvalues) {
        this.qc_thresvalues = qc_thresvalues;
    }

    public double[][] getQc_ct() {
        return qc_ct;
    }

    public void setQc_ct(double[][] qc_ct) {
        this.qc_ct = qc_ct;
    }
}
