package com.bete.lamp.db;

import org.litepal.crud.LitePalSupport;

public class CheckResultItem extends LitePalSupport {
    //    @Column(unique = true, defaultValue = "unknown")
    public int ID;
    public String samplenumber;
    public String usrname;
    public String type;
    public String age;
    public String mydatetime;
    public String panpianNO;
    public String qcresult;
    public String check_item_name;
    public double check_item_result;
    public double check_item_min;
    public double check_item_max;
    public String check_item_danwei;
    public String check_project_name;
    public int check_item_typeid;
    public boolean check_type;
    public String abstate;
    public String mydata;//存放曲线数据

    public String getMydata() {
        return mydata;
    }

    public void setMydata(String mydata) {
        this.mydata = mydata;
    }



    public boolean isCheck_type() {
        return check_type;
    }

    public void setCheck_type(boolean check_type) {
        this.check_type = check_type;
    }


    public String getSamplenumber() {
        return samplenumber;
    }

    public void setSamplenumber(String samplenumber) {
        this.samplenumber = samplenumber;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
    public void setSampleNumber(String samplenumber) {
        this.samplenumber = samplenumber;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setMydatetime(String mydatetime) {
        this.mydatetime = mydatetime;
    }

    public void setPanpianNO(String panpianNO) {
        this.panpianNO = panpianNO;
    }

    public void setQcresult(String qcresult) {
        this.qcresult = qcresult;
    }

    public void setCheck_item_name(String check_item_name) {
        this.check_item_name = check_item_name;
    }

    public void setCheck_item_result(double check_item_result) {
        this.check_item_result = check_item_result;
    }

    public void setCheck_item_min(double check_item_min) {
        this.check_item_min = check_item_min;
    }

    public void setCheck_item_max(double check_item_max) {
        this.check_item_max = check_item_max;
    }

    public void setCheck_item_danwei(String check_item_danwei) {
        this.check_item_danwei = check_item_danwei;
    }

    public void setCheck_project_name(String check_project_name) {
        this.check_project_name = check_project_name;
    }

    public void setCheck_item_typeid(int check_item_typeid) {
        this.check_item_typeid = check_item_typeid;
    }

    public String getSampleNumber() {
        return samplenumber;
    }

    public String getUsrname() {
        return usrname;
    }

    public String getType() {
        return type;
    }

    public String getAge() {
        return age;
    }

    public String getMydatetime() {
        return mydatetime;
    }

    public String getPanpianNO() {
        return panpianNO;
    }

    public String getQcresult() {
        return qcresult;
    }

    public String getCheck_item_name() {
        return check_item_name;
    }

    public double getCheck_item_result() {
        return check_item_result;
    }

    public double getCheck_item_min() {
        return check_item_min;
    }

    public double getCheck_item_max() {
        return check_item_max;
    }

    public String getCheck_item_danwei() {
        return check_item_danwei;
    }

    public String getCheck_project_name() {
        return check_project_name;
    }

    public int getCheck_item_typeid() {
        return check_item_typeid;
    }

    public String getAbstate() {
        return abstate;
    }

    public void setAbstate(String abstate) {
        this.abstate = abstate;
    }

    @Override
    public String toString() {
        return String.valueOf(ID)+","+samplenumber+","+usrname+","+type+","+age+","+mydatetime+","+panpianNO+","+qcresult+","+check_item_name+
                ","+String.valueOf(check_item_result)+","+String.valueOf(check_item_min)+","+String.valueOf(check_item_max)+","+check_item_danwei+","+
                check_project_name+","+String.valueOf(check_item_typeid)+","+Boolean.valueOf(check_type)+","+ abstate +","+mydata+"\n";
    }
}
