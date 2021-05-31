#include "PcrAnaDataAlgorithm.h"
#include "PcrDeclares.h"
//#include "PcrDeclares.h"
#include <vector>
/**
 * \ingroup RtPCR
 * 分析算法类，
 *
 * \par requirements
 * win98 or later\n
 * win2k or later\n
 * MFC\n
 *
 * \version 1.0
 * first version
 *
 * \date 2006-06-22
 *
 * \author 
 *
 * \par license
 * This code is absolutely free to use and modify. The code is provided "as is" with
 * no expressed or implied warranty. The author accepts no liability if it causes
 * any damage to your computer, causes your pet to fall ill, increases baldness
 * or makes your car start emitting strange noises when you start it up.
 * This code has no bugs, just undocumented features!
 * 
 * \todo 
 *
 * \bug 
 *
 */
//数字滤波类型
//enum eFilterType
//{
//	CubicSmooth5,CubicSmooth7,QuadraticSmooth7,QuadraticSmooth5,TriangularSmooths_29,GaussianSmooths_31,DtMean
//};

//归一化分析参数结构(熔曲)
typedef struct structNormalizeParaminfo {
    bool bAble;            //可否进行归一化
    int nMinPos;        //归一化最小值位置
    int nMaxPos;        //归一化最大值位置
    float fMinTemp;
    float fMaxTemp;

    structNormalizeParaminfo() {
        bAble = false;
        nMinPos = -1;
        nMaxPos = -1;
        fMinTemp = 0;
        fMaxTemp = 0;
    }

} NORMAPARAMINFO, *PNORMAPARAMINFO;

typedef struct tagFunAbleToCalcuParamInfo {
    bool bNormalization;
    int nCurveType;
    double dAmpNoNormalizeStd;
    double dAmpNoNormalizeRatio;
    double dAmpNormalizeStd;
    double dAmpNormalizeRatio;

    double dFDNoNormalizeVal;
    double dFDNoNormalizeRatio;
    double dFDNormalizeVal;
    double dFDNormalizeRatio;

    double dMeltNoNormalizeStd;
    double dMeltNoNormalizeRatio;
    double dMeltNormalizeStd;
    double dMeltNormalizeRatio;

    int bkCalcDotNum;
    double dCrestBkRatio;

    tagFunAbleToCalcuParamInfo() {
        bNormalization = false;
        nCurveType = 0;//0，扩增曲线；1，普通熔解曲线；2，蛋白熔曲
        dAmpNoNormalizeStd = 45;
        dAmpNoNormalizeRatio = 1.1;
        dAmpNormalizeStd = 0.007;
        dAmpNormalizeRatio = 1.1;

        dFDNoNormalizeVal = 40;
        dFDNoNormalizeRatio = 2;
        dFDNormalizeVal = 0.01;
        dFDNormalizeRatio = 2;

        dMeltNoNormalizeStd = 45;
        dMeltNoNormalizeRatio = 1.1;
        dMeltNormalizeStd = 0.01;
        dMeltNormalizeRatio = 1.1;

        bkCalcDotNum = 3;
        dCrestBkRatio = 0.002;
    }
} FUNABLETOCALCUPARAMINFO, *PFUNABLETOCALCUPARAMINFO;

typedef struct tagFunAmpNormalizedAnaParamInfo {
    bool bAbleToCalcu;
    int nAvgNum;
    int nNormalizeStartPreXPos;
    int nFiltNum;

    bool bBaseCorrection;
    double dBCK;
    double dBCR2;
    int nBCXPosThre;
    int nBCStart;
    int nBCEndPreXPos;
    int nBCEndPreNumPos;

    bool bMinus1;

    bool bBaseSuppression;
    double dBSRatioAllData;
    double dBSRatioBaseLine;

    tagFunAmpNormalizedAnaParamInfo() {
        bAbleToCalcu = false;
        nAvgNum = 3;
        nNormalizeStartPreXPos = 5;
        nFiltNum = 4;
        bBaseCorrection = true;
        dBCK = 0xFFFF;
        dBCR2 = 0.5;
        nBCXPosThre = 10;
        nBCStart = 1;
        nBCEndPreXPos = 3;
        nBCEndPreNumPos = 0;
        bMinus1 = true;
        bBaseSuppression = true;
        dBSRatioAllData = 0.5;
        dBSRatioBaseLine = 0.5;
    }
} FUNAMPNORMALIZEDANAPARAMINFO, *PFUNAMPNORMALIZEDANAPARAMINFO;

typedef struct BaseLineParam {
    bool bSelfDefine;
    int selfStart;
    int selfEnd;
    int defStart;
    int defForwadDotNum;

    BaseLineParam() {
        bSelfDefine = false;
        selfStart = 1;
        selfEnd = 8;
        defStart = 1;
        defForwadDotNum = 3;
    }
} BASELINEPARAM, *PBASELINEPARAM;

class CPcrAnaAlgorithm : CPcrAnaDataAlgorithm {
public:
    CPcrAnaAlgorithm(void);

    ~CPcrAnaAlgorithm(void);

protected:
/*-----------------------变量声明------------------------------------------------------*/
////数字滤波类型
//enum eFilterType
//{
//	CubicSmooth5,CubicSmooth7,QuadraticSmooth7,QuadraticSmooth5
//};
    static CPcrAnaAlgorithm *m_pInstance;

public:
    static CPcrAnaAlgorithm *GetInstance();

/*-----------------------基本合成函数------------------------------------------------------*/
//插值法计算峰值
    void FindCrestWithSpline(double *x, double *y, int nNum, double &dReX, double &dReY);

    static void FindCrestWithParabola(double *x, double *y, int nNum, int nWndWidth, double &dXPos);

//线性拟合
    static void LinearFit(double *x, double *y, int nNum, double *dReturn);

//数字滤波
    static void DigitalFilter(double *x, double *dReturn, int nNum,
                              eFilterType eType = FILTERTYPE_CubicSmooth_5, int nWnd = 3);

//插值计算某一点值
    void Spline(double *x, double *y, int nNum, double xCur, double &yCur);

    void SplineEx(double *x, double *y, int nNum, double xCur, double &yCur);

//数组中的元素头尾两端依次对调
    void Reverse_array(double *x, int nNum, double *dReturn);

    void findPeak(const std::vector<int> &v, std::vector<int> &peakPositions);

/*----------------------用户调用的函数--------------------------------------------------*/
    void OnNoneLinearEquation();

    void OnLinearFit();

    void GenStdCurveBySndDerivative(int nCurveNum, int nPtNum, double *dX, double *dY, double *dCt,
                                    double *dLinePara, bool bStdCurve, int nFiltNum = 4);

    void GenStdCurveBySelFluThres(int nCurveNum, int nPtNum, double *dX, double *dY, double dYTemp,
                                  double *dCt, double *dLinePara, bool bStdCurve);

    void GenStdCurveByStd(int nCurveNum, int nPtNum, int nStar, int nEnd, int nMultiple, double *dX,
                          double *dY, double *dCt, double *dLinePara, bool bStdCurve,
                          bool bBaseLine);

    void FirstNegDerivative(int nCurveNum, int nPtNum, double *dX, double *dY, double *dXReturn,
                            double *dYReturn);

    void DataDigitalFilter(int nCurveNum, int nPtNum, double *dX, double *dY, double *dYReturn);

    void DataSmooth(int nCurveNum, int nPtNum, int nWidth, double *dY, double *dYReturn);

    void AdjustBaseLine(int nNum, int nAvgNum, double *pInput, double *pOutput);

    void AdjustBaseLine(int nNum, int nAvgNum, double *pdx, double *pdyInput, double *pdyOutput,
                        bool bNormalization = false);

    static void SortDouble(int nNum, double *pInput, double *pOutput);

    void AutoFindCrest(int nCrestNum, int nPtNum, double *dXValue, double *dYValue, double *dReturn,
                       double dThrePercent = 0.2);

    void
    OnFourLogsFit_Aml(double *dx, double *dy, int nPtNum, int nLeft, int nRight, double *dYReturn);

    void
    OnFourLogsFit_Melt(double *dx, double *dy, int nPtNum, int nLeft, int nRight, double *dYReturn);

    void NormalizedAnalysis(int nNum, double *pInput, double *pOutput);

    //计算一阶/二阶导数
    static void NewCalDerivative(double *x, double *y, int nNum, int nDev, double *dReturn);

    bool NewAbleToCalculate(double *dx, double *dy, int nNum, tagFunAbleToCalcuParamInfo paraminfo,
                            int newStart, int newForwardNum, float newStdNum, int minX);

    void NormalizedAnalysisBySndDerivativeOpenBaseLineParam(int nNum, double *pdx, double *pdyInput,
                                                            double *pdyOutput,
                                                            tagFunAmpNormalizedAnaParamInfo paraminfo,
                                                            BASELINEPARAM baseLineParam,
                                                            int curIndex,
                                                            double *pYDisplay);

    void
    KdsptForwardMBackN_NoShrink(int n, double *pY, double *pReturn, int forwardM, int backwardN);

    void KdsptForwardMBackN_New(int n, double *pY, double *pReturn, int forwardM, int backwardN);

    void KdsptForwardMBackN(int n, double *pY, double *pReturn, int forwardM, int backwardN);

    void DeltaRnAnalysisBySndDerivativeOpenBaseLineParam(int nNum, double *pdx, double *pdyInput,
                                                         double *pdyOutput,
                                                         tagFunAmpNormalizedAnaParamInfo paraminfo,
                                                         BASELINEPARAM baseLineParam,
                                                         double *pYDisplay);

    double Median(double *x, int nNum);


};
