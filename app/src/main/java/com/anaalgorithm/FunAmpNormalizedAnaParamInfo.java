package com.anaalgorithm;

public class FunAmpNormalizedAnaParamInfo {

	public FunAmpNormalizedAnaParamInfo() {
		// TODO Auto-generated constructor stub
	}

	public FunAmpNormalizedAnaParamInfo(boolean bAbleToCalcu) {
		this.bAbleToCalcu = bAbleToCalcu;
	}

	public boolean bAbleToCalcu = false;
	public int nAvgNum = 3;        ///< 归一背景值，计算平均值的数据窗口（从数据源起点后移）[XPos-nNormalizeStartPreXPos，XPos-nNormalizeStartPreXPos+3）
	public int nNormalizeStartPreXPos = 5; ///< 计算归一背景值，数据源起点位置（相对起峰位置前移若干点） 
	public int nFiltNum = 4;
	
	public boolean bBaseCorrection = true; ///< 是否执行基线矫正
	public double dBCK =0xFFFF;
	public double  dBCR2 = 0.7d;     ///< 若执行基线矫正，数据源线性回归，判定系数大于0.5视为线性关系成立
	public int     nBCXPosThre = 10; ///< 若判定为起峰曲线，粗算起峰位置XPos,若XPos大于nBCXPosThre，则执行基线矫正
	public int     nBCStart = 1;     ///< 若判定为起峰曲线，基线段起点位置
	public int     nBCEndPreXPos = 3; ///< 若判定为起峰曲线，基线段终点位置 = XPos-nBCEndPreXPos（相对起峰位置前移若干点）
	public int     nBCEndPreNumPos = 0;
	public boolean bMinus1 = true;  ///< 归一化之后，是否执行减1操作（便于对数坐标系下曲线表现）
	
	public boolean bBaseSuppression = true; ///< 是否执行背景信号压制
	public double  dBSRatioAllData =0.5d;   ///< 若判定为未起峰曲线，则全体数据参与信号压制
	public double  dBSRatioBaseLine=0.5d;   ///< 若判定为起峰曲线，则基线段数据参与信号压制
}
