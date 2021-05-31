#include "PcrAnaAlgorithm.h"
#include "PcrAnaDataAlgorithm.h"
#include "PcrAnaSort.h"
#include <math.h>
#include <android/log.h>
#include "CString.h"
#include "PcrDeclares.h"
#include "auto_tchar.h"
#include "CArray.h"
#include "AnaAlgorithm.h"

//#include "LogisticFit.h"
//#include "Fitting.h"

CPcrAnaAlgorithm *CPcrAnaAlgorithm::m_pInstance = NULL;

CPcrAnaAlgorithm::CPcrAnaAlgorithm(void) {
}

CPcrAnaAlgorithm::~CPcrAnaAlgorithm(void) {
}

CPcrAnaAlgorithm *CPcrAnaAlgorithm::GetInstance() {
    if (m_pInstance == NULL) {
        m_pInstance = new CPcrAnaAlgorithm;
    }

    return m_pInstance;
}

//数字滤波器
void CPcrAnaAlgorithm::DigitalFilter(double *x, double *dReturn, int nNum, eFilterType eType,
                                     int nWnd /*= 3*/) {
    switch (eType) {
        case FILTERTYPE_CubicSmooth_5:
            Kdspt(nNum, x, dReturn);
            break;
        case FILTERTYPE_CubicSmooth_7:
            Kdspt7_3(nNum, x, dReturn);
            break;
        case FILTERTYPE_QuadraticSmooth_5:
            Kdspt5_2(nNum, x, dReturn);
            break;
        case FILTERTYPE_QuadraticSmooth_7:
            Kdspt7_2(nNum, x, dReturn);
            break;
        case FILTERTYPE_TriangularSmooths_29:
            Kdspt_TriangularSmooths_29(nNum, x, dReturn);
            break;
        case FILTERTYPE_GaussianSmooths_31:
            Kdspt_GaussianSmooths_31(nNum, x, dReturn);
            break;
        case FILTERTYPE_DTMEAN3:
            Kdspt_dtmean(nNum, 3, x, dReturn);
            break;
        case FILTERTYPE_DTMEAN4:
            Kdspt_dtmean(nNum, 4, x, dReturn);
            break;
        case FILTERTYPE_DTMEAN5:
            Kdspt_dtmean(nNum, 5, x, dReturn);
            break;
        case FILTERTYPE_DTMEAN6:
            Kdspt_dtmean(nNum, 6, x, dReturn);
            break;
        case FILTERTYPE_DTMEAN7:
            Kdspt_dtmean(nNum, 7, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN3:
            Kdspt_cfmean(nNum, 3, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN5:
            Kdspt_cfmean(nNum, 5, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN7:
            Kdspt_cfmean(nNum, 7, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN9:
            Kdspt_cfmean(nNum, 9, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN11:
            Kdspt_cfmean(nNum, 11, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN13:
            Kdspt_cfmean(nNum, 13, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN15:
            Kdspt_cfmean(nNum, 15, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN17:
            Kdspt_cfmean(nNum, 17, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN19:
            Kdspt_cfmean(nNum, 19, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN21:
            Kdspt_cfmean(nNum, 21, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN23:
            Kdspt_cfmean(nNum, 23, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN25:
            Kdspt_cfmean(nNum, 25, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN27:
            Kdspt_cfmean(nNum, 27, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN29:
            Kdspt_cfmean(nNum, 29, x, dReturn);
            break;
        case FILTERTYPE_CFMEAN31:
            Kdspt_cfmean(nNum, 31, x, dReturn);
            break;
        case FILTERTYPE_MEDIAN:
            Kdspt_Median(nNum, nWnd, x, dReturn);
            break;
        default:
            Kdspt(nNum, x, dReturn);
            break;
    }
}

//根据确定标准方差计算ct值，并生成标准曲线
void
CPcrAnaAlgorithm::GenStdCurveByStd(int nCurveNum, int nPtNum, int nStar, int nEnd, int nMultiple,
                                   double *dX, double *dY, double *dCt, double *dLinePara,
                                   bool bStdCurve, bool bBaseLine) {
    if (nPtNum <= nEnd) {
        for (int i = 0; i < nCurveNum; i++)
            dCt[i] = 0;
        return;
    }
    if (nEnd - nStar <= 0)
        return;
    double *dScPoints = new double[nCurveNum];
    double xCur = 0, yCur = 0;
    double *x = new double[nPtNum];
    double *y = new double[nPtNum];
    double *temp = new double[nPtNum];
    for (int i = 0; i < nCurveNum; i++) {
        for (int j = 0; j < nPtNum; j++) {
            x[j] = dX[i * nPtNum + j];
            y[j] = dY[i * nPtNum + j];
        }
        if (!bBaseLine) {
            double dAvg = 0;
            SortDouble(nPtNum, y, temp);
            for (int k = 0; k < 5; k++)
                dAvg += temp[k];
            dAvg /= 5;
            for (int j = 0; j < nPtNum; j++)
                y[j] -= dAvg;
        }
        double avg = 0, std = 0;
        for (int j = nStar; j < nEnd; j++)
            avg += y[j];
        avg /= (nEnd - nStar + 1);
        for (int j = nStar; j < nEnd; j++)
            std += (y[j] - avg) * (y[j] - avg);
        yCur = nMultiple * sqrt(std / (nEnd - nStar));

        double dMin = y[0], dMax = y[0];
        int nMin = 0, nMax = nPtNum - 1;
        for (int j = 1; j < nPtNum; j++) {
            dMin = fmin(dMin, y[j]);
            if (y[j] < dMin) nMin = j;
            dMax = fmax(dMax, y[j]);
            if (y[j] > dMax) nMax = j;
        }
        if (yCur > dMin && yCur < dMax)
            Spline(y, x, nPtNum, yCur, xCur);
        else if (yCur <= dMin)
            xCur = x[nMin];
        else if (yCur >= dMax)
            xCur = x[nMax];
        dScPoints[i] = xCur;
    }
    for (int i = 0; i < nCurveNum; i++)
        dCt[i] = dScPoints[i];

    if (bStdCurve) {
        double *sx = new double[nCurveNum];
        double *sy = new double[nCurveNum];
        for (int i = 0; i < nCurveNum; i++) {
            sx[i] = nCurveNum - i + 1;
            sy[i] = dScPoints[i];
        } // for( i=0; i<nCurveNum; i++)

        double dReCof[2] = {0};
        //最小二乘法拟合标准曲线
        LinearFit(sx, sy, nCurveNum, dReCof);
        dLinePara[0] = dReCof[0];
        dLinePara[1] = dReCof[1];
        delete[] sx;
        delete[] sy;

    }
    delete[] dScPoints;
    delete[] x;
    delete[] y;
    delete[] temp;
}

//根据确定的荧光阈值计算ct值，并生成标准曲线
void CPcrAnaAlgorithm::GenStdCurveBySelFluThres(int nCurveNum, int nPtNum, double *dX, double *dY,
                                                double dYTemp, double *dCt, double *dLinePara,
                                                bool bStdCurve) {
    double *dScPoints = new double[nCurveNum];
    double xCur = 0, yCur = dYTemp;
    double *x = new double[nPtNum];
    double *y = new double[nPtNum];
    for (int i = 0; i < nCurveNum; i++) {
        for (int j = 0; j < nPtNum; j++) {
            x[j] = dX[i * nPtNum + j];
            y[j] = dY[i * nPtNum + j];
        }
        double *dFiltered = new double[nPtNum];
        //两次滤波
        DigitalFilter(y, dFiltered, nPtNum);
        DigitalFilter(dFiltered, y, nPtNum);

        SplineEx(y, x, nPtNum, yCur, xCur);
        delete[] dFiltered;
        dScPoints[i] = xCur;
    }

    for (int i = 0; i < nCurveNum; i++) {
        dCt[i] = dScPoints[i];
    }

    if (bStdCurve) {
        double *sx = new double[nCurveNum];
        double *sy = new double[nCurveNum];
        for (int i = 0; i < nCurveNum; i++) {
            sx[i] = nCurveNum - i + 1;
            sy[i] = dScPoints[i];
        } // for( i=0; i<nCurveNum; i++)

        double dReCof[2] = {0};
        //最小二乘法拟合标准曲线
        LinearFit(sx, sy, nCurveNum, dReCof);
        dLinePara[0] = dReCof[0];
        dLinePara[1] = dReCof[1];
        delete[] sx;
        delete[] sy;

    }
    delete[] dScPoints;
    delete[] x;
    delete[] y;

}

//最大二阶导数自动生成标准曲线
void CPcrAnaAlgorithm::GenStdCurveBySndDerivative(int nCurveNum, int nPtNum, double *dX, double *dY,
                                                  double *dCt, double *dLinePara, bool bStdCurve,
                                                  int nFiltNum) {
    double *dScPoints = new double[nCurveNum];
    double *x = new double[nPtNum];
    double *y = new double[nPtNum];

    for (int i = 0; i < nCurveNum; i++) {
        for (int j = 0; j < nPtNum; j++) {
            x[j] = dX[i * nPtNum + j];
            y[j] = dY[i * nPtNum + j];
        }
        //double temp;

        double *dFiltered = new double[nPtNum];
        double *dReturn = new double[nPtNum];
        //滤波
        for (int itemp = 0; itemp < nFiltNum; itemp++) {
            DigitalFilter(y, dFiltered, nPtNum);
            memcpy(y, dFiltered, sizeof(double) * nPtNum);
            //DigitalFilter(dFiltered,y,nPtNum);
        }
        //计算二阶导数
        NewCalDerivative(x, y, nPtNum, 2, dReturn);

        //计算最大值
//		FindCrestWithSpline(x,dReturn,nPtNum,dScPoints[i], temp);
        FindCrestWithParabola(x, dReturn, nPtNum, 6, dScPoints[i]);

        delete[] dReturn;
        delete[] dFiltered;
    }

    for (int i = 0; i < nCurveNum; i++) {
        dCt[i] = dScPoints[i];
    }

    if (bStdCurve) {
        double *sx = new double[nCurveNum];
        double *sy = new double[nCurveNum];
        for (int i = 0; i < nCurveNum; i++) {
            sx[i] = nCurveNum - i + 1;
            sy[i] = dScPoints[i];
        }
        double dReCof[2] = {0};

        //最小二乘法拟合标准曲线
        LinearFit(sx, sy, nCurveNum, dReCof);
        dLinePara[0] = dReCof[0];
        dLinePara[1] = dReCof[1];
        delete[] sx;
        delete[] sy;
    }

    delete[] dScPoints;
    delete[] x;
    delete[] y;
}

/*
void CPcrAnaAlgorithm::Spline(double *x, double *y, int nNum, double xCur,double &yCur)
{
	int k = -1;
    static double s[5];
	Akspl(x,y,nNum,k,xCur,s,1);
	yCur = s[4];
}
*/
void CPcrAnaAlgorithm::Spline(double *x, double *y, int nNum, double xCur, double &yCur) {
    if (nNum < 1)
        return;
    int k = -1;
    static double s[5];

    double dXMin = x[0];
    int nIndex = 0;
    //for( int i=1; i< nNum; i++)
    //{
    //	dXMin = x[i] < dXMin ? x[i],nIndex = i : dXMin;
    //}

    int nTempNum = nNum - nIndex;
    double *dX = new double[nTempNum];
    double *dY = new double[nTempNum];
    for (int i = 0; i < nTempNum; i++) {
        dX[i] = x[i + nIndex];
        dY[i] = y[i + nIndex];
    }
    Akspl(dX, dY, nTempNum, k, xCur, s, 1);
    yCur = s[4];
    delete[] dX;
    delete[] dY;
}

void CPcrAnaAlgorithm::SplineEx(double *x, double *y, int nNum, double xCur, double &yCur) {
    if (nNum < 1)
        return;
    int k = -1;
    static double s[5];

    double dXMin = x[0];
    int nIndex = 0;
    //for( int i=1; i< nNum; i++)
    //{
    //	dXMin = x[i] < dXMin ? x[i],nIndex = i : dXMin;
    //}

    int nTempNum = nNum - nIndex;
    double *dX = new double[nTempNum];
    double *dY = new double[nTempNum];
    for (int i = 0; i < nTempNum; i++) {
        dX[i] = x[i + nIndex];
        dY[i] = y[i + nIndex];
    }
    AksplEx(dX, dY, nTempNum, k, xCur, s, 1);
    yCur = s[4];
    delete[] dX;
    delete[] dY;
}

/*
void CPcrAnaAlgorithm::LinearFit(double *x, double *y,int nNum, double *dReturn)
{
	double dt[6],a[2];
	Jbsqt(x,y,nNum,a,dt);
	dReturn[0] = a[0];
	dReturn[1] = a[1];
	double xMean = 0, yMean = 0;
	for(int i=0; i<nNum; i++)
	{
		xMean += x[i];
		yMean += y[i];
	} // for(int i=0; i<nNum; i++)
	xMean /= nNum;
	yMean /= nNum;
	double aUp =0, bDown=0, cDown =0;
	for(i=0; i<nNum; i++)
	{
		aUp += fabs((x[i]-xMean)*(y[i]-yMean));
		bDown += (x[i]-xMean)*(x[i]-xMean);
		cDown += (y[i]-yMean)*(y[i]-yMean);
	} // for(i=0; i<nNum; i++)
	if( bDown!= 0 && cDown!= 0)
		dReturn[2] = aUp/(sqrt(bDown*cDown));
	else
		dReturn[2] = 0;
}
*/

void CPcrAnaAlgorithm::LinearFit(double *x, double *y, int nNum, double *dReturn) {
    double dt[6], a[2];
    Jbsqt(x, y, nNum, a, dt);
    dReturn[0] = a[0];
    dReturn[1] = a[1];

    /////////////////R2////////////////////
    double xMean = 0, yMean = 0;
    for (int i = 0; i < nNum; i++) {
        xMean += x[i];
        yMean += y[i];
    } // for(int i=0; i<nNum; i++)
    xMean /= nNum;
    yMean /= nNum;
    double aUp = 0, bDown = 0, cDown = 0;
    for (int i = 0; i < nNum; i++) {
        aUp += fabs((x[i] - xMean) * (y[i] - yMean));
        bDown += (x[i] - xMean) * (x[i] - xMean);
        cDown += (y[i] - yMean) * (y[i] - yMean);
    } // for(i=0; i<nNum; i++)

    //////////////////R///////////////////////
    double dSumXY = 0, dSumX = 0, dSumY = 0, dSumX2 = 0, dSumY2 = 0;
    for (int i = 0; i < nNum; i++) {
        dSumXY += x[i] * y[i];
        dSumX += x[i];
        dSumY += y[i];
        dSumX2 += x[i] * x[i];
        dSumY2 += y[i] * y[i];
    }
    double dUp = nNum * dSumXY - dSumX * dSumY;
    double dDown = sqrt(nNum * dSumX2 - dSumX * dSumX) * sqrt(nNum * dSumY2 - dSumY * dSumY);

    //////////////////////////////////////////
    //	if( bDown!= 0 && cDown!= 0)
    //		dReturn[2] = aUp/(sqrt(bDown*cDown));
    if (dDown != 0)
        dReturn[2] = dUp / dDown;
    else
        dReturn[2] = 0;
}

//最小二乘法线性拟合
void CPcrAnaAlgorithm::OnLinearFit() {
    int nNum = 6;
    double x[6] = {2, 3, 4, 5, 6, 7};
    double y[6] =
//	{ 28.6179461709536,26.6957354044485,23.7462818647008,20.6926920686423,17.6140939967402,15.5767647993065};
            {32.72778746, 29.52240012, 25.96351989, 22.46459362, 19.17151802, 15.10207976};

    double dReturn[3] = {0};
    LinearFit(x, y, nNum, dReturn);

}

void
CPcrAnaAlgorithm::FindCrestWithSpline(double *x, double *y, int nNum, double &dReX, double &dReY) {
    int k = -1;
    int n = nNum;
    static double s[5];
    int nResolution = 10;
    int num = n * nResolution;
    double *xx = new double[num];
    double *yy = new double[num];
    double max = -1000;
    int maxx = 0;
    int nBegin = num / 9;
    int nEnd = (n - 3) * nResolution > num * 7 / 8 ? (n - 3) * nResolution : num * 7 / 8;//num*7/8;
    double dXThres = x[0] + nNum / 9 + 2;
    for (int i = nBegin; i < nEnd; i++) {
        xx[i] = x[0] + i / (double) nResolution;
        Akspl(x, y, n, k, xx[i], s, 0);
        yy[i] = s[4];
        if (yy[i] > max && xx[i] > dXThres) {
            max = yy[i];
            maxx = i;
        }
    }
    dReX = xx[maxx];
//	dReX = dReX < x[0]+nNum/9+2? nNum : dReX;
    dReY = max;

    delete[] xx;
    delete[] yy;
}

void CPcrAnaAlgorithm::FindCrestWithParabola(double *x, double *y, int nNum, int nWndWidth,
                                             double &dXPos) {
    if (nNum < nWndWidth || nWndWidth < 3) {
        dXPos = -1;
        return;
    }

    double *dTempY = new double[nNum];
    memcpy(dTempY, y, nNum * sizeof(double));

    for (int k = 1; k < nNum - 1; k++) {
        if (dTempY[k] < y[k - 1] && dTempY[k] < y[k + 1])
            dTempY[k] = (y[k - 1] + y[k + 1]) / 2;
    }

    double dSum, dMax = -1000, dMaxCrest = -1000;
    int nCrestPos = 0;

    for (int i = 0; i < nNum - nWndWidth; i++) {
        dSum = 0;
        for (int j = 0; j < nWndWidth; j++) {
            dSum += dTempY[i + j];
        }
        if (dMax < dSum) {
            dMax = dSum;
            nCrestPos = i;
        }
        dMaxCrest = dMaxCrest < dTempY[i] ? dTempY[i] : dMaxCrest;
    }

    dMaxCrest /= 10;
    int end, start = 0;
    start = nCrestPos;
    end = nCrestPos + nWndWidth - 1;

    int sumn = (int) (end - start) + 1;

    if (sumn < 3) {
        dXPos = -1;
        return;
    }

    double sumx = 0.0, sumx2 = 0.0, sumx3 = 0.0, sumx4 = 0.0;
    double sumy = 0.0, sumxy = 0.0, sumx2y = 0.0;

    double doubj;
    for (int j = start; j <= end; j++) {
        doubj = (double) j - start;

        sumx += doubj;
        sumx2 += doubj * doubj;
        sumx3 += doubj * doubj * doubj;
        sumx4 += doubj * doubj * doubj * doubj;
        sumy += dTempY[j] / dMaxCrest;                      // Division is to normalize data
        sumxy += doubj * (dTempY[j] / dMaxCrest);
        sumx2y += doubj * doubj * (dTempY[j] / dMaxCrest);  // Division is to normalize data
    }

    dXPos = 0.0;        /* for use in case of failure */

    delete[] dTempY;
    double determ = sumn * (sumx2 * sumx4 - sumx3 * sumx3) - sumx * (sumx * sumx4 - sumx2 * sumx3)
                    + sumx2 * (sumx * sumx3 - sumx2 * sumx2);

    if (fabs(determ) < 1.0E-12) {
        dXPos = -1;
        return;
    }

    double parba = sumy * (sumx2 * sumx4 - sumx3 * sumx3) - sumxy * (sumx * sumx4 - sumx2 * sumx3)
                   + sumx2y * (sumx * sumx3 - sumx2 * sumx2);

    parba /= determ;
    double parbb = -sumy * (sumx * sumx4 - sumx2 * sumx3) + sumxy * (sumn * sumx4 - sumx2 * sumx2)
                   - sumx2y * (sumn * sumx3 - sumx * sumx2);

    parbb /= determ;
    double parbc = sumy * (sumx * sumx3 - sumx2 * sumx2) - sumxy * (sumn * sumx3 - sumx * sumx2)
                   + sumx2y * (sumn * sumx2 - sumx * sumx);

    parbc /= determ;
    dXPos = -(parbb) / (2.0 * (parbc));


    dXPos += start;

    //该函数有bug，尚未修复
    if ((int) (dXPos + 0.5) >= nNum) {
        dXPos = nNum - 1;
    } else if ((int) (dXPos + 0.5) <= 1) {
        dXPos = start + sumn / 2.0;
    }
}

void CPcrAnaAlgorithm::OnNoneLinearEquation() {
//求解非线性方程组，包括非方阵求逆  Ax = b -> x = iv(A)*b
//------------------------------------------------------------------------//
    int m, n, ka, i, j;
    static double a[3][3] =
            {{6, 3,   2.2},
             {3, 2.2, 1.8},
             {2, 1.8, 1.5664}};
//	{ {1.0,2.0,3.0,4.0},{6.0,7.0,8.0,9.0},{1.0,2.0,13.0,0.0},{16.0,17.0,8.0,9.0},{2.0,4.0,3.0,4.0}};
    static double aa[3][3], c[3][3], u[3][3], v[3][3];
    double eps;
    m = 3;
    n = 3; //
    ka = 3;
    eps = 0.000001;

    i = Dginv((double *) a, m, n, (double *) aa, eps, (double *) u, (double *) v, ka);

    FILE *fp;
    fp = fopen("D:\\chenhr\\rt_PCR\\Workshop\\Source\\data.txt", "w+");
    CString str;
    str = _T(" Mat A is\n");
    fwrite(&str, 1, str.GetLength(), fp);
    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            str.Format(_T("%f	"), a[i][j]);
            fwrite(&str, 1, str.GetLength(), fp);
        }
        str = _T("\n");
        fwrite(&str, 1, str.GetLength(), fp);
    }
    str = _T("\n Mat A+ \n");
    fwrite(&str, 1, str.GetLength(), fp);
    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            str.Format(_T("%f	"), aa[i][j]);
            fwrite(&str, 1, str.GetLength(), fp);
        }
        str = _T("\n");
        fwrite(&str, 1, str.GetLength(), fp);
    }

    double tt[3][1] = {{26.2},
                       {15.2},
                       {11.1664}};
    double ttt[3][1];
    Damul((double *) aa, (double *) tt, 3, 3, 1, (double *) ttt);
    str = _T("\n Solution is: \n");
    fwrite(&str, 1, str.GetLength(), fp);
    for (j = 0; j < 3; j++) {
        str.Format(_T("%f	\n"), ttt[j][0]);
        fwrite(str, 1, str.GetLength(), fp);
    }
    str = _T("\n");
    fwrite(&str, 1, str.GetLength(), fp);

    i = Dginv((double *) aa, n, m, (double *) c, eps, (double *) v, (double *) u, ka);
    str = _T("\n Mat A++ \n");
    fwrite(&str, 1, str.GetLength(), fp);
    for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
            str.Format(_T("%f	"), c[i][j]);
            fwrite(str, 1, str.GetLength(), fp);
        }
        str = _T("\n");
        fwrite(&str, 1, str.GetLength(), fp);
    }


    fclose(fp);
}

/**
 * 一阶负导数
 * \param nCurveNum
 * \param nPtNum
 * \param *dX
 * \param *dY
 * \param *dYReturn
 */
void CPcrAnaAlgorithm::FirstNegDerivative(int nCurveNum, int nPtNum, double *dX, double *dY,
                                          double *dXReturn, double *dYReturn) {
    double *x = new double[nPtNum];
    double *y = new double[nPtNum];
    double *dFiltered = new double[nPtNum];
    double *dReturn = new double[nPtNum];
    for (int i = 0; i < nCurveNum; i++) {
        for (int j = 0; j < nPtNum; j++) {
            x[j] = dX[i * nPtNum + j];
            y[j] = dY[i * nPtNum + j];
        }
        //两次滤波
        DigitalFilter(x, dFiltered, nPtNum, FILTERTYPE_CFMEAN5);
        DigitalFilter(dFiltered, x, nPtNum, FILTERTYPE_CFMEAN5);

        DigitalFilter(y, dFiltered, nPtNum, FILTERTYPE_CFMEAN5);
        DigitalFilter(dFiltered, y, nPtNum, FILTERTYPE_CFMEAN5);

        //计算二阶导数
        NewCalDerivative(x, y, nPtNum, 1, dReturn);


        DigitalFilter(x, dFiltered, nPtNum, FILTERTYPE_CFMEAN5);
        DigitalFilter(dFiltered, dXReturn, nPtNum, FILTERTYPE_CFMEAN5);

        DigitalFilter(dReturn, dFiltered, nPtNum, FILTERTYPE_CFMEAN5);
        DigitalFilter(dFiltered, dReturn, nPtNum, FILTERTYPE_CFMEAN5);


        for (int j = 0; j < nPtNum; j++)
            dYReturn[i * nPtNum + j] = -dReturn[j];
    }
    delete[] dReturn;
    delete[] dFiltered;
    delete[] x;
    delete[] y;
}

/**
 * 数字滤波
 * \param nCurveNum
 * \param nPtNum
 * \param *dX
 * \param *dY
 * \param *dYReturn
 */
void CPcrAnaAlgorithm::DataDigitalFilter(int nCurveNum, int nPtNum, double *dX, double *dY,
                                         double *dYReturn) {
    double *y = new double[nPtNum];
    double *dFiltered = new double[nPtNum];
    for (int i = 0; i < nCurveNum; i++) {
        for (int j = 0; j < nPtNum; j++) {
            y[j] = dY[i * nPtNum + j];
        }
        DigitalFilter(y, dFiltered, nPtNum);
        DigitalFilter(dFiltered, y, nPtNum);

        for (int j = 0; j < nPtNum; j++)
            dYReturn[i * nPtNum + j] = y[j];
    }
    delete[] dFiltered;
    delete[] y;
}

/**
 * 数据平滑
 * \param nCurveNum： 曲线个数
 * \param nPtNum： 每条曲线点数
 * \param nWidth ：平滑窗宽度
 * \param *dY ：待处理数据
 * \param *dYReturn ：返回指针
 */
void
CPcrAnaAlgorithm::DataSmooth(int nCurveNum, int nPtNum, int nWidth, double *dY, double *dYReturn) {
    if (nPtNum < nWidth || nWidth <= 1) {
        for (int i = 0; i < nCurveNum; i++)
            for (int j = 0; j < nPtNum; j++)
                dYReturn[i * nPtNum + j] = dY[j];
        return;
    }
    double *y = new double[nPtNum];
    double *dFiltered = new double[nPtNum];
    int nStar = 0, nEnd = nPtNum;
    if (nWidth % 2) {
        nStar = nWidth / 2;
        nEnd = nPtNum - nStar;
    } // if( nWith%2)
    else {
        nStar = (nWidth - 1) / 2;
        nEnd = nPtNum - nStar - 1;
    }

    for (int i = 0; i < nCurveNum; i++) {
        for (int j = 0; j < nPtNum; j++) {
            y[j] = dY[i * nPtNum + j];
        }
        for (int j = nStar; j < nEnd; j++) {
            double sum = 0;
            for (int k = -nStar; k < nWidth - nStar - 1; k++)
                sum += y[j + k];
            dYReturn[i * nPtNum + j] = sum / nWidth;
        } // for( j=nStar; j< nEnd; j++)
        for (int j = 0; j < nStar; j++)
            dYReturn[i * nPtNum + j] = y[j];
        for (int j = nEnd; j < nPtNum; j++)
            dYReturn[i * nPtNum + j] = y[j];

    }
    delete[] dFiltered;
    delete[] y;
}

/**
 * 自动搜索峰值
 * \param nCrestNum ：待寻找峰的个数
 * \param nPtNum
 * \param *dXValue
 * \param *dYValue
 * \param *dReturn
 */
void CPcrAnaAlgorithm::AutoFindCrest(int nCrestNum, int nPtNum, double *dXValue, double *dYValue,
                                     double *dReturn, double dThrePercent /* = 0.2 */) {
    CArray<int, int> arrayTemp;
    arrayTemp.RemoveAll();
    double temp[3] = {0};
    for (int i = 1; i < nPtNum - 2; i++) {
        for (int j = 0; j < 3; temp[j++] = 0);
        temp[0] = dYValue[i] - dYValue[i - 1];
        temp[1] = dYValue[i] - dYValue[i + 1];
        temp[2] = dYValue[i] - dYValue[i + 2];
        if (temp[0] > 0 && temp[1] > 0 && temp[2] > 0)
            arrayTemp.Add(i);
    }
    int nSize = (int) arrayTemp.GetSize();
    double *dTemp = new double[nSize];
    double *dOut = new double[nSize];
    for (int i = 0; i < nSize; i++)
        dTemp[i] = dYValue[arrayTemp.GetAt(i)];
    SortDouble(nSize, dTemp, dOut);
    //进一步处理，以最高峰值的20%(缺省)作为阈值线，高于阈值线的即为有效峰
    float fThre = 0;
    if (nSize >= 1) {
        fThre = dOut[nSize - 1] * dThrePercent;
    }

    for (int i = 0; i < nSize; i++) {
        if (dOut[i] < fThre)
            dOut[i] = -1;
    }
    int count = 0;
    for (int i = nSize - 1; i >= nSize - nCrestNum; i--) {
        bool bfind = false;
        for (int j = 0; j < nSize; j++) {
            if (dYValue[arrayTemp.GetAt(j)] == dOut[i]) {
                bfind = true;
                dReturn[count++] = dXValue[arrayTemp.GetAt(j)];
            }
        }
        if (!bfind) {
            dReturn[count++] = -1;
        }
    }

    if (nCrestNum > nSize) {
        for (int i = nSize; i < nCrestNum; i++)
            dReturn[i] = -1;
    }

    delete[] dTemp;
    delete[] dOut;
}

/**
 * 排序
 * \param nNum
 * \param *pInput
 * \param *pOutput
 */
void CPcrAnaAlgorithm::SortDouble(int nNum, double *pInput, double *pOutput) {
    int sType = 3;
    CPcrAnalysisSort<double> s(nNum);
    for (int i = 0; i < nNum; i++)
        pOutput[i] = pInput[i];
    s.SelectSortType(sType, pOutput);
}

/**
 * 调整基线
 * \param nNum
 * \param nAvgNum
 * \param *pInput
 * \param *pOutput
 */
void CPcrAnaAlgorithm::AdjustBaseLine(int nNum, int nAvgNum, double *pInput, double *pOutput) {
    if (nAvgNum >= nNum || nAvgNum <= 0) {
        for (int i = 0; i < nNum; i++)
            pOutput[i] = pInput[i];
        return;
    }
    SortDouble(nNum, pInput, pOutput);
    double sum = 0;
    for (int i = 0; i < nAvgNum; i++)
        sum += pOutput[i];
    sum /= nAvgNum;

    for (int i = 0; i < nNum; i++)
        pOutput[i] = pInput[i] - sum;

}


/**
 * 归一化处理
 * \param nNum
 * \param nAvgNum
 * \param *pInput
 * \param *pOutput
 */
void CPcrAnaAlgorithm::NormalizedAnalysis(int nNum, double *pInput, double *pOutput) {
    if (nNum < 1)
        return;
    for (int i = 0; i < nNum; i++)
        pOutput[i] = pInput[i];

    SortDouble(nNum, pInput, pOutput);
    double dmin = pOutput[0];
    if (dmin == 0) {
        return;
    }
    for (int i = 0; i < nNum; i++)
        pOutput[i] = pInput[i] / dmin;
}

//新的计算二阶导数函数
void CPcrAnaAlgorithm::NewCalDerivative(double *x, double *y, int nNum, int nDev, double *dReturn) {
    int n = nNum;
    double *dy = new double[n];
    double *ddy = new double[n];

    dy[n - 1] = 0;
    for (int i = 0; i < n - 1; ++i) {
        dy[i] = y[i + 1] - y[i];
    }

    ddy[0] = 0;
    ddy[n - 1] = 0;
    for (int i = 1; i < n - 1; ++i) {
        ddy[i] = dy[i] - dy[i - 1];
    }

    if (nDev == 1) {
        for (int i = 0; i < n; i++)
            dReturn[i] = dy[i];
    } else if (nDev == 2) {
        for (int i = 0; i < n; i++)
            dReturn[i] = ddy[i];
    }

    //delete [] pReturn;

    delete[] dy;
    delete[] ddy;
}

/**
 * 判断数据点是否能够计算ct值
 * \param *dx
 * \param *dy
 * \param nNum
 * \return
 */
bool CPcrAnaAlgorithm::NewAbleToCalculate(double *dx, double *dy, int nNum,
                                          tagFunAbleToCalcuParamInfo paraminfo,
                                          int newStart /*= 1*/, int newForwardNum /*= 3*/,
                                          float newStdNum /*= 3*/, int minX /*= 5*/) {
//    LOGD("bkCalcDotNum:%d,bNormalization:%d,dAmpNoNormalizeRatio:%f,dAmpNoNormalizeStd:%f",
//         paraminfo.bkCalcDotNum, paraminfo.bNormalization, paraminfo.dAmpNoNormalizeRatio,
//         paraminfo.dAmpNoNormalizeStd);
//    LOGD("dAmpNormalizeRatio:%f,dAmpNormalizeStd:%f,dCrestBkRatio:%f,dFDNoNormalizeRatio:%f",
//         paraminfo.dAmpNormalizeRatio, paraminfo.dAmpNormalizeStd, paraminfo.dCrestBkRatio,
//         paraminfo.dFDNoNormalizeRatio);
//    LOGD("dFDNoNormalizeVal:%f,dMeltNoNormalizeRatio:%f,dFDNormalizeRatio:%f,dMeltNoNormalizeStd:%f",
//         paraminfo.dFDNoNormalizeVal, paraminfo.dMeltNoNormalizeRatio, paraminfo.dFDNormalizeRatio,
//         paraminfo.dMeltNoNormalizeStd);
//    LOGD("dFDNormalizeVal:%f,dMeltNormalizeRatio:%f,dMeltNormalizeStd:%f,nCurveType:%d",
//         paraminfo.dFDNormalizeVal, paraminfo.dMeltNormalizeRatio, paraminfo.dMeltNormalizeStd,
//         paraminfo.nCurveType);
//    LOGD("nNum:%d,newStart:%d,newForwardNum:%d,newStdNum:%f", nNum, newStart, newForwardNum,
//         newStdNum);

    if (nNum < 1)
        return false;
    double avg = 0, std = 0;
    double max = fabs(dy[0]);
    double *y = new double[nNum];
    for (int i = 0; i < nNum; i++) y[i] = dy[i];
    for (int i = 0; i < nNum; i++) {
        max = max > fabs(y[i]) ? max : fabs(y[i]);
//        LOGD("y[%d]:%lf,fabs(y[%d]):%lf",i,y[i],i,fabs(y[i]));
    }
    // 如果数据全为0，退出
    if (max == 0) {
        delete[] y;
        return false;
    }

    double *temp = new double[nNum];
    SortDouble(nNum, y, temp);

    double *dFiltered = new double[nNum];
    ::memset(dFiltered, 0, nNum);

    double dForeAvg = 0, dEndAvg = 0;
    int nForeID = 0, nEndID = 0;
    if (2 == paraminfo.nCurveType)//蛋白熔曲
    {
        nForeID = (int) (nNum / 5.0 + 0.5);
        nEndID = (int) (nNum * 4 / 5.0 + 0.5);
    } else {
        //0,扩增曲线；1，普通熔曲
        nForeID = (int) (nNum / 3.0 + 0.5);
        nEndID = (int) (nNum */*2/3.0*/ 8 / 9 + 0.5);
    }
    for (int i = 0; i < nForeID; i++) {
        if (/*0*/2 == paraminfo.nCurveType) {
            dForeAvg += /*y[i]*/temp[i];
        } else {
            dForeAvg += y[i];
        }
    }
    if (2 == paraminfo.nCurveType) {
        //蛋白熔曲，取排序后尾端的数据
        for (int i = nEndID; i < nNum; i++)
            dEndAvg += temp[i];
    } else {
        //尾端最多取5个点均值，实测数据效果相对更好
        if (nEndID >= 10) nEndID = nNum -/*5*/3;
        for (int i = nEndID; i < nNum; i++)
            dEndAvg += y[i];
    }

    dForeAvg = nForeID > 0 ? dForeAvg / (nForeID/*+1*/) : 0;
    dEndAvg = nEndID > 0 ? dEndAvg / (nNum - nEndID/*+1*/) : 0;

    //计算变异系数
    for (int i = 0; i < nNum; i++)
        avg += y[i];
    avg /= nNum;
    for (int i = 0; i < nNum; i++)
        std += (y[i] - avg) * (y[i] - avg);
    std = sqrt(std / (nNum - 1));
    double cv = 0;
    if (avg != 0)
        cv = std / avg;
    else
        cv = 0;

    bool bFindNormalD2Max = true;
    bool bGreaterThanMinX = true;


    double *dTemp = new double[nNum];
    memcpy(dTemp, y, nNum * sizeof(double));

    double *dReturn = new double[nNum];
    ::memset(dReturn, 0, nNum);

    double dCrestBkRatio = 0;

    {
        for (int itemp = 0; itemp < 4; itemp++) {
            DigitalFilter(dTemp, dFiltered, nNum);
            memcpy(dTemp, dFiltered, sizeof(double) * nNum);
        }

        //计算二阶导数
        NewCalDerivative(dx, dFiltered, nNum, 2, dReturn);
        //计算最大值
        double dmax = dReturn[0];
        double dmin = dReturn[0];

        double dXPosMax = 1;

        for (int t = 0; t < nNum; t++) {
            if (dReturn[t] < 0) dReturn[t] = 0;
        }
        FindCrestWithParabola(dx, dReturn, nNum, 6/*5*/, dXPosMax);

        int nxpos_max = (int) (dXPosMax + 0.5);
        dmax = dReturn[nxpos_max - 1];

        //判断二阶导最大值位置是否大于minX
        if (nxpos_max < minX) {
            bGreaterThanMinX = false;
        }

        int newCalcBaseNum = nxpos_max - newForwardNum - newStart + 1;

        if (newCalcBaseNum > 1) {
            double newAvg = 0, newStd = 0;
            //计算变异系数
            for (int i = 0; i < newCalcBaseNum; ++i)
                newAvg += dReturn[i];
            newAvg /= newCalcBaseNum;
            for (int i = 0; i < nxpos_max - 3; i++)
                newStd += (dReturn[i] - newAvg) * (dReturn[i] - newAvg);
            newStd = sqrt(newStd / (newCalcBaseNum - 1));

            if (dmax > (newAvg + newStdNum * newStd)) {
                bFindNormalD2Max = true;
            } else {
                bFindNormalD2Max = false;
            }
        }

        //计算二阶导最大值（从离散数据中寻找）和背景值（取滤波后数据前N个点）的比值
        double dBkSum = 0, dBkAvg = 0;
        for (int i = 0; i < paraminfo.bkCalcDotNum; ++i) {
            dBkSum += dy[i];
        }
        if (paraminfo.bkCalcDotNum > 0) {
            dBkAvg = dBkSum / 3;
        }

        if (0 != dBkAvg) {
            dCrestBkRatio = fabs(dmax / dBkAvg);
        }
//        LOGD("dBkAvg:%lf,dBkSum:%lf,dy[0]:%lf,dy[1]:%lf,dy[2]:%lf,dmax:%lf,dCrestBkRatio:%lf",dBkAvg, dBkSum, dy[0],dy[1],dy[2], dmax, dCrestBkRatio);
        delete[] dTemp;
        delete[] dFiltered;
        delete[] dReturn;
    }

    delete[] y;
    delete[] temp;
    bool tempbool = false;
    //主要用标准方差判断，阈值/阈值范围可根据本系统试验数据进行调整
    if (!paraminfo.bNormalization) {
//        LOGD("bFindNormalD2Max:%d,dCrestBkRatio:%lf,dCrestBkRatio1:%lf,std:%lf,dAmpNoNormalizeStd:%lf,dForeAvg * paraminfo.dAmpNoNormalizeRatio:%lf,dEndAvg:%lf",
//             bFindNormalD2Max, dCrestBkRatio, paraminfo.dCrestBkRatio, std,
//             paraminfo.dAmpNoNormalizeStd, dForeAvg * paraminfo.dAmpNoNormalizeRatio, dEndAvg);
        if (1 != paraminfo.nCurveType) {
            if (bGreaterThanMinX && bFindNormalD2Max && dCrestBkRatio > paraminfo.dCrestBkRatio &&
                std > paraminfo.dAmpNoNormalizeStd &&
                dForeAvg * paraminfo.dAmpNoNormalizeRatio < dEndAvg)
                tempbool = true;
            else
                tempbool = false;
        } else {
            //Tm值
            if (std > paraminfo.dMeltNoNormalizeStd && dForeAvg > dEndAvg *
                                                                  paraminfo.dMeltNoNormalizeRatio /*&& cv >= 0.04*/ )//&& dForeAvg*8 < dEndAvg)
                tempbool = true;
            else
                tempbool = false;
        }
    } else {
//        LOGD("bFindNormalD2Max:%d,dCrestBkRatio:%lf,dCrestBkRatio1:%lf,std:%lf,dAmpNormalizeStd:%lf,dForeAvg * paraminfo.dAmpNormalizeRatio:%lf,dEndAvg:%lf",
//             bFindNormalD2Max, dCrestBkRatio, paraminfo.dCrestBkRatio, std,
//             paraminfo.dAmpNormalizeStd, dForeAvg * paraminfo.dAmpNormalizeRatio, dEndAvg);
        if (1 != paraminfo.nCurveType) {
            if (bGreaterThanMinX && bFindNormalD2Max && dCrestBkRatio > paraminfo.dCrestBkRatio &&
                std > paraminfo.dAmpNormalizeStd &&
                dForeAvg * paraminfo.dAmpNormalizeRatio < dEndAvg)
                tempbool = true;
            else
                tempbool = false;
        } else {
            //Tm值
            if (std > /*0.1*/paraminfo.dMeltNormalizeStd && dForeAvg > dEndAvg *
                                                                       paraminfo.dMeltNormalizeRatio /*&& cv >= 0.04*/ )//&& dForeAvg*8 < dEndAvg)
                tempbool = true;
            else
                tempbool = false;
        }
    }

    {//新加 解决S型曲线 问题
        double *dFilteredTemp = new double[nNum];
        double *dReturnTemp = new double[nNum];
        double *dTempTemp = new double[nNum];
        for (int k = 0; k < nNum; k++)
            dTempTemp[k] = dy[k];

        //滤波
        for (int itemp = 0; itemp < 4; itemp++) {
            DigitalFilter(dTempTemp, dFilteredTemp, nNum);
            memcpy(dTempTemp, dFilteredTemp, sizeof(double) * nNum);
        }
        //计算二阶导数
        NewCalDerivative(dx, dFilteredTemp, nNum, 2, dReturnTemp);
        //计算最大值
        double dmaxTemp = dReturnTemp[0], dminTemp = dReturnTemp[0];
        for (int t = 0; t < nNum; t++) {
            if (dReturnTemp[t] < 0) dReturnTemp[t] = 0;
        }
        FindCrestWithParabola(dx, dReturnTemp, nNum, 6/*5*/, dmaxTemp);
//        if(dmaxTemp <= 3)
//        {
//            FindCrestWithParabola( dx + 3, dReturnTemp + 3, nNum - 3, 6/*5*/, dmaxTemp);
//            bAbandonFore3 = TRUE;
//        }
        int nxpos_maxTemp = (int) (dmaxTemp + 0.5);
        double dMaxY = dReturnTemp[nxpos_maxTemp - 1];

        if (dmaxTemp > 7 * nNum / 8.0) {
            return tempbool;
        } else {
            if (dMaxY < 5) {
                return false;
            } else {
                if(nNum > 5)
                {
                    if(dy[nNum - 1] > dy[0] && dy[nNum - 1] > dy[2] && dy[nNum - 2] > dy[0] && dy[nNum - 2] > dy[2])
                    {
                        return true;
                    } else {
                        return false;
                    }
                } else
                    return false;
            }
        }
        delete[] dFilteredTemp;
        delete[] dReturnTemp;
        delete[] dTempTemp;
    }
}

void CPcrAnaAlgorithm::NormalizedAnalysisBySndDerivativeOpenBaseLineParam(int nNum, double *pdx,
                                                                          double *pdyInput,
                                                                          double *pdyOutput,
                                                                          tagFunAmpNormalizedAnaParamInfo paraminfo,
                                                                          BASELINEPARAM baseLineParam,
                                                                          int curIndex /*= 1*/,
                                                                          double *pYDisplay /*= NULL*/) {
    if ((nNum > 3) && (nNum <= paraminfo.nAvgNum)) {
        paraminfo.nAvgNum = 3;
    }
    if (paraminfo.nAvgNum >= nNum || paraminfo.nAvgNum <= 0) {
        for (int i = 0; i < nNum; i++)
            pdyOutput[i] = pdyInput[i];
        return;
    }

    double backval = 0;
    double dbegin = 1, dend = 1;

    bool bAble = paraminfo.bAbleToCalcu;
    if (bAble) {
        int nxpos_max = 0, nxpos_min = 0;
        double dmax = 0, dmin = 0;
        //二阶导数最大值对应的位置点，向前移动5点作为起始点，向后取连续3点的均值作为背景值
        {
            double *dFiltered = new double[nNum];
            double *dReturn = new double[nNum];
            double *dTemp = new double[nNum];
            for (int k = 0; k < nNum; k++)
                dTemp[k] = pdyInput[k];

            for (int itemp = 0; itemp < paraminfo.nFiltNum; itemp++) {
                DigitalFilter(dTemp, dFiltered, nNum);
                memcpy(dTemp, dFiltered, sizeof(double) * nNum);
            }

            //计算二阶导数
            NewCalDerivative(pdx, dFiltered, nNum, 2, dReturn);
            //计算最大值
            dmax = dReturn[0];
            dmin = dReturn[0];

            for (int t = 0; t < nNum; t++) {
                if (dReturn[t] < 0) dReturn[t] = 0;
            }
            FindCrestWithParabola(pdx, dReturn, nNum, 6/*5*/, dmax);

            nxpos_max = (int) (dmax + 0.5);

            if (paraminfo.bBaseCorrection) {
                if (/*(nxpos_max > paraminfo.nBCXPosThre) && */(nxpos_max < nNum)) {
                    //归一前数据进行曲线矫正
                    int nTemp = nxpos_max - paraminfo.nBCEndPreXPos;
                    nTemp -= paraminfo.nBCStart;
                    if (nTemp >= 2) {
                        double dCoef[3] = {0};
                        double *dTemp = new double[nTemp];
                        memcpy(dTemp, pdyInput + (paraminfo.nBCStart - 1),
                               sizeof(double) * nTemp);
                        LinearFit(pdx, dTemp, nTemp, dCoef);

                        if ((dCoef[2] * dCoef[2] > paraminfo.dBCR2) &&
                            (dCoef[1] < paraminfo.dBCK) && (dCoef[1] > paraminfo.dBCK * -1)) {
                            for (int i = 0; i < nNum; i++) {
                                pdyInput[i] -= (pdx[i] * dCoef[1]);
                                dFiltered[i] -= (pdx[i] * dCoef[1]);
                                if (NULL != pYDisplay) {
                                    pYDisplay[i] -= pdx[i] * dCoef[1];
                                }
                            }

                        }
                        delete[] dTemp;
                    }

                    //重新计算二阶导最大值
                    NewCalDerivative(pdx, dFiltered, nNum, 2, dReturn);
                    //计算最大值
                    dmax = dReturn[0];
                    dmin = dReturn[0];

                    for (int t = 0; t < nNum; t++) {
                        if (dReturn[t] < 0) dReturn[t] = 0;
                    }
                    FindCrestWithParabola(pdx, dReturn, nNum, 6/*5*/, dmax);

                    nxpos_max = (int) (dmax + 0.5);
                }
            }

            delete[] dReturn;
            delete[] dFiltered;
            delete[] dTemp;
        }

        //提取有效数据源
        if (!baseLineParam.bSelfDefine) {
            dbegin = baseLineParam.defStart /*paraminfo.nAvgNum*/;
            dend = dmax - baseLineParam.defForwadDotNum /*paraminfo.nNormalizeStartPreXPos*/;
        } else {
            dbegin = baseLineParam.selfStart;
            dend = baseLineParam.selfEnd;
        }

        if ((dbegin < 1) || (dbegin > nNum))//数据点从1开始计数
        {
            dbegin = 1;
        }
        if ((dend < 1) || (dend > nNum))//数据点从1开始计数
        {
            dend = 1;
        }
        if (dbegin > dend) {
            dbegin = 1;
            dend = 1;
        }

        int nBegin = (int) dbegin;
        int nEnd = (int) dend;
        double sum = 0;
        for (int i = nBegin - 1; i < nEnd; i++) {
            sum += pdyInput[i];
        }
        backval = sum / (nEnd - nBegin + 1);
    } else {
        if (paraminfo.bBaseCorrection) {
            int nTemp = nNum - paraminfo.nBCEndPreNumPos;
            if (nTemp >= 2) {
                double dCoef[3] = {0};
                LinearFit(pdx, pdyInput, nTemp, dCoef);
                paraminfo.dBCR2 = 0;
                if (dCoef[2] * dCoef[2] > paraminfo.dBCR2) {
                    for (int i = 0; i < nNum; i++) {
                        pdyInput[i] -= (pdx[i] * dCoef[1]);
                        if (NULL != pYDisplay) {
                            pYDisplay[i] -= pdx[i] * dCoef[1];
                        }
                    }
                }
            }
        }

        if (!baseLineParam.bSelfDefine) {
            //除去 nAvgNum个最小值的均值
            SortDouble(nNum, pdyInput, pdyOutput);
            double sum = 0;

            int times = 3;
            if (nNum < 3) {
                times = nNum;
            }
            for (int i = 0; i < times; i++)
                sum += pdyOutput[i];
            backval = sum / times;
        } else {
            double sum = 0;
            if (baseLineParam.selfStart < 1) {
                baseLineParam.selfStart = 1;
            }
            if (baseLineParam.selfEnd > nNum) {
                baseLineParam.selfEnd = nNum;
            }
            if (baseLineParam.selfStart > baseLineParam.selfEnd) {
                baseLineParam.selfStart = 1;
                baseLineParam.selfEnd = 1;
            }
            for (int i = baseLineParam.selfStart - 1; i < baseLineParam.selfEnd; ++i) {
                sum += pdyInput[i];
            }
            backval = sum / (baseLineParam.selfEnd - baseLineParam.selfStart + 1);
        }
    }

    //减1操作,便于对数曲线表现
    if (paraminfo.bMinus1) {
        if (0 != backval) {
            for (int i = 0; i < nNum; i++) {
                pdyOutput[i] = pdyInput[i] / backval - 1;
                if (NULL != pYDisplay) {
                    pYDisplay[i] = pYDisplay[i] / backval - 1;
                }
            }
        }
    } else {
        if (0 != backval) {
            for (int i = 0; i < nNum; i++) {
                pdyOutput[i] = pdyInput[i] / backval;
                if (NULL != pYDisplay) {
                    pYDisplay[i] = pYDisplay[i] / backval;
                }
            }
        }
    }
    //背景信号压制
    if (paraminfo.bBaseSuppression) {
        if (!bAble) {
            if (paraminfo.bMinus1) {
                for (int i = 0; i < nNum; i++) {
                    pdyOutput[i] += (0 - pdyOutput[i]) * paraminfo.dBSRatioAllData;
                    if (NULL != pYDisplay) {
                        pYDisplay[i] += (0 - pYDisplay[i]) * paraminfo.dBSRatioAllData;
                    }
                }
            } else {
                for (int i = 0; i < nNum; i++) {
                    pdyOutput[i] += (1 - pdyOutput[i]) * paraminfo.dBSRatioAllData;
                    if (NULL != pYDisplay) {
                        pYDisplay[i] += (1 - pYDisplay[i]) * paraminfo.dBSRatioAllData;
                    }
                }
            }
        } else {
            if (paraminfo.bMinus1) {
                for (int i = 0; i < dbegin - 1; i++) {
                    pdyOutput[i] += (0 - pdyOutput[i]) * paraminfo.dBSRatioBaseLine;
                    if (NULL != pYDisplay) {
                        pYDisplay[i] += (0 - pYDisplay[i]) * paraminfo.dBSRatioBaseLine;
                    }
                }
            } else {
                for (int i = 0; i < dbegin - 1; i++) {
                    pdyOutput[i] += (1 - pdyOutput[i]) * paraminfo.dBSRatioBaseLine;
                    if (NULL != pYDisplay) {
                        pYDisplay[i] += (1 - pYDisplay[i]) * paraminfo.dBSRatioBaseLine;
                    }
                }
            }
        }
    }
}

//数字滤波，前M点后N点,共M + N + 1点,五点中心滤波尾端使用特殊不收缩算法
void CPcrAnaAlgorithm::KdsptForwardMBackN(int n, double *pY, double *pReturn, int forwardM,
                                          int backwardN) {
    //测试五点中心滤波尾端不收缩
    if (forwardM == backwardN && 2 == forwardM) {
        KdsptForwardMBackN_NoShrink(n, pY, pReturn, forwardM, backwardN);
        return;
    }

    if (NULL == pY || NULL == pReturn) {
        return;
    }

    if (forwardM < 0 || backwardN < 0) {
        memcpy(pReturn, pY, n * sizeof(double));
        return;
    }

    if (n < forwardM + backwardN + 1) {
        memcpy(pReturn, pY, n * sizeof(double));
        return;
    }

    int start = 0;
    int end = 0;
    int count = 0;
    double dSum = 0;
    for (int i = 0; i < n; ++i) {
        dSum = 0;
        count = 0;
        if (i < forwardM) {
            start = 0;
            end = i + backwardN;
        } else if (i >= forwardM && i < n - backwardN) {
            start = i - forwardM;
            end = i + backwardN;
        } else {
            start = i - forwardM;
            end = n - 1;
        }

        if (forwardM == backwardN) {
            int nPre = i - start;
            int nBehind = end - i;
            (nPre > nBehind) ? (start = i - nBehind) : (end = i + nPre);
        } else {
            //if(i < forwardM)
            //{
            //	int nPre = i - start ;
            //	int nBehind = end - i;
            //	(nPre > nBehind) ?(start = i - nBehind):(end = i + nPre);
            //}
            //else if(i > (n - 1 - backwardN))
            //{
            //	int nPre = i - start ;
            //	int nBehind = end - i;
            //	(nPre > nBehind) ?(start = i - nBehind):(end = i + nPre);
            //	//start = end = i;
            //}

            /*if(i == n - 1)
            {
            start = end = i;
            }*/
        }

        for (int k = start; k <= end; ++k) {
            dSum += pY[k];
            ++count;
        }

        if (forwardM != backwardN) {
            if (i > (n - 1 - backwardN)) {
                for (int k = i + backwardN; k > n - 1; --k) {
                    dSum += pY[n - 1];
                    ++count;
                }
            }
        }

        if (0 != count) {
            pReturn[i] = dSum / count;
        } else {
            pReturn[i] = pY[i];
        }
    }

    return;
}

//数字滤波，前M点后N点,共M + N + 1点,五点中心滤波尾端使用逐渐收缩算法
void CPcrAnaAlgorithm::KdsptForwardMBackN_New(int n, double *pY, double *pReturn, int forwardM,
                                              int backwardN) {
    if (NULL == pY || NULL == pReturn) {
        return;
    }
//    LOGD("forwardM:%d,backwardN:%d", forwardM, backwardN);
    if (forwardM < 0 || backwardN < 0) {
        memcpy(pReturn, pY, n * sizeof(double));
        return;
    }

    if (n < forwardM + backwardN + 1) {
        memcpy(pReturn, pY, n * sizeof(double));
        return;
    }

    int start = 0;
    int end = 0;
    int count = 0;
    double dSum = 0;
    for (int i = 0; i < n; ++i) {
        dSum = 0;
        count = 0;
        if (i < forwardM) {
            start = 0;
            end = i + backwardN;
        } else if (i >= forwardM && i < n - backwardN) {
            start = i - forwardM;
            end = i + backwardN;
        } else {
            start = i - forwardM;
            end = n - 1;
        }

        if (forwardM == backwardN) {
            int nPre = i - start;
            int nBehind = end - i;
            (nPre > nBehind) ? (start = i - nBehind) : (end = i + nPre);
        } else {
            //if(i < forwardM)
            //{
            //	int nPre = i - start ;
            //	int nBehind = end - i;
            //	(nPre > nBehind) ?(start = i - nBehind):(end = i + nPre);
            //}
            //else if(i > (n - 1 - backwardN))
            //{
            //	int nPre = i - start ;
            //	int nBehind = end - i;
            //	(nPre > nBehind) ?(start = i - nBehind):(end = i + nPre);
            //	//start = end = i;
            //}

            /*if(i == n - 1)
            {
            start = end = i;
            }*/
        }

        for (int k = start; k <= end; ++k) {
            dSum += pY[k];
            ++count;
        }

        //使用最后一个数据扩充数据队列用于滤波
        if (forwardM != backwardN) {
            if (i > (n - 1 - backwardN)) {
                for (int k = i + backwardN; k > n - 1; --k) {
                    dSum += pY[n - 1];
                    ++count;
                }
            }
        }

        if (0 != count) {
            pReturn[i] = dSum / count;
        } else {
            pReturn[i] = pY[i];
        }
    }

    return;
}

//数字滤波，前M点后N点,共M + N + 1点，首尾不收缩
void
CPcrAnaAlgorithm::KdsptForwardMBackN_NoShrink(int n, double *pY, double *pReturn, int forwardM,
                                              int backwardN) {
    if (NULL == pY || NULL == pReturn) {
        return;
    }

    if (forwardM < 0 || backwardN < 0) {
        memcpy(pReturn, pY, n * sizeof(double));
        return;
    }

    if (n < forwardM + backwardN + 1) {
        memcpy(pReturn, pY, n * sizeof(double));
        return;
    }

    int start = 0;
    int end = 0;
    int count = 0;
    double dSum = 0;
    for (int i = 0; i < n; ++i) {
        dSum = 0;
        count = 0;
        if (i < forwardM) {
            start = 0;
            end = i + backwardN;
        } else if (i >= forwardM && i < n - backwardN) {
            start = i - forwardM;
            end = i + backwardN;
        } else {
            start = i - forwardM;
            end = n - 1;

            if (2 == backwardN) {
                if (n - 2 == i) {
                    start = i - 1;
                    end = n - 1;
                } else if (n - 1 == i) {
                    start = i - 1;
                    end = n - 1;
                }
            }
        }

        for (int k = start; k <= end; ++k) {
            dSum += pY[k];
            ++count;
        }

        if (0 != count) {
            pReturn[i] = dSum / count;
        } else {
            pReturn[i] = pY[i];
        }
    }

    return;
}

void CPcrAnaAlgorithm::DeltaRnAnalysisBySndDerivativeOpenBaseLineParam(int nNum, double *pdx,
                                                                       double *pdyInput,
                                                                       double *pdyOutput,
                                                                       tagFunAmpNormalizedAnaParamInfo paraminfo,
                                                                       BASELINEPARAM baseLineParam,
                                                                       double *pYDisplay /*= NULL*/) {
    if ((nNum > 3) && (nNum <= paraminfo.nAvgNum)) {
        paraminfo.nAvgNum = 3;
    }
    if (paraminfo.nAvgNum >= nNum || paraminfo.nAvgNum <= 0) {
        for (int i = 0; i < nNum; i++)
            pdyOutput[i] = pdyInput[i];
        return;
    }

    double backval = 0;
    double dbegin = 1, dend = 1;
    int nbegin = 1;
    int nend = 1;
    bool bAble = paraminfo.bAbleToCalcu;

    if (bAble) {
        int nxpos_max = 0, nxpos_min = 0;
        double dmax = 0, dmin = 0;
        //二阶导数最大值对应的位置点，向前移动5点作为起始点，前后各取连续3点的均值作为背景值
        {
            double *dFiltered = new double[nNum];
            double *dReturn = new double[nNum];
            double *dTemp = new double[nNum];
            for (int k = 0; k < nNum; k++)
                dTemp[k] = pdyInput[k];

            for (int itemp = 0; itemp < 1; itemp++) {
                DigitalFilter(dTemp, dFiltered, nNum, FILTERTYPE_CFMEAN7);
                memcpy(dTemp, dFiltered, sizeof(double) * nNum);
            }
            for (int itemp = 0; itemp < paraminfo.nFiltNum; itemp++) {
                DigitalFilter(dTemp, dFiltered, nNum);
                memcpy(dTemp, dFiltered, sizeof(double) * nNum);
            }

            //计算二阶导数
            NewCalDerivative(pdx, dFiltered, nNum, 2, dReturn);
            //计算最大值
            dmax = dReturn[0];
            dmin = dReturn[0];
            for (int t = 0; t < nNum; t++) {
                if (dReturn[t] < 0) dReturn[t] = 0;
            }
            FindCrestWithParabola(pdx, dReturn, nNum, 6/*5*/, dmax);
            nxpos_max = (int) (dmax + 0.5);

            if (paraminfo.bBaseCorrection) {
                if ((nxpos_max > paraminfo.nBCXPosThre) && (nxpos_max < nNum)) {
                    //归一前数据进行基线矫正
                    int nTemp = nxpos_max - paraminfo.nBCEndPreXPos;
                    nTemp -= paraminfo.nBCStart;
                    if (nTemp >= 2) {
                        double dCoef[3] = {0};
                        double *dTemp = new double[nTemp];
                        memcpy(dTemp, pdyInput + (paraminfo.nBCStart - 1),
                               sizeof(double) * nTemp);
                        LinearFit(pdx, dTemp, nTemp, dCoef);
                        if ((dCoef[2] * dCoef[2] > paraminfo.dBCR2) &&
                            (dCoef[1] < paraminfo.dBCK) && (dCoef[1] > paraminfo.dBCK * -1)) {
                            for (int i = 0; i < nNum; i++) {
                                pdyInput[i] -= (pdx[i] * dCoef[1]);
                                dFiltered[i] -= (pdx[i] * dCoef[1]);

                                if (NULL != pYDisplay) {
                                    pYDisplay[i] -= (pdx[i] * dCoef[1]);
                                }

                            }
                        }
                        delete[] dTemp;
                    }

                    //重新计算二阶导最大值
                    NewCalDerivative(pdx, dFiltered, nNum, 2, dReturn);
                    //计算最大值
                    dmax = dReturn[0];
                    dmin = dReturn[0];
                    for (int t = 0; t < nNum; t++) {
                        if (dReturn[t] < 0) dReturn[t] = 0;
                    }
                    FindCrestWithParabola(pdx, dReturn, nNum, 6/*5*/, dmax);
                    nxpos_max = (int) (dmax + 0.5);
                }
            }

            delete[] dReturn;
            delete[] dFiltered;
            delete[] dTemp;
        }

        //提取有效数据源
        if (!baseLineParam.bSelfDefine) {
            nbegin = baseLineParam.defStart;
            nend = nxpos_max - baseLineParam.defForwadDotNum;
        } else {
            nbegin = baseLineParam.selfStart;
            nend = baseLineParam.selfEnd;
        }

        if ((nbegin < 1) || (nbegin > nNum))//数据点从1开始计数
        {
            nbegin = 1;
        }
        if (nend > nNum) {
            nend = nNum;
        }
        if (nbegin > nend) {
            nbegin = 1;
            nend = 1;
        }

        double sum = 0;
        for (int i = nbegin - 1; i < nend; i++)
            sum += pdyInput[i];
        backval = sum / (nend - nbegin + 1);
    } else {
        if (paraminfo.bBaseCorrection) {
            int nTemp = nNum - paraminfo.nBCEndPreNumPos;
            if (nTemp >= 2) {
                double dCoef[3] = {0};
                LinearFit(pdx, pdyInput, nTemp/*nNum*/, dCoef);
                paraminfo.dBCR2 = 0;
                if (dCoef[2] * dCoef[2] > paraminfo.dBCR2) {
                    for (int i = 0; i < nNum; i++) {
                        pdyInput[i] -= (pdx[i] * dCoef[1]);

                        if (NULL != pYDisplay) {
                            pYDisplay[i] -= (pdx[i] * dCoef[1]);
                        }
                    }
                }
            }
        }

        if (!baseLineParam.bSelfDefine) {
            //中位值
            backval = Median(pdyInput, nNum);
        } else {
            double sum = 0;
            if (baseLineParam.selfStart < 1) {
                baseLineParam.selfStart = 1;
            }
            if (baseLineParam.selfEnd > nNum) {
                baseLineParam.selfEnd = nNum;
            }
            if (baseLineParam.selfStart > baseLineParam.selfEnd) {
                baseLineParam.selfStart = 1;
                baseLineParam.selfEnd = 1;
            }
            for (int i = baseLineParam.selfStart - 1; i < baseLineParam.selfEnd; ++i) {
                sum += pdyInput[i];
            }
            backval = sum / (baseLineParam.selfEnd - baseLineParam.selfStart + 1);
        }
    }

    //计算△Rn
    for (int i = 0; i < nNum; i++) {
        pdyOutput[i] = pdyInput[i] - backval;
        if (NULL != pYDisplay) {
            pYDisplay[i] = pYDisplay[i] - backval;
        }
    }
}

double CPcrAnaAlgorithm::Median(double *x, int nNum) {
    double dVal = 0;
    if (nNum <= 0) return dVal;
    double *temp = new double[nNum];
    memset(temp, 0, sizeof(double) * nNum);
    SortDouble(nNum, x, temp);
    if (nNum % 2 == 0)//若为偶数，取中间的两个数做平均
    {
        dVal = ((temp[nNum / 2] + temp[nNum / 2 - 1])) / 2;
    } else {
        dVal = temp[nNum / 2];
    }
    delete[] temp;
    return dVal;
}

///**
// * 四参数拟合
// * \param *dx
// * \param *dy
// * \param nPtNum
// * \param nLeft
// * \param nRight
// * \param *dYReturn
// */
//void CPcrAnaAlgorithm::OnFourLogsFit_Aml(double *dx,double *dy, int nPtNum, int nLeft,int nRight,double *dYReturn)
//{
//	for (int k = 0; k < nPtNum ; k++)
//		dYReturn[k] = dy[k];
//	do
//	{
//		if( nPtNum <4 )
//			break;
//		int nxpos_max = 0, nxpos_min = 0;
//		//提取数据源，二阶导数最大值最小值对应的位置点，前后各移动若干数据点作为拟合数据源
//		{
//			double *dFiltered = new double [nPtNum];
//			double *dReturn = new double[nPtNum];
//			double *dTemp = new double [nPtNum];
//			for( int k=0; k< nPtNum; k++)
//				dTemp[k] = dy[k];
//			//滤波
//			for (int itemp = 0;itemp<5;itemp++)
//			{
//				DigitalFilter(dTemp,dFiltered,nPtNum,FILTERTYPE_CFMEAN5);
//				memcpy(dTemp,dFiltered,sizeof(double)*nPtNum);
//			}
//			for (int itemp = 0;itemp<5;itemp++)
//			{
//				DigitalFilter(dTemp,dFiltered,nPtNum);
//				memcpy(dTemp,dFiltered,sizeof(double)*nPtNum);
//			}
//			//计算二阶导数
//			CalDerivative(dx,dFiltered,nPtNum,2,dReturn);
//
//			//计算最大值
//
//			double dmax = dReturn[0];double dmin = dReturn[0];
//			for (int k = 0; k < nPtNum;k++)
//			{
//				if (dReturn[k] >= dmax)
//				{
//					dmax = dReturn[k];
//					nxpos_max = k+1;
//				}
//				else if (dmin > dReturn[k])
//				{
//					dmin =dReturn[k];
//					nxpos_min = k+1;
//				}
//			}
//
//			delete [] dReturn;
//			delete [] dFiltered;
//			delete [] dTemp;
//		}
//
//		//提取有效数据源
//		int nbegin = nxpos_max - nLeft;
//		if (nbegin < 1/*数据点从1开始计数*/)
//		{
//			nbegin = 1;
//		}
//		int nend = nxpos_min + nRight;
//		if (nend > nPtNum)
//		{
//			nend = nPtNum;
//		}
//		int nDataNum = nend - nbegin +1;
//		if (nDataNum < 4)
//		{
//			//数据不满足拟合条件或计算异常
//			break;
//		}
//		double* x = new double[nDataNum];
//		double* y = new double[nDataNum];
//		for (int k = nbegin;k <= nend;k++)
//		{
//			x[k-nbegin]= k;
//			y[k-nbegin]=dy[k-1];
//		}
//		double* pdCoef = new double[4];
//		memset(pdCoef, 0, sizeof(double)*4);
//		double dR = 0;
//		CLogisticFit fit;
//		dR = fit.LogisticFitSol(x, y, pdCoef, nDataNum,0);
//		if (dR <= 0)
//		{
//			//拟合失败
//		}
//		else
//		{
//			//拟合成功
//			for (int k = 0; k < nPtNum;k++)
//			{
//				dYReturn[k] = pdCoef[0] + (pdCoef[1] - pdCoef[0])/(1 + pow((dx[k]/pdCoef[2]), pdCoef[3]));
//			}
//		}
//
//		delete []x;
//		delete []y;
//		delete []pdCoef;
//
//	} while (0);
//}
//
///**
// * 四参数拟合
// * \param *dx
// * \param *dy
// * \param nPtNum
// * \param nLeft
// * \param nRight
// * \param *dYReturn
// */
//void CPcrAnaAlgorithm::OnFourLogsFit_Melt(double *dx,double *dy, int nPtNum, int nLeft,int nRight,double *dYReturn)
//{
//	for (int k = 0; k < nPtNum ; k++)
//		dYReturn[k] = dy[k];
//	do
//	{
//		if( nPtNum <4 )
//			break;
//		int nxpos_max = 0, nxpos_min = 0;
//		//提取数据源，二阶导数最大值最小值对应的位置点，前后各移动若干数据点作为拟合数据源
//		{
//			double *dFiltered = new double [nPtNum];
//			double *dReturn = new double[nPtNum];
//			double *dTemp = new double [nPtNum];
//			for( int k=0; k< nPtNum; k++)
//				dTemp[k] = dy[k];
//			//滤波
//			for (int itemp = 0;itemp<5;itemp++)
//			{
//				DigitalFilter(dTemp,dFiltered,nPtNum,FILTERTYPE_CFMEAN5);
//				memcpy(dTemp,dFiltered,sizeof(double)*nPtNum);
//			}
//			for (int itemp = 0;itemp<5;itemp++)
//			{
//				DigitalFilter(dTemp,dFiltered,nPtNum);
//				memcpy(dTemp,dFiltered,sizeof(double)*nPtNum);
//			}
//			//计算二阶导数
//			CalDerivative(dx,dFiltered,nPtNum,2,dReturn);
//
//			//计算最大值
//
//			double dmax = dReturn[0];double dmin = dReturn[0];
//			for (int k = 0; k < nPtNum;k++)
//			{
//				if (dReturn[k] >= dmax)
//				{
//					dmax = dReturn[k];
//					nxpos_max = k+1;
//				}
//				else if (dmin > dReturn[k])
//				{
//					dmin =dReturn[k];
//					nxpos_min = k+1;
//				}
//			}
//
//			delete [] dReturn;
//			delete [] dFiltered;
//			delete [] dTemp;
//		}
//
//		//提取有效数据源
//		int nbegin = nxpos_min - nLeft;
//		if (nbegin < 1/*数据点从1开始计数*/)
//		{
//			nbegin = 1;
//		}
//		int nend = nxpos_max + nRight;
//		if (nend > nPtNum)
//		{
//			nend = nPtNum;
//		}
//		int nDataNum = nend - nbegin +1;
//		if (nDataNum <= 0)
//		{
//			//数据不满足拟合条件或计算异常
//			break;
//		}
//		double* x = new double[nDataNum];
//		double* y = new double[nDataNum];
//		for (int k = nbegin;k <= nend;k++)
//		{
//			x[k-nbegin]= k;
//			y[k-nbegin]=dy[k-1];
//		}
//		double* pdCoef = new double[4];
//		memset(pdCoef, 0, sizeof(double)*4);
//		double dR = 0;
//		CLogisticFit fit;
//		dR = fit.LogisticFitSol(x, y, pdCoef, nDataNum,0);
//		if (dR <= 0)
//		{
//			//拟合失败
//		}
//		else
//		{
//			//拟合成功
//			for (int k = 0; k < nPtNum;k++)
//			{
//				dYReturn[k] = pdCoef[0] + (pdCoef[1] - pdCoef[0])/(1 + pow((dx[k]/pdCoef[2]), pdCoef[3]));
//			}
//		}
//
//		delete []x;
//		delete []y;
//		delete []pdCoef;
//
//	} while (0);
//}


void CPcrAnaAlgorithm::Reverse_array(double *x, int nNum, double *dReturn) {
    double *src = new double[nNum];
    memset(src, 0, sizeof(double) * nNum);
    memcpy(src, x, sizeof(double) * nNum);
    for (int i = 0; i < nNum; i++) {
        *(dReturn + i) = *(src + nNum - 1 - i);
    }
    delete[]src;
}

/***************************************************************************************************
*
*函数名:findPeak
*
*参数：
*
*vector<int> v     原始数据
*vector<int> peakPositions        峰值（凸峰或凹峰）对应的数据点位置值
*
*返回值:无
*
*功能：查找数据波形中的峰值位置
*
****************************************************************************************************/

void CPcrAnaAlgorithm::findPeak(const std::vector<int> &v, std::vector<int> &peakPositions) {
    std::vector<int> diff_v(v.size() - 1, 0);
    // 计算V的一阶差分和符号函数trend
    for (std::vector<int>::size_type i = 0; i != diff_v.size(); i++) {
        if (v[i + 1] - v[i] > 0)
            diff_v[i] = 1;
        else if (v[i + 1] - v[i] < 0)
            diff_v[i] = -1;
        else
            diff_v[i] = 0;
    }
    // 对Trend作了一个遍历
    for (int i = diff_v.size() - 1; i >= 0; i--) {
        if (diff_v[i] == 0 && i == diff_v.size() - 1) {
            diff_v[i] = 1;
        } else if (diff_v[i] == 0) {
            if (diff_v[i + 1] >= 0)
                diff_v[i] = 1;
            else
                diff_v[i] = -1;
        }
    }

    for (std::vector<int>::size_type i = 0; i != diff_v.size() - 1; i++) {
        if (diff_v[i + 1] - diff_v[i] == -2)
            peakPositions.push_back(i + 1);
    }
}