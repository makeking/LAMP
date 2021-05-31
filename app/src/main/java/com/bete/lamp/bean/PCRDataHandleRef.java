package com.bete.lamp.bean;

import static com.myutils.GlobalDate.GUANGNUM;

public class PCRDataHandleRef implements Cloneable{
    public int g_zhongzhi_lvbo_num=5;
    public int  g_zhongzhi_lvbo_cnt=1;
    public int g_huadong_lvbo_num=5;
    public int  g_huadong_lvbo_cnt=3;

    public int g_ct_suanfa_type = 1;//1:插值，0：二阶导 默认为二阶导。
    public int g_jieguo_usedata_type =0;//0:归一，1：插值，默认为插值。
    public double g_guiyithresvalues[] = new double[GUANGNUM];
    public boolean g_guiyiself_thresvalues[] = new boolean[GUANGNUM];
    public int g_guiyibaseline_starts[] = new int[GUANGNUM];
    public int g_guiyibaseline_stops[] = new int[GUANGNUM];
    public boolean g_guiyiself_baselines[] = new boolean[GUANGNUM];

    public double g_nthresvalues[] = new double[GUANGNUM];
    public boolean g_nself_thresvalues[] = new boolean[GUANGNUM];
    public int g_nbaseline_starts[] = new int[GUANGNUM];
    public int g_nbaseline_stops[] = new int[GUANGNUM];
    public boolean g_nself_baselines[] = new boolean[GUANGNUM];
    public int g_nBegin=1;
    public int g_nEnd=5;
    public int g_nMultiple=10;

    public PCRDataHandleRef() {
        g_zhongzhi_lvbo_num=5;
        g_zhongzhi_lvbo_cnt=1;
        g_huadong_lvbo_num=5;
        g_huadong_lvbo_cnt=3;
//        this.g_ct_suanfa_type = 0;
//        this.g_jieguo_usedata_type = 1;
        this.g_guiyithresvalues[0] = 0.037;
        this.g_guiyithresvalues[1] = 0.037;
        this.g_guiyithresvalues[2] = 0.037;
        this.g_guiyithresvalues[3] = 0.037;
        this.g_guiyiself_thresvalues[0] = true;
        this.g_guiyiself_thresvalues[1] = true;
        this.g_guiyiself_thresvalues[2]= true;
        this.g_guiyiself_thresvalues[3]= true;
        this.g_guiyibaseline_starts[0] = 3;
        this.g_guiyibaseline_starts[1] = 3;
        this.g_guiyibaseline_starts[2] = 3;
        this.g_guiyibaseline_starts[3] = 3;

        this.g_guiyibaseline_stops[0] = 5;
        this.g_guiyibaseline_stops[1] = 5;
        this.g_guiyibaseline_stops[2] = 5;
        this.g_guiyibaseline_stops[3] = 5;
        this.g_guiyiself_baselines[0] = true;
        this.g_guiyiself_baselines[1] = true;
        this.g_guiyiself_baselines[2]= true;
        this.g_guiyiself_baselines[3]= true;
//        this.g_guiyibaseline_starts[0] = 1;
//        this.g_guiyibaseline_starts[1] = 1;
//        this.g_guiyibaseline_starts[2] = 1;
//        this.g_guiyibaseline_starts[3] = 1;
//
//        this.g_guiyibaseline_stops[0] = 3;
//        this.g_guiyibaseline_stops[1] = 3;
//        this.g_guiyibaseline_stops[2] = 3;
//        this.g_guiyibaseline_stops[3] = 3;
//        this.g_guiyiself_baselines[0] = false;
//        this.g_guiyiself_baselines[1] = false;
//        this.g_guiyiself_baselines[2]= false;
//        this.g_guiyiself_baselines[3]= false;

        this.g_nthresvalues[0] = 1.037;
        this.g_nthresvalues[1] = 1.037;
        this.g_nthresvalues[2] = 1.037;
        this.g_nthresvalues[3] = 1.037;
        this.g_nself_thresvalues[0] = true;
        this.g_nself_thresvalues[1] = true;
        this.g_nself_thresvalues[2]= true;
        this.g_nself_thresvalues[3]= true;
        this.g_nbaseline_starts[0] = 3;
        this.g_nbaseline_starts[1] = 3;
        this.g_nbaseline_starts[2] = 3;
        this.g_nbaseline_starts[3] = 3;

        this.g_nbaseline_stops[0] = 5;
        this.g_nbaseline_stops[1] = 5;
        this.g_nbaseline_stops[2] = 5;
        this.g_nbaseline_stops[3] = 5;

        this.g_nself_baselines[0] = true;
        this.g_nself_baselines[1] = true;
        this.g_nself_baselines[2]= true;
        this.g_nself_baselines[3]= true;

//        this.g_nbaseline_starts[0] = 1;
//        this.g_nbaseline_starts[1] = 1;
//        this.g_nbaseline_starts[2] = 1;
//        this.g_nbaseline_starts[3] = 1;
//
//        this.g_nbaseline_stops[0] = 3;
//        this.g_nbaseline_stops[1] = 3;
//        this.g_nbaseline_stops[2] = 3;
//        this.g_nbaseline_stops[3] = 3;
//        this.g_nself_baselines[0] = false;
//        this.g_nself_baselines[1] = false;
//        this.g_nself_baselines[2]= false;
//        this.g_nself_baselines[3]= false;
        this.g_nBegin = 1;
        this.g_nEnd = 5;
        this.g_nMultiple = 10;
    }

    public PCRDataHandleRef(int g_zhongzhi_lvbo_num, int g_zhongzhi_lvbo_cnt, int g_huadong_lvbo_num, int g_huadong_lvbo_cnt, int g_ct_suanfa_type, int g_jieguo_usedata_type, double[] g_guiyithresvalues, boolean[] g_guiyiself_thresvalues, int[] g_guiyibaseline_starts, int[] g_guiyibaseline_stops, boolean[] g_guiyiself_baselines, double[] g_nthresvalues, boolean[] g_nself_thresvalues, int[] g_nbaseline_starts, int[] g_nbaseline_stops, boolean[] g_nself_baselines, int g_nBegin, int g_nEnd, int g_nMultiple) {
        this.g_zhongzhi_lvbo_num = g_zhongzhi_lvbo_num;
        this.g_zhongzhi_lvbo_cnt = g_zhongzhi_lvbo_cnt;
        this.g_huadong_lvbo_num = g_huadong_lvbo_num;
        this.g_huadong_lvbo_cnt = g_huadong_lvbo_cnt;
        this.g_ct_suanfa_type = g_ct_suanfa_type;
        this.g_jieguo_usedata_type = g_jieguo_usedata_type;
        this.g_guiyithresvalues = g_guiyithresvalues;
        this.g_guiyiself_thresvalues = g_guiyiself_thresvalues;
        this.g_guiyibaseline_starts = g_guiyibaseline_starts;
        this.g_guiyibaseline_stops = g_guiyibaseline_stops;
        this.g_guiyiself_baselines = g_guiyiself_baselines;
        this.g_nthresvalues = g_nthresvalues;
        this.g_nself_thresvalues = g_nself_thresvalues;
        this.g_nbaseline_starts = g_nbaseline_starts;
        this.g_nbaseline_stops = g_nbaseline_stops;
        this.g_nself_baselines = g_nself_baselines;
        this.g_nBegin = g_nBegin;
        this.g_nEnd = g_nEnd;
        this.g_nMultiple = g_nMultiple;
    }

    public int getG_zhongzhi_lvbo_num() {
        return g_zhongzhi_lvbo_num;
    }

    public void setG_zhongzhi_lvbo_num(int g_zhongzhi_lvbo_num) {
        this.g_zhongzhi_lvbo_num = g_zhongzhi_lvbo_num;
    }

    public int getG_zhongzhi_lvbo_cnt() {
        return g_zhongzhi_lvbo_cnt;
    }

    public void setG_zhongzhi_lvbo_cnt(int g_zhongzhi_lvbo_cnt) {
        this.g_zhongzhi_lvbo_cnt = g_zhongzhi_lvbo_cnt;
    }

    public int getG_huadong_lvbo_num() {
        return g_huadong_lvbo_num;
    }

    public void setG_huadong_lvbo_num(int g_huadong_lvbo_num) {
        this.g_huadong_lvbo_num = g_huadong_lvbo_num;
    }

    public int getG_huadong_lvbo_cnt() {
        return g_huadong_lvbo_cnt;
    }

    public void setG_huadong_lvbo_cnt(int g_huadong_lvbo_cnt) {
        this.g_huadong_lvbo_cnt = g_huadong_lvbo_cnt;
    }

    public int getG_ct_suanfa_type() {
        return g_ct_suanfa_type;
    }

    public void setG_ct_suanfa_type(int g_ct_suanfa_type) {
        this.g_ct_suanfa_type = g_ct_suanfa_type;
    }

    public int getG_jieguo_usedata_type() {
        return g_jieguo_usedata_type;
    }

    public void setG_jieguo_usedata_type(int g_jieguo_usedata_type) {
        this.g_jieguo_usedata_type = g_jieguo_usedata_type;
    }

    public double[] getG_guiyithresvalues() {
        return g_guiyithresvalues;
    }

    public void setG_guiyithresvalues(double[] g_guiyithresvalues) {
        this.g_guiyithresvalues = g_guiyithresvalues;
    }

    public boolean[] getG_guiyiself_thresvalues() {
        return g_guiyiself_thresvalues;
    }

    public void setG_guiyiself_thresvalues(boolean[] g_guiyiself_thresvalues) {
        this.g_guiyiself_thresvalues = g_guiyiself_thresvalues;
    }

    public int[] getG_guiyibaseline_starts() {
        return g_guiyibaseline_starts;
    }

    public void setG_guiyibaseline_starts(int[] g_guiyibaseline_starts) {
        this.g_guiyibaseline_starts = g_guiyibaseline_starts;
    }

    public int[] getG_guiyibaseline_stops() {
        return g_guiyibaseline_stops;
    }

    public void setG_guiyibaseline_stops(int[] g_guiyibaseline_stops) {
        this.g_guiyibaseline_stops = g_guiyibaseline_stops;
    }

    public boolean[] getG_guiyiself_baselines() {
        return g_guiyiself_baselines;
    }

    public void setG_guiyiself_baselines(boolean[] g_guiyiself_baselines) {
        this.g_guiyiself_baselines = g_guiyiself_baselines;
    }

    public double[] getG_nthresvalues() {
        return g_nthresvalues;
    }

    public void setG_nthresvalues(double[] g_nthresvalues) {
        this.g_nthresvalues = g_nthresvalues;
    }

    public boolean[] getG_nself_thresvalues() {
        return g_nself_thresvalues;
    }

    public void setG_nself_thresvalues(boolean[] g_nself_thresvalues) {
        this.g_nself_thresvalues = g_nself_thresvalues;
    }

    public int[] getG_nbaseline_starts() {
        return g_nbaseline_starts;
    }

    public void setG_nbaseline_starts(int[] g_nbaseline_starts) {
        this.g_nbaseline_starts = g_nbaseline_starts;
    }

    public int[] getG_nbaseline_stops() {
        return g_nbaseline_stops;
    }

    public void setG_nbaseline_stops(int[] g_nbaseline_stops) {
        this.g_nbaseline_stops = g_nbaseline_stops;
    }

    public boolean[] getG_nself_baselines() {
        return g_nself_baselines;
    }

    public void setG_nself_baselines(boolean[] g_nself_baselines) {
        this.g_nself_baselines = g_nself_baselines;
    }

    public int getG_nBegin() {
        return g_nBegin;
    }

    public void setG_nBegin(int g_nBegin) {
        this.g_nBegin = g_nBegin;
    }

    public int getG_nEnd() {
        return g_nEnd;
    }

    public void setG_nEnd(int g_nEnd) {
        this.g_nEnd = g_nEnd;
    }

    public int getG_nMultiple() {
        return g_nMultiple;
    }

    public void setG_nMultiple(int g_nMultiple) {
        this.g_nMultiple = g_nMultiple;
    }

    public void setPCRCtSuanFaRef(PCRDataHandleRef pcrCtSuanFaRef) {
        this.g_ct_suanfa_type = pcrCtSuanFaRef.g_ct_suanfa_type;
        this.g_jieguo_usedata_type = pcrCtSuanFaRef.g_jieguo_usedata_type;
        for(int i=0;i<GUANGNUM;i++)
        {
            this.g_guiyithresvalues[i] = pcrCtSuanFaRef.g_guiyithresvalues[i];
            this.g_guiyiself_thresvalues[i] = pcrCtSuanFaRef.g_guiyiself_thresvalues[i];
            this.g_guiyibaseline_starts[i] = pcrCtSuanFaRef.g_guiyibaseline_starts[i];
            this.g_guiyibaseline_stops[i] = pcrCtSuanFaRef.g_guiyibaseline_stops[i];
            this.g_guiyiself_baselines[i] = pcrCtSuanFaRef.g_guiyiself_baselines[i];

            this.g_nthresvalues[i] = pcrCtSuanFaRef.g_nthresvalues[i];
            this.g_nself_thresvalues[i] = pcrCtSuanFaRef.g_nself_thresvalues[i];
            this.g_nbaseline_starts[i] = pcrCtSuanFaRef.g_nbaseline_starts[i];
            this.g_nbaseline_stops[i] = pcrCtSuanFaRef.g_nbaseline_stops[i];
            this.g_nself_baselines[i] = pcrCtSuanFaRef.g_nself_baselines[i];
        }
        this.g_nBegin = pcrCtSuanFaRef.g_nBegin;
        this.g_nEnd = pcrCtSuanFaRef.g_nEnd;
        this.g_nMultiple = pcrCtSuanFaRef.g_nMultiple;
    }
}
