package com.bete.lamp.ui.fsetting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bete.lamp.R;
import com.bete.lamp.ui.normal.BaseFragment;
import com.example.mywificonnect.ui.adapter.MyListViewAdapter;
import com.example.mywificonnect.ui.api.OnNetworkChangeListener;
import com.example.mywificonnect.ui.component.MyListView;
import com.example.mywificonnect.ui.dialog.WifiConnDialog;
import com.example.mywificonnect.ui.dialog.WifiStatusDialog;
import com.example.mywificonnect.utils.WifiAdminUtils;
import com.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;
import static com.myutils.GlobalDate.packageVersion;

public class SheZhiSettingWifiFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingWifiFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    View view;
    WifiManager wifiManager;  //管理wifi
    Button bt_open_close,bt_scan;

    private static final int REFRESH_CONN = 100;
    // Wifi管理类
    private WifiAdminUtils mWifiAdmin;
    // 扫描结果列表
    private List<ScanResult> list = new ArrayList<ScanResult>();
    // 显示列表
    private MyListView listView;

    private MyListViewAdapter mAdapter;
    //下标
    private int mPosition;

    private WifiReceiver mReceiver;

    private OnNetworkChangeListener mOnNetworkChangeListener = new OnNetworkChangeListener() {

        @Override
        public void onNetWorkDisConnect() {
            getWifiListInfo();
            mAdapter.setDatas(list);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNetWorkConnect() {
            getWifiListInfo();
            mAdapter.setDatas(list);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    };
    public SheZhiSettingWifiFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingWifiFragment newInstance(String param1, String param2) {
//        SheZhiSettingWifiFragment fragment = new SheZhiSettingWifiFragment();
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
            registerReceiver();
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
            unregisterReceiver();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
        unregisterReceiver();
//        onHiddenChanged(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shezhi_setting_wifi, container, false);
        bt_open_close = (Button)view.findViewById(R.id.bt_wifi_open_close);
        bt_scan = (Button) view.findViewById(R.id.bt_wifi_scan);

        //1.WifiManager实例化
        wifiManager= (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
//        //动态权限
//        if (Build.VERSION.SDK_INT >= 23) {
//            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        }

        bt_open_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(v);
            }
        });

        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan(v);
            }
        });

        if (wifiManager.isWifiEnabled()) {
            bt_open_close.setText("关闭");
        }
        else
        {
            bt_open_close.setText("打开");
        }

        initData();
        initView();
        setListener();
        //refreshWifiStatusOnTime();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mWifiAdmin = new WifiAdminUtils(getActivity());
        // 获得Wifi列表信息
        getWifiListInfo();
    }

    /**
     * 初始化View
     */
    private void initView() {
        listView = (MyListView) view.findViewById(R.id.freelook_listview);
        mAdapter = new MyListViewAdapter(getActivity(), list);
        listView.setAdapter(mAdapter);
        //检查当前wifi状态
        int wifiState = mWifiAdmin.checkState();
        //WIFI_STATE_DISABLED  WIFI网卡不可用
        //WIFI_STATE_DISABLING  WIFI网卡正在关闭
        //WIFI_STATE_ENABLED  WIFI网卡状态未知
        if (wifiState == WifiManager.WIFI_STATE_DISABLED
                || wifiState == WifiManager.WIFI_STATE_DISABLING
                || wifiState == WifiManager.WIFI_STATE_UNKNOWN) {

        } else {

        }
    }

    private void registerReceiver() {
        mReceiver = new WifiReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mReceiver, filter);
    }

    private void setListener() {
        // 设置刷新监听
        listView.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        getWifiListInfo();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mAdapter.setDatas(list);
                        mAdapter.notifyDataSetChanged();
                        listView.onRefreshComplete();
                    }

                }.execute();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                mPosition = pos - 1;
                ScanResult scanResult = list.get(mPosition);
                String desc = "";
                String descOri = scanResult.capabilities;
                if (descOri.toUpperCase().contains("WPA-PSK")) {
                    desc = "WPA";
                }
                if (descOri.toUpperCase().contains("WPA2-PSK")) {
                    desc = "WPA2";
                }
                if (descOri.toUpperCase().contains("WPA-PSK")
                        && descOri.toUpperCase().contains("WPA2-PSK")) {
                    desc = "WPA/WPA2";
                }

                if (desc.equals("")) {
                    isConnectSelf(scanResult);
                    return;
                }
                isConnect(scanResult);
            }

            /**
             * 有密码验证连接
             * @param scanResult
             */
            private void isConnect(ScanResult scanResult) {
                if (mWifiAdmin.isConnect(scanResult)) {
                    // 已连接，显示连接状态对话框
                    WifiStatusDialog mStatusDialog = new WifiStatusDialog(
                            getActivity(), R.style.defaultDialogStyle,
                            scanResult, mOnNetworkChangeListener);
                    mStatusDialog.show();
                } else {
                    // 未连接显示连接输入对话框
                    WifiConnDialog mDialog = new WifiConnDialog(
                            getActivity(), R.style.defaultDialogStyle, listView, mPosition, mAdapter,
                            scanResult, list, mOnNetworkChangeListener);
                    mDialog.show();
                }
            }

            /**
             * 无密码直连
             * @param scanResult
             */
            private void isConnectSelf(ScanResult scanResult) {
                if (mWifiAdmin.isConnect(scanResult)) {
                    // 已连接，显示连接状态对话框
                    WifiStatusDialog mStatusDialog = new WifiStatusDialog(
                            getActivity(), R.style.defaultDialogStyle,
                            scanResult, mOnNetworkChangeListener);
                    mStatusDialog.show();
                } else {
                    boolean iswifi = mWifiAdmin.connectSpecificAP(scanResult);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (iswifi) {
                        Toast.makeText(getActivity(), "连接成功",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 得到wifi的列表信息
     */
    private void getWifiListInfo() {
        LogUtils.d("getWifiListInfo");
        mWifiAdmin.startScan();
        List<ScanResult> tmpList = mWifiAdmin.getWifiList();
        if (tmpList == null) {
            list.clear();
        } else {
            list = tmpList;
        }
    }

    private Handler mHandler = new MyHandler(this);

    protected boolean isUpdate = true;

    private static class MyHandler extends Handler {

        private WeakReference<SheZhiSettingWifiFragment> reference;

        public MyHandler(SheZhiSettingWifiFragment activity) {
            this.reference = new WeakReference<SheZhiSettingWifiFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SheZhiSettingWifiFragment activity = reference.get();
            switch (msg.what) {
                case REFRESH_CONN:
                    activity.getWifiListInfo();
                    activity.mAdapter.setDatas(activity.list);
                    activity.mAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }
    }

    /**
     * 定时刷新Wifi列表信息
     *
     * @author Xiho
     */
    private void refreshWifiStatusOnTime() {
        new Thread() {
            public void run() {
                mHandler.sendEmptyMessage(REFRESH_CONN);
                while (isUpdate) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver();
        LogUtils.d(TAG+"onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isUpdate = false;
        unregisterReceiver();
        LogUtils.d("onDestroy()");
    }

    /**
     * 取消广播
     */
    private void unregisterReceiver() {
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private class WifiReceiver extends BroadcastReceiver {
        //记录网络断开的状态
        private boolean isDisConnected = false;
        //记录正在连接的状态
        private boolean isConnecting = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {// wifi连接上与否
                LogUtils.d( "网络已经改变");
                NetworkInfo info = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    if (!isDisConnected) {
                        LogUtils.d( "wifi已经断开");
                        isDisConnected = true;
                    }
                } else if (info.getState().equals(NetworkInfo.State.CONNECTING)) {
                    if (!isConnecting) {
                        LogUtils.d( "正在连接...");
                        isConnecting = true;
                    }
                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    WifiManager wifiManager = (WifiManager) context
                            .getSystemService(WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    LogUtils.d( "连接到网络：" + wifiInfo.getBSSID());
                }

            } else if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR,
                        0);
                switch (error) {

                    case WifiManager.ERROR_AUTHENTICATING:
                        LogUtils.d("密码认证错误Code为：" + error);
//                        Toast.makeText(getActivity().getApplicationContext(), "Wifi密码认证错误", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }

            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
//                LogUtils.e("H3c", "wifiState" + wifiState);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLING:
                        LogUtils.d("wifi正在启用");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        LogUtils.d("Wi-Fi已启用。");
                        break;

                }
            }
        }

    }

    public void open(View v) {
        //!wifiManager.isWifiEnabled()关闭状态
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            bt_open_close.setText("关闭");
        }
        else {
            wifiManager.setWifiEnabled(false);
            bt_open_close.setText("打开");
        }
    }

    public void scan(View v) {
        mHandler.sendEmptyMessage(REFRESH_CONN);
    }

}
