#pragma once
/**
 * \ingroup RtPCR
 *
 *
 * \par requirements
 * win98 or later\n
 * win2k or later\n
 * MFC\n
 *
 * \version 1.0
 * first version
 *
 * \date 2006-06-29
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
class CPcrAnaDataAlgorithm
{
public:
	CPcrAnaDataAlgorithm(void);
	~CPcrAnaDataAlgorithm(void);
/*-----------------基本函数----------------------------------------------*/
//样条插值求一阶微分――1
	void Alspl(double x0,double h,int n,double *y,int k,double t, double *s);
//样条插值求一阶微分――2
	void Amspl(double *x,double *y,int n,double *dy,double *ddy);
//最小二乘拟合
	static void Jbsqt(double *x, double *y, int n, double *a, double *dt);
//数字滤波器
	static void Kdspt(int n, double *y, double *yy);
	static void Kdspt7_3(int n, double *y, double *yy);
	static void Kdspt5_2(int n, double *y, double *yy);
	static void Kdspt7_2(int n, double *y, double *yy);
	static void Kdspt_dtmean(int n, int dtnum, double *y, double *yy);
	static void Kdspt_cfmean(int n, int cfnum, double *y, double *yy);//中心滑动滤波，dtnum 为奇数
	static void Kdspt_TriangularSmooths_29(int n, double *y, double *yy);//29点三角平滑
	static void Kdspt_GaussianSmooths_31(int n, double *y, double *yy);//31点伪高斯平滑
	static void Kdspt_Median(int n, int cfnum, double *y, double *yy); //中位数滤波
	static void SortDouble(int nNum, double *pInput, double *pOutput);
//样条插值
	void Akspl(double *x,double *y,int n,int k,double t,double *s,bool bSpline);
	void AksplEx(double *x,double *y,int n,int k,double t,double *s,bool bSpline);
//非方阵求逆，用于多项式拟合
	int Dginv(double *a,int m,int n,double *aa,double eps,double *u,double *v,int ka);
//Dluav, Sss, Ppp矩阵分解，用于求逆函数
	int  Dluav(double *a,int m,int n,double *u,double *v,double eps,int ka);
	void Sss(double *fg,double *cs);
	void Ppp(double *a,double *e,double *s,double *v,int m,int n);
//矩阵乘法
	void Damul(double *a,double *b,int m,int n,int k,double *c);

};
