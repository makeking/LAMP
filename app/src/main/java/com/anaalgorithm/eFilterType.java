package com.anaalgorithm;

public enum eFilterType {
	FILTERTYPE_CFMEAN3(0),            	///< 中点滑动平均3点
	FILTERTYPE_CFMEAN5(1),			    ///< 中点滑动平均5点	
	FILTERTYPE_CFMEAN7(2),			    ///< 中点滑动平均7点
	FILTERTYPE_MEDIAN(26),				///< 中值滤波
	FILTERTYPE_QuadraticSmooth_5(3),	///< 五点两次
	FILTERTYPE_CubicSmooth_5(4),		///< 五点三次
	FILTERTYPE_QuadraticSmooth_7(5),	///< 七点两次
	FILTERTYPE_CubicSmooth_7(6);		///< 七点三次
	
	private int value;
	private eFilterType(int value)
	{
		this.value = value;
	}//
	public void SetValue(int value) {
		this.value = value;
	}
	public int GetValue() {
		return this.value;
	}
}
