package com.bete.lamp.bean;

import com.myutils.GlobalDate;

import java.io.Serializable;
import java.util.LinkedList;

import static com.myutils.GlobalDate.LIUCHENGNUM;

public class PCRProject implements Serializable {
    public GlobalDate.ProjectType project_type= GlobalDate.ProjectType.dingxing;//1定性、2定量
    public String project_filename;
    public String project_name;
    public String project_lot;//批号
    public String project_limit;
    public String project_danwei;
    public LinkedList<PCRLiuChengCanShuItem> pcrLiuChengCanShuItems = new LinkedList<>();

    public int project_neican_tongdao =0;
    public int[] project_item_ids = new int[GlobalDate.GUANGNUM];
    public boolean[] project_item_ables= new boolean[GlobalDate.GUANGNUM];
    public String[] project_item_names = new String[GlobalDate.GUANGNUM];
    public double[] project_item_refs = new double[GlobalDate.GUANGNUM];
    public double[] project_item_rns = new double[GlobalDate.GUANGNUM];
    public double[] project_item_ks = new double[GlobalDate.GUANGNUM];
    public double[] project_item_bs = new double[GlobalDate.GUANGNUM];
    public int project_ncs[]=new int[GlobalDate.GUANGNUM];
    public double project_neican;
    public double project_babiaos[]=new double[GlobalDate.GUANGNUM];
    public double project_dingliang_biaozhunnongdu;

    public PCRProject() {
        this.project_type = GlobalDate.ProjectType.dingxing;
        this.project_filename = "";
        this.project_name = "";
        this.project_lot = "";
        this.project_limit = "";
        this.project_danwei = "";
        for(int i = 1; i<= 3; i++) {
            pcrLiuChengCanShuItems.add(new PCRLiuChengCanShuItem(i));
        }
        this.project_neican_tongdao = 0;
        this.project_item_ids[0] = 1;
        this.project_item_ids[1] = 2;
        this.project_item_ids[2] = 3;
        this.project_item_ids[3] = 4;
        this.project_item_ables[0] = false;
        this.project_item_ables[1] = false;
        this.project_item_ables[2] = false;
        this.project_item_ables[3] = false;
        this.project_item_names[0] = "";
        this.project_item_names[1] = "";
        this.project_item_names[2] = "";
        this.project_item_names[3] = "";
        this.project_item_refs[0] = 0;
        this.project_item_refs[1] = 0;
        this.project_item_refs[2] = 0;
        this.project_item_refs[3] = 0;
        this.project_item_rns[0] = 0;
        this.project_item_rns[1] = 0;
        this.project_item_rns[2] = 0;
        this.project_item_rns[3] = 0;
        this.project_item_ks[0] = 1;
        this.project_item_ks[1] = 1;
        this.project_item_ks[2] = 1;
        this.project_item_ks[3] = 1;
        this.project_item_bs[0] = 0;
        this.project_item_bs[1] = 0;
        this.project_item_bs[2] = 0;
        this.project_item_bs[3] = 0;
        this.project_ncs[0] = 0;
        this.project_ncs[1] = 0;
        this.project_ncs[2] = 0;
        this.project_ncs[3] = 0;
        this.project_neican = 0;
        this.project_babiaos[0] = 0;
        this.project_babiaos[1] = 0;
        this.project_babiaos[2] = 0;
        this.project_babiaos[3] = 0;
        this.project_dingliang_biaozhunnongdu = 0;
    }

    public PCRProject(GlobalDate.ProjectType project_type, String project_filename, String project_name, String project_lot) {
        this();
        this.project_type = project_type;
        this.project_filename = project_filename;
        this.project_name = project_name;
        this.project_lot = project_lot;
    }

    public PCRProject(GlobalDate.ProjectType project_type, String project_filename, String project_name, String project_lot, String project_limit, String project_danwei, LinkedList<PCRLiuChengCanShuItem> pcrLiuChengCanShuItems, int project_neican_tongdao,//
                      boolean project_item1_able, boolean project_item2_able, boolean project_item3_able, boolean project_item4_able, String project_item1_name, String project_item2_name, String project_item3_name, String project_item4_name, //
                      double project_item1_ref, double project_item2_ref, double project_item3_ref, double project_item4_ref, double project_item1_rn, double project_item2_rn, double project_item3_rn, double project_item4_rn,//
                      double project_item1_k, double project_item2_k, double project_item3_k, double project_item4_k, double project_item1_b, double project_item2_b, double project_item3_b, double project_item4_b,//
                      int project_nc_1, int project_nc_2, int project_nc_3, int project_nc_4, double project_neican, double project_babiao_1, double project_babiao_2, double project_babiao_3, double project_babiao_4, double project_dingliang_biaozhunnongdu) {
        this.project_type = project_type;
        this.project_filename = project_filename;
        this.project_name = project_name;
        this.project_lot = project_lot;
        this.project_limit = project_limit;
        this.project_danwei = project_danwei;
        this.pcrLiuChengCanShuItems = pcrLiuChengCanShuItems;
        this.project_neican_tongdao = project_neican_tongdao;
        this.project_item_ids[0] = 1;
        this.project_item_ids[1] = 2;
        this.project_item_ids[2] = 3;
        this.project_item_ids[3] = 4;
        this.project_item_ables[0] = project_item1_able;
        this.project_item_ables[1] = project_item2_able;
        this.project_item_ables[2] = project_item3_able;
        this.project_item_ables[3] = project_item4_able;
        this.project_item_names[0] = project_item1_name;
        this.project_item_names[1] = project_item2_name;
        this.project_item_names[2] = project_item3_name;
        this.project_item_names[3] = project_item4_name;
        this.project_item_refs[0] = project_item1_ref;
        this.project_item_refs[1] = project_item2_ref;
        this.project_item_refs[2] = project_item3_ref;
        this.project_item_refs[3] = project_item4_ref;
        this.project_item_rns[0] = project_item1_rn;
        this.project_item_rns[1] = project_item2_rn;
        this.project_item_rns[2] = project_item3_rn;
        this.project_item_rns[3] = project_item4_rn;
        this.project_item_ks[0] = project_item1_k;
        this.project_item_ks[1] = project_item2_k;
        this.project_item_ks[2] = project_item3_k;
        this.project_item_ks[3] = project_item4_k;
        this.project_item_bs[0] = project_item1_b;
        this.project_item_bs[1] = project_item2_b;
        this.project_item_bs[2] = project_item3_b;
        this.project_item_bs[3] = project_item4_b;
        this.project_ncs[0] = project_nc_1;
        this.project_ncs[1] = project_nc_2;
        this.project_ncs[2] = project_nc_3;
        this.project_ncs[3] = project_nc_4;
        this.project_neican = project_neican;
        this.project_babiaos[0] = project_babiao_1;
        this.project_babiaos[1] = project_babiao_2;
        this.project_babiaos[2] = project_babiao_3;
        this.project_babiaos[3] = project_babiao_4;
        this.project_dingliang_biaozhunnongdu = project_dingliang_biaozhunnongdu;
    }

//    public String getFilename() {
//        String filename ="";
//        filename += (project_item_able[0]==true)?((project_item_name[0].isEmpty()?" ":project_item_name[0])+"-"):(" "+"-");
//        filename += (project_item_able[1]==true)?((project_item_name[1].isEmpty()?" ":project_item_name[1])+"-"):(" "+"-");
//        filename += (project_item_able[2]==true)?((project_item_name[2].isEmpty()?" ":project_item_name[2])+"-"):(" "+"-");
//        filename += (project_item_able[3]==true)?((project_item_name[3].isEmpty()?" ":project_item_name[3])+"-"):(" "+"-");
//        filename += project_lot.isEmpty()?" ":project_lot;
//        if(project_type==1)
//            filename += ".x";
//        else
//            filename += ".l";
//        return filename;
//    }


    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_danwei() {
        return project_danwei;
    }

    public void setProject_danwei(String project_danwei) {
        this.project_danwei = project_danwei;
    }

    public GlobalDate.ProjectType getProject_type() {
        return project_type;
    }

    public void setProject_type(GlobalDate.ProjectType project_type) {
        this.project_type = project_type;
    }

    public String getProject_lot() {
        return project_lot;
    }

    public void setProject_lot(String project_lot) {
        this.project_lot = project_lot;
    }

    public String getProject_limit() {
        return project_limit;
    }

    public void setProject_limit(String project_limit) {
        this.project_limit = project_limit;
    }

    public boolean isProject_item1_able() {
        return project_item_ables[0];
    }

    public void setProject_item1_able(boolean project_item1_able) {
        this.project_item_ables[0] = project_item1_able;
    }

    public boolean isProject_item2_able() {
        return project_item_ables[1];
    }

    public void setProject_item2_able(boolean project_item2_able) {
        this.project_item_ables[1] = project_item2_able;
    }

    public boolean isProject_item3_able() {
        return project_item_ables[2];
    }

    public void setProject_item3_able(boolean project_item3_able) {
        this.project_item_ables[2] = project_item3_able;
    }

    public boolean isProject_item4_able() {
        return project_item_ables[3];
    }

    public void setProject_item4_able(boolean project_item4_able) {
        this.project_item_ables[3] = project_item4_able;
    }

    public String getProject_item1_name() {
        return project_item_names[0];
    }

    public void setProject_item1_name(String project_item1_name) {
        this.project_item_names[0]= project_item1_name;
    }

    public String getProject_item2_name() {
        return project_item_names[1];
    }

    public void setProject_item2_name(String project_item2_name) {
        this.project_item_names[1] = project_item2_name;
    }

    public String getProject_item3_name() {
        return project_item_names[2];
    }

    public void setProject_item3_name(String project_item3_name) {
        this.project_item_names[2] = project_item3_name;
    }

    public String getProject_item4_name() {
        return project_item_names[3];
    }

    public void setProject_item4_name(String project_item4_name) {
        this.project_item_names[3] = project_item4_name;
    }

    public int getProject_item1_id() {
        return project_item_ids[0];
    }

    public void setProject_item1_id(int project_item1_id) {
        this.project_item_ids[0]= project_item1_id;
    }

    public int getProject_item2_id() {
        return project_item_ids[1];
    }

    public void setProject_item2_id(int project_item2_id) {
        this.project_item_ids[1] = project_item2_id;
    }

    public int getProject_item3_id() {
        return project_item_ids[2];
    }

    public void setProject_item3_id(int project_item3_id) {
        this.project_item_ids[2] = project_item3_id;
    }

    public int getProject_item4_id() {
        return project_item_ids[3];
    }

    public void setProject_item4_id(int project_item4_id) {
        this.project_item_ids[3] = project_item4_id;
    }

    public double getProject_item1_ref() {
        return project_item_refs[0];
    }

    public void setProject_item1_ref(double project_item1_ref) {
        this.project_item_refs[0]= project_item1_ref;
    }

    public double getProject_item2_ref() {
        return project_item_refs[1];
    }

    public void setProject_item2_ref(double project_item2_ref) {
        this.project_item_refs[1] = project_item2_ref;
    }

    public double getProject_item3_ref() {
        return project_item_refs[2];
    }

    public void setProject_item3_ref(double project_item3_ref) {
        this.project_item_refs[2] = project_item3_ref;
    }

    public double getProject_item4_ref() {
        return project_item_refs[3];
    }

    public void setProject_item4_ref(double project_item4_ref) {
        this.project_item_refs[3] = project_item4_ref;
    }

    public double getProject_item1_rn() {
        return project_item_rns[0];
    }

    public void setProject_item1_rn(double project_item1_rn) {
        this.project_item_rns[0]= project_item1_rn;
    }

    public double getProject_item2_rn() {
        return project_item_rns[1];
    }

    public void setProject_item2_rn(double project_item2_rn) {
        this.project_item_rns[1] = project_item2_rn;
    }

    public double getProject_item3_rn() {
        return project_item_rns[2];
    }

    public void setProject_item3_rn(double project_item3_rn) {
        this.project_item_rns[2] = project_item3_rn;
    }

    public double getProject_item4_rn() {
        return project_item_rns[3];
    }

    public void setProject_item4_rn(double project_item4_rn) {
        this.project_item_rns[3] = project_item4_rn;
    }

    public double getProject_item1_k() {
        return project_item_ks[0];
    }

    public void setProject_item1_k(double project_item1_k) {
        this.project_item_ks[0]= project_item1_k;
    }

    public double getProject_item2_k() {
        return project_item_ks[1];
    }

    public void setProject_item2_k(double project_item2_k) {
        this.project_item_ks[1] = project_item2_k;
    }

    public double getProject_item3_k() {
        return project_item_ks[2];
    }

    public void setProject_item3_k(double project_item3_k) {
        this.project_item_ks[2] = project_item3_k;
    }

    public double getProject_item4_k() {
        return project_item_ks[3];
    }

    public void setProject_item4_k(double project_item4_k) {
        this.project_item_ks[3] = project_item4_k;
    }

    public double getProject_item1_b() {
        return project_item_bs[0];
    }

    public void setProject_item1_b(double project_item1_b) {
        this.project_item_bs[0]= project_item1_b;
    }

    public double getProject_item2_b() {
        return project_item_bs[1];
    }

    public void setProject_item2_b(double project_item2_b) {
        this.project_item_bs[1] = project_item2_b;
    }

    public double getProject_item3_b() {
        return project_item_bs[2];
    }

    public void setProject_item3_b(double project_item3_b) {
        this.project_item_bs[2] = project_item3_b;
    }

    public double getProject_item4_b() {
        return project_item_bs[3];
    }

    public void setProject_item4_b(double project_item4_b) {
        this.project_item_bs[3] = project_item4_b;
    }



    public String getProject_filename() {
        return project_filename;
    }

    public void setProject_filename(String project_filename) {
        this.project_filename = project_filename;
    }

    public LinkedList<PCRLiuChengCanShuItem> getPcrLiuChengCanShuItems() {
        return pcrLiuChengCanShuItems;
    }

    public void setPcrLiuChengCanShuItems(LinkedList<PCRLiuChengCanShuItem> pcrLiuChengCanShuItems) {
        this.pcrLiuChengCanShuItems = pcrLiuChengCanShuItems;
    }

    public int getProject_neican_tongdao() {
        return project_neican_tongdao;
    }

    public void setProject_neican_tongdao(int project_neican_tongdao) {
        this.project_neican_tongdao = project_neican_tongdao;
    }

    public int[] getProject_item_ids() {
        return project_item_ids;
    }

    public void setProject_item_ids(int[] project_item_ids) {
        this.project_item_ids = project_item_ids;
    }

    public boolean[] getProject_item_ables() {
        return project_item_ables;
    }

    public void setProject_item_ables(boolean[] project_item_ables) {
        this.project_item_ables = project_item_ables;
    }

    public String[] getProject_item_names() {
        return project_item_names;
    }

    public void setProject_item_names(String[] project_item_names) {
        this.project_item_names = project_item_names;
    }

    public double[] getProject_item_refs() {
        return project_item_refs;
    }

    public void setProject_item_refs(double[] project_item_refs) {
        this.project_item_refs = project_item_refs;
    }

    public double[] getProject_item_rns() {
        return project_item_rns;
    }

    public void setProject_item_rns(double[] project_item_rns) {
        this.project_item_rns = project_item_rns;
    }

    public double[] getProject_item_ks() {
        return project_item_ks;
    }

    public void setProject_item_ks(double[] project_item_ks) {
        this.project_item_ks = project_item_ks;
    }

    public double[] getProject_item_bs() {
        return project_item_bs;
    }

    public void setProject_item_bs(double[] project_item_bs) {
        this.project_item_bs = project_item_bs;
    }

    public int[] getProject_ncs() {
        return project_ncs;
    }

    public void setProject_ncs(int[] project_ncs) {
        this.project_ncs = project_ncs;
    }

    public double getProject_neican() {
        return project_neican;
    }

    public void setProject_neican(double project_neican) {
        this.project_neican = project_neican;
    }

    public double[] getProject_babiaos() {
        return project_babiaos;
    }

    public void setProject_babiaos(double[] project_babiaos) {
        this.project_babiaos = project_babiaos;
    }

    public double getProject_dingliang_biaozhunnongdu() {
        return project_dingliang_biaozhunnongdu;
    }

    public void setProject_dingliang_biaozhunnongdu(double project_dingliang_biaozhunnongdu) {
        this.project_dingliang_biaozhunnongdu = project_dingliang_biaozhunnongdu;
    }
}
