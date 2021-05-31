package com.bete.lamp.ui.fsetting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.bean.FileStruct;
import com.bete.lamp.bean.HexRec;
import com.bete.lamp.thread.McuSerial;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.utils.CRCUtil;
import com.utils.LogUtils;
import com.utils.MemoryConstants;
import com.utils.simpleArrayAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SheZhiSettingHardwareUpdataFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingHardwareUpdataFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    View view;
    private Button bt_getbootversion, bt_selecthexfile, bt_crccheck;
    private Button bt_jmpapp, bt_mcureset, bt_flasherase, bt_program;
    private Button bt_xinselecthexfile, bt_xinprogram, bt_xinjmpapp;
    private Spinner spin_bootorapp;
    private TextView tv_response;
    private String SelectHexPath;
    ProgressDialog progressDialog;
    private Thread updateThread, linThread;
    McuSerial tempthread = McuSerial.getInstance();
    int REQUESTCODE_FROM_ACTIVITY = 1000;
    static StringBuffer binbuffer = new StringBuffer();
    static byte[] binbytes = new byte[60 * MemoryConstants.KB];
    static int BOOT_SECTOR_BEGIN = 0x7FC000;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                tv_response.append((String) msg.obj);
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("固件升级完成,请设置模式-重启或跳转APP！");
                builder.setTitle("固件升级");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 2) {
                tv_response.append((String) msg.obj);
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("固件升级失败！");
                builder.setTitle("固件升级");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 3) {
                tv_response.append((String) msg.obj);
                closeProgressDialog();
            }
            if (msg.what == 4) {
                tv_response.append((String) msg.obj);
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("EEPROM擦除失败！");
                builder.setTitle("固件升级");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 5) {
                tv_response.append((String) msg.obj);
                closeProgressDialog();
            }
            if (msg.what == 6) {
                tv_response.append((String) msg.obj);
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("CRC校验失败！");
                builder.setTitle("固件升级");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 7) {
                closeProgressDialog();
                Object[] tempObjects = null;
                tempObjects = (Object[]) msg.obj;
                if ((int) tempObjects[0] != 1) {
                    bt_flasherase.setEnabled(false);
                    bt_selecthexfile.setEnabled(false);
                    bt_crccheck.setEnabled(false);
                    bt_jmpapp.setEnabled(false);
                    bt_program.setEnabled(false);
                    bt_xinselecthexfile.setEnabled(false);
                    bt_xinprogram.setEnabled(false);
                    bt_xinjmpapp.setEnabled(false);
                } else {
                    String log = "get version id:" + String.valueOf((int) tempObjects[1]) + "\n";
                    tv_response.append(log);
                    bt_flasherase.setEnabled(true);
                    bt_selecthexfile.setEnabled(true);
                    bt_crccheck.setEnabled(true);
                    bt_jmpapp.setEnabled(true);
                    bt_program.setEnabled(true);
                    bt_xinselecthexfile.setEnabled(true);
                    bt_xinprogram.setEnabled(true);
                    bt_xinjmpapp.setEnabled(true);
                }
            }
            if (msg.what == 8) {
                closeProgressDialog();
                spin_bootorapp.setSelection(1);
                tv_response.append((String) msg.obj);
            }
            if (msg.what == 9) {
                closeProgressDialog();
                spin_bootorapp.setSelection(0);
                tv_response.append((String) msg.obj);
            }
        }
    };

    public SheZhiSettingHardwareUpdataFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingHardwareUpdataFragment newInstance(String param1, String param2) {
//        SheZhiSettingHardwareUpdataFragment fragment = new SheZhiSettingHardwareUpdataFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop();");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            LogUtils.d(TAG, "onHiddenChanged show");
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
//        onHiddenChanged(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shezhi_setting_hardware_updata, container, false);
        binbuffer = new StringBuffer();
        initview();
        initdata();
        spin_bootorapp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {
                    String log = "set starup mode to app\n";
                    tv_response.append(log);
                    tempthread.setStartupBootOrApp(0);
                } else {
                    String log = "set starup mode to boot\n";
                    tv_response.append(log);
                    tempthread.setStartupBootOrApp(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String log = "set starup mode to nothing\n";
                tv_response.append(log);
                spin_bootorapp.setSelection(2);
            }
        });
        return view;
    }

    public void initview() {
        bt_getbootversion = (Button) view.findViewById(R.id.bt_getbootversion);
        bt_selecthexfile = (Button) view.findViewById(R.id.bt_selecthexfile);
        bt_crccheck = (Button) view.findViewById(R.id.bt_crccheck);
        bt_jmpapp = (Button) view.findViewById(R.id.bt_jmpapp);
        bt_mcureset = (Button) view.findViewById(R.id.bt_mcureset);
        bt_flasherase = (Button) view.findViewById(R.id.bt_flasherase);
        bt_program = (Button) view.findViewById(R.id.bt_program);
        spin_bootorapp = (Spinner) view.findViewById(R.id.spin_bootorapp);
        tv_response = (TextView) view.findViewById(R.id.tv_response);
        tv_response.setMovementMethod(ScrollingMovementMethod.getInstance());
        bt_xinselecthexfile = (Button) view.findViewById(R.id.bt_xinselecthexfile);
        bt_xinprogram = (Button) view.findViewById(R.id.bt_xinprogram);
        bt_xinjmpapp = (Button) view.findViewById(R.id.bt_xinjmpapp);

        List<String> bootorapp_data_list = new ArrayList<String>();
        bootorapp_data_list.add("app");
        bootorapp_data_list.add("boot");
        bootorapp_data_list.add("请选择启动模式");
        //适配器
        simpleArrayAdapter bootorapp_arrAdapter = new simpleArrayAdapter<String>(getActivity(), R.layout.spinner_showed_item, bootorapp_data_list);
        //设置样式
        bootorapp_arrAdapter.setDropDownViewResource(R.layout.spinner_option_items);
        //加载适配器
        spin_bootorapp.setAdapter(bootorapp_arrAdapter);
    }

    public void initdata() {
        showProgressDialog();
        linThread = new Thread() {
            Object[] tempObjects = null;
            @Override
            public void run() {
                tempObjects = tempthread.cmdBootInfo();
                Message msg = new Message();
                msg.what = 7;
                msg.obj = tempObjects;
                handler.sendMessage(msg);
                if (tempthread.getStartupBootOrApp() == 1) {
                    String log ="get startup mode is boot";
                    Message msg1 = new Message();
                    msg1.what = 8;
                    msg1.obj = log;
                    handler.sendMessage(msg1);
                } else {
                    String log ="get startup mode is app";
                    Message msg1 = new Message();
                    msg1.what = 9;
                    msg1.obj = log;
                    handler.sendMessage(msg1);
                }
            }
        };
        linThread.start();
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            LogUtils.d("showProgressDialog: ");
            progressDialog.setMessage("请稍后...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void goMcuResetButtonHandle(View view) {
        tv_response.setText("");
        String log = "reset mcu\n";
        tv_response.append(log);
        showProgressDialog();
        linThread = new Thread() {

            @Override
            public void run() {
                Object[] tempObjects = null;
                tempthread.cmdResetMcu();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tempObjects = tempthread.cmdBootInfo();
                Message msg = new Message();
                msg.what = 7;
                msg.obj = tempObjects;
                handler.sendMessage(msg);
                if (tempthread.getStartupBootOrApp() == 1) {
                    String log ="get startup mode is boot\n";
                    Message msg1 = new Message();
                    msg1.what = 8;
                    msg1.obj = log;
                    handler.sendMessage(msg1);
                } else {
                    String log ="get startup mode is app\n";
                    Message msg1 = new Message();
                    msg1.what = 9;
                    msg1.obj = log;
                    handler.sendMessage(msg1);
                }
            }
        };
        linThread.start();
    }

    public void goHexFileSelectButtonHandle(View view) {
        new LFilePicker()
                .withTitle("选择Hex升级文件")
                .withActivity(getActivity())
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                //.withStartPath("/storage/emulated/0/Download")
                .withStartPath("/storage/sdcard0/LAMP/hex")
                .withFileFilter(new String[]{".hex"})
                .withIsGreater(false)
                .withChooseMode(true)
                .withMutilyMode(false)
                .withFileSize(500 * 1024)
                .start();
    }

    public void goFlashEraseButtonHandle(View view) {
        showProgressDialog();
        linThread = new Thread() {
            @Override
            public void run() {
                if (((int) tempthread.cmdBootEraseFlash()[0]) == 1) {
                    String log = "erase Success\n";
                    Message msg = new Message();
                    msg.what = 3;
                    msg.obj = log;
                    handler.sendMessage(msg);
                    return;
                } else {
                    String log = "erase Fail\n";
                    Message msg = new Message();
                    msg.what = 4;
                    msg.obj = log;
                    handler.sendMessage(msg);
                    return;
                }
            }
        };
        linThread.start();
    }

    public void goFlashProgramButtonHandle(View view) {
        if (SelectHexPath == null) {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage("请选择升级文件！");
            builder.setTitle("固件升级");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        } else {
            showProgressDialog();
            updateThread = new Thread() {
                List<HexRec> hexRecs = new ArrayList<>();
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                String hexLineStr = null;
                byte[] tempbytes = null;
                byte[] bytes = new byte[1000];
                int datalength;
                Object[] tempObjects = null;
                File mfile = new File(SelectHexPath);

                @Override
                public void run() {
                    try {
                        inputStream = new FileInputStream(mfile);
                        //转成 reader 以 行 为单位读取文件
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        //当前行字符串
                        //String hexLineStr = null;
                        hexLineStr = bufferedReader.readLine();
                        while ( hexLineStr != null ) {
                            datalength = 0;
                            LogUtils.d("hexLineStr:" + hexLineStr);
                            if (!hexLineStr.startsWith(":")) {
                                String log = "hex file have err format\n";
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = log;
                                handler.sendMessage(msg);
                                return;
                            }
                            tempbytes = hexToByteArray(hexLineStr.substring(1));
                            System.arraycopy(tempbytes, 0, bytes, datalength, hexLineStr.substring(1).length() / 2);
                            datalength += hexLineStr.substring(1).length() / 2;
                            int totalRecords = 10;
                            while ( totalRecords != 0 ) {
                                hexLineStr = bufferedReader.readLine();
                                if (hexLineStr != null) {
                                    tempbytes = hexToByteArray(hexLineStr.substring(1));
                                    System.arraycopy(tempbytes, 0, bytes, datalength, hexLineStr.substring(1).length() / 2);
                                    datalength += hexLineStr.substring(1).length() / 2;
                                }
                                totalRecords--;
                            }
                            if (datalength == 0) {
                                String log = "hex file have err format\n";
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = log;
                                handler.sendMessage(msg);
                                return;
                            }
                            LogUtils.d(bytes);
                            tempObjects = tempthread.cmdBootProgramFlash(bytes, datalength);
                            if ((int) tempObjects[0] != 1) {
                                String log = "program fail by communicate\n";
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = log;
                                handler.sendMessage(msg);
                                return;
                            }
                            hexLineStr = bufferedReader.readLine();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String log = "program success\n";
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = log;
                    handler.sendMessage(msg);
                    return;
                }
            };
            updateThread.start();
        }
    }
    public void goXinFlashProgramButtonHandle(View view) {
        if (SelectHexPath == null) {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage("请选择升级文件！");
            builder.setTitle("固件升级");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        } else {
            showProgressDialog();
            updateThread = new Thread() {
                List<HexRec> hexRecs = new ArrayList<>();
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                String hexLineStr = null;
                byte[] tempbytes = null;
                byte[] bytes = new byte[1000];
                int datalength;
                Object[] tempObjects = null;
                File mfile = new File(SelectHexPath);
                String log = "";
                @Override
                public void run() {
                    try {
                        if (((int) tempthread.cmdBootEraseFlash()[0]) == 1) {
                            log += "erase Success\n";
                        } else {
                            String log = "erase Fail\n";
                            Message msg = new Message();
                            msg.what = 4;
                            msg.obj = log;
                            handler.sendMessage(msg);
                            return;
                        }

                        inputStream = new FileInputStream(mfile);
                        //转成 reader 以 行 为单位读取文件
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        //当前行字符串
                        //String hexLineStr = null;
                        hexLineStr = bufferedReader.readLine();
                        while ( hexLineStr != null ) {
                            datalength = 0;
                            LogUtils.d("hexLineStr:" + hexLineStr);
                            if (!hexLineStr.startsWith(":")) {
                                log += "hex file have err format\n";
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = log;
                                handler.sendMessage(msg);
                                return;
                            }
                            tempbytes = hexToByteArray(hexLineStr.substring(1));
                            System.arraycopy(tempbytes, 0, bytes, datalength, hexLineStr.substring(1).length() / 2);
                            datalength += hexLineStr.substring(1).length() / 2;
                            int totalRecords = 10;
                            while ( totalRecords != 0 ) {
                                hexLineStr = bufferedReader.readLine();
                                if (hexLineStr != null) {
                                    tempbytes = hexToByteArray(hexLineStr.substring(1));
                                    System.arraycopy(tempbytes, 0, bytes, datalength, hexLineStr.substring(1).length() / 2);
                                    datalength += hexLineStr.substring(1).length() / 2;
                                }
                                totalRecords--;
                            }
                            if (datalength == 0) {
                                log += "hex file have err format\n";
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = log;
                                handler.sendMessage(msg);
                                return;
                            }
                            LogUtils.d(bytes);
                            tempObjects = tempthread.cmdBootProgramFlash(bytes, datalength);
                            if ((int) tempObjects[0] != 1) {
                                log += "program fail by communicate\n";
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = log;
                                handler.sendMessage(msg);
                                return;
                            }
                            hexLineStr = bufferedReader.readLine();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (true) {
                        String log = "program success\n";
                        if(readFile(SelectHexPath)!=0)
                        {
                            log = "crc flash jisuan fail\n";
                            Message msg = new Message();
                            msg.what = 6;
                            msg.obj = log;
                            handler.sendMessage(msg);
                            return;
                        }
                        Object[] tempObjects = null;
                        tempObjects = tempthread.cmdBootCRC((int) programadrr, (int) proframlen, (int) mycrc);//proframlen
                        if ((int) tempObjects[0] == 1) {
                            if ((int) tempObjects[1] == mycrc) {
                                log += "crc is same\n";
                                Message msg = new Message();
                                msg.what = 5;
                                msg.obj = log;
                                handler.sendMessage(msg);
                                return;
                            } else {
                                log += "crc is different\n";
                                log += "device crc is" + String.valueOf((int) tempObjects[1]) +"\n";
                                Message msg = new Message();
                                msg.what = 6;
                                msg.obj = log;
                                handler.sendMessage(msg);
                            }
                        } else {
                            log += "crc flash fail\n";
                            Message msg = new Message();
                            msg.what = 6;
                            msg.obj = log;
                            handler.sendMessage(msg);
                        }
                    }
                }
            };
            updateThread.start();
        }
    }



    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public void goCRCCheckButtonHandle(View view) {
        if (SelectHexPath == null) {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage("请选择升级文件！");
            builder.setTitle("固件升级");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        }
        showProgressDialog();
        linThread = new Thread() {
            @Override
            public void run() {
                if(readFile(SelectHexPath)!=0)
                {
                    String log = "crc flash jisuan fail\n";
                    Message msg = new Message();
                    msg.what = 6;
                    msg.obj = log;
                    handler.sendMessage(msg);
                }
                Object[] tempObjects = null;
                tempObjects = tempthread.cmdBootCRC((int) programadrr, (int) proframlen, (int) mycrc);//proframlen
                if ((int) tempObjects[0] == 1) {
                    if ((int) tempObjects[1] == mycrc) {
                        String log = "crc is same\n";
                        Message msg = new Message();
                        msg.what = 5;
                        msg.obj = log;
                        handler.sendMessage(msg);
                        return;
                    } else {
                        String log = "crc is different\n";
                        log += "device crc is" + String.valueOf((int) tempObjects[1]);
                        Message msg = new Message();
                        msg.what = 6;
                        msg.obj = log;
                        handler.sendMessage(msg);
                    }
                } else {
                    String log = "crc flash cmd fail\n";
                    Message msg = new Message();
                    msg.what = 6;
                    msg.obj = log;
                    handler.sendMessage(msg);
                }
            }
        };
        linThread.start();
    }

    public void goBootVersionButtonHandle(View view) {
        initdata();
    }

    public void goJmpAppButtonHandle(View view) {
        String log = "jump to app\n";
        tv_response.append(log);
        showProgressDialog();
        linThread = new Thread() {
            @Override
            public void run() {
                Object[] tempObjects = null;
                tempthread.cmdJmpToApp();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tempObjects = tempthread.cmdBootInfo();
                Message msg = new Message();
                msg.what = 7;
                msg.obj = tempObjects;
                handler.sendMessage(msg);
                if (tempthread.getStartupBootOrApp() == 1) {
                    String log ="get startup mode is boot";
                    Message msg1 = new Message();
                    msg1.what = 8;
                    msg1.obj = log;
                    handler.sendMessage(msg1);
                } else {
                    String log ="get startup mode is app";
                    Message msg1 = new Message();
                    msg1.what = 9;
                    msg1.obj = log;
                    handler.sendMessage(msg1);
                }
            }
        };
        linThread.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                //If it is a file selection mode, you need to get the path collection of all the files selected
                //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                List<String> list = data.getStringArrayListExtra("paths");
                //If it is a folder selection mode, you need to get the folder path of your choice
//                String path = data.getStringExtra("path");
                SelectHexPath = list.get(0);
                String log = "select hex file : " + SelectHexPath + "\n";
                tv_response.append(log);
            }
        }
    }

    /**
     * @return -1 文件解析错误 0 表示成功 -2 初始buf太小
     */
    static long proframlen, programadrr, mycrc;

    private static int readFile(String hexfilepath) {
        File mfile = new File(hexfilepath);
        List<HexRec> hexRecs = new ArrayList<>();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        int i = 0, j = 0;            //索引
        long l_addr;
        long len = 0;//数组索引
        long minAddr = 4294967295L;
        long maxAddr = 0;
        byte[] tempbyte;
        FileStruct hex = new FileStruct();
        try {
            if (mfile == null) {
                System.out.println("文件为空");
            }
            inputStream = new FileInputStream(mfile);
            //转成 reader 以 行 为单位读取文件
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            //当前行字符串
            String hexLineStr;
            //当前行数
            int hexLineNum = 0;
            while ( (hexLineStr = bufferedReader.readLine()) != null ) {
                System.out.println(hexLineStr);
                hexLineNum++;
                if (!hexLineStr.startsWith(":", 0)) {
                    return -1;
                }
                if (hexLineStr.length() >= 11) {
                    hex.setStart(":");

                    byte[] data = hexToByteArray(hexLineStr.substring(1));//判断数据的正确是是不是0—F
                    //byte[] data = hexString2ByteArray(hexLineStr.substring(1));//判断数据的正确是是不是0—F
                    if (data == null) return -1;
                    //解析数据
//                    hex.setLength(Integer.parseInt(hexLineStr.substring(1, 3), 16));
//                    hex.setOffset(Integer.parseInt(hexLineStr.substring(3, 7), 16));
//                    hex.setType(Integer.parseInt(hexLineStr.substring(7, 9), 16));

                    hex.setLength(Long.parseLong("00" + hexLineStr.substring(1, 3), 16));
                    hex.setOffset(Long.parseLong("00" + hexLineStr.substring(3, 7), 16));
                    hex.setType(Integer.parseInt(hexLineStr.substring(7, 9), 16));
                    ///////////////////////////////////////////////////////////////////
                    //判断数据类型是否合法, 未处理05的数据
                    if (0x00 != hex.getType() && 0x01 != hex.getType() && 0x02 != hex.getType() && 0x04 != hex.getType() && 0x05 != hex.getType()) {
                        return -1;
                    }
                    if (0x05 == hex.getType()) {//不处理05类型的数据
                        continue;
                    }
                    if (hex.getLength() > 0) {
                        hex.setData(hexLineStr.substring(9, 9 + (int) hex.getLength() * 2));
                    }
                    if (!checkValue(hexLineStr)) return -1;
                    switch (hex.type) {
                        case 0x00:    //本行的数据类型为“数据记录”
                            //本行所从属的数据类型为“数据记录”
                            if (0x00 == hex.format) {
                                l_addr = hex.offset;
                            }
                            //本行所从属的数据类型为“扩展段地址记录”(HEX86)--20位地址
                            else if (0x02 == hex.format) {
                                l_addr = (hex.address << 4) + hex.offset;
                            }
                            //本行所从属的数据类型为“扩展线性地址记录”(HEX386)--32位地址
                            else if (0x04 == hex.format) {
                                l_addr = (hex.address << 16) + hex.offset;
                            }
                            //文件结束
                            else {
                                i = 1;
                                break;
                            }
                            //记录地址中的最大值
                            if (minAddr > l_addr) minAddr = l_addr;
                            if (hex.length > 0) {
                                HexRec hexRec = new HexRec();
                                hexRec.setAddr(l_addr);
                                hexRec.setLen(hex.length);
                                hexRec.setBuf(hex.data);
                                hexRecs.add(hexRec);
                                len += hex.length;
                            }
                            break;

                        case 0x01:    //本行的数据类型为“文件结束记录”
                            //文件结束记录的数据个数一定是0x00
                            if (hex.length == 0x00) i = 1;
                            hex.format = 0x01;
                            break;

                        case 0x02:    //本行的数据类型为“扩展段地址记录”
                            //扩展段地址记录的数据个数一定是0x02
                            if (hex.length != 0x02) i = 3;
                            //扩展段地址记录的地址一定是0x0000
                            if (hex.offset != 0x0000) i = 3;
                            //更改hex从属的数据类型
                            hex.format = 0x02;
                            //获取段地址
                            String hexStr = hex.getData().substring(0, 4);

                            byte[] hexBytes = hexToByteArray(hexStr);
                            hex.address = (long) ((hexBytes[1] & 0xFF) | ((hexBytes[0] & 0xFF) << 8));
                            break;

                        case 0x04://本行的数据类型为“扩展线性地址记录”
                            //扩展线性地址记录中的数据个数一定是0x02
                            if (hex.length != 0x02) i = 4;
                            //扩展线性地址记录的地址一定是0x0000
                            if (hex.offset != 0x0000) i = 4;
                            //更改hex从属的数据类型
                            hex.format = 0x04;
                            //获取高16位地址
                            hexStr = hex.getData().substring(0, 4);

                            hexBytes = hexToByteArray(hexStr);
                            hex.address = (long) ((hexBytes[1] & 0xFF) | ((hexBytes[0] & 0xFF) << 8));
                            break;
                    }
                }
                //如果出现异常或文件结束退出循环
                if (i == 1) {
                    break;
                }
                if (i > 0) {
                    return -1;//文件解析出错
                }
            }
            len = 0;
            long minLen = 0;
            long offset = 0;
            binbuffer.delete(0, binbuffer.length());
            for (int x = 0; x < binbytes.length; x++) {
                if ((x + 1) % 4 != 0) {
                    binbytes[x] = (byte) 0xFF;
                } else {
                    binbytes[x] = 0x00;
                }
            }
            for (int a = 0; a < hexRecs.size(); a++) {
                if (hexRecs.get(a).getAddr() < BOOT_SECTOR_BEGIN) {
                    if (minLen > hexRecs.get(a).getAddr()) {
                        minLen = hexRecs.get(a).getAddr();
                    }
                    if (maxAddr < hexRecs.get(a).getAddr() + hexRecs.get(a).getLen()) {
                        maxAddr = hexRecs.get(a).getAddr() + hexRecs.get(a).getLen();
                    }
                    tempbyte = hexString2ByteArray(hexRecs.get(a).getBuf());
                    System.arraycopy(tempbyte, 0, binbytes, (int) hexRecs.get(a).getAddr(), (int) (hexRecs.get(a).getLen()));
                    //LogUtils.d(tempbyte);
                }
            }
//            binbuffer.delete(0,binbuffer.length());
//            for (int a = 0; a < hexRecs.size(); a++) {
//                offset = (int) (hexRecs.get(a).getAddr() - minAddr);
//                binbuffer.append(hexRecs.get(a).getBuf());
//                if (minLen < offset + hexRecs.get(a).getLen()) {
//                    minLen = offset + hexRecs.get(a).getLen();
//                }
//                len += hexRecs.get(a).getLen();
//            }

//            if (len < minLen) {
//                len = minLen;
//            }
//            proframlen=len;
//            programadrr=minAddr;
//            System.out.println(binbuffer);
//            binbytes =  hexToByteArray(binbuffer.toString());
//            //binbytes =  hexString2ByteArray(binbuffer.toString());

            minAddr -= minAddr % 4;
            maxAddr += maxAddr % 4;

            len = maxAddr - minAddr;
            proframlen = len;
            programadrr = minAddr / 2;

            //LogUtils.d(binbytes);
            LogUtils.d(proframlen);
            LogUtils.d(programadrr);
            mycrc = CRCUtil.CRC_16_XMODEM(binbytes, (int) minAddr, (int) proframlen);//proframlen
            LogUtils.d(mycrc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param hexString
     * @return
     */
    public static byte[] hexString2ByteArray(String hexString) {
        try {
            if (hexString == null || hexString.equals("")) {
                return null;
            }
            hexString = hexString.toUpperCase();
            int length = hexString.length() / 2;
            char[] hexChars = hexString.toCharArray();
            byte[] d = new byte[length];
            for (int i = 0; i < length; i++) {
                int pos = i * 2;
                d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 校验和必然是256的整数倍,如果有余数则认为校验和失败
     *
     * @return false 校验失败 反正成功
     */
    public static boolean checkValue(String hexLineStr) {

        byte[] buf = hexToByteArray(hexLineStr.substring(1));
        //byte[] buf = hexString2ByteArray(hexLineStr.substring(1));
        byte temp = 0;
        for (int i = 0; i < buf.length; i++) {
            temp += buf[i];
        }
        //if (temp % 0xFF == 0) {
        if (temp % 0x100 == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
    }
}
