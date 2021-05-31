package com.bete.lamp.barfragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.bete.lamp.R;
import com.bete.lamp.bean.BarRareData;
import com.bete.lamp.bean.Barcode;
import com.bete.lamp.bean.PCRProject;
import com.bete.lamp.thread.PrintSerial;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.SheZhiActivity;
import com.myutils.SharedPreferencesUtils;
import com.utils.FileCommonUtil;
import com.utils.LogUtils;
import com.utils.QRCodeUtil;
import com.utils.RxQRCode;
import com.leon.lfilepickerlibrary.LFilePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class BarCodeImageFragment extends BaseFragment {
    private EditText et_value1;
    private ImageView img_createqr;
    private Bitmap bitmap_createqr;
    private Button bt_ok,bt_okxml,bt_peizhi_open_file,bt_print;
    private String savePath;
    private int REQUESTCODE_FROM_FRAGMENT = 1001;

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

    public void saveScanPeiZhi(String pathstr) {
        PCRProject pcrProject = ((SheZhiActivity) getActivity()).getPcrProject();
        SharedPreferencesUtils.savePCRProject(pcrProject, pathstr);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d("onCreateView");
        View view = inflater.inflate(R.layout.fragment_bar_code_image, container, false);
        et_value1 = (EditText) view.findViewById(R.id.et_value1);
        img_createqr = (ImageView) view.findViewById(R.id.img_createqr);
        bt_ok = (Button) view.findViewById(R.id.bt_ok);
        bt_okxml = (Button) view.findViewById(R.id.bt_okxml);
        bt_peizhi_open_file = (Button) view.findViewById(R.id.bt_peizhi_open_file);
        bt_print = (Button) view.findViewById(R.id.bt_print);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File savefile = FileCommonUtil.getFileByPath(savePath+".png");
                if (FileCommonUtil.createOrExistsFile(savefile))
                {
                    saveBitmapAsPng(bitmap_createqr,savefile);
                }
            }
        });
        bt_okxml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File savefile = FileCommonUtil.getFileByPath(savePath);
                if (FileCommonUtil.createOrExistsFile(savefile))
                {
                    saveScanPeiZhi(savePath);
                }
            }
        });
        bt_peizhi_open_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withSupportFragment(BarCodeImageFragment.this)
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
        });
        bt_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintSerial.getInstance().printImg(img_createqr.getWidth(),img_createqr.getHeight(),bitmap_createqr);
            }
        });
        verifyStoragePermissions(getActivity());
        showIamge();
        return view;
    }

    private void showIamge() {
        PCRProject pcrProject = ((SheZhiActivity) getActivity()).getPcrProject();
        String barCodeStr = Barcode.setReagentDetailBarCodeToBuff(pcrProject);
        LogUtils.d(barCodeStr);
        if (barCodeStr.isEmpty()) {
            Toast.makeText(getActivity(), "二维码文字内容不能为空！", Toast.LENGTH_SHORT).show();
            img_createqr.setVisibility(View.GONE);
            bt_ok.setEnabled(false);
        } else {
            //二维码生成方式一  推荐此方法
//            bitmap_createqr = RxQRCode.builder(barCodeStr).
//                    backColor(0xFFFFFFFF).
//                    codeColor(0xFF000000).
//                    codeSide(300).
//                    into(img_createqr);

            bitmap_createqr = QRCodeUtil.createImage(barCodeStr,pcrProject.project_name,300,300,0xFFFFFFFF,0xFF000000);
            img_createqr.setImageBitmap(bitmap_createqr);
            img_createqr.setVisibility(View.VISIBLE);
            bt_ok.setEnabled(true);
            //二维码生成方式二 默认宽和高都为800 背景为白色 二维码为黑色
            // RxQRCode.createQRCode(str,mIvQrCode);
            Toast.makeText(getActivity(), "二维码已生成!", Toast.LENGTH_SHORT).show();
        }
    }


    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUESTCODE_FROM_FRAGMENT);
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
                    savePath = list.get(0);
                    et_value1.setText(savePath);
//                    bitmap_createqr = BitmapFactory.decodeFile(savePath);
//                    img_createqr.setImageBitmap(bitmap_createqr);
                    LogUtils.d(savePath);
                } else {
                    Toast.makeText(getActivity(), "未选中文件", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    public void goButtonSelectFileHandle(View view) {
//        new LFilePicker()
//                .withSupportFragment(this)
//                .withRequestCode(REQUESTCODE_FROM_FRAGMENT)
//                //.withStartPath("/storage/emulated/0/Download")
//                .withStartPath("/sdcard/MC100/")
//                //.withFileFilter(new String[]{""})
//                .withIsGreater(false)
//                .withChooseMode(true)
//                .withMutilyMode(false)
//                .withFileSize(500 * 1024)
//                .start();
    }

//    public void goButtonOkHandle(View view) {
//        File savefile = FileCommonUtil.getFileByPath(savePath);
//        if (FileCommonUtil.createOrExistsFile(savefile))
//        {
//            saveBitmapAsPng(bitmap_createqr,savefile);
//        }
//    }

    public void saveBitmapAsPng(Bitmap bmp, File f) {
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(getActivity(),"已保存至"+savePath+"^.^",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        showIamge();
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

        LogUtils.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        LogUtils.d("onDetach");
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