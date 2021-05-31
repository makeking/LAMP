#pragma once

// PcrDeclares.h

//// 系统公用的数据声明，包括结构、集合、常量（消息等）
//#define	  BIO_DEMO          // 演示程序
////#define     TUBE36            // 36管
////#define  TEST_LIQIN             // LIQIN添加的测试代码
//#define   BIO_NEWPCR_LQ			//LIQIN添加的新的PCR项目相关代码
//#define   BIO_NEWMELT           //新的熔曲控制方式
//#define   BIO_DOUBLEFAM         //熔曲双FAM扫描
//#define   BIO_EPROM             //从EPROM获取参数值
//#define   BIO_MELT_FREERUN      //广谱熔曲
////#define   BIO_CORRECTDATA       //人为修正原始数据以满足特性需求
////#define   BIO_USER              //用户程序
//#define   BIO_RATE                //Step支持升降温速率
//#define   BIO_POREPLATE_MODE      //样本设置窗口为孔板模式
//#define   BIO_POREPLATE           //孔板设置窗口，染料选择不再与实验文件类型关联
//#define   BIO_SELFCHECK           //仪器自检
////*********************************************************************************************
//// 系统公用的消息数据声明
//
//#define  WM_STATUS_TIP	    WM_USER + 201         // 系统状态栏提示信息
//
//const UINT WM_SETUP_MSG = WM_USER + 202; ///< 发送处理消息函数
//const UINT WM_SYSTEM_TIP = WM_USER + 202; ///< 系统提示信息，计划显示在状态栏上
//
////---------------------------------------------------------------------------//
//const UINT WM_REDRAW_TDATA = WM_USER + 500;   ///< 定义的温度数据刷新消息
//const UINT WM_REDRAW_FDATA = WM_USER + 501;   ///< 定义的温度数据刷新消息
//const UINT WM_DEVICE_ERROR = WM_USER + 502;   ///< 定义错误信息
//const UINT WM_FINISH_CMMD = WM_USER + 503;    ///< 定义实验完成
//
////---------------------------------------------------------------------------//
//const UINT WM_ADD_SEGMENT = WM_USER + 601;    ///< 添加Segment
//const UINT WM_INSERT_SEGMENT = WM_USER + 602; ///< 插入Segment
//const UINT WM_ADD_STEP = WM_USER + 603;       ///< 添加Step
//const UINT WM_INSERT_STEP = WM_USER + 604;    ///< 插入Step
//const UINT WM_DELETE_ITEM = WM_USER + 605;    ///< 删除Step
//const UINT WM_CHECK_NONE = WM_USER + 606;  ///< 不检测
//const UINT WM_CHECK_ENDPT = WM_USER + 607;  ///< 末点检测
//const UINT WM_CHECK_ALL = WM_USER + 608;  ///< 全部检测
//const UINT WM_SHOW_CURVE = WM_USER + 609; ///< 显示曲线
//const UINT WM_ADD_SEG = WM_USER +610; ///< Segment中增加循环数
//const UINT WM_EXP_ERROR = WM_USER+ 611; ///< 实验中通讯错误
//const UINT WM_SHOW_DRAW = WM_USER + 612; ///< 控制曲线显示的时间轴
//const UINT WM_SEG_RUN = WM_USER + 613; ///< 开始运行实验
//const UINT WM_SEG_STOP = WM_USER + 614; ///< 停止运行实验
//const UINT WM_SEG_MODI = WM_USER + 615; ///< 修改循环数
//const UINT WM_SEG_CONTINUE = WM_USER + 616; ///< 实验继续进行
//
//const UINT WM_FILE_OPEN = WM_USER + 617; ///< 读数据完成
//const UINT WM_FILE_SAVE = WM_USER + 618; ///< 保存数据之前
//
//const UINT WM_FILE_DYECHK = WM_USER + 619; ///< 设置染料检测点
//const UINT WM_COM_PROGRESS = WM_USER + 620; ///< 更新进度
//const UINT WM_COM_NEWTEMP = WM_USER + 621; ///< 收到新温度数据
//const UINT WM_COM_NEWFLU = WM_USER + 622; ///< 收到新荧光数据
//const UINT WM_COM_FINISH = WM_USER + 623; ///< 实验完成
//const UINT WM_COM_STATUS = WM_USER + 624; ///< 设置状态栏
//const UINT WM_COM_MODI = WM_USER + 625; ///< 修改成功应答
//
//const UINT WM_RESET_ANA_TUBES = WM_USER + 626; //< 重新设置分析试管
//const UINT WM_MOVE_LEFT = WM_USER + 627;  ///< 移动光标
//const UINT WM_MOVE_RIGHT = WM_USER + 628; ///< 右移动光标
//const UINT WM_RUN_RESET = WM_USER + 629; ///< 清除监控窗口数据
//const UINT WM_CLOSE_RULE = WM_USER + 630; ///关闭测量尺
//const UINT WM_SEG_INIT = WM_USER + 631;  ///< 初始数据
//const UINT WM_COM_WAIT = WM_USER + 632;  ///< 设备进入降温状态、
//const UINT WM_FOCUS_CURVE = WM_USER + 633;///< 变更焦点曲线
//const UINT WM_SEG_NEXT = WM_USER + 634; ///< 终止当前循环,执行下一个过程
//
//const UINT WM_COM_DEBUG = WM_USER + 635; ///< 设置调试信息
//const UINT WM_COM_TIPINFO = WM_USER + 636; ///< 更新实验运行状态提示信息
//const UINT WM_EXP_START_TIMING = WM_USER + 637; ///< 实验开始计时
//////////////////////  BioGraphWnd  //////////////////////////
//
////#define IDC_HAND            MAKEINTRESOURCE( 32649 )   ///< 图形界面手形鼠标
//
//////////////////////  PcrAnalysisCvCtDlg  //////////////////////////
//#define CONST_WND_WIDTH (1024)    ///< 基本窗宽
//#define CONST_WND_HEIGHT (768)   ///< 基本窗高
//#define CONST_TUBE_NUM ( 96)
//#define WM_CHANGE_RANGE ( WM_USER + 100)	///< 图形坐标轴范围改动
//#define WM_LEGEND_SHOW ( WM_USER +  101)	///< 显示图标
//#define WM_ZOOM         ( WM_USER + 102)	///< 缩放图形
//#define WM_RESET_CLMK    ( WM_USER+ 103)	///< 恢复颜色和清除标记
//#define WM_AXIS_LINEAR ( WM_USER + 104)		///< 线性/ 对数坐标轴
//#define	WM_GRID_SHOW   ( WM_USER + 105)     ///< 显示背景网格
//
//////////////////////  PcrAnaAmlWnd & PcrAnaMeltWnd  //////////////////////////
//
//#define WM_AML_LIST		(WM_USER + 213)		///< 更新列表内容
//#define WM_AML_UPDATE_COMBO		(WM_USER + 214)		///< 更新combo控件内容
//#define WM_AML_UPDATE_THRES		(WM_USER + 215)		///< 更新插值法水平标志线数值
//
//#define CONST_MELT_STAR (128)	///< 融解段消息起始值
//#define WM_CURVE_PROPERTY ( WM_USER + 210)	///< 弹出曲线属性对话框消息
//#define WM_MARKLINE_VALUE	( WM_USER+211)		///< 标志线数值
//#define WM_CTLDLG_SHOW		( WM_USER + 212)	///< 曲线控制对话框消息
//#define WM_INIT_UPDATE ( WM_USER + 216)		///< 界面初始化消息
//#define WM_ANA_UPDATE_CONTROL ( WM_USER + 217)	///<　更新控件状态
//
//const UINT WM_TUBE_SEL_CHANGE = WM_USER + 219; ///< 选择进行处理的试管改变（一般用在分析时）
//const UINT WM_DYE_SEL_CHANGE = WM_USER + 220; ///< 用户选择的染料改变
//#define WM_LAST_CYCLE ( WM_USER + 221)  ///< 改变最后循环数
//#define WM_TAB_CHANGE_UPDATE_LIST ( WM_USER + 222)
//#define WM_AML_UPDATE_ABSOLUTE  ( WM_USER + 223)  ///< 绝对定量模式，实验完毕或载入模板文件后自动计算标曲
//#define WM_MELT_UPDATE_ORG (WM_USER + 224) ///< 熔解曲线模式，实验完毕或载入模板文件后自动计算原始归一曲线
//
//////////////////////  PcrAnalysisView  //////////////////////////
//
//#define WM_AML_METHOD ( WM_USER + 301)	///< 扩增段计算ct值方法
//#define WM_AML_BASELINE ( WM_USER + 302 )	///< 扩增段基线调整
//#define WM_AML_ANALYSIS ( WM_USER + 303)	///< 扩增段分析按钮
//#define WM_AML_CYCLE_BEIGN ( WM_USER + 304)	///< 扩增段标准方差起始循环数
//#define WM_AML_CYCLE_END ( WM_USER + 305)	///< 扩增段标准方差终止循环数
//
//#define WM_AML_LOAD ( WM_USER + 306)	///< 导入数据
//#define WM_AML_LIST_SEL ( WM_USER + 307)	///< 列表选择
//#define WM_AML_SELALL ( WM_USER + 308)	///< 列表全选
//#define WM_AML_INDIVIDUAL ( WM_USER + 309)	///< 单独分析
//#define WM_AML_COLLECTIVE ( WM_USER + 310)	///< 分组发西
//#define WM_AML_UPDATE_TIPS ( WM_USER + 311)	///< 更新提示信息
//
//#define WM_AML_ALLE_TYPE ( WM_USER + 312)	///< 等位基因数据类型
//#define WM_AML_ALLE_RECAL ( WM_USER + 313)	///< 等位基因重新计算
//#define WM_AML_ALLE_XDYE ( WM_USER + 314)	///< 等位基因x轴染料
//#define WM_AML_ALLE_YDYE ( WM_USER + 315)	///< 等位基因y轴染料
//#define WM_AML_REL_NORM_DYE ( WM_USER + 316)	///< 相对定量归一化染料
//
//
////-----------------------------------------------------//
//
//#define WM_MELT_CLEAR_LIST ( WM_USER + 317)		///< 融解段清空列表中融解温度数据
//#define WM_MELT_SMOOTH ( WM_USER + 318)		///< 融解段平滑窗宽
//#define WM_MELT_CHECK_AUTO ( WM_USER + 319)	///< 融解段自动/手动分析
//#define WM_MELT_CREST_NUM ( WM_USER + 320)	///< 融解段自动分析峰值个数
//
//#define WM_MELT_CHECK_NORMALIZE (WM_USER + 329) ///< 溶解段归一化分析
//#define WM_MELT_CHECK_TEMPCOMPENSATION (WM_USER + 330)  ///< 溶解段96孔温度补偿分析
////---------------PcrAnalysisView 补充 2018.04.02-------------------------//
//#define WM_AML_FILTER  ( WM_USER + 321)    ///< 扩增段数据滤波调整
//#define WM_AML_FOURPARAMFIT (WM_USER + 322)  ///< 扩增段四参数拟合调整
//#define WM_AML_NORMALIZE (WM_USER + 323)   ///< 归一化调整
//#define WM_AML_CHANGEMARKLINE (WM_USER + 331) ///< 用户输入插值法阈值参数
//
////---------------PcrSampleWnd 补充 2018.05.28-------------------------//
//#define WM_SET_SELECTALL  ( WM_USER + 324)    ///< 样本设置页面全选
//#define WM_SET_CLEARWELLS (WM_USER + 325)  ///< 清空选中样本设置参数
//#define WM_SET_SELDYE (WM_USER + 326) ///< 选中样本设置染料参数
//#define WM_SET_SAMPLETYPE (WM_USER + 327) ///< 样本设置页面选择样本类型参数
//#define WM_SAMPLE_LBTNUP  (WM_USER + 328) ///<PcrSampleWnd/PcrPorePlateWnd 左键弹起
//
//#define WM_SET_DYECONCENTRATION  (WM_USER + 329) ///<选中样本设置浓度
//#define WM_SET_VIEWTYPE (WM_USER + 330)  ///<样本设置页面，显示参数类型（染料名称/浓度值）
//#define WM_SET_GROUPNO (WM_USER + 331)  ///<样本设置页面，设置选中样本重复测试分组序号
//#define WM_SET_SAMPLENAME (WM_USER + 332)  ///<样本设置页面，设置样本名称
//#define WM_SET_SELTARGET (WM_USER + 333)  ///<样本设置页面，设置Target名称
//
//
////*********************************************************************************************
//// 系统公用的其它常量声明
//#define TIME_RESOLUTION    (10)
//#define TEMP_RESOLUTION    (100.0)
////*********************************************************************************************
//// 系统公用的结构声明
//// 试管颜色表
//static const COLORREF sysTColorMap[] =
//        {
//                // mapping from color in DIB to system color
//                RGB(0x8B, 0x00, 0x00), //   _T("深红")
//                RGB(0xFF, 0x68, 0x20), //   _T("桔黄")
//                RGB(0x8B, 0x8B, 0x00), //   _T("深黄")
//                RGB(0x00, 0x93, 0x00), //   _T("绿色")
//                RGB(0x38, 0x8E, 0x8E), //   _T("青色")
//
//                RGB(0x00, 0x00, 0xFF), //   _T("蓝色")
//                RGB(0x7B, 0x7B, 0xC0), //   _T("蓝灰")
//                RGB(154, 115, 122), //   _T("粉红")  c
//                RGB(0xFF, 0xD7, 0x00), //   _T("金色")
//                RGB(161, 161, 0x00), //   _T("黄色")  c
//
//                RGB(0x00, 0xFF, 0x00), //   _T("鲜绿")
//                RGB(0x40, 0xE0, 0xD0), //   _T("青绿")
//                RGB(100, 133, 133), //   _T("天蓝")  c
//                RGB(0x48, 0x00, 0x48), //   _T("梅红")
//                RGB(0xFF, 0x00, 0x00), //   _T("红色")
//
//                RGB(0xFF, 0xAD, 0x5B), //   _T("浅桔色")
//                RGB(0x32, 0xCD, 0x32), //   _T("酸桔色")
//                RGB(0xC3, 0x4C, 0x8E), //   _T("海绿")  18
//                RGB(83, 167, 137), //   _T("宝石蓝")c
//                RGB(0x82, 0x61, 0x3F), //   _T("浅蓝")  20
//
//                RGB(0x80, 0x00, 0x80), //   _T("紫色")
//                RGB(0x00, 0x00, 0x8B), //   _T("深蓝")
//                RGB(0x4B, 0x00, 0x82), //   _T("靛蓝")
//                RGB(0xA5, 0x2A, 0x00), //   _T("褐色")
//                RGB(0x00, 0x40, 0x40), //   _T("橄榄绿")
//
//                RGB(0x00, 0x55, 0x00), //   _T("深绿")
//                RGB(0x00, 0x00, 0x5E), //   _T("深灰蓝")
//                RGB(131, 56, 77), //   _T("玫瑰红")cC
//                RGB(84, 84, 111), //   _T("棕色")  cC
//                RGB(91, 120, 88), //   _T("浅黄")  cC
//
//                RGB(61, 101, 61), //   _T("浅绿 ") c
//                RGB(0x50, 0x11, 0x11), //   _T("浅青绿")32
//                RGB(0x68, 0x83, 0x8B), //   _T("淡蓝")
//                RGB(163, 14, 128), //   _T("紫色")  cC
//                RGB(9, 39, 41), //   _T("黑色")  C
//
//                RGB(4, 79, 137),  //   _T("白色")  cC
//
//                RGB(0x8B, 0x00, 0x00), //   _T("深红")
//                RGB(0xFF, 0x68, 0x20), //   _T("桔黄")
//                RGB(0x8B, 0x8B, 0x00), //   _T("深黄")
//                RGB(0x00, 0x93, 0x00), //   _T("绿色")
//                RGB(0x38, 0x8E, 0x8E), //   _T("青色")
//
//                RGB(0x00, 0x00, 0xFF), //   _T("蓝色")
//                RGB(0x7B, 0x7B, 0xC0), //   _T("蓝灰")
//                RGB(154, 115, 122), //   _T("粉红")  c
//                RGB(0xFF, 0xD7, 0x00), //   _T("金色")
//                RGB(161, 161, 0x00), //   _T("黄色")  c
//
//                RGB(0x00, 0xFF, 0x00), //   _T("鲜绿")
//                RGB(0x40, 0xE0, 0xD0), //   _T("青绿")
//                RGB(100, 133, 133), //   _T("天蓝")  c
//                RGB(0x48, 0x00, 0x48), //   _T("梅红")
//                RGB(0xFF, 0x00, 0x00), //   _T("红色")
//
//                RGB(0xFF, 0xAD, 0x5B), //   _T("浅桔色")
//                RGB(0x32, 0xCD, 0x32), //   _T("酸桔色")
//                RGB(0xC3, 0x4C, 0x8E), //   _T("海绿")  18
//                RGB(83, 167, 137), //   _T("宝石蓝")c
//                RGB(0x82, 0x61, 0x3F), //   _T("浅蓝")  20
//
//                RGB(0x80, 0x00, 0x80), //   _T("紫色")
//                RGB(0x00, 0x00, 0x8B), //   _T("深蓝")
//                RGB(0x4B, 0x00, 0x82), //   _T("靛蓝")
//                RGB(0xA5, 0x2A, 0x00), //   _T("褐色")
//                RGB(0x00, 0x40, 0x40), //   _T("橄榄绿")
//
//                RGB(0x00, 0x55, 0x00), //   _T("深绿")
//                RGB(0x00, 0x00, 0x5E), //   _T("深灰蓝")
//                RGB(131, 56, 77), //   _T("玫瑰红")cC
//                RGB(84, 84, 111), //   _T("棕色")  cC
//                RGB(91, 120, 88), //   _T("浅黄")  cC
//
//                RGB(61, 101, 61), //   _T("浅绿 ") c
//                RGB(0x50, 0x11, 0x11), //   _T("浅青绿")32
//                RGB(0x68, 0x83, 0x8B), //   _T("淡蓝")
//                RGB(163, 14, 128), //   _T("紫色")  cC
//                RGB(9, 39, 41), //   _T("黑色")  C
//
//                RGB(4, 79, 137),  //   _T("白色")  cC
//
//                RGB(0x8B, 0x00, 0x00), //   _T("深红")
//                RGB(0xFF, 0x68, 0x20), //   _T("桔黄")
//                RGB(0x8B, 0x8B, 0x00), //   _T("深黄")
//                RGB(0x00, 0x93, 0x00), //   _T("绿色")
//                RGB(0x38, 0x8E, 0x8E), //   _T("青色")
//
//                RGB(0x00, 0x00, 0xFF), //   _T("蓝色")
//                RGB(0x7B, 0x7B, 0xC0), //   _T("蓝灰")
//                RGB(154, 115, 122), //   _T("粉红")  c
//                RGB(0xFF, 0xD7, 0x00), //   _T("金色")
//                RGB(161, 161, 0x00), //   _T("黄色")  c
//
//                RGB(0x00, 0xFF, 0x00), //   _T("鲜绿")
//                RGB(0x40, 0xE0, 0xD0), //   _T("青绿")
//                RGB(100, 133, 133), //   _T("天蓝")  c
//                RGB(0x48, 0x00, 0x48), //   _T("梅红")
//                RGB(0xFF, 0x00, 0x00), //   _T("红色")
//
//                RGB(0xFF, 0xAD, 0x5B), //   _T("浅桔色")
//                RGB(0x32, 0xCD, 0x32), //   _T("酸桔色")
//                RGB(0xC3, 0x4C, 0x8E), //   _T("海绿")  18
//                RGB(83, 167, 137), //   _T("宝石蓝")c
//                RGB(0x82, 0x61, 0x3F), //   _T("浅蓝")  20
//
//                RGB(0x80, 0x00, 0x80), //   _T("紫色")
//                RGB(0x00, 0x00, 0x8B), //   _T("深蓝")
//                RGB(0x4B, 0x00, 0x82), //   _T("靛蓝")
//                RGB(0xA5, 0x2A, 0x00), //   _T("褐色")
//        };
//
//
////*********************************************************************************************
//// 系统公用的集合声明
//enum eMouseState
//{
//    MOUSE_ARROW,
//    MOUSE_EDIT,
//    MOUSE_SIZE
//};
//
////曲线点显示类型
//enum eCurveShType{
//    SH_LINE = 0,       /// 直线连接
//    SH_TRIANGLE = 1,   /// 三角形显示
//    SH_XMARK = 2,      /// x mark
//    SH_ELLIPSE = 3,    /// ellipse
//    SH_RECTANGLE = 4,  /// rectangle
//    SH_UDTRIANGLE = 5, /// upside down triangle
//    SH_DIAMOND = 6     /// diamond
//};
//
//enum eDataType
//{
//    DTYPE_CHAR = 0,          ///< 表示是字符串
//    DTYPE_NUM_I01 = 1,       ///< 表示是值只能取01
//    DTYPE_NUM_I23 = 2,       ///< 表示是值只能取23
//    DTYPE_NUM_I012 = 3,      ///< 表示值只能取012
//    DTYPE_NUM_I16 = 4,       ///< 数据小于16
//    DTYPE_NUM_I36 = 5,       ///< 数据小于64
//    DTYPE_NUM_I1K = 6,       ///< 数据小于1024
//    DTYPE_NUM_SINT = 7,      ///< 表示是 0 - 32768
//    DTYPE_NUM_INT = 8,       ///< 表示是整型 -32768 - 32767
//    DTYPE_NUM_UNLINT = 9,    ///< 数据需要乘0 - 65535
//    DTYPE_NUM_LONG = 10      ///< 数据取值范围-3200000 - 3200000
//};
//
//union tagValue
//{
//    TCHAR strCont[20];
//    int  nCont;
//    long lCont;
//    double fKb;
//} ;
//
////EEPROM 参数类型
//enum eParaType
//{
//    PARA_STR = 0, ///< 参数类型是字符串
//    PARA_NUMER = 1,  ///< 参数类型是数值
//};
//
//struct tagEEPRom
//{
//    BOOL      bIsWrite;
//    CString   strName;        ///<  名称
//    eParaType InfoOrParam;    ///< 参数类型
//    WORD      Address;        ///< 地址
//    eDataType propety;        ///< 属性
//    tagValue context;         ///< 内容
//};
//
////编辑类型
//enum eEditType{
//    EEDIT_ABNOR = -1,///< 未知
//    ESTEP_AIMT = 1,   ///< 目标温度
//    ESTEP_DELAY = 2,  ///< 延迟时间
//    ESTEP_CKTP = 3,   ///< step检测
//    ESEG_CYCLE = 4,   ///< seg循环数
//    ESEG_NAME = 5,    ///< seg名称
//    ESEG_CKTP = 6,     ///< 检测方式
//    ESTEP_UPT = 7     ///< 梯度温度上限
//};
//
////Segment 的类型
//enum eSegType{
//    SEG_NONE = 0,
//    SEG_AMPLY = 1,
//    SEG_MELT = 2,
//    SEG_PWM = 3
//};
//
//enum eRunSaveState
//{
//    RUNSAVE_NEW = 0,
//    RUNSAVE_RUN = 1,
//    RUNSAVE_OVER = 2
//};
//
////Step 检测类型
//enum eCheck{
//    CHECK_NONE = 0,
//    CHECK_EPOINT = 1,
//    CHECK_ALL = 2,
//    CHECK_PWM = 3
//};
//
//enum eShowType
//{
//    SHOW_INTUBE = 0,    ///< 显示管内
//    SHOW_DOUBLE = 1,    ///< 显示2路
//    SHOW_OUTTUBE = 2    ///< 显示管外
//};
//
////系统状态
//enum eSystemState
//{
//    SYSTEM_FORBID = 0,  ///< 系统禁止运行
//    SYSTEM_ALLOWRUN = 1,///< 系统允许运行
//    SYSTEM_RUNNING = 2, ///< 系统正在运行
//    SYSTEM_WAITING = 3, ///< 系统等待停止
//};
//
////检测设备初始状态
//enum eCheckExperiment
//{
//    CHECK_DEV_OK = 0,   ///< 检测正常
//    CHECK_DEV_ERR = 1,  ///< 系统故障，无法完成实验.
//    CHECK_DEV_LED = 2,  ///< led没有打开,无法完成实验.
//    CHECK_DEV_TPSW = 3,	///< 温度保护开关未打开，无法完成实验
//    CHECK_DEV_DOOR = 4  ///< 仓门没有关闭,无法完成实验
//};
//
////设备状态
//enum eHardDevRunState
//{
//    HARD_DEV_OK = 0,               ///< 执行正确
//    HARD_DEVERR_INIT = 1,          ///< 初始化错误
//    HARD_DEVERR_EEPROM = 2,        ///< EEPROM 读写错误
//    HARD_DEVERR_COMDATA = 3,       ///< 通讯数据错误
//    HARD_DEVERR_DOOR = 4,          ///< 仓门错误
//    HARD_DEVERR_TPROTECTED = 5,    ///< 温度保护错误
//    HARD_DEVERR_CHTUNEL = 6,       ///< 通道切换错误
//    HARD_DEVERR_TAD = 7,           ///< 温度采集错误
//    HARD_DEVERR_SETEXP = 8         ///< 试验设置错误
//};
//
//
//
//enum eHardDevInitState
//{
//    HARD_INIT_UNKNOWN = 0,  ///< 系统未初始化,状态不定
//    HARD_INIT_OVER = 1,     ///< 系统初始化完成
//    HARD_INIT_ERROR = 2,    ///< 系统初始化错误
//    HARD_INIT_TEMP = 3,     ///< 初始化温度错误
//    HARD_INIT_PMT = 4,      ///< 初始化PMT错误
//    HARD_INIT_FILTER = 5    ///< 初始化滤色片组件错误
//};
//
//enum eHardEEPromState
//{
//    HARD_EEPROM_OK = 0,     ///< 系统EEPROM 读写正常
//    HARD_EEPROM_RERROR = 1, ///< 系统EEPROM 读错误
//    HARD_EEPROM_WERROR = 2, ///< 系统EEPROM 写错误
//    HARD_EEPROM_DERROR = 3  ///< 系统EEPROM 数据错误
//};
//
//enum eHardDevCommState
//{
//    HARD_COMM_NORMAL = 0,   ///< 系统通讯正常
//    HARD_COMM_SDTEMP = 1,   ///< 发送温度失败
//    HARD_COMM_SDFLU = 2,    ///< 发送荧光失败
//    HARD_COMM_CHSTEP = 3,   ///< 发送步骤切换失败
//    HARD_COMM_FINSH = 4,    ///< 发送试验完成命令失败
//    HARD_COMM_CHECK = 5     ///< 校验和错误
//};
//
//enum eHardDoorState
//{
//    HARD_DOOR_UNKNOWN = 0,   ///< 仓门状态不定
//    HARD_DOOR_CLOSE = 1,     ///< 仓门关
//    HARD_DOOR_OPEN = 2       ///< 仓门开
//};
//
//enum eHardTProtected
//{
//    HARD_TEMP_UNKNOWN = 0,   ///< 温度保护状态不定
//    HARD_TEMP_CLOSE = 1,    ///< 状态正常
//    HARD_TEMP_OPEN = 2      ///< 过热保护
//};
//
//enum eHardLedSate
//{
//    HARD_LED_CLOSE = 0,      ///< LED 关
//    HARD_LEN_OPEN = 1        ///< LED 开
//};
//
//enum eHardLightSw
//{
//    HARD_LSW_UNKNOWN = 0,    ///< 零位,码盘开关状态不定
//    HARD_LSW_ZCOPEN = 1,     ///< 主轴转盘零位,码盘开关打开
//    HARD_LSW_ZCCLOSE = 2     ///< 主轴转盘零位,码盘开关关闭
//};
//
//enum eHardEmHoer
//{//激发光路
//    HARD_EMHSW_UNKNOWN = 0,  ///< 霍尔开关状态不定
//    HARD_EMHSW_OPEN = 1,     ///< 霍尔开关打开
//    HARD_EMHSW_CLOSE = 2     ///< 霍尔开关关闭
//};
//
//enum eHardExHoer
//{//发射光路
//    HARD_EXHSW_UNKNOWN = 0,  ///< 霍尔开关状态不定
//    HARD_EXHSW_OPEN = 1,     ///< 霍尔开关打开
//    HARD_EXHSW_CLOSE = 2     ///< 霍尔开关关闭
//};
//
//enum eHardFilterState
//{
//    HARD_FLT_NORMAL = 0,     ///< 滤色片组件正常
//    HARD_FLT_EMTIMEOUT = 1,  ///< 激发光路切换超时
//    HARD_FLT_EXTIMEOUT = 2,  ///< 发射光路切换超时
//    HARD_FLT_PMT = 3,        ///< 设置PMT增益错误
//    HARD_FLT_EMERROR = 4,    ///< 激发光路滤色片设置错误
//    HARD_FLT_EXERROR = 5     ///< 发射光路滤色片设置错误
//};
//
//enum eHardExFilter
//{
//    HARD_EXFILTER_UNKNOWN = 0, ///< 状态未知
//    HARD_EXFILTER_F1 = 1,      ///< 发射滤色片1
//    HARD_EXFILTER_F2 = 2,      ///< 发射滤色片2
//    HARD_EXFILTER_F3 = 3,      ///< 发射滤色片3
//    HARD_EXFILTER_F4 = 4,      ///< 发射滤色片4
//    HARD_EXFILTER_F5 = 5       ///< 发射滤色片5
//};
//
//enum eHardEmFilter
//{
//    HARD_EMFILTER_UNKNOWN = 0, ///< 状态未知
//    HARD_EMFILTER_F1 = 1,      ///< 激发滤色片1
//    HARD_EMFILTER_F2 = 2,      ///< 激发滤色片2
//    HARD_EMFILTER_F3 = 3,      ///< 激发滤色片3
//    HARD_EMFILTER_F4 = 4,      ///< 激发滤色片4
//    HARD_EMFILTER_F5 = 5       ///< 激发滤色片5
//};
//
//enum eHardADGatherT
//{
//    HARD_ADT_NORMAL = 0,    ///< 温度采集正常
//    HARD_ADT_T1ERR = 1,     ///< 温度通道1采集错误
//    HARD_ADT_T2ERR = 2,     ///< 温度通道2采集错误
//    HARD_ADT_T3ERR = 3,     ///< 温度通道3采集错误
//    HARD_ADT_T4ERR = 4,     ///< 温度通道4采集错误
//    HARD_ADT_TINERR = 5,    ///< 管内温度采集错误
//    HARD_ADT_TOUTERR = 6    ///< 管外温度采集错误
//};
//
//enum eHardExpSetting
//{
//    HARD_EXPSET_OK = 0,         ///< 实验设置正确
//    HARD_EXPSET_NOFILTER = 1,   ///< 无滤色片组合信息
//    HARD_EXPSET_CONFIGERR = 2,  ///< CONFIG设置超范围
//    HARD_EXPSET_CYCLEERR = 3,   ///< CYCLE设置超范围
//    HARD_EXPSET_MTRATEERR = 4,  ///< 溶解曲线升降温速度设置超范围
//    HARD_EXPSET_PMTVERR = 5,    ///< PMT控制电压序号超范围
//    HARD_EXPSET_TINADERR = 6,   ///< 管内温度采样采用第几路ADC错误
//    HARD_EXPSET_TOUTADERR = 7   ///< 管外温度采样采用第几路ADC错误
//};
//
//enum eHardMainMotor
//{
//    HARD_MMOTOR_UNKNOWN = 0, ///< 电机状态不定
//    HARD_MMOTOR_STOP = 1,    ///< 电机停转
//    HARD_MMOTOR_LOWSP = 2,   ///< 电机低速转
//    HARD_MMOTOR_HIGHSP = 3   ///< 电机高速转
//};
//
//enum eHardExperment
//{
//    HARD_NORMAL = 0,     ///< 实际实验
//    HARD_SIMULATE = 1    ///< 模拟实验
//};
//
//enum eHardUpData
//{
//    HARD_NOUP = 0,  ///< 无数据上传
//    HARD_UP = 1     ///< 有数据上传
//};
//
//enum eHardState
//{
//    HARD_HELLO_HASDATA = 2, ///< 存在有数据
//    HARD_HELLO_OK = 1,     ///< HELLO正常
//    HARD_HELLO_FAIL = 0,   ///< HELLO失败
//    HARD_COMOPEN_FAIL = -1, ///< 设备打开串口失败
//    HARD_COMSET_FAIL = -2,  ///< 设置串口失败
//    HARD_DEVINFO_FAIL = -3, ///< 读设备信息失败
//    HARD_DEVPARA_FAIL = -4, ///< 读设备参数失败
//    HARD_DEVTEST_FAIL = -5,  ///< 设备自检失败
//    HARD_DEVRSTATE_FAIL = -6 ///< 读设备状态失败
//};
//
//enum eHardSelfCheckState
//{
//    HARD_SELFCHECK_UNKNOWN = 0,///< 状态未知
//    HARD_SELFCHECK_OK = 1,     ///< 自检成功
//    HARD_SELFCHECK_ERROR = 2,   ///< 自检错误
//    HARD_SELFCHECK_ABOVEUPPERLIMIT = 3, ///< 实测数值高于缺省上限
//    HARD_SELFCHECK_BELOWLOWERLIMIT = 4  ///< 实测数值低于缺省下限
//};
//
//
///**
//* 实验类型
//*/
//enum eExperimentType
//{
//    TYPE_EXP_SYBR = 0,
//    TYPE_EXP_TAQMAN = 1,
//    //TYPE_EXP_MB, ///< 分子信标
//            TYPE_EXP_PREAD ///< 终点读板
//};
//
//// 分析类型
//const UINT	TYPE_ANA_MELT		= 0x0001; ///< 融解段分析
//const UINT	TYPE_ANA_RELATIVE	= 0x0002; ///< 相对定量分析
//const UINT	TYPE_ANA_ABSOLUTE	= 0x0004; ///< 绝对定量分析
//const UINT	TYPE_ANA_ALLELE		= 0x0008; ///< 等位基因分析
//
///**
//* 实验样本类型
//*/
//enum eSampleType
//{
//    SAMPLE_TYPE_UNKNOWN = 0,   ///< 未知
//    SAMPLE_TYPE_STANDARD = 1,  ///< 标准
//    SAMPLE_TYPE_BUFFER = 2,  ///< 缓冲液
//    SAMPLE_TYPE_CALIBRATOR = 3, //校正样本
//    SAMPLE_TYPE_NEGATIVE = 4,   //阴性对照
//    SAMPLE_TYPE_POSITIVE = 5   //阳性对照
//};
//
///**
//* 颜料通道相关信息
//*/
//struct tagDyeChannelInfo
//{
//    CString strDyeName; ///< 染料名称
//    UINT nExWaveLen;     ///< 激发光波长
//    UINT nEmWaveLen;     ///< 发射光波长
//    UINT nExChannel;     ///< 激发通道
//    UINT nEmChannel;     ///< 发射通道
//    eExperimentType	nExpType; ///< 实验类型
//};
//typedef CArray<tagDyeChannelInfo, tagDyeChannelInfo>	CDyeArray;
//
///**
//* 颜料浓度相关信息（标准品用）
//*/
//struct tagDyeQuantityInfo
//{
//    TCHAR strDyeName[256];///< 染料名称
//    //CString strDyeName; ///< 染料名称
//    double  dQuantity;  ///< 标准品包含的指定染料标记的待测物的拷贝数
//};
//typedef CArray<tagDyeQuantityInfo, tagDyeQuantityInfo>	CQuantityArray;
//
//struct tagFilterInfo
//{
//    UINT nExPosition; ///< 激发光位置
//    UINT nExWaveLen; ///< 激发光中心波长
//    UINT nExRange; ///< 波长有效范围
//    UINT nEmPosition; ///< 激发光位置
//    UINT nEmWaveLen; ///< 激发光中心波长
//    UINT nEmRange; ///< 波长有效范围
//};
//
//typedef CArray<tagFilterInfo, tagFilterInfo>	CFilterArray;
///** 样品设置标题栏用户界面参数
//*/
//struct tagExpSampleTitleUI
//{
//    eExperimentType	nExpType; ///< 实验类型
//    UINT			nColCount; ///< 栏数
//    CUIntArray		arrayColWidth; ///< 栏宽度
//    CStringArray	arrayName; ///< 栏名称
//};
//
//
//struct tagPointProperty
//{
//    int nDyeID;
//    int nGroupID;
//    int nTubeID;  //试管号
//    double dCopies;
//};
//
//typedef CArray<tagPointProperty,tagPointProperty>  CPointPropertyArray;
//
////Print info
//#define	 B5_W			182				//B5纸宽度mm
//#define	 B5_H			257				//B5纸高度mm
//#define  A4_W           210
//#define  A4_H           297
//#define	 B5_ONELINE		29				//B5纸第一页行数
//#define	 B5_OTHERLINE	30				//B5纸其它页行数
//
////打印结构
//typedef struct structPrintinfo
//{
//    int		nCountPage;			//一共页数
//    int		nCurPage;			//当前页码
//    int     nPageSize;        //页面大小
//    int     nLinePerPage;   //每页行数
//    int     nLineHeight;  //行高
//    int     nOrientation;  //方向：0 ver，　1 hor
//    BOOL	IsPrint;			//是否打印
//    BOOL    IsPrnCurPage;     //是否打印当前页
//    HWND	hWnd;				//窗口句柄
//}PRNINFO, *PPRNINFO;
//
///**
// *  回调函数
// */
//typedef void(*PRINTPREVIEW) (CDC &MemDC, PRNINFO PrnInfo,void* pVoid);
///**
//* 报告类型
//*/
//enum eReportType
//{
//    REPORT_SUMMARY_BASE = 0,   ///< 扩增段基本分析报告 （2页）
//    REPORT_SUMMARY_STD = 1,  ///< 扩增段绝对定量分析报告（2页）
//    REPORT_SUMMARY_REL = 2,  ///< 扩增段相对定量分析报告（2页）
//    REPORT_SUMMARY_ALLE = 3, ///< 扩增段等位基因分析报告（2页）
//    REPORT_SUMMARY_MELT = 4,  ///< 融解段分析报告（2页）
//    REPORT_SUMMARY_WND = 5  ///< 窗口报告（1页）
//};
////////////////////////////////////////////////////////////////////////////////////////////////////////
//enum eChartStyle
//{
//    CHART_STYLE_CURVE = 0,  // 折线
//    CHART_STYLE_HIST = 1, // 柱状图
//    CHART_STYLE_SPOT = 2,  // 散点图
//    CHART_STYLE_CSPOT = 3   // 折线散点
//};
//struct tagGraphInfo
//{
//    CString strTitle;  // 图名称
//    BOOL bLegend;  // 是否显示图标
//    BOOL bZoom;    // 是否支持缩放
//    BOOL bXGrid;   // 横向网格
//    BOOL bYGrid;   // 纵向网格
//    BOOL bXTimeGrid;  // 横向时间坐标
//    BOOL bXAllMark;  // 横轴显示所有坐标值
//    BOOL bSetPtProperty;  // 点属性
//    BOOL bSetCvProperty;  //曲线点属性
//    COLORREF cBg;  // 背景颜色
//    COLORREF cEdge;  // 边界填充颜色
//    COLORREF cGrid;  // 网格颜色
//    COLORREF cAxis;  // 坐标轴颜色
//    int nGridLineType;  // 网格线的类型
//};
//struct tagDefaultPara
//{
//    int nLeftSpan;
//    int nTopSpan;
//    int nRightSpan;
//    int nBottomSpan;
//};
////坐标轴属性
//struct tagAxisProperty
//{
//    CString strAxisName;
//    CString strUnitName;  // 单位名称
//    bool bLinear;	      // 线性坐标轴或对数坐标轴
//    double dMinValue;
//    double dMaxValue;
//    double dOriginalMin;
//    double dOriginalMax;
//    double dStepValue;
//    double dDataMin;
//    double dDataMax;
//    int nPixelMin;
//    int nPixelMax;
//    int nPixelStep;
//};
//
////曲线属性
//struct tagCurveProperty
//{
//    bool bShow;
//    COLORREF cColor;
//    int nPointStyle; // 点型
//    int nLineStyle;  // 曲线类型:0折线，1柱状图，2等位基因散点，3标准曲线散点
//    int nCurveStyle; // 线型
//    int nPtNumber;   // 点数
//    int nDyeID;	     // 染料类型
//    int nGroupID;
//    int nTubeID;     //试管号
//    int	nLineWidth;  ///< 线宽，只在折线时有用
//    TCHAR cLineName[30]; //曲线名称
//};
//
//struct tagPointValue
//{
//    int xPos;
//    int yPos;
//    int nCurveID;  //试管号
//};
//typedef CArray<tagPointValue,tagPointValue>  CPointArray;
//
//struct tagPtDoubleValue
//{
//    double dXValue;
//    double dYValue;
//    bool bLogPt;
//};
//typedef CArray<tagPtDoubleValue,tagPtDoubleValue>  CPtDoubleValue;
//
////标志线属性
//struct tagMarkLineProperty
//{
//    int nType ; // line style
//    COLORREF cColor;
//    bool bShow;
//    bool bMoving;
//    double dValue; // x-y position
//    double dLogValue;
//    int nPixPos;
//    int nLineWidth;
//};
//
//typedef CArray<int,int> CIntArray;
//struct tagDoubleArray
//{
//    double x;
//    double y;
//};
//
//typedef CArray<tagDoubleArray,tagDoubleArray>  CDoubleArray;
//typedef CArray<double,double> CDoubleAy;
//
//struct tagResultList
//{
//    int nType;
//    int nCol;   // 列数
//    int nRow;   // 行数
//    CStringArray arrayColWidth;  // 列宽数组
//    CStringArray arrayColName;   // 列名数组
//    CStringArray arrayContent;   // 按列存储内容
//};
//
////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//#define  SEG_MARK           0x5650                //数据段标识
//#define  TF_MARK            0x5651                //温度荧光标识
//#define  FL_MARK            0x5652                //荧光标识
//#define  TE_MARK            0x5653                //温度标识
//
//#define  MIN_DELAY          5                     ///< 最小时间延迟
//#define  MAX_DELAY          3600                  ///< 最大时间延迟
//#define  MAX_SEGNAMELEN     20                    ///< segment name 字符长度
//#define  MIN_CYCLE          1                     ///< 最小循环数
//#define  MAX_CYCLE          255                   ///< 最大循环数
//#define  PRE_ZEROT          4                    ///< 对应第一个温度值
//#define  PRE_ZTMAX          100                   ///< 对应最大温度
//#define  TEP_MAX            100                   ///< 对应显示刻度最大温度
//#define  BYTEHIGHT          256                   ///< 数据字节高8位
//#define  UPDN               10                    ///< 对应数值上限
//#define  ITEMWIDTH          100                   ///< 每项的宽度
//#define  RULE_DIS           5                     ///< 距离边界的宽度
//#define  MAX_DIST           40000                 ///< 表示是否是溢出
//#define  MAX_BASE           0x10000               ///< 荧光温度时间基值
//#define  HARD_MAX           MAX_BASE-1            ///< 硬件最大时间值
//#define  HARD_FLITER        4                     ///< 硬件滤色片通道
//#define  EEPROMWORD         16                    ///< EEPROM
//
//const  int  NO_MNTUBE = 1000; //模拟数据位置常量
//const  int  DEVBINFO_SIZE = 0x2FF;
//const  int  DEVBPARA_SIZE = 0x200;
//const  int  DEVSTATE_SIZE = 256;
//
//const  int  DEVPARAMS_SIZE = 0xA00;
//
//const int SCYCLE = 60;    ///< 循环次数
//const  int  ADHARD = 10;  ///< 采样AD分变率
//const  int  HARDSEC = 10; ///< 系统是0.1s
//const  int  DEV_CHEK_PT = 2; //检测试管的个数
//
//const  int  DEV_CHEK_LOC1 = 9; //
//const  int  DEV_CHEK_LOC2 = 27; //
//const  int  DEV_CHEK_FLU = 2; //荧光表定温度点个数
//const  int  INSTRUCT_DELAY = 1000; ///< 两条指令时间间隔
///*
//#ifdef TUBE36            // 36管
//const  int  DEV_CHEK_LOC1 = 9; //
//const  int  DEV_CHEK_LOC2 = 27; //
//const  int  DEV_CHEK_FLU = 2; //荧光表定温度点个数
//#else
//const  int  DEV_CHEK_LOC1 = 9; //
//const  int  DEV_CHEK_LOC2 = 27; //
//const  int  DEV_CHEK_LOC3 = 18; //
//const  int  DEV_CHEK_LOC4 = 37; //
//const  int  DEV_CHEK_FLU = 4;
//#endif
//*/
//
//const  char strRunningFile[] = "\\Run.sf~";
//const  char strRunTempFile[] = "\\Run.sfd";
//
//const  int  MAX_WRITE_TIME = 50;    //5秒钟保存数据
//const  int  MAX_REPEAT_TTIME = 30;  //温度数据最大重发时间间隔
//const  int  MAX_REPEAT_FTIME = 100; //温度数据最大重发时间间隔
//
//const  WORD FLU_GRID_GRADE1 = 5000;  //起始荧光值
//const  WORD FLU_GRID_GRADE2 = 7500;
//const  WORD FLU_GRID_GRADE3 = 10000; //起始荧光值
//const  WORD FLU_GRID_GRADE4 = 12500;
//const  WORD FLU_GRID_GRADE5 = 15000; //起始荧光值
//const  WORD FLU_GRID_GRADE6 = 17500;
//const  WORD FLU_GRID_GRADE7 = 20000; //起始荧光值
//const  WORD FLU_GRID_GRADE8 = 40000; //起始荧光值
//const  WORD FLU_GRID_GRADE9 = 60000; //起始荧光值
//const  WORD FLU_GRID_GRADE10 = 65535; //起始荧光值
//const  DWORD FLU_GRID_GRADE11 = 80000; //起始荧光值
//
//const int  UHOUR = 36000; ///< 1小时 = 3600秒 36000 .1秒
//const int  UMINUTE = 600; ///< 1分钟 = 60秒   600   .1秒
//
//const int STABLYNUM = 20; //稳定数组长度
//const DWORD ANORTIME = 10000;  //正常命令延迟
//const DWORD STOPTIME = 120000; //停止命令延迟
//const WORD  MAX_FLU_VALUE = 65535; //最大荧光数据
//
//struct tagSegmentStep{
//    float fAimT;       ///< 目标温度
//    int nLastTime;     ///< 持续时间
//    float fRate;       ///< 温度上升速率
//    int nYgCheck;      ///< 荧光检测,none,single,contine
//};
//
//struct tagSegmentStepEx{
//    float fAimT;       ///< 目标温度
//    float fUpT;        ///< 梯度温度上限
//    int   nSelGradient;///< 勾选梯度模式
//    int nLastTime;     ///< 持续时间
//    float fRate;       ///< 温度上升速率
//    int nYgCheck;      ///< 荧光检测,none,single,contine
//};
//
//struct tagSegmentItem{
//    TCHAR cSName[255];  ///< 子项名称
//    int nCycleNum;     ///< 循环数
//    int nSType;        ///< 步骤类型 none，标定型，融解型
//    int nSubNum;       ///< 所带目标子项数
//
//    HGLOBAL hMemSItem;
//    tagSegmentStep *pSItem;               //所带子项
//};
//
//struct tagSegmentItemEx{
//    TCHAR cSName[255];  ///< 子项名称
//    int nCycleNum;     ///< 循环数
//    int nSType;        ///< 步骤类型 none，标定型，融解型
//    int nSubNum;       ///< 所带目标子项数
//
//    HGLOBAL hMemSItem;
//    tagSegmentStepEx *pSItem;               //所带子项
//};
//
//struct tagSampleHead{
//    WORD wId;               ///< 数据文件标识
//    WORD wVersion;          ///< 数据文件版本
//    DWORD dwReserve;        ///< 保留数据(0x0102及以上数据文件版本 代表多道实时温度个数)
//
//    DWORD dwAnalyType;       ///< 模板类型
//    DWORD dwExpType;        ///< 实验类型
//    DWORD dwExpNum;         ///< 实验步骤数
//    DWORD dwTubeNum;        ///< 试管数据个数
//    DWORD dwTempNum;        ///< 温度数据个数
//    DWORD dwCheckPtNum;     ///< 检测点个数
//};
//
//struct tagTemperatureData
//{
//    DWORD dwTime;     ///< 时间序号
//    WORD  wTData;     ///< 温度数据
//};
//
//typedef CArray<tagTemperatureData, tagTemperatureData>	TemperatureDataArray;
//
//struct tagMultiTempData
//{
//    DWORD dwTime;     ///< 时间序号
//    WORD wTargetData;///< 目标温度值
//    WORD wT1Data;    ///< 第1路温度数据
//    WORD wT2Data;	 ///< 第2路温度数据
//    WORD wT3Data;	 ///< 第3路温度数据
//};
//
//typedef CArray<tagMultiTempData, tagMultiTempData>	MultiTemperatureDataArray;
//
//
//struct tagMultiTempDataEx
//{
//    DWORD dwTime;     ///< 时间序号
//    WORD wTargetData;///< 目标温度值
//    WORD wT1Data;    ///< 第1路温度数据
//    WORD wT2Data;	 ///< 第2路温度数据
//    WORD wT3Data;	 ///< 第3路温度数据
//
//    WORD wT1StartPoint; ///< 第1路温度整板扫描起点温度值
//    WORD wT1EndPoint;   ///< 第1路温度整板扫描终点温度值
//    WORD wT2StartPoint; ///< 第2路温度整板扫描起点温度值
//    WORD wT2EndPoint;   ///< 第2路温度整板扫描终点温度值
//    WORD wT3StartPoint; ///< 第3路温度整板扫描起点温度值
//    WORD wT3EndPoint;   ///< 第3路温度整板扫描终点温度值
//};
//typedef CArray<tagMultiTempDataEx, tagMultiTempDataEx>	MultiTemperatureDataExArray;
//
//struct tagDyChanel
//{
//    TCHAR strDyeName[256];///< 染料名称
//    int nExWaveLen;      ///< 激发光波长
//    int nEmWaveLen;      ///< 发射光波长
//    int	nExChannel;      ///< 激发通道
//    int nEmChannel;      ///< 发射通道
//};
//
//typedef CArray<tagDyChanel, tagDyChanel>	DyeChannelArray;
//
//struct tagTube{
//    BYTE nTubId;              ///< 试管编号
//    TCHAR chTubName[256];      ///< 名称
//    BYTE nSampleType;         ///< 类型
//    BYTE nGroupNo;            ///< 组号
//    double dwQuantity;        ///< 浓度
//    COLORREF rgbColor;        ///< 颜色
//    TCHAR chNote[256];         ///< 注释
//
//    WORD wDyeNum;             ///< 染料数
//    DWORD dwLength;           ///< 试管中数据长度
//
//    HGLOBAL hMemDyeCh;        ///< 染料数
//    tagDyChanel *pDyeCh;      ///< 染料指针
//};
//
//struct tagTubeEx{
//    BYTE nTubId;              ///< 试管编号
//    TCHAR chTubName[256];      ///< 名称
//    BYTE nSampleType;         ///< 类型
//    BYTE nGroupNo;            ///< 组号
//    double dwQuantity;        ///< 浓度
//    COLORREF rgbColor;        ///< 颜色
//    TCHAR chNote[256];         ///< 注释
//
//    WORD wDyeNum;             ///< 染料数
//    DWORD dwLength;           ///< 试管中数据长度
//
//    HGLOBAL hMemDyeCh;        ///< 染料数
//    tagDyChanel *pDyeCh;      ///< 染料指针
//
//    WORD wQuanNum;            ///< 浓度个数
//    HGLOBAL hMemDyeQuantity;        ///< 浓度个数
//    tagDyeQuantityInfo *pDyeQuantity;    ///< 浓度指针
//};
//
//struct tagControlTable{
//    BYTE nSegmentId;   ///< Segment 编号
//    BYTE nStepId;      ///< Step 编号
//    WORD wAimC;        ///< 目标温度
//    DWORD dwUpDn;      ///< 升降温时间
//    DWORD dwDelay;     ///< 延迟时间
//    BYTE nRate;        ///< 上升速率，0：不考虑 1：融解段（0.1/s）
//    BYTE nCheck;       ///< 检测方式
//    WORD wCycle;       ///< 循环次数
//};
//
//typedef CArray <tagControlTable,tagControlTable>	ControlTableArray;
//
//struct tagFluData
//{
//    UINT nSegment;
//    UINT nStep;
//    UINT nChannel;
//    UINT nTubeNo;
//    UINT nCircle;
//    DWORD dwTime; ///< 采集荧光时间
//    WORD wFluValue; ///< 荧光值
//};
//
//typedef CArray <tagFluData*,tagFluData*> PFluDataArray;
//
//struct tagCheckPointData
//{
//    eSegType nSegKind;   //segment 类型
//    UINT nCheckPoint;
//    PFluDataArray *pArray; ///< 数据指针
//};
//
//struct tagSaveChPtFluData
//{
//    UINT nSegKind;
//    UINT nCheckPoint;
//    UINT nPtSize;
//
//    HGLOBAL hMemSubData;        ///< 染料数
//    tagFluData *pSubData;      ///< 染料指针
//};
//
//struct tagCtAnalysis
//{
//    CString strTub;
//    double dCt;
//    double dCtAverage;
//};
///**
//* 设备硬件状态码定义
//*/
//struct tagStatusCode
//{
//    BYTE nDevRun;          ///< 系统状态+错误号	1
//
//    BYTE nXMotState;       ///< X轴初始化状态+工作状态	1
//    WORD wXMotPos;		   ///<	X轴当前位置	2
//    BYTE nXMotDirection;   ///< X轴当前运行方向	1
//
//    BYTE nYMotState;	   ///< Y轴初始化状态+工作状态	1
//    WORD wYMotPos;		   ///< Y轴当前位置	2
//    BYTE nYMotDirection;   ///< Y轴当前运行方向	1
//
//    BYTE nZMotState;			///< Z轴初始化状态+工作状态	1
//    WORD wZMotPos;				///< Z轴当前位置	2
//    BYTE nZMotDirection;		///< Z轴当前运行方向	1
//
//    BYTE nUMotState;			///< U轴进出仓初始化状态+工作状态	1
//    WORD wUMotPos;				///< U轴进出仓当前位置	2
//    BYTE nUMotDirection;		///< U轴进出仓当前运行方向	1
//
//    BYTE nMotSensor;			///< 光电开关电源状态
//    ///< X轴光电开关状态	1
//    ///< Y轴光电开关状态
//    ///< Z轴光电开关状态
//    ///< U轴光电开关状态
//
//    BYTE nLED;					///< 光源FAM1状态	1
//    ///< 光源CY5状态
//    ///< 光源VIC状态
//    ///< 光源FAM4状态
//    ///< 光源ROX状态
//    ///< 光源CY3状态
//    BYTE nPmtAnalogState;		///< PMT模拟电路状态	1
//    BYTE nEeprom;				///< EEPROM读写状态	1
//    BYTE nSMBUart;				///< SMB-UART状态	1
//    WORD wPMT7512Out;			///< PMT当前增益	2
//    WORD wSMBBackground;		///< SMB电路背景噪声	2
//    BYTE nSysBox;				///< 系统进仓出仓状态	1
//    WORD wScannertTemp;				///< 扫描头当前温度	2
//    BYTE nSMBParameterInit;				///< SMB参数传递状态	1
//    BYTE nWKBParaInit;			///< WKB参数传递状态	1
//    BYTE nSysStateLed;			///< 状态灯状态	1
//    BYTE nCurtTempCtrlMode_TubeBlockMode;	///< 当前温控模式+ tube block加热模式	1
//    BYTE nPETUnit1State;		///< 帕尔贴1当前状态	1
//    WORD wPETUnit1Set;			///< 帕尔贴1当前设置	2
//    WORD wPETUnit1tTemp;		///< 帕尔贴1当前温度	2
//    BYTE nPETUnit2State;		///< 帕尔贴2当前状态	1
//    WORD wPETUnit2Set;			///< 帕尔贴2当前设置	2
//    WORD wPETUnit2tTemp;		///< 帕尔贴2当前温度	2
//    BYTE nPETUnit3State;		///< 帕尔贴3当前状态	1
//    WORD wPETUnit3Set;			///< 帕尔贴3当前设置	2
//    WORD wPETUnit3tTemp;		///< 帕尔贴3当前温度	2
//    BYTE nCapState;				///< 热盖当前状态	1
//    WORD wCapSet;				///< 热盖当前设置	2
//    WORD wCaptTemp;				///< 热盖当前温度	2
//    BYTE nAuxerState;			///< 辅助加热当前状态	1
//    WORD wAuxerSet;				///< 辅助加热当前设置	2
//    WORD wFanTemp;			///< 散热器当前温度	2
//    WORD wFanState;			///< 散热器风扇当前状态	2
//    WORD wFanSet;				///< 散热器风扇当前设置	2
//    WORD wSysFanSet;			///< 系统风扇当前设置	2
//    WORD wSysFantTemp;			///< 系统环境当前温度	2
//    BYTE nA4940Rst;				///< 功率板工作状态	1
//    WORD wWKBBackground;		///< WKB电路背景噪声	2
//    BYTE nAuxerHState;          ///< 辅助加热H当前状态
//    WORD wAuxerHSet;            ///< 辅助加热H当前设置
//    BYTE nTransportlockState;    ///< 运输锁状态
//    WORD wPET1PWMOut;           ///< 帕尔帖1PWM输出值
//    WORD wPET2PWMOut;           ///< 帕尔帖2PWM输出值
//    WORD wPET3PWMOut;           ///< 帕尔帖3PWM输出值
//};
//
///**
//* 设备硬件状态位定义
//*/
//struct tagStatusBit
//{
//    BYTE nXMot;					///<  	X轴状态，0-正常；1-错误；
//    BYTE nYMot;					///<  	Y轴状态，0-正常；1-错误；
//    BYTE nZMot;					///<    Z轴状态，0-正常；1-错误；
//    BYTE nUMot;					///<    U轴状态，0-正常；1-错误；
//    BYTE nMotSense;				///< 	光电开关，0-正常；1-错误；
//    BYTE nEprom;				///<	EEPROM状态，0-正常；1-错误；
//    BYTE nPMT;					///<	PMT状态，0-正常；1-错误；
//
//    BYTE nPETUnit1;				///<	帕尔贴1状态，0-正常；1-错误；
//    BYTE nPETUnit2;				///<	帕尔贴2状态，0-正常；1-错误；
//    BYTE nPETUnit3;				///<    帕尔贴3状态，0-正常；1-错误；
//    BYTE nCap;					///<	热盖状态，0-正常；1-错误；
//    BYTE nFanState;				///<	散热器风扇状态，0-正常；1-错误；
//    BYTE nAuxerState;			///<	辅助加热状态，0-正常；1-错误
//    BYTE nA4940Rst;				///<	功率板状态，0-正常；1-错误；
//    BYTE nSysBox;				///<	进出仓按钮状态，0-正常；1-有按下
//
//};
//
///**
//* 设备参数集
//*/
//#pragma pack(1)
//struct tagDeviceParams
//{
//    //当前读取总范围（0x000 - 0x9FF）,共2560个字节
//    //仪器硬件信息页（0x000 - 0x1FF）
//    char cReserved112[112];
//    char cPmtSN[16];
//    char cReserved384[384];
//    //仪器硬件参数页（0x200 - 0x5FF）
//    WORD wXOffSet;	///< 0x200
//    WORD wXMaxFreq;
//    WORD wXFreq0;
//    WORD wXScanFreq;
//    WORD wXHFreq;
//    WORD wXAccPls;
//    WORD wXMaxPls;
//    WORD wXScanPls;
//    WORD w0x210;
//    WORD wOx212;
//    WORD wYOffSet;	///< 0x214
//    WORD wYMaxFreq;
//    WORD wYFreq0;
//    WORD wYScanFreq;
//    WORD wYHFreq;
//    WORD wYAccPls;
//    WORD wYMaxPls;
//    WORD wYScanPls;
//    WORD w0x224;
//    WORD wOx226;
//    WORD wZOffSet;	///< 0x228
//    WORD wZMaxFreq;
//    WORD wZFreq0;
//    WORD wZScanFreq;
//    WORD wZHFreq;
//    WORD wZAccPls;
//    WORD wZMaxPls;
//    WORD wZDownPls;
//    WORD w0x238;
//    WORD wOx23a;
//    WORD wUOffSet;	///< 0x23c
//    WORD wUMaxFreq;
//    WORD wUFreq0;
//    WORD wUScanFreq;
//    WORD wUHFreq;
//    WORD wUAccPls;
//    WORD wUMaxPls;
//    WORD wUDdPls;
//    WORD wUOutBoxPls;
//    WORD w0x24e;
//
//    WORD wFam1K;	///< 0x250
//    WORD wCy5K;
//    WORD wVicK;
//    WORD wFam4K;
//    WORD wRoxK;
//    WORD wCy3K;
//    WORD wOddRowPeakUp;
//    WORD wEvenRowPeakUp;
//    WORD wPktSize;
//    WORD wPgRows;
//    WORD wSingleWellTimes;
//    WORD wSmpPls;
//    WORD wOffLedPls;
//    WORD wKantiSat;
//    WORD wLedDefault;
//    WORD wPmtCtrlVoltage; ///< 0x26e
//    WORD wPmtCorrectCoefAByTubeType; ///< 0x270
//    WORD wPmtCorrectCoefBByTubeType; ///< 0x272
//    char cReserved77[72];
//
//    double dT1P1;	///< 0x2bc
//    double dT1I1;
//    double dT1D1;
//    double dT1P2;
//    double dT1I2;
//    double dT1D2;
//    double dT1P3;
//    double dT1I3;
//    double dT1D3;
//    double T1K1;
//    double T1K2;
//    double T1B;
//
//    double dT2P1;
//    double dT2I1;
//    double dT2D1;
//    double dT2P2;
//    double dT2I2;
//    double dT2D2;
//    double dT2P3;
//    double dT2I3;
//    double dT2D3;
//    double T2K1;
//    double T2K2;
//    double T2B;
//
//    double dT3P1;
//    double dT3I1;
//    double dT3D1;
//    double dT3P2;
//    double dT3I2;
//    double dT3D2;
//    double dT3P3;
//    double dT3I3;
//    double dT3D3;
//    double T3K1;
//    double T3K2;
//    double T3B;
//
//    double dCapMaxPwm;
//    double dCapLPwm;
//    double dCapK1;
//    double dCapB;
//    double dSrqtK1;
//    double dSrqtK2;
//    double dSrqtKB;
//    double dSrqP;
//    double dSrqI;
//    double dSrqT1;
//    double dSrqT2;
//    double dSrqT3;
//    double dSrqMaxPwm;
//    double dSrqMinPwm;
//    double dAuxP;
//    double dAuxI;
//    double dAuxK;
//    double dAuxB;
//    double dAuxPwm;
//    double dFanMaxPwm;
//    double dFanMinPwm;
//    double dFanPwm;
//    double dPLT1POutK;
//    double dPLT2POutK;
//    double dPLT3POutK;
//    double dPLT1VTUp;
//    double dPLT1VTDown;
//    double dPLT2VTUp;
//    double dPLT2VTDown;
//    double dPLT3VTUp;
//    double dPLT3VTDown;	///< 0x4cc
//
//    char cReserved307[300];
//    //仪器硬件参数页（0x600 - 0x9FF）
//    float fA1TempCompensation;
//    float fA2TempCompensation;
//    float fA3TempCompensation;
//    float fA4TempCompensation;
//    float fA5TempCompensation;
//    float fA6TempCompensation;
//    float fA7TempCompensation;
//    float fA8TempCompensation;
//    float fA9TempCompensation;
//    float fA10TempCompensation;
//    float fA11TempCompensation;
//    float fA12TempCompensation;
//
//    float fB1TempCompensation;
//    float fB2TempCompensation;
//    float fB3TempCompensation;
//    float fB4TempCompensation;
//    float fB5TempCompensation;
//    float fB6TempCompensation;
//    float fB7TempCompensation;
//    float fB8TempCompensation;
//    float fB9TempCompensation;
//    float fB10TempCompensation;
//    float fB11TempCompensation;
//    float fB12TempCompensation;
//
//    float fC1TempCompensation;
//    float fC2TempCompensation;
//    float fC3TempCompensation;
//    float fC4TempCompensation;
//    float fC5TempCompensation;
//    float fC6TempCompensation;
//    float fC7TempCompensation;
//    float fC8TempCompensation;
//    float fC9TempCompensation;
//    float fC10TempCompensation;
//    float fC11TempCompensation;
//    float fC12TempCompensation;
//
//    float fD1TempCompensation;
//    float fD2TempCompensation;
//    float fD3TempCompensation;
//    float fD4TempCompensation;
//    float fD5TempCompensation;
//    float fD6TempCompensation;
//    float fD7TempCompensation;
//    float fD8TempCompensation;
//    float fD9TempCompensation;
//    float fD10TempCompensation;
//    float fD11TempCompensation;
//    float fD12TempCompensation;
//
//    float fE1TempCompensation;
//    float fE2TempCompensation;
//    float fE3TempCompensation;
//    float fE4TempCompensation;
//    float fE5TempCompensation;
//    float fE6TempCompensation;
//    float fE7TempCompensation;
//    float fE8TempCompensation;
//    float fE9TempCompensation;
//    float fE10TempCompensation;
//    float fE11TempCompensation;
//    float fE12TempCompensation;
//
//    float fF1TempCompensation;
//    float fF2TempCompensation;
//    float fF3TempCompensation;
//    float fF4TempCompensation;
//    float fF5TempCompensation;
//    float fF6TempCompensation;
//    float fF7TempCompensation;
//    float fF8TempCompensation;
//    float fF9TempCompensation;
//    float fF10TempCompensation;
//    float fF11TempCompensation;
//    float fF12TempCompensation;
//
//    float fG1TempCompensation;
//    float fG2TempCompensation;
//    float fG3TempCompensation;
//    float fG4TempCompensation;
//    float fG5TempCompensation;
//    float fG6TempCompensation;
//    float fG7TempCompensation;
//    float fG8TempCompensation;
//    float fG9TempCompensation;
//    float fG10TempCompensation;
//    float fG11TempCompensation;
//    float fG12TempCompensation;
//
//    float fH1TempCompensation;
//    float fH2TempCompensation;
//    float fH3TempCompensation;
//    float fH4TempCompensation;
//    float fH5TempCompensation;
//    float fH6TempCompensation;
//    float fH7TempCompensation;
//    float fH8TempCompensation;
//    float fH9TempCompensation;
//    float fH10TempCompensation;
//    float fH11TempCompensation;
//    float fH12TempCompensation;
//
//    float fPLT1MeltPwmK;
//    float fPLT1MeltPwmB;
//    float fPLT2MeltPwmK;
//    float fPLT2MeltPwmB;
//    float fPLT3MeltPwmK;
//    float fPLT3MeltPwmB;	///< 0x794
//
//    char cReserved107[104];
//    //(0x800--- 0x9FF)
//    double dTempGradientCoefA_K;
//    double dTempGradientCoefA_B;
//    double dTempGradientCoefB;
//    double dTempGradientCoefC;
//    double dTempGradientCoefD_K;
//    double dTempGradientCoefD_B; ///< 0x828
//
//    char cReserved32[32];
//
//    WORD wMppcVolGear_High;
//    WORD wMppcVolGear_Middle;
//    WORD wMppcVolGear_Low;
//
//    char cReserved263[426];	///< 0x9FF*/
//    ///< 0xA00 - 0x10 0000
//};
//#pragma pack()
//
//
///**
//* 状态灯闪烁模式
//*/
//enum eStateLEDWorkMode
//{
//    STALED_RED_ERROR = 0,      ///< 错误指示灯，有错误时红灯亮
//    STALED_GREEN_IDLE  = 1,    ///< 系统空闲时绿灯常亮
//    STALED_GREEN_BUSY  = 2,    ///< 系统忙时绿灯均速闪烁
//    STALED_GREEN_GETIN_GETOUT = 3, ///< 进仓、出仓时绿灯低频闪烁
//    STALED_GREEN_BTNDOWN = 4,      ///< 有效键按下时绿灯低频闪三下
//    STALED_YELLOW_PCR_BTNDOWN = 5  ///< 系统PCR过程中，有键按下时黄灯亮，用户确认后取消黄灯状态并修改系统工作状态
//};
//
///**
//* 进出仓状态
//*/
//
//enum eSysBoxState
//{
//    BOXSTATE_IN = 0,         ///< 进仓状态
//    BOXSTATE_OUT = 1,        ///< 出仓状态
//    BOXSTATE_OTHER = 2,      ///< 其他明确位置
//    BOXSTATE_UNKNOW = 3      ///< 未初始化，未知位置
//};
//
/**
* 滤波类型
*/

enum eFilterType
{
    FILTERTYPE_CFMEAN3 = 0,            ///< 中点滑动平均3点
    FILTERTYPE_CFMEAN5 = 1,			   ///< 中点滑动平均5点
    FILTERTYPE_CFMEAN7 = 2,			   ///< 中点滑动平均7点
    FILTERTYPE_CFMEAN9 = 14,
    FILTERTYPE_CFMEAN11 = 15,
    FILTERTYPE_CFMEAN13 = 16,
    FILTERTYPE_CFMEAN15 = 17,
    FILTERTYPE_CFMEAN17 = 18,
    FILTERTYPE_CFMEAN19 = 19,
    FILTERTYPE_CFMEAN21 = 20,
    FILTERTYPE_CFMEAN23 = 21,
    FILTERTYPE_CFMEAN25 = 22,
    FILTERTYPE_CFMEAN27 = 23,
    FILTERTYPE_CFMEAN29 = 24,
    FILTERTYPE_CFMEAN31 = 25,

    FILTERTYPE_MEDIAN = 26,

    FILTERTYPE_QuadraticSmooth_5 = 3,	///< 五点两次
    FILTERTYPE_CubicSmooth_5 = 4,		///< 五点三次
    FILTERTYPE_QuadraticSmooth_7 = 5,	///< 七点两次
    FILTERTYPE_CubicSmooth_7 = 6,		///< 七点三次

    FILTERTYPE_DTMEAN3 = 7,				///< 前置滑动平均3点
    FILTERTYPE_DTMEAN4 = 8,				///< 前置滑动平均4点
    FILTERTYPE_DTMEAN5 = 9,				///< 前置滑动平均5点
    FILTERTYPE_DTMEAN6 = 10,				///< 前置滑动平均6点
    FILTERTYPE_DTMEAN7 = 11,				///< 前置滑动平均7点

    FILTERTYPE_TriangularSmooths_29 = 12,///< 29点三角滤波
    FILTERTYPE_GaussianSmooths_31 = 13  ///< 31点伪高斯
};
//
///**
//* 设备子模块类型
//*/
//enum eModuleType
//{
//    MODULETYPE_MOVE = 1,   ///< 运动模块
//    MODULETYPE_SENSOR =2,  ///< 传感器模块
//    MODULETYPE_HEAT =3,    ///< 加热模块
//    MODULETYPE_OPTICS =4   ///< 光学模块
//};
//
///**
//* 运动模块子类型
//*/
//enum eMoveModuleType
//{
//    MODULETYPE_MOVE_MOTOR = 1,   ///< 电机类
//};
//
///**
//* 传感器模块子类型
//*/
//enum eSensorModuleType
//{
//    MODULETYPE_SENSOR_TEMRERATURE = 1,   ///< 温度传感器类
//};
//
///**
//* 加热模块子类型
//*/
//enum eHeatModuleType
//{
//    MODULETYPE_HEAT_PET = 1,   ///< 帕尔帖元器件
//};
//
///**
//* 光学模块子类型
//*/
//enum eOpticsModuleType
//{
//    MODULETYPE_OPTICS_LED = 1,   ///< LED元器件
//};
//
///**
//* MPPC模块控制电压档位
//*/
//enum eMPPCVoltageGear
//{
//    MPPC_VOLGEAR_HIGH = 0,   ///< 高档位
//    MPPC_VOLGEAR_MIDDLE = 1,   ///< 中间档位
//    MPPC_VOLGEAR_LOW = 2,   ///< 低档位
//};
//
//struct tagMotorSelfcheckParam
//{
//    int nEnable;
//    eHardSelfCheckState eState;
//    tagMotorSelfcheckParam()
//    {
//        nEnable = 0;
//        eState = HARD_SELFCHECK_UNKNOWN;
//    }
//};
//typedef CArray<tagMotorSelfcheckParam,tagMotorSelfcheckParam> MotorSelfcheckParamArray;
//
//struct tagTemperatureSensorSelfcheckParam
//{
//    int nEnable;
//    int nIndex;
//    float fReferenceL;
//    float fReferenceH;
//    eHardSelfCheckState eState;
//    tagTemperatureSensorSelfcheckParam()
//    {
//        nEnable = 0;
//        nIndex = 0;
//        fReferenceL = 0;
//        fReferenceH = 0;
//        eState = HARD_SELFCHECK_UNKNOWN;
//    }
//};
//typedef CArray<tagTemperatureSensorSelfcheckParam,tagTemperatureSensorSelfcheckParam> TemperatureSensorSelfcheckParamArray;
//
//struct tagPETSelfcheckParam
//{
//    int nEnable;
//    int nIndex;
//    float fTthreshold;
//    int   nReleaseWaitTime;
//    float fRiseDelta;
//    int   nRiseTime;     ///< ms
//    float fRiseChangeOfTheOtherTwoChannel;
//    float fDropDelta;
//    int   nDropTime;     ///< ms
//    float fDropChangeOfTheOtherTwoChannel;
//    eHardSelfCheckState eState;
//    tagPETSelfcheckParam()
//    {
//        nEnable = 0;
//        nIndex = 0;
//        fTthreshold = 0;
//        nReleaseWaitTime = 0;
//        fRiseDelta = 0;
//        nRiseTime = 0;
//        fRiseChangeOfTheOtherTwoChannel = 0;
//        fDropDelta = 0;
//        nDropTime = 0;
//        fDropChangeOfTheOtherTwoChannel = 0;
//        eState = HARD_SELFCHECK_UNKNOWN;
//    }
//};
//typedef CArray<tagPETSelfcheckParam,tagPETSelfcheckParam> PETSelfcheckParamArray;
//
//struct tagLEDSelfcheckParam
//{
//    int nEnable;
//    int nIndex;
//    int nGain;
//    int nSignalValueDelta;
//    eHardSelfCheckState eState;
//    tagLEDSelfcheckParam()
//    {
//        nEnable = 0;
//        nIndex = 1;
//        nGain = 0;
//        nSignalValueDelta = 0;
//        eState = HARD_SELFCHECK_UNKNOWN;
//    }
//};
//typedef CArray<tagLEDSelfcheckParam,tagLEDSelfcheckParam> LEDSelfcheckParamArray;
//
//struct tagSystemSelfCheckParam
//{
//    MotorSelfcheckParamArray SystemSelfcheck_Move_Motorary;
//    TemperatureSensorSelfcheckParamArray SystemSelfcheck_Sensor_Temperatureary;
//    PETSelfcheckParamArray  SystemSelfcheck_Heat_PETary;
//    LEDSelfcheckParamArray  SystemSelfcheck_Optics_LEDary;
//};
//
//
//
//struct tagMeltFlu
//{
//    double dxTemperature;
//    double dyFluValue;
//};
//typedef CArray<tagMeltFlu,tagMeltFlu> MeltFluArray;
//
//struct tagMeltDeltaTParam
//{
//    float fDeltaT;
//    UINT  nWaitTime;
//};
//typedef CArray<tagMeltDeltaTParam,tagMeltDeltaTParam> MeltDeltaTArray;
//
//struct tagTubeTypeParam
//{
//    CString strName;
//    int   nType;
//    int   nVol;
//    float  fCoefOfSignalValue;
//
//    tagTubeTypeParam()
//    {
//        strName = _T("");
//        nType = 0;
//        nVol = 100;
//        fCoefOfSignalValue = 1.00f;
//    }
//};
//typedef CArray<tagTubeTypeParam,tagTubeTypeParam> TubeTypeParamArray;
//
//
//
///**
//* 每个反应管分析后的荧光数据
//*/
//struct tagTubeAnalysisChannelFlu
//{
//    UINT nChannel;						///<  	光路通道 0x11
//    CIntArray arrayOrgFlu;				///<    原始数据
//    CIntArray arrayOrgFourParamsFlu;   ///< 原始四参数拟合数据
//
//    CIntArray arrayOrgFilterFlu;        ///<    原始滤波数据
//    CIntArray arrayOrgFilterFourParamsFlu;///< 滤波四参数拟合数据
//
//    CIntArray arrayOrgBackSubFlu;       ///<    原始减背景数据
//    CIntArray arrayOrgBackSubFourParamsFlu; ///< 原始减背景四参数拟合数据
//
//    CIntArray arrayFilterBackSubFlu;    ///<    滤波减背景数据
//    CIntArray arrayFilterBackSubFourParamsFlu; ///< 滤波减背景四参数拟合数据
//
//    CIntArray arrayMeltOrgFlu;				///<    熔解曲线原始数据
//    CIntArray arrayMeltOrgFourParamsFlu;   ///< 熔解曲线原始四参数拟合数据
//
//    CIntArray arrayMeltOrgFilterFlu;        ///<    熔解曲线原始滤波数据
//    CIntArray arrayMeltOrgFilterFourParamsFlu;///< 熔解曲线滤波四参数拟合数据
//
//    CIntArray arrayMeltOrgBackSubFlu;       ///<    熔解曲线原始减背景数据
//    CIntArray arrayMeltOrgBackSubFourParamsFlu; ///< 熔解曲线原始减背景四参数拟合数据
//
//    CIntArray arrayMeltFilterBackSubFlu;    ///<    熔解曲线滤波减背景数据
//    CIntArray arrayMeltFilterBackSubFourParamsFlu; ///< 熔解曲线滤波减背景四参数拟合数据
//
//    CDoubleAy arrayNormalizationFlu;       ///< 归一化数据
//    MeltFluArray arrayMeltNormalizationFlu;   ///< 熔解曲线归一化数据
//
//};
//
//typedef CArray <tagTubeAnalysisChannelFlu*,tagTubeAnalysisChannelFlu*> PTubeAnalysisDataArray;
//
//struct tagFilterParam
//{
//    eFilterType etype;
//    int         nNumber;
//};
//
//struct tagFourParamFitParam
//{
//    int nLeftNumber;
//    int nRightNumber;
//};
//
//enum eStepStatusMode
//{
//    STEPSTATUS_IDLE = 0,
//    STEPSTATUS_PREHEATING = 1,
//    STEPSTATUS_RUNNING = 2
//};
//
//struct tagGradientDistribution
//{
//    int nSeg;
//    int nStep;
//    float fRange;
//    float fColTemp[12];
//};
///**
//* 样本通用设置，标准品，设置染料浓度信息
//*/
//struct tagDyeQuantityCheckInfo
//{
//    CUIntArray CheckDyeChannelAry;
//    double dConcentration;
//};
//
//enum eSampleSubRowViewType
//{
//    VIEWTYPE_DYENAME = 0,
//    VIEWTYPE_CONCENTRATION = 1,
//    VIEWTYPE_TARGETNAME = 2,
//
//    VIEWTYPE_SAMPLENAME_HIDE = 3,
//    VIEWTYPE_SAMPLENAME_NOHIDE = 4
//};