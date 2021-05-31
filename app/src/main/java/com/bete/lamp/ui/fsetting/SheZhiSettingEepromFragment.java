package com.bete.lamp.ui.fsetting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.adapter.EepromItemAdapter;
import com.bete.lamp.bean.EepromItem;
import com.bete.lamp.thread.McuSerial;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.utils.Byte2TypeUtils;
import com.utils.FileIOUtils;
import com.utils.IniFile;
import com.utils.LogUtils;
import com.utils.SystemTimeSettingUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.myutils.Canstant.INNER_EEPROM_DIR;
import static com.myutils.GlobalDate.DEVICE;
import static com.myutils.GlobalDate.deviceno;

public class SheZhiSettingEepromFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingEepromFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private List<EepromItem> eepromItems = new ArrayList<>();
    EepromItemAdapter eepromItemAdapter;
    String SelectFilePath = "";
    Button bt_ee_all, bt_ee_reall, bt_ee_read, bt_ee_write, bt_ee_add, bt_ee_del, bt_ee_import, bt_ee_export;
    McuSerial mcuSerial;
    private ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog(String text) {
        closeProgressDialog();
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(text);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("地址错误！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }

            if (msg.what == 2) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("读取出错！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 3) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("读取完成！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 4) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("数据出错！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 5) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("写入出错！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 6) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("写入完成！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 7) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("未选择数据类型！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 8) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("EEPROM 读取错误！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 9) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("长度错误！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            eepromItemAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public SheZhiSettingEepromFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingEepromFragment newInstance(String param1, String param2) {
//        SheZhiSettingEepromFragment fragment = new SheZhiSettingEepromFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_eeprom, container, false);
        bt_ee_all = (Button) view.findViewById(R.id.bt_ee_all);
        bt_ee_reall = (Button) view.findViewById(R.id.bt_ee_reall);
        bt_ee_read = (Button) view.findViewById(R.id.bt_ee_read);
        bt_ee_write = (Button) view.findViewById(R.id.bt_ee_write);
        bt_ee_add = (Button) view.findViewById(R.id.bt_ee_add);
        bt_ee_del = (Button) view.findViewById(R.id.bt_ee_del);
        bt_ee_import = (Button) view.findViewById(R.id.bt_ee_import);
        bt_ee_export = (Button) view.findViewById(R.id.bt_ee_export);

        bt_ee_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAllHandle(v);
            }
        });

        bt_ee_reall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goReallHandle(v);
            }
        });

        bt_ee_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goReadHandle(v);
            }
        });

        bt_ee_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goWriteHandle(v);
            }
        });

        bt_ee_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddHandle(v);
            }
        });

        bt_ee_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goDelHandle(v);
            }
        });

        bt_ee_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goImportHandle(v);
            }
        });

        bt_ee_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goExportHandle(v);
            }
        });

        //initKBAdjustItems();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_kb);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        eepromItemAdapter = new EepromItemAdapter(getContext(), eepromItems);
        recyclerView.setAdapter(eepromItemAdapter);
        changeButtonState();
        if (DEVICE)
            mcuSerial = McuSerial.getInstance();
        return view;
    }


    private Thread eepromReadThread;

    public void goReadHandle(View view) {
        showProgressDialog("读取中");
        eepromReadThread = new Thread() {
            @Override
            public void run() {
                int count = eepromItems.size();
                EepromItem eepromItem;
                int addr, len;
                Object[] objects = new Object[3];
                for (int i = 0; i < count; i++) {
                    eepromItem = eepromItems.get(i);
                    if (eepromItem.isAble) {
                        try {
                            if ((eepromItem.addr.startsWith("0x")) || (eepromItem.addr.startsWith("0X"))) {
                                addr = Integer.parseInt(eepromItem.addr.substring(2, eepromItem.addr.length()), 16);
                            } else {
                                addr = Integer.parseInt(eepromItem.addr);
                            }
                        } catch (Exception e) {
                            //地址错误
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                            return;
                        }

                        try {
                            if ((eepromItem.len.startsWith("0x")) || (eepromItem.len.startsWith("0X"))) {
                                len = Integer.parseInt(eepromItem.len.substring(2, eepromItem.len.length()), 16);
                            } else {
                                len = Integer.parseInt(eepromItem.len);
                            }
                        } catch (Exception e) {
                            //地址错误
                            Message msg = new Message();
                            msg.what = 9;
                            handler.sendMessage(msg);
                            return;
                        }
                        if (DEVICE) {
                            if (eepromItem.type.equals("Int")) {
                                objects = mcuSerial.cmdLampReadEeprom(addr, 4);
                            } else if (eepromItem.type.equals("Float")) {
                                objects = mcuSerial.cmdLampReadEeprom(addr, 4);
                            } else if (eepromItem.type.equals("Double")) {
                                objects = mcuSerial.cmdLampReadEeprom(addr, 8);
                            } else if (eepromItem.type.equals("Short")) {
                                objects = mcuSerial.cmdLampReadEeprom(addr, 2);
                            } else if (eepromItem.type.equals("Char")) {
                                objects = mcuSerial.cmdLampReadEeprom(addr, Integer.parseInt(eepromItem.len));
                            } else if (eepromItem.type.equals("Unsigned int")) {
                                objects = mcuSerial.cmdLampReadEeprom(addr, 4);
                            } else if (eepromItem.type.equals("Unsigned short")) {
                                objects = mcuSerial.cmdLampReadEeprom(addr, 2);
                            } else {
                                Message msg = new Message();
                                msg.what = 7;
                                handler.sendMessage(msg);
                                return;
                            }
                        }
                        if ((objects[1] != null) && (int) objects[1] == 0) {
                            if (eepromItem.type.equals("Int")) {
                                int tempInt = (int) (((((byte[]) objects[2])[3] & 0xff) << 24) | ((((byte[]) objects[2])[2] & 0xff) << 16) | ((((byte[]) objects[2])[1] & 0xff) << 8) | (((byte[]) objects[2])[0] & 0xff));
                                eepromItem.setValue(String.valueOf(tempInt));
                            } else if (eepromItem.type.equals("Float")) {
                                float tempFloat = Byte2TypeUtils.getFloat(((byte[]) objects[2]), 0);
                                if (Float.isNaN(tempFloat))
                                    eepromItem.setValue("NAN");
                                else
                                    eepromItem.setValue(new BigDecimal(Float.toString(tempFloat)).toString());
                            } else if (eepromItem.type.equals("Double")) {
                                double tempDouble = Byte2TypeUtils.getDouble(((byte[]) objects[2]), 0);
                                if (Double.isNaN(tempDouble))
                                    eepromItem.setValue("NAN");
                                else
                                    eepromItem.setValue(new BigDecimal(Double.toString(tempDouble)).toString());
                            } else if (eepromItem.type.equals("Short")) {
                                short tempShort = (short) (((((byte[]) objects[2])[1] & 0xff) << 8) | (((byte[]) objects[2])[0] & 0xff));
                                eepromItem.setValue(String.valueOf(tempShort));
                            } else if (eepromItem.type.equals("Char")) {
//                                byte tempByte = (byte) (((byte[]) objects[2])[0] & 0xff);
//                                eepromItem.setValue(String.valueOf(tempByte));
                                byte[] bytes = new byte[len];
                                for (int z = 0; z < len; z++) {
                                    bytes[z] = ((byte[]) objects[2])[z];
                                }
                                LogUtils.d(new String(bytes));
                                eepromItem.setValue(new String(bytes));
                            } else if (eepromItem.type.equals("Unsigned int")) {
                                long tempUnInt = (long) (((0x00 & 0xff) << 32) | ((((byte[]) objects[2])[3] & 0xff) << 24) | ((((byte[]) objects[2])[2] & 0xff) << 16) | ((((byte[]) objects[2])[1] & 0xff) << 8) | (((byte[]) objects[2])[0] & 0xff));
                                eepromItem.setValue(String.valueOf(tempUnInt));
                            } else if (eepromItem.type.equals("Unsigned short")) {
                                int tempUnShort = (int) (((((byte[]) objects[2])[1] & 0xff) << 8) | (((byte[]) objects[2])[0] & 0xff));
                                eepromItem.setValue(String.valueOf(tempUnShort));
                            } else {
                                //未选中
                                Message msg = new Message();
                                msg.what = 8;
                                handler.sendMessage(msg);
                                return;
                            }
                        } else {
                            //EEPROM 读取错误
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                            return;
                        }
                    }
                }
                //读取完成
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
                return;
            }
        };
        eepromReadThread.start();
    }

    private Thread eepromWriteThread;

    public void goWriteHandle(View view) {
        showProgressDialog("写入中");
        eepromWriteThread = new Thread() {
            @Override
            public void run() {
                int count = eepromItems.size();
                EepromItem eepromItem;
                int addr, len;
                byte[] buff = new byte[8];
                Object[] objects = new Object[2];
                for (int i = 0; i < count; i++) {
                    eepromItem = eepromItems.get(i);
                    if (eepromItem.isAble) {
                        try {
                            if ((eepromItem.addr.startsWith("0x")) || (eepromItem.addr.startsWith("0X"))) {
                                addr = Integer.parseInt(eepromItem.addr.substring(2, eepromItem.addr.length()), 16);
                            } else {
                                addr = Integer.parseInt(eepromItem.addr);
                            }
                        } catch (Exception e) {
                            //地址错误
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                            return;
                        }

                        try {
                            if ((eepromItem.len.startsWith("0x")) || (eepromItem.len.startsWith("0X"))) {
                                len = Integer.parseInt(eepromItem.len.substring(2, eepromItem.len.length()), 16);
                            } else {
                                len = Integer.parseInt(eepromItem.len);
                            }
                        } catch (Exception e) {
                            //地址错误
                            Message msg = new Message();
                            msg.what = 9;
                            handler.sendMessage(msg);
                            return;
                        }

                        if (eepromItem.type.equals("Int")) {
                            int valueInt;
                            try {
                                valueInt = Integer.valueOf(eepromItem.value);
                            } catch (Exception e) {
                                //数据错误
                                Message msg = new Message();
                                msg.what = 4;
                                handler.sendMessage(msg);
                                return;
                            }
                            Byte2TypeUtils.putInt(buff, valueInt, 0);
                            if (DEVICE)
                                objects = mcuSerial.cmdLampWriteEeprom(addr, buff, 4);
                        } else if (eepromItem.type.equals("Float")) {
                            float valueFloat;
                            try {
                                valueFloat = Float.valueOf(eepromItem.value);
                            } catch (Exception e) {
                                //数据错误
                                Message msg = new Message();
                                msg.what = 4;
                                handler.sendMessage(msg);
                                return;
                            }
                            Byte2TypeUtils.putFloat(buff, valueFloat, 0);
                            if (DEVICE)
                            objects = mcuSerial.cmdLampWriteEeprom(addr, buff, 4);
                        } else if (eepromItem.type.equals("Double")) {
                            double valueDouble;
                            try {
                                valueDouble = Double.valueOf(eepromItem.value);
                            } catch (Exception e) {
                                //数据错误
                                Message msg = new Message();
                                msg.what = 4;
                                handler.sendMessage(msg);
                                return;
                            }
                            Byte2TypeUtils.putDouble(buff, valueDouble, 0);
                            if (DEVICE)
                            objects = mcuSerial.cmdLampWriteEeprom(addr, buff, 8);
                            break;
                        } else if (eepromItem.type.equals("Short")) {
                            short valueShort;
                            try {
                                valueShort = Short.valueOf(eepromItem.value);
                            } catch (Exception e) {
                                //数据错误
                                Message msg = new Message();
                                msg.what = 4;
                                handler.sendMessage(msg);
                                return;
                            }
                            Byte2TypeUtils.putShort(buff, valueShort, 0);
                            if (DEVICE)
                            objects = mcuSerial.cmdLampWriteEeprom(addr, buff, 2);
                        } else if (eepromItem.type.equals("Char")) {
//                            byte valueByte;
//                            try {
//                                valueByte = Byte.valueOf(eepromItem.value);
//                            } catch (Exception e) {
//                                //数据错误
//                                Message msg = new Message();
//                                msg.what = 4;
//                                handler.sendMessage(msg);
//                                return;
//                            }
//                            buff[0] = valueByte;
//                            objects = mcuSerial.cmdLampWriteEeprom(addr, buff, 1);

                            byte[] valueBytes = new byte[len];
                            try {
                                System.arraycopy(eepromItem.value.getBytes(), 0, valueBytes, 0, Math.min(eepromItem.value.getBytes().length, len));
//                                valueBytes = eepromItem.value.getBytes();
                            } catch (Exception e) {
                                //数据错误
                                Message msg = new Message();
                                msg.what = 4;
                                handler.sendMessage(msg);
                                return;
                            }
                            if (DEVICE)
                            objects = mcuSerial.cmdLampWriteEeprom(addr, valueBytes, len);

                        } else if (eepromItem.type.equals("Unsigned int")) {
                            long valueLong;
                            try {
                                valueLong = Long.valueOf(eepromItem.value);
                            } catch (Exception e) {
                                //数据错误
                                Message msg = new Message();
                                msg.what = 4;
                                handler.sendMessage(msg);
                                return;
                            }
                            Byte2TypeUtils.putLong(buff, valueLong, 0);
                            if (DEVICE)
                            objects = mcuSerial.cmdLampWriteEeprom(addr, buff, 4);
                        } else if (eepromItem.type.equals("Unsigned short")) {
                            int valueUnShort;
                            try {
                                valueUnShort = Integer.valueOf(eepromItem.value);
                            } catch (Exception e) {
                                //数据错误
                                Message msg = new Message();
                                msg.what = 4;
                                handler.sendMessage(msg);
                                return;
                            }
                            Byte2TypeUtils.putInt(buff, valueUnShort, 0);
                            if (DEVICE)
                            objects = mcuSerial.cmdLampWriteEeprom(addr, buff, 2);
                        } else {
                            //未选中
                            Message msg = new Message();
                            msg.what = 7;
                            handler.sendMessage(msg);
                            return;
                        }
                        if (DEVICE){
                            LogUtils.d(buff);
                            if ((int) objects[1] != 0) {
                                //EEPROM 写入错误
                                Message msg = new Message();
                                msg.what = 5;
                                handler.sendMessage(msg);
                                return;
                            }
                        }

                    }
                }
                //EEPROM 写入完成
                Message msg = new Message();
                msg.what = 6;
                handler.sendMessage(msg);
                return;
            }
        };
        eepromWriteThread.start();
    }

    public void goAddHandle(View view) {
        int position = eepromItemAdapter.getMposition();
        if (position >= 0) {
            eepromItems.add(position + 1, new EepromItem());
        } else {
            eepromItems.add(new EepromItem());
        }
        changeButtonState();
        eepromItemAdapter.notifyDataSetChanged();

    }

    public void goDelHandle(View view) {
        if (eepromItemAdapter.getItemCount() == 0)
            return;
        int position = eepromItemAdapter.getMposition();
        if (position < 0)
            return;
        LogUtils.d("Position:" + position);
        eepromItems.remove(position);
        position = position - 1;
        eepromItemAdapter.setMposition(position);
        changeButtonState();
        eepromItemAdapter.notifyDataSetChanged();
    }

    private void changeButtonState() {
        if (eepromItems.size() > 0) {
            bt_ee_del.setEnabled(true);
        } else {
            bt_ee_del.setEnabled(false);
        }
    }

    public void goAllHandle(View view) {
        for (int i = 0; i < eepromItems.size(); i++) {
            eepromItems.get(i).isAble = true;
        }
        eepromItemAdapter.notifyDataSetChanged();
    }

    public void goReallHandle(View view) {
        for (int i = 0; i < eepromItems.size(); i++) {
            eepromItems.get(i).isAble = false;
        }
        eepromItemAdapter.notifyDataSetChanged();
    }

    int Count = 0;
    int REQUESTCODE_FROM_FRAGMENT1 = 1000;
    int REQUESTCODE_FROM_FRAGMENT2 = 1001;

    public void goImportHandle(View view) {
        new LFilePicker()
                .withSupportFragment(SheZhiSettingEepromFragment.this)
                .withRequestCode(REQUESTCODE_FROM_FRAGMENT1)
                //.withStartPath("/storage/emulated/0/Download")
                .withStartPath("/sdcard/" + INNER_EEPROM_DIR)
                //.withFileFilter(new String[]{""})
                .withIsGreater(false)
                .withChooseMode(true)
                .withMutilyMode(false)
                .withFileSize(500 * 1024)
                .start();
    }

    public void getDataFromFile(String filepath) {
        String iniFileName = filepath;
        IniFile ini = new IniFile(iniFileName);
        String temp = ini.getStringProperty("NodeEEParam", "ParamNum", "");
        if ((temp == null) || (temp.isEmpty()))
            return;
        try {
            Count = Integer.valueOf(temp);
        } catch (NumberFormatException e) {
            //读取文件错误
            return;
        }
        eepromItems.clear();
        for (int i = 0; i < Count; i++) {
            EepromItem eepromItemTemp = new EepromItem();
            eepromItemTemp.name = ini.getStringProperty("Param" + (i + 1), "ParamName", "");
            eepromItemTemp.addr = ini.getStringProperty("Param" + (i + 1), "ParamAddress", "");
            eepromItemTemp.value = ini.getStringProperty("Param" + (i + 1), "ParamValue", "");
            eepromItemTemp.len = ini.getStringProperty("Param" + (i + 1), "ParamLen", "");
            eepromItemTemp.type = ini.getStringProperty("Param" + (i + 1), "ParamType", "");
            eepromItemTemp.prop = ini.getStringProperty("Param" + (i + 1), "ParamProp", "");
            eepromItems.add(eepromItemTemp);
            LogUtils.d(eepromItemTemp.name);
        }
        eepromItemAdapter.notifyDataSetChanged();
        changeButtonState();
    }

    public void goExportHandle(View view) {
        new LFilePicker()
                .withSupportFragment(SheZhiSettingEepromFragment.this)
                .withRequestCode(REQUESTCODE_FROM_FRAGMENT2)
                //.withStartPath("/storage/emulated/0/Download")
                .withStartPath("/sdcard/" + INNER_EEPROM_DIR)
                //.withFileFilter(new String[]{""})
                .withIsGreater(false)
                .withChooseMode(true)
                .withMutilyMode(false)
                .withFileSize(500 * 1024)
                .start();
    }

    public void setDataToFile(String filepath) {
        String iniFileName = filepath;
        FileIOUtils.writeFileFromString(iniFileName, "", false);
        IniFile ini = new IniFile(iniFileName);
        int Count = eepromItemAdapter.getItemCount();
        ini.setStringProperty("NodeEEParam", "ParamNum", String.valueOf(eepromItemAdapter.getItemCount()));
        for (int i = 0; i < Count; i++) {
            EepromItem eepromItemTemp = eepromItems.get(i);
            ini.setStringProperty("Param" + (i + 1), "ParamName", eepromItemTemp.name);
            ini.setStringProperty("Param" + (i + 1), "ParamAddress", eepromItemTemp.addr);
            ini.setStringProperty("Param" + (i + 1), "ParamValue", eepromItemTemp.value);
            ini.setStringProperty("Param" + (i + 1), "ParamLen", eepromItemTemp.len);
            ini.setStringProperty("Param" + (i + 1), "ParamType", eepromItemTemp.type);
            ini.setStringProperty("Param" + (i + 1), "ParamProp", eepromItemTemp.prop);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_FRAGMENT1) {
                //If it is a file selection mode, you need to get the path collection of all the files selected
                //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                List<String> list = data.getStringArrayListExtra("paths");
                //If it is a folder selection mode, you need to get the folder path of your choice
//                String path = data.getStringExtra("path");
                SelectFilePath = list.get(0);
                String log = "select hex file : " + SelectFilePath + "\n";
                LogUtils.d("read from:" + SelectFilePath);
                getDataFromFile(SelectFilePath);
            }
            if (requestCode == REQUESTCODE_FROM_FRAGMENT2) {
                //If it is a file selection mode, you need to get the path collection of all the files selected
                //List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);//Constant.RESULT_INFO == "paths"
                List<String> list = data.getStringArrayListExtra("paths");
                //If it is a folder selection mode, you need to get the folder path of your choice
//                String path = data.getStringExtra("path");
                SelectFilePath = list.get(0);
                String log = "select hex file : " + SelectFilePath + "\n";
                LogUtils.d("write to :" + SelectFilePath);
                setDataToFile(SelectFilePath);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
        closeProgressDialog();
    }
}
