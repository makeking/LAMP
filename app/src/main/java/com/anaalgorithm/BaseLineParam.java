package com.anaalgorithm;

public class BaseLineParam {
    public BaseLineParam() {
    }

    public BaseLineParam(boolean bSelfDefine) {
        this.bSelfDefine = bSelfDefine;
    }

    public BaseLineParam(int selfStart, int selfEnd, boolean bSelfDefine) {
        this.selfStart = selfStart;
        this.selfEnd = selfEnd;
        this.bSelfDefine = bSelfDefine;
    }

    public BaseLineParam(int selfStart, int selfEnd, int defStart, int defForwadDotNum, boolean bSelfDefine) {
        this.selfStart = selfStart;
        this.selfEnd = selfEnd;
        this.defStart = defStart;
        this.defForwadDotNum = defForwadDotNum;
        this.bSelfDefine = bSelfDefine;
    }

    public int selfStart=1;
    public int selfEnd=8;
    public int defStart =1;
    public int defForwadDotNum = 3;
    public boolean bSelfDefine = false;
}
