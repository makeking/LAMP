package com.anaalgorithm;

public class FunAbleToCalcuParamInfo {

	public FunAbleToCalcuParamInfo() {
		// TODO Auto-generated constructor stub
	}

	public FunAbleToCalcuParamInfo(boolean bNormalization) {
		this.bNormalization = bNormalization;
	}

	public FunAbleToCalcuParamInfo(double dAmpNoNormalizeStd, double dAmpNoNormalizeRatio, double dAmpNormalizeStd, double dAmpNormalizeRatio, int bkCalcDotNum, double dCrestBkRatio) {
		this.dAmpNoNormalizeStd = dAmpNoNormalizeStd;
		this.dAmpNoNormalizeRatio = dAmpNoNormalizeRatio;
		this.dAmpNormalizeStd = dAmpNormalizeStd;
		this.dAmpNormalizeRatio = dAmpNormalizeRatio;
		this.bkCalcDotNum = bkCalcDotNum;
		this.dCrestBkRatio = dCrestBkRatio;
	}

	public FunAbleToCalcuParamInfo(boolean bNormalization, double dAmpNoNormalizeStd, double dAmpNoNormalizeRatio, double dAmpNormalizeStd, double dAmpNormalizeRatio) {
		this.bNormalization = bNormalization;
		this.dAmpNoNormalizeStd = dAmpNoNormalizeStd;
		this.dAmpNoNormalizeRatio = dAmpNoNormalizeRatio;
		this.dAmpNormalizeStd = dAmpNormalizeStd;
		this.dAmpNormalizeRatio = dAmpNormalizeRatio;
	}

	public boolean bNormalization = false;
	public int     nCurveType = 0; //0,扩增曲线；1，普通熔解曲线；2，蛋白熔曲
	
	public double  dAmpNoNormalizeStd = 50d;
	public double  dAmpNoNormalizeRatio = 1.1d;
	public double  dAmpNormalizeStd = 0.007d;
	public double  dAmpNormalizeRatio = 1.1d;

//	public double  dAmpNoNormalizeStd = 45d;
//	public double  dAmpNoNormalizeRatio = 1.1d;
//	public double  dAmpNormalizeStd = 0.007d;
//	public double  dAmpNormalizeRatio = 1.1d;

	public double dFDNoNormalizeVal = 10d;
	public double dFDNoNormalizeRatio = 1.1d;
	public double dFDNormalizeVal  = 0.01d;
	public double dFDNormalizeRatio = 1.1d;

//	public double dFDNoNormalizeVal = 40d;
//	public double dFDNoNormalizeRatio = 2d;
//	public double dFDNormalizeVal  = 0.01d;
//	public double dFDNormalizeRatio = 2d;

	public double  dMeltNoNormalizeStd = 45d;
	public double  dMeltNoNormalizeRatio = 1.1d;
	public double  dMeltNormalizeStd = 0.01d;
	public double  dMeltNormalizeRatio = 1.1d;

	public int bkCalcDotNum = 3;
	public double dCrestBkRatio = 0.0015d;
}
