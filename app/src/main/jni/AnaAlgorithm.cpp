#include <jni.h>
#include <string.h>
#include <android/log.h>
#include "AnaAlgorithm.h"
#include "PcrAnaAlgorithm.h"

//#ifdef _DEBUG
//#define new DEBUG_NEW
//#endif

/**
* 数字滤波
* \return
*/
extern "C" JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_DigitalFilter(JNIEnv *env, jobject obj, jdoubleArray x,
                                                 jdoubleArray dReturn, jint nNum, jobject eType,
                                                 jint nWnd) {
//获取Java中的实例类
    jclass jclazz = (env)->FindClass("com/anaalgorithm/eFilterType");
//得到对应的方法
    jmethodID jmethodid_GetVal = env->GetMethodID(jclazz, "GetValue", "()I");
//得到参数值
    int filtertype = env->CallIntMethod(eType, jmethodid_GetVal);

    double *c_x = new double[nNum];
    double *c_dReturn = new double[nNum];
    memset(c_x, 0, sizeof(double) * nNum);
    memset(c_dReturn, 0, sizeof(double) * nNum);
    env->GetDoubleArrayRegion(x, 0, nNum, c_x);
    CPcrAnaAlgorithm::GetInstance()->DigitalFilter(c_x, c_dReturn, nNum, (eFilterType) filtertype,
                                                   nWnd);
    env->SetDoubleArrayRegion(dReturn, 0, nNum, c_dReturn);
    delete[]c_x;
    delete[]c_dReturn;
}


///**
//* 扩增\熔曲信号曲线形态是否满足初步计算条件
//* \return
//*/
//extern "C" JNIEXPORT jboolean JNICALL
//Java_com_anaalgorithm_AnaAlgorithm_AbleToCalculate___3D_3DILcom_anaalgorithm_FunAbleToCalcuParamInfo_2(
//        JNIEnv *env, jobject obj, jdoubleArray dx, jdoubleArray dy, jint nNum, jobject paraminfo) {
//    double *c_pdx = new double[nNum];
//    double *c_pdy = new double[nNum];
//    memset(c_pdx, 0, sizeof(double) * nNum);
//    memset(c_pdy, 0, sizeof(double) * nNum);
//
//    env->GetDoubleArrayRegion(dx, 0, nNum, c_pdx);
//    env->GetDoubleArrayRegion(dy, 0, nNum, c_pdy);
//
//    tagFunAbleToCalcuParamInfo ableinfo;
//    jclass jclazz = (env)->FindClass("com/anaalgorithm/FunAbleToCalcuParamInfo");
//
//    jfieldID jid = env->GetFieldID(jclazz, "bNormalization", "Z");
//    ableinfo.bNormalization = env->GetBooleanField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "nCurveType", "I");
//    ableinfo.nCurveType = env->GetIntField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "dAmpNoNormalizeStd", "D");
//    ableinfo.dAmpNoNormalizeStd = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dAmpNoNormalizeRatio", "D");
//    ableinfo.dAmpNoNormalizeRatio = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dAmpNormalizeStd", "D");
//    ableinfo.dAmpNormalizeStd = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dAmpNormalizeRatio", "D");
//    ableinfo.dAmpNormalizeRatio = env->GetDoubleField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "dMeltNoNormalizeStd", "D");
//    ableinfo.dMeltNoNormalizeStd = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dMeltNoNormalizeRatio", "D");
//    ableinfo.dMeltNoNormalizeRatio = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dMeltNormalizeStd", "D");
//    ableinfo.dMeltNormalizeStd = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dMeltNormalizeRatio", "D");
//    ableinfo.dMeltNormalizeRatio = env->GetDoubleField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "bkCalcDotNum", "I");
//    ableinfo.bkCalcDotNum = env->GetIntField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dCrestBkRatio", "D");
//    ableinfo.dCrestBkRatio = env->GetDoubleField(paraminfo, jid);
//
//    jboolean bAble = CPcrAnaAlgorithm::GetInstance()->NewAbleToCalculate(c_pdx, c_pdy, nNum,
//                                                                         ableinfo, 1, 3, 3);
//
//    delete[]c_pdx;
//    delete[]c_pdy;
//
//    return bAble;
//}

///**
//* 扩增\熔曲信号曲线形态是否满足初步计算条件
//* \return
//*/
//extern "C" JNIEXPORT jboolean JNICALL
//Java_com_anaalgorithm_AnaAlgorithm_AbleToCalculate___3D_3DILcom_anaalgorithm_FunAbleToCalcuParamInfo_2IIF(
//        JNIEnv *env, jobject obj, jdoubleArray dx, jdoubleArray dy, jint nNum, jobject paraminfo,
//        jint newStart, jint newForwardNum, jfloat newStdNum) {
//    double *c_pdx = new double[nNum];
//    double *c_pdy = new double[nNum];
//    memset(c_pdx, 0, sizeof(double) * nNum);
//    memset(c_pdy, 0, sizeof(double) * nNum);
//
//    env->GetDoubleArrayRegion(dx, 0, nNum, c_pdx);
//    env->GetDoubleArrayRegion(dy, 0, nNum, c_pdy);
//
//    tagFunAbleToCalcuParamInfo ableinfo;
//    jclass jclazz = (env)->FindClass("com/anaalgorithm/FunAbleToCalcuParamInfo");
//
//    jfieldID jid = env->GetFieldID(jclazz, "bNormalization", "Z");
//    ableinfo.bNormalization = env->GetBooleanField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "nCurveType", "I");
//    ableinfo.nCurveType = env->GetIntField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "dAmpNoNormalizeStd", "D");
//    ableinfo.dAmpNoNormalizeStd = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dAmpNoNormalizeRatio", "D");
//    ableinfo.dAmpNoNormalizeRatio = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dAmpNormalizeStd", "D");
//    ableinfo.dAmpNormalizeStd = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dAmpNormalizeRatio", "D");
//    ableinfo.dAmpNormalizeRatio = env->GetDoubleField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "dFDNoNormalizeVal", "D");
//    ableinfo.dFDNoNormalizeVal = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dFDNoNormalizeRatio", "D");
//    ableinfo.dFDNoNormalizeRatio = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dFDNormalizeVal", "D");
//    ableinfo.dFDNormalizeVal = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dFDNormalizeRatio", "D");
//    ableinfo.dFDNormalizeRatio = env->GetDoubleField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "dMeltNoNormalizeStd", "D");
//    ableinfo.dMeltNoNormalizeStd = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dMeltNoNormalizeRatio", "D");
//    ableinfo.dMeltNoNormalizeRatio = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dMeltNormalizeStd", "D");
//    ableinfo.dMeltNormalizeStd = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dMeltNormalizeRatio", "D");
//    ableinfo.dMeltNormalizeRatio = env->GetDoubleField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "bkCalcDotNum", "I");
//    ableinfo.bkCalcDotNum = env->GetIntField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dCrestBkRatio", "D");
//    ableinfo.dCrestBkRatio = env->GetDoubleField(paraminfo, jid);
//
//    jboolean bAble = CPcrAnaAlgorithm::GetInstance()->NewAbleToCalculate(c_pdx, c_pdy, nNum,
//                                                                         ableinfo, newStart,
//                                                                         newForwardNum, newStdNum);
//
//    delete[]c_pdx;
//    delete[]c_pdy;
//
//    return bAble;
//}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_anaalgorithm_AnaAlgorithm_AbleToCalculate(JNIEnv *env, jobject thiz, jdoubleArray dx,
                                                   jdoubleArray dy, jint n_num, jobject param_info,
                                                   jint new_start, jint new_forward_num,
                                                   jfloat new_std_num, jint min_x) {
    double *c_pdx = new double[n_num];
    double *c_pdy = new double[n_num];
    memset(c_pdx, 0, sizeof(double) * n_num);
    memset(c_pdy, 0, sizeof(double) * n_num);

    env->GetDoubleArrayRegion(dx, 0, n_num, c_pdx);
    env->GetDoubleArrayRegion(dy, 0, n_num, c_pdy);

    tagFunAbleToCalcuParamInfo ableinfo;
    jclass jclazz = (env)->FindClass("com/anaalgorithm/FunAbleToCalcuParamInfo");

    jfieldID jid = env->GetFieldID(jclazz, "bNormalization", "Z");
    ableinfo.bNormalization = env->GetBooleanField(param_info, jid);
    jid = env->GetFieldID(jclazz, "nCurveType", "I");
    ableinfo.nCurveType = env->GetIntField(param_info, jid);

    jid = env->GetFieldID(jclazz, "dAmpNoNormalizeStd", "D");
    ableinfo.dAmpNoNormalizeStd = env->GetDoubleField(param_info, jid);
    jid = env->GetFieldID(jclazz, "dAmpNoNormalizeRatio", "D");
    ableinfo.dAmpNoNormalizeRatio = env->GetDoubleField(param_info, jid);
    jid = env->GetFieldID(jclazz, "dAmpNormalizeStd", "D");
    ableinfo.dAmpNormalizeStd = env->GetDoubleField(param_info, jid);
    jid = env->GetFieldID(jclazz, "dAmpNormalizeRatio", "D");
    ableinfo.dAmpNormalizeRatio = env->GetDoubleField(param_info, jid);

    jid = env->GetFieldID(jclazz, "dFDNoNormalizeVal", "D");
    ableinfo.dFDNoNormalizeVal = env->GetDoubleField(param_info, jid);
    jid = env->GetFieldID(jclazz, "dFDNoNormalizeRatio", "D");
    ableinfo.dFDNoNormalizeRatio = env->GetDoubleField(param_info, jid);
    jid = env->GetFieldID(jclazz, "dFDNormalizeVal", "D");
    ableinfo.dFDNormalizeVal = env->GetDoubleField(param_info, jid);
    jid = env->GetFieldID(jclazz, "dFDNormalizeRatio", "D");
    ableinfo.dFDNormalizeRatio = env->GetDoubleField(param_info, jid);

    jid = env->GetFieldID(jclazz, "dMeltNoNormalizeStd", "D");
    ableinfo.dMeltNoNormalizeStd = env->GetDoubleField(param_info, jid);
    jid = env->GetFieldID(jclazz, "dMeltNoNormalizeRatio", "D");
    ableinfo.dMeltNoNormalizeRatio = env->GetDoubleField(param_info, jid);
    jid = env->GetFieldID(jclazz, "dMeltNormalizeStd", "D");
    ableinfo.dMeltNormalizeStd = env->GetDoubleField(param_info, jid);
    jid = env->GetFieldID(jclazz, "dMeltNormalizeRatio", "D");
    ableinfo.dMeltNormalizeRatio = env->GetDoubleField(param_info, jid);

    jid = env->GetFieldID(jclazz, "bkCalcDotNum", "I");
    ableinfo.bkCalcDotNum = env->GetIntField(param_info, jid);
    jid = env->GetFieldID(jclazz, "dCrestBkRatio", "D");
    ableinfo.dCrestBkRatio = env->GetDoubleField(param_info, jid);

    jboolean bAble = CPcrAnaAlgorithm::GetInstance()->NewAbleToCalculate(
            c_pdx, c_pdy, n_num,
            ableinfo, new_start,
            new_forward_num, new_std_num,min_x);

    delete[]c_pdx;
    delete[]c_pdy;
//    if(bAble) {
//        return JNI_TRUE;//    return static_cast<jboolean>(true);
//    }else {
//        return JNI_FALSE;//return static_cast<jboolean>(false);
//    }
    return bAble;
}


/**
* 快速排序
* \return
*/
extern "C" JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_SortDouble(JNIEnv *env, jobject obj, jint nNum,
                                              jdoubleArray pInput, jdoubleArray pOutput) {
    double *c_pInput = new double[nNum];
    double *c_pOutput = new double[nNum];
    memset(c_pInput, 0, sizeof(double) * nNum);
    memset(c_pOutput, 0, sizeof(double) * nNum);

    env->GetDoubleArrayRegion(pInput, 0, nNum, c_pInput);

    CPcrAnaAlgorithm::GetInstance()->SortDouble(nNum, c_pInput, c_pOutput);

    env->SetDoubleArrayRegion(pOutput, 0, nNum, c_pOutput);

    delete[]c_pInput;
    delete[]c_pOutput;
}

/**
* 一阶负导数
* \return
*/
extern "C" JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_FirstNegDerivative(JNIEnv *env, jobject obj, jint nCurveNum,
                                                      jint nPtNum, jdoubleArray dX,
                                                      jdoubleArray dY, jdoubleArray dXReturn,
                                                      jdoubleArray dYReturn) {
    double *c_pdx = new double[nPtNum];
    double *c_pdxOutput = new double[nPtNum];
    double *c_pdyInput = new double[nPtNum];
    double *c_pdyOutput = new double[nPtNum];
    memset(c_pdx, 0, sizeof(double) * nPtNum);
    memset(c_pdxOutput, 0, sizeof(double) * nPtNum);
    memset(c_pdyInput, 0, sizeof(double) * nPtNum);
    memset(c_pdyOutput, 0, sizeof(double) * nPtNum);

    env->GetDoubleArrayRegion(dX, 0, nPtNum, c_pdx);
    env->GetDoubleArrayRegion(dY, 0, nPtNum, c_pdyInput);

    CPcrAnaAlgorithm::GetInstance()->FirstNegDerivative(nCurveNum, nPtNum, c_pdx, c_pdyInput,
                                                        c_pdxOutput, c_pdyOutput);

    env->SetDoubleArrayRegion(dXReturn, 0, nPtNum, c_pdxOutput);
    env->SetDoubleArrayRegion(dYReturn, 0, nPtNum, c_pdyOutput);

    delete[]c_pdx;
    delete[]c_pdxOutput;
    delete[]c_pdyInput;
    delete[]c_pdyOutput;
}


/**
* 光滑曲线（两次数字滤波）
* \return
*/
extern "C" JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_DataDigitalFilter(JNIEnv *env, jobject obj, jint nCurveNum,
                                                     jint nPtNum, jdoubleArray dX,
                                                     jdoubleArray dY, jdoubleArray dYReturn) {
    double *c_pdx = new double[nPtNum];
    double *c_pdyInput = new double[nPtNum];
    double *c_pdyOutput = new double[nPtNum];
    memset(c_pdx, 0, sizeof(double) * nPtNum);
    memset(c_pdyInput, 0, sizeof(double) * nPtNum);
    memset(c_pdyOutput, 0, sizeof(double) * nPtNum);

    env->GetDoubleArrayRegion(dX, 0, nPtNum, c_pdx);
    env->GetDoubleArrayRegion(dY, 0, nPtNum, c_pdyInput);

    CPcrAnaAlgorithm::GetInstance()->DataDigitalFilter(nCurveNum, nPtNum, c_pdx, c_pdyInput,
                                                       c_pdyOutput);

    env->SetDoubleArrayRegion(dYReturn, 0, nPtNum, c_pdyOutput);

    delete[]c_pdx;
    delete[]c_pdyInput;
    delete[]c_pdyOutput;
}


/**
* 插值计算某一点值
* \return
*/
extern "C" JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_Spline(JNIEnv *env, jobject obj, jdoubleArray x,
                                          jdoubleArray y, jint nNum, jdouble xCur,
                                          jobject yCur) {
    double *c_pdx = new double[nNum];
    double *c_pdy = new double[nNum];
    memset(c_pdx, 0, sizeof(double) * nNum);
    memset(c_pdy, 0, sizeof(double) * nNum);

    env->GetDoubleArrayRegion(x, 0, nNum, c_pdx);
    env->GetDoubleArrayRegion(y, 0, nNum, c_pdy);

    double c_yCur = 1;
    CPcrAnaAlgorithm::GetInstance()->Spline(c_pdx, c_pdy, nNum, xCur, c_yCur);

//修改参数值
    jclass jInt = (env)->FindClass("com/anaalgorithm/MyDouble");
    jfieldID jid = env->GetFieldID(jInt, "value", "D");
    env->SetDoubleField(yCur, jid, c_yCur);

    delete[]c_pdx;
    delete[]c_pdy;
}

/**
* N阶导数
* dX
* dY
* nNum （总的数据点数）
* nDev
* dReturn
* \return
*/
extern "C" JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_CalDerivative(JNIEnv *env, jobject obj, jdoubleArray dX,
                                                 jdoubleArray dY, jint nPtNum, jint nDev,
                                                 jdoubleArray dReturn) {
    double *c_pdx = new double[nPtNum];
    double *c_pdy = new double[nPtNum];
    double *c_preturn = new double[nPtNum];

    memset(c_pdx, 0, sizeof(double) * nPtNum);
    memset(c_pdy, 0, sizeof(double) * nPtNum);
    memset(c_preturn, 0, sizeof(double) * nPtNum);

    env->GetDoubleArrayRegion(dX, 0, nPtNum, c_pdx);
    env->GetDoubleArrayRegion(dY, 0, nPtNum, c_pdy);

    CPcrAnaAlgorithm::GetInstance()->NewCalDerivative(c_pdx, c_pdy, nPtNum, nDev,
                                                      c_preturn);

    env->SetDoubleArrayRegion(dReturn, 0, nPtNum, c_preturn);

    delete[]c_pdx;
    delete[]c_pdy;
    delete[]c_preturn;
}


/**
* 最大二阶导数自动计算Ct值
* nCurveNum (缺省值 = 1)
* nPtNum （总的数据点数，单条曲线数据点 = nPtNum/nCurveNum）
* dX
* dY
* dCt（double dCt[1]）
* dLinePara( double dPara[2])
* bStdCurve(false)
* nFiltNum ()
* \return
*/
extern "C" JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_GenStdCurveBySndDerivative(JNIEnv *env, jobject obj,
                                                              jint nCurveNum, jint nPtNum,
                                                              jdoubleArray dX, jdoubleArray dY,
                                                              jdoubleArray dCt,
                                                              jdoubleArray dLinePara,
                                                              jboolean bStdCurve,
                                                              jint nFiltNum) {
    double *c_pdx = new double[nPtNum];
    double *c_pdy = new double[nPtNum];
    double *c_pCt = new double[nCurveNum];
    double *c_pLinePara = new double[2];

    memset(c_pdx, 0, sizeof(double) * nPtNum);
    memset(c_pdy, 0, sizeof(double) * nPtNum);
    memset(c_pCt, 0, sizeof(double) * nCurveNum);
    memset(c_pLinePara, 0, sizeof(double) * 2);

    env->GetDoubleArrayRegion(dX, 0, nPtNum, c_pdx);
    env->GetDoubleArrayRegion(dY, 0, nPtNum, c_pdy);

    CPcrAnaAlgorithm::GetInstance()->GenStdCurveBySndDerivative(nCurveNum, nPtNum, c_pdx, c_pdy,
                                                                c_pCt, c_pLinePara, bStdCurve,
                                                                nFiltNum);

    env->SetDoubleArrayRegion(dCt, 0, nCurveNum, c_pCt);
    env->SetDoubleArrayRegion(dLinePara, 0, 2, c_pLinePara);

    delete[]c_pdx;
    delete[]c_pdy;
    delete[]c_pCt;
    delete[]c_pLinePara;
}


/**
* 根据确定的荧光阈值计算ct值
* nCurveNum (缺省值 = 1)
* nPtNum （总的数据点数，单条曲线数据点 = nPtNum/nCurveNum）
* dX
* dY
* dYTemp 阈值
* dCt（double dCt[1]）
* dLinePara( double dPara[2])
* bStdCurve(false)
* \return
*/
extern "C" JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_GenStdCurveBySelFluThres(JNIEnv *env, jobject obj,
                                                            jint nCurveNum, jint nPtNum,
                                                            jdoubleArray dX, jdoubleArray dY,
                                                            jdouble dYTemp, jdoubleArray dCt,
                                                            jdoubleArray dLinePara,
                                                            jboolean bStdCurve) {
    double *c_pdx = new double[nPtNum];
    double *c_pdy = new double[nPtNum];
    double *c_pCt = new double[nCurveNum];
    double *c_pLinePara = new double[2];

    memset(c_pdx, 0, sizeof(double) * nPtNum);
    memset(c_pdy, 0, sizeof(double) * nPtNum);
    memset(c_pCt, 0, sizeof(double) * nCurveNum);
    memset(c_pLinePara, 0, sizeof(double) * 2);

    env->GetDoubleArrayRegion(dX, 0, nPtNum, c_pdx);
    env->GetDoubleArrayRegion(dY, 0, nPtNum, c_pdy);

    CPcrAnaAlgorithm::GetInstance()->GenStdCurveBySelFluThres(nCurveNum, nPtNum, c_pdx, c_pdy,
                                                              dYTemp, c_pCt, c_pLinePara,
                                                              bStdCurve);

    env->SetDoubleArrayRegion(dCt, 0, nCurveNum, c_pCt);
    env->SetDoubleArrayRegion(dLinePara, 0, 2, c_pLinePara);

    delete[]c_pdx;
    delete[]c_pdy;
    delete[]c_pCt;
    delete[]c_pLinePara;
}
/**
* 根据基线段标准差的倍率阈值计算ct值
* nCurveNum (缺省值 = 1)
* nPtNum （总的数据点数，单条曲线数据点 = nPtNum/nCurveNum）
* nStar 基线起点下标（数组序号从0开始）
* nEnd  基线终点下标（数组序号从0开始）//基线段的取值范围[nStar,nEnd)--for(int i=nStar;i<nEnd;i++)
* nMultiple 标准差倍率（缺省值=10）
* dX
* dY
* dCt（double dCt[1]，返回计算得出的Ct值）
* dLinePara( double dPara[2]，需传入参数，尚未用到)
* bStdCurve(缺省值 =false)
* bBaseLine(扣除基线背景 缺省值=false) true,信号值减去基线背景值；false，信号值不做处理
* \return
*/
extern "C" JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_GenStdCurveByStd(JNIEnv *env, jobject obj, jint nCurveNum,
                                                    jint nPtNum, jint nStar, jint nEnd,
                                                    jint nMultiple, jdoubleArray dX,
                                                    jdoubleArray dY, jdoubleArray dCt,
                                                    jdoubleArray dLinePara, jboolean bStdCurve,
                                                    jboolean bBaseLine) {
    double *c_pdx = new double[nPtNum];
    double *c_pdy = new double[nPtNum];
    double *c_pCt = new double[nCurveNum];
    double *c_pLinePara = new double[2];

    memset(c_pdx, 0, sizeof(double) * nPtNum);
    memset(c_pdy, 0, sizeof(double) * nPtNum);
    memset(c_pCt, 0, sizeof(double) * nCurveNum);
    memset(c_pLinePara, 0, sizeof(double) * 2);

    env->GetDoubleArrayRegion(dX, 0, nPtNum, c_pdx);
    env->GetDoubleArrayRegion(dY, 0, nPtNum, c_pdy);

    CPcrAnaAlgorithm::GetInstance()->GenStdCurveByStd(nCurveNum, nPtNum, nStar, nEnd, nMultiple,
                                                      c_pdx, c_pdy, c_pCt, c_pLinePara, bStdCurve,
                                                      bBaseLine);
    env->SetDoubleArrayRegion(dCt, 0, nCurveNum, c_pCt);
    env->SetDoubleArrayRegion(dLinePara, 0, 2, c_pLinePara);

    delete[]c_pdx;
    delete[]c_pdy;
    delete[]c_pCt;
    delete[]c_pLinePara;
}

/**
* 自动搜索波峰最大值位置，按峰值信号的高低依次输出各自的峰值位置信息
* nCrestNum (待搜索的波峰的数量，缺省值 = 1)
* nPtNum （总的数据点数）
* dXValue
* dYValue
* dReturn (double dReturn[nCrestNum],返回实际搜索到的峰位置信息；若峰不存在，dReturn[i] = -1)
* \return
*/
extern "C" JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_AutoFindCrest(JNIEnv *env, jobject obj, jint nCrestNum,
                                                 jint nPtNum, jdoubleArray dXValue,
                                                 jdoubleArray dYValue,
                                                 jdoubleArray dReturn, jdouble dThre) {
    double *c_pdx = new double[nPtNum];
    double *c_pdy = new double[nPtNum];
    double *c_pReturn = new double[nCrestNum];

    memset(c_pdx, 0, sizeof(double) * nPtNum);
    memset(c_pdy, 0, sizeof(double) * nPtNum);
    memset(c_pReturn, 0, sizeof(double) * nCrestNum);

    env->GetDoubleArrayRegion(dXValue, 0, nPtNum, c_pdx);
    env->GetDoubleArrayRegion(dYValue, 0, nPtNum, c_pdy);

    CPcrAnaAlgorithm::GetInstance()->AutoFindCrest(nCrestNum, nPtNum, c_pdx, c_pdy, c_pReturn, 0.2);

    env->SetDoubleArrayRegion(dReturn, 0, nCrestNum, c_pReturn);

    delete[]c_pdx;
    delete[]c_pdy;
    delete[]c_pReturn;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_NormalizedAnalysisBySndDerivativeOpenBaseLineParam(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jint n_num,
                                                                                      jdoubleArray pdx,
                                                                                      jdoubleArray pdy_input,
                                                                                      jdoubleArray pdy_output,
                                                                                      jobject paraminfo,
                                                                                      jobject base_line_param,
                                                                                      jint cur_index,
                                                                                      jdoubleArray p_ydisplay_input,
                                                                                      jdoubleArray p_ydisplay_output) {
    double *c_pdx = new double[n_num];
    double *c_pdyInput = new double[n_num];
    double *c_pdyOutput = new double[n_num];
    double *c_pdyDisplayIOutput = new double[n_num];
    memset(c_pdx, 0, sizeof(double) * n_num);
    memset(c_pdyInput, 0, sizeof(double) * n_num);
    memset(c_pdyOutput, 0, sizeof(double) * n_num);
    memset(c_pdyDisplayIOutput, 0, sizeof(double) * n_num);

    env->GetDoubleArrayRegion(pdx, 0, n_num, c_pdx);
    env->GetDoubleArrayRegion(pdy_input, 0, n_num, c_pdyInput);
    env->GetDoubleArrayRegion(p_ydisplay_input, 0, n_num, c_pdyDisplayIOutput);

    tagFunAmpNormalizedAnaParamInfo normainfo;
    jclass jclazz = (env)->FindClass("com/anaalgorithm/FunAmpNormalizedAnaParamInfo");

    jfieldID jid = env->GetFieldID(jclazz, "bAbleToCalcu", "Z");
    normainfo.bAbleToCalcu = env->GetBooleanField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "nAvgNum", "I");
    normainfo.nAvgNum = env->GetIntField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "nNormalizeStartPreXPos", "I");
    normainfo.nNormalizeStartPreXPos = env->GetIntField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "nFiltNum", "I");
    normainfo.nFiltNum = env->GetIntField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "bBaseCorrection", "Z");
    normainfo.bBaseCorrection = env->GetBooleanField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "dBCK", "D");
    normainfo.dBCK = env->GetDoubleField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "dBCR2", "D");
    normainfo.dBCR2 = env->GetDoubleField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "nBCXPosThre", "I");
    normainfo.nBCXPosThre = env->GetIntField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "nBCStart", "I");
    normainfo.nBCStart = env->GetIntField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "nBCEndPreXPos", "I");
    normainfo.nBCEndPreXPos = env->GetIntField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "nBCEndPreNumPos", "I");
    normainfo.nBCEndPreNumPos = env->GetIntField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "bMinus1", "Z");
    normainfo.bMinus1 = env->GetBooleanField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "bBaseSuppression", "Z");
    normainfo.bBaseSuppression = env->GetBooleanField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "dBSRatioAllData", "D");
    normainfo.dBSRatioAllData = env->GetDoubleField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "dBSRatioBaseLine", "D");
    normainfo.dBSRatioBaseLine = env->GetDoubleField(paraminfo, jid);

    BaseLineParam baseLineParam;
    jclass jclaxx = (env)->FindClass("com/anaalgorithm/BaseLineParam");

    jid = env->GetFieldID(jclaxx, "bSelfDefine", "Z");
    baseLineParam.bSelfDefine = env->GetBooleanField(base_line_param, jid);

    jid = env->GetFieldID(jclaxx, "selfStart", "I");
    baseLineParam.selfStart = env->GetIntField(base_line_param, jid);
    jid = env->GetFieldID(jclaxx, "selfEnd", "I");
    baseLineParam.selfEnd = env->GetIntField(base_line_param, jid);
    jid = env->GetFieldID(jclaxx, "defStart", "I");
    baseLineParam.defStart = env->GetIntField(base_line_param, jid);
    jid = env->GetFieldID(jclaxx, "defForwadDotNum", "I");
    baseLineParam.defForwadDotNum = env->GetIntField(base_line_param, jid);

    CPcrAnaAlgorithm::GetInstance()->NormalizedAnalysisBySndDerivativeOpenBaseLineParam(n_num,
                                                                                        c_pdx,
                                                                                        c_pdyInput,
                                                                                        c_pdyOutput,
                                                                                        normainfo,
                                                                                        baseLineParam,
                                                                                        cur_index,
                                                                                        c_pdyDisplayIOutput);

    env->SetDoubleArrayRegion(pdy_output, 0, n_num, c_pdyOutput);
    env->SetDoubleArrayRegion(p_ydisplay_output, 0, n_num, c_pdyDisplayIOutput);
    delete[]c_pdx;
    delete[]c_pdyInput;
    delete[]c_pdyOutput;
    delete[]c_pdyDisplayIOutput;
}

//extern "C" JNIEXPORT void JNICALL
//Java_com_anaalgorithm_AnaAlgorithm_NormalizedAnalysisBySndDerivativeOpenBaseLineParam(JNIEnv *env,
//                                                                                      jobject thiz,
//                                                                                      jint n_num,
//                                                                                      jdoubleArray pdx,
//                                                                                      jdoubleArray pdy_input,
//                                                                                      jdoubleArray pdy_output,
//                                                                                      jobject paraminfo,
//                                                                                      jobject base_line_param,
//                                                                                      jint cur_index,
//                                                                                      jdoubleArray p_ydisplay) {
//    double *c_pdx = new double[n_num];
//    double *c_pdyInput = new double[n_num];
//    double *c_pdyOutput = new double[n_num];
//    double *c_pdyDisplayIOutput = new double[n_num];
//    memset(c_pdx, 0, sizeof(double) * n_num);
//    memset(c_pdyInput, 0, sizeof(double) * n_num);
//    memset(c_pdyOutput, 0, sizeof(double) * n_num);
//    memset(p_ydisplay, 0, sizeof(double) * n_num);
//
//    env->GetDoubleArrayRegion(pdx, 0, n_num, c_pdx);
//    env->GetDoubleArrayRegion(pdy_input, 0, n_num, c_pdyInput);
//    env->GetDoubleArrayRegion(p_ydisplay, 0, n_num, c_pdyDisplayIOutput);
//
//    tagFunAmpNormalizedAnaParamInfo normainfo;
//    jclass jclazz = (env)->FindClass("com/anaalgorithm/FunAmpNormalizedAnaParamInfo");
//
//    jfieldID jid = env->GetFieldID(jclazz, "bAbleToCalcu", "Z");
//    normainfo.bAbleToCalcu = env->GetBooleanField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "nAvgNum", "I");
//    normainfo.nAvgNum = env->GetIntField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "nNormalizeStartPreXPos", "I");
//    normainfo.nNormalizeStartPreXPos = env->GetIntField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "nFiltNum", "I");
//    normainfo.nFiltNum = env->GetIntField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "bBaseCorrection", "Z");
//    normainfo.bBaseCorrection = env->GetBooleanField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "dBCK", "D");
//    normainfo.dBCK = env->GetDoubleField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "dBCR2", "D");
//    normainfo.dBCR2 = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "nBCXPosThre", "I");
//    normainfo.nBCXPosThre = env->GetIntField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "nBCStart", "I");
//    normainfo.nBCStart = env->GetIntField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "nBCEndPreXPos", "I");
//    normainfo.nBCEndPreXPos = env->GetIntField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "nBCEndPreNumPos", "I");
//    normainfo.nBCEndPreNumPos = env->GetIntField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "bMinus1", "Z");
//    normainfo.bMinus1 = env->GetBooleanField(paraminfo, jid);
//
//    jid = env->GetFieldID(jclazz, "bBaseSuppression", "Z");
//    normainfo.bBaseSuppression = env->GetBooleanField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dBSRatioAllData", "D");
//    normainfo.dBSRatioAllData = env->GetDoubleField(paraminfo, jid);
//    jid = env->GetFieldID(jclazz, "dBSRatioBaseLine", "D");
//    normainfo.dBSRatioBaseLine = env->GetDoubleField(paraminfo, jid);
//
//    BaseLineParam baseLineParam;
//    jclass jclaxx = (env)->FindClass("com/anaalgorithm/BaseLineParam");
//
//    jid = env->GetFieldID(jclaxx, "bSelfDefine", "Z");
//    baseLineParam.bSelfDefine = env->GetBooleanField(base_line_param, jid);
//
//    jid = env->GetFieldID(jclaxx, "selfStart", "I");
//    baseLineParam.selfStart = env->GetIntField(base_line_param, jid);
//    jid = env->GetFieldID(jclaxx, "selfEnd", "I");
//    baseLineParam.selfEnd = env->GetIntField(base_line_param, jid);
//    jid = env->GetFieldID(jclaxx, "defStart", "I");
//    baseLineParam.defStart = env->GetIntField(base_line_param, jid);
//    jid = env->GetFieldID(jclaxx, "defForwadDotNum", "I");
//    baseLineParam.defForwadDotNum = env->GetIntField(base_line_param, jid);
//
//    CPcrAnaAlgorithm::GetInstance()->NormalizedAnalysisBySndDerivativeOpenBaseLineParam(n_num,
//                                                                                        c_pdx,
//                                                                                        c_pdyInput,
//                                                                                        c_pdyOutput,
//                                                                                        normainfo,
//                                                                                        baseLineParam,
//                                                                                        cur_index,
//                                                                                        c_pdyDisplayIOutput);
//
//    env->SetDoubleArrayRegion(pdy_output, 0, n_num, c_pdyOutput);
//    env->SetDoubleArrayRegion(p_ydisplay, 0, n_num, c_pdyDisplayIOutput);
//    delete[]c_pdx;
//    delete[]c_pdyInput;
//    delete[]c_pdyOutput;
//    delete[]c_pdyDisplayIOutput;
//}

extern "C"
JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_KdsptForwardMBackN_1New(JNIEnv *env, jobject thiz, jint n,
                                                           jdoubleArray p_y, jdoubleArray p_return,
                                                           jint forward_m, jint backward_n) {
    double *c_pdy = new double[n];
    double *c_pdyOutput = new double[n];

    memset(c_pdy, 0, sizeof(double) * n);
    memset(c_pdyOutput, 0, sizeof(double) * n);

    env->GetDoubleArrayRegion(p_y, 0, n, c_pdy);
//    LOGD("forward_m:%d,backward_n:%d",forward_m,backward_n);
    CPcrAnaAlgorithm::GetInstance()->KdsptForwardMBackN_New(n, c_pdy, c_pdyOutput,forward_m, backward_n);

    env->SetDoubleArrayRegion(p_return, 0, n, c_pdyOutput);

    delete[]c_pdy;
    delete[]c_pdyOutput;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_KdsptForwardMBackN(JNIEnv *env, jobject thiz, jint n,
                                                      jdoubleArray p_y, jdoubleArray p_return,
                                                      jint forward_m, jint backward_n) {
    double *c_pdy = new double[n];
    double *c_pdyOutput = new double[n];

    memset(c_pdy, 0, sizeof(double) * n);
    memset(c_pdyOutput, 0, sizeof(double) * n);

    env->GetDoubleArrayRegion(p_y, 0, n, c_pdy);

    CPcrAnaAlgorithm::GetInstance()->KdsptForwardMBackN(n, c_pdy, c_pdyOutput,forward_m, backward_n);

    env->SetDoubleArrayRegion(p_return, 0, n, c_pdyOutput);

    delete[]c_pdy;
    delete[]c_pdyOutput;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_anaalgorithm_AnaAlgorithm_DeltaRnAnalysisBySndDerivativeOpenBaseLineParam(JNIEnv *env,
                                                                                   jobject thiz,
                                                                                   jint n_num,
                                                                                   jdoubleArray pdx,
                                                                                   jdoubleArray pdy_input,
                                                                                   jdoubleArray pdy_output,
                                                                                   jobject paraminfo,
                                                                                   jobject base_line_param,
                                                                                   jdoubleArray p_ydisplay_input,
                                                                                   jdoubleArray p_ydisplay_output) {
    double *c_pdx = new double[n_num];
    double *c_pdyInput = new double[n_num];
    double *c_pdyOutput = new double[n_num];
    double *c_pdyDisplayIOutput = new double[n_num];
    memset(c_pdx, 0, sizeof(double) * n_num);
    memset(c_pdyInput, 0, sizeof(double) * n_num);
    memset(c_pdyOutput, 0, sizeof(double) * n_num);
    memset(c_pdyDisplayIOutput, 0, sizeof(double) * n_num);

    env->GetDoubleArrayRegion(pdx, 0, n_num, c_pdx);
    env->GetDoubleArrayRegion(pdy_input, 0, n_num, c_pdyInput);
    env->GetDoubleArrayRegion(p_ydisplay_input, 0, n_num, c_pdyDisplayIOutput);

    tagFunAmpNormalizedAnaParamInfo normainfo;
    jclass jclazz = (env)->FindClass("com/anaalgorithm/FunAmpNormalizedAnaParamInfo");

    jfieldID jid = env->GetFieldID(jclazz, "bAbleToCalcu", "Z");
    normainfo.bAbleToCalcu = env->GetBooleanField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "nAvgNum", "I");
    normainfo.nAvgNum = env->GetIntField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "nNormalizeStartPreXPos", "I");
    normainfo.nNormalizeStartPreXPos = env->GetIntField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "nFiltNum", "I");
    normainfo.nFiltNum = env->GetIntField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "bBaseCorrection", "Z");
    normainfo.bBaseCorrection = env->GetBooleanField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "dBCK", "D");
    normainfo.dBCK = env->GetDoubleField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "dBCR2", "D");
    normainfo.dBCR2 = env->GetDoubleField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "nBCXPosThre", "I");
    normainfo.nBCXPosThre = env->GetIntField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "nBCStart", "I");
    normainfo.nBCStart = env->GetIntField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "nBCEndPreXPos", "I");
    normainfo.nBCEndPreXPos = env->GetIntField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "nBCEndPreNumPos", "I");
    normainfo.nBCEndPreNumPos = env->GetIntField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "bMinus1", "Z");
    normainfo.bMinus1 = env->GetBooleanField(paraminfo, jid);

    jid = env->GetFieldID(jclazz, "bBaseSuppression", "Z");
    normainfo.bBaseSuppression = env->GetBooleanField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "dBSRatioAllData", "D");
    normainfo.dBSRatioAllData = env->GetDoubleField(paraminfo, jid);
    jid = env->GetFieldID(jclazz, "dBSRatioBaseLine", "D");
    normainfo.dBSRatioBaseLine = env->GetDoubleField(paraminfo, jid);

    BaseLineParam baseLineParam;
    jclass jclaxx = (env)->FindClass("com/anaalgorithm/BaseLineParam");

    jid = env->GetFieldID(jclaxx, "bSelfDefine", "Z");
    baseLineParam.bSelfDefine = env->GetBooleanField(base_line_param, jid);

    jid = env->GetFieldID(jclaxx, "selfStart", "I");
    baseLineParam.selfStart = env->GetIntField(base_line_param, jid);
    jid = env->GetFieldID(jclaxx, "selfEnd", "I");
    baseLineParam.selfEnd = env->GetIntField(base_line_param, jid);
    jid = env->GetFieldID(jclaxx, "defStart", "I");
    baseLineParam.defStart = env->GetIntField(base_line_param, jid);
    jid = env->GetFieldID(jclaxx, "defForwadDotNum", "I");
    baseLineParam.defForwadDotNum = env->GetIntField(base_line_param, jid);

    CPcrAnaAlgorithm::GetInstance()->DeltaRnAnalysisBySndDerivativeOpenBaseLineParam(n_num,
                                                                                        c_pdx,
                                                                                        c_pdyInput,
                                                                                        c_pdyOutput,
                                                                                        normainfo,
                                                                                        baseLineParam,
                                                                                        c_pdyDisplayIOutput);

    env->SetDoubleArrayRegion(pdy_output, 0, n_num, c_pdyOutput);
    env->SetDoubleArrayRegion(p_ydisplay_output, 0, n_num, c_pdyDisplayIOutput);
    delete[]c_pdx;
    delete[]c_pdyInput;
    delete[]c_pdyOutput;
    delete[]c_pdyDisplayIOutput;
}