package com.bete.lamp.barfragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.Toast;


import com.bete.lamp.R;
import com.bete.lamp.barfragment.decoding.InactivityTimer;
import com.bete.lamp.bean.Barcode;
import com.bete.lamp.bean.PCRProject;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.utils.LogUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.leon.lfilepickerlibrary.LFilePicker;


import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import static android.app.Activity.RESULT_OK;

public class BarCodeScanFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private ImageView top_openpicture;
    /**
     * 扫描结果监听
     */
    private OnRxScanerListener mScanerListener;

    private InactivityTimer inactivityTimer;

    /**
     * 扫描处理
     */
    private CaptureActivityHandler handler;

    private MultiFormatReader multiFormatReader;

    /**
     * 扫描边界的宽度
     */
    private int mCropWidth = 0;

    /**
     * 扫描边界的高度
     */
    private int mCropHeight = 0;

    /**
     * 是否有预览
     */
    private boolean hasSurface;

    /**
     * 扫描成功后是否震动
     */
    private boolean vibrate = true;

    /**
     * 闪光灯开启状态
     */
    private boolean mFlashing = true;

    /**
     * 扫描结果显示框
     */
    //private RxDialogSure rxDialogSure;

    /**
     * 设置扫描信息回调
     */
    public void setScanerListener(OnRxScanerListener scanerListener) {
        mScanerListener = scanerListener;
    }

    public int getCropWidth() {
        return mCropWidth;
    }

    public void setCropWidth(int cropWidth) {
        mCropWidth = cropWidth;
    }

    public int getCropHeight() {
        return mCropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.mCropHeight = cropHeight;
    }

    private int REQUESTCODE_FROM_FRAGMENT = 1001;
    public void btn(View view) {
        int viewId = view.getId();
        if (viewId == R.id.top_openpicture) {
//            RxPhotoTool.openLocalImage(getActivity());
            new LFilePicker()
                    .withSupportFragment(BarCodeScanFragment.this)
                    .withRequestCode(REQUESTCODE_FROM_FRAGMENT)
                    //.withStartPath("/storage/emulated/0/Download")
                    .withStartPath("/sdcard/LAMP/")
                    //.withFileFilter(new String[]{""})
                    .withIsGreater(false)
                    .withChooseMode(true)
                    .withMutilyMode(false)
                    .withFileSize(500 * 1024)
                    .start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_FRAGMENT) {
                //If it is a file selection mode, you need to get the path collection of all the files selected
                //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                List<String> list = data.getStringArrayListExtra("paths");
                if ((list.size() > 0) && (!list.get(0).isEmpty())) {
                    Bitmap photo = RxImageTool.getBitmap(list.get(0));
                    // 开始对图像资源解码
                    Result rawResult = RxQrBarTool.decodeFromPhoto(photo);
                    if (rawResult != null) {
                        if (mScanerListener == null) {
                            //initDialogResult(rawResult);
                            //LogUtils.d("图片识别成功:From to Picture", rawResult);
                        } else {
                            mScanerListener.onSuccess("From to Picture", rawResult);
                        }
                    } else {
                        if (mScanerListener == null) {
                            //RxToast.error("图片识别失败.");
                            //LogUtils.d("图片识别失败:From to Picture", rawResult);
                        } else {
                            mScanerListener.onFail("From to Picture", "图片识别失败");
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "未选中文件", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    private void light() {
        if (mFlashing) {
            mFlashing = false;
            // 开闪光灯
        } else {
            mFlashing = true;
            // 关闪光灯

        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d("onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d("onCreateView");
        view = inflater.inflate(R.layout.fragment_bar_code_scan, container, false);
        //界面控件初始化
        initDecode();
        initView();
        //权限初始化
        initPermission();
        //扫描动画初始化
        initScanerAnimation();
        //初始化 CameraManager
        hasSurface = false;
        inactivityTimer = new InactivityTimer(getActivity());
        setScanerListener(new OnRxScanerListener() {
            @Override
            public void onSuccess(String type, Result result) {
                PCRProject pcrProject = Barcode.GetReagentDetailBarCode(result.getText().getBytes());
                //PCRProject pcrProject = new PCRProject();
                if(pcrProject!=null)
                {
                    ((SheZhiActivity)getActivity()).setPcrProject(pcrProject);
                    CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
                    builder1.setMessage("解析正确！");
                    builder1.setTitle("扫描");
                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder1.create(R.layout.dialog_normal_feiquanping_message).show();
                }
                else
                {
                    CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
                    builder1.setMessage("解析错误，请重新扫描！");
                    builder1.setTitle("扫描");
                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder1.create(R.layout.dialog_normal_feiquanping_message).show();
                }
            }

            @Override
            public void onFail(String type, String message) {
                //LogUtils.d("图片识别失败:From to Picture");
                CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
                builder1.setMessage("图片识别失败，请重新扫描！");
                builder1.setTitle("扫描");
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.create(R.layout.dialog_normal_feiquanping_message).show();
            }
        });
        return view;
    }

    private void initDecode() {
        multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            Vector<BarcodeFormat> PRODUCT_FORMATS = new Vector<BarcodeFormat>(5);
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
            // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
            Vector<BarcodeFormat> ONE_D_FORMATS = new Vector<BarcodeFormat>(PRODUCT_FORMATS.size() + 4);
            ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
            ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
            ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
            ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
            ONE_D_FORMATS.add(BarcodeFormat.ITF);
            Vector<BarcodeFormat> QR_CODE_FORMATS = new Vector<BarcodeFormat>(1);
            QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
            Vector<BarcodeFormat> DATA_MATRIX_FORMATS = new Vector<BarcodeFormat>(1);
            DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(ONE_D_FORMATS);
            decodeFormats.addAll(QR_CODE_FORMATS);
            decodeFormats.addAll(DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        multiFormatReader.setHints(hints);
    }

    private void initView() {
        top_openpicture = (ImageView)view.findViewById(R.id.top_openpicture);
        top_openpicture.setOnClickListener(this);
    }

    private void initPermission() {
        //请求Camera权限 与 文件读写 权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void initScanerAnimation() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d( "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inactivityTimer.shutdown();
        mScanerListener = null;
        LogUtils.d("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d( "onDetach");
    }

    public void handleDecode(Result result) {
//        inactivityTimer.onActivity();
        //扫描成功之后的振动与声音提示
//        RxBeepTool.playBeep(getActivity(), vibrate);
//
//        String result1 = result.getText();
//        LogUtils.d("二维码/条形码 扫描结果", result1);
//        if (mScanerListener == null) {
//            //RxToast.success(result1);
//            //initDialogResult(result);
//
//        } else {
//            mScanerListener.onSuccess("From to Camera", result);
//        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (handler == null) {
            handler = new CaptureActivityHandler();
        }
    }

    @Override
    public void onClick(View v) {
        btn(v);
    }

//==============================================================================================解析结果 及 后续处理 end

    final class CaptureActivityHandler extends Handler {

        private State state;

        public CaptureActivityHandler() {
            state = State.SUCCESS;
            restartPreviewAndDecode();
        }

        @Override
        public void handleMessage(Message message) {
            if (message.what == R.id.auto_focus) {
                if (state == State.PREVIEW) {
                }
            } else if (message.what == R.id.restart_preview) {
                restartPreviewAndDecode();
            } else if (message.what == R.id.decode_succeeded) {
                state = State.SUCCESS;
                handleDecode((Result) message.obj);// 解析成功，回调
            } else if (message.what == R.id.decode_failed) {
                state = State.PREVIEW;
            }
        }

        private void restartPreviewAndDecode() {
            if (state == State.SUCCESS) {
                state = State.PREVIEW;
            }
        }
    }

    private enum State {
        //预览
        PREVIEW,
        //成功
        SUCCESS,
        //完成
        DONE
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            LogUtils.d( "onHiddenChanged show");
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
        }
    }
}
