package com.bete.lamp.ui.fsetting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
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
import com.bete.lamp.adapter.DateExportItemAdapter;
import com.bete.lamp.adapter.IItem;
import com.bete.lamp.adapter.SpacesItemDecoration;
import com.bete.lamp.bean.DateExportItem;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.myutils.Canstant;
import com.myutils.GlobalDate;
import com.utils.FileCommonUtil;
import com.utils.LogUtils;
import com.utils.OkhttpUploadUtil;
import com.utils.StorageUtil;
import com.utils.SystemTimeSettingUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SheZhiSettingOnlineLogExportFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingOnlineLogExportFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private View view;
    private Context context;
    private Button bt_return;
    private Button bt_export_log;
    private RecyclerView recyclerView;
    private File selectFile = null;
    //?????????????????????????????????????????????
    private boolean mShouldScroll;
    List<String> fileNames = new LinkedList<>();
    //?????????????????????
    private int mToPosition;
    private int current_index = 0;
    private ProgressDialog progressDialog;
    private Thread copyThread;
    DateExportItemAdapter adapter;
    private List<DateExportItem> dateexportItemList = new ArrayList<>();


    private boolean isShowCheck;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.rizhishangchuanwancheng));
                builder.setTitle(getString(R.string.tishi));
                builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            } else if (msg.what == 2) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.rizhishangchuanshibai));
                builder.setTitle(getString(R.string.tishi));
                builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            } else if (msg.what == 3) {
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.rizhijiamishibai));
                builder.setTitle(getString(R.string.tishi));
                builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
        }
    };

    public SheZhiSettingOnlineLogExportFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingOnlineLogExportFragment newInstance(String param1, String param2) {
//        SheZhiSettingOnlineLogExportFragment fragment = new SheZhiSettingOnlineLogExportFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_online_log_export, container, false);
        bt_export_log= (Button) view.findViewById(R.id.bt_export_log);
        bt_export_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goExportSelectHandle();
            }
        });
        initLogExportItems();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_log_online);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        int space = 15;
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScroll = false;
                    smoothMoveToPosition(recyclerView, mToPosition);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                current_index = l.findFirstVisibleItemPosition();
            }
        });
        refreshUI();
        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
        mLinearSnapHelper.attachToRecyclerView(recyclerView);
        initListener();
        return view;
    }

    private void refreshUI() {
        if (adapter == null) {
            adapter = new DateExportItemAdapter(dateexportItemList);
            recyclerView.setAdapter(adapter);
            adapter.setiItem(new IItem() {
                @Override
                public void setOnItem(int position) {
                    selectFile = dateexportItemList.get(position).getFile();
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * ????????????
     */
    private void initListener() {
        //????????????????????????????????????????????????????????????isShowCheck?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????*/
        adapter.setOnItemClickListener(new DateExportItemAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
            }

            @Override
            public boolean onLongClick(View view, int pos) {
                if (isShowCheck) {
                    adapter.setShowCheckBox(false);
                    refreshUI();
                } else {
                    adapter.setShowCheckBox(true);
                    refreshUI();
                }
                isShowCheck = !isShowCheck;
                return false;
            }
        });
    }

    public void goExportSelectHandle() {
        final int[] ret = {0};
        if (!isNetworkAvailable(getActivity())) {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage("?????????????????????");
            builder.setTitle(getString(R.string.tishi));
            builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    return;
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        } else {
            if ((adapter.getCheckList()==null)||(adapter.getCheckList().isEmpty())) {
                CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
                builder1.setMessage("??????????????????");
                builder1.setTitle(getString(R.string.tishi));
                builder1.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return;
                    }
                });
                builder1.create(R.layout.dialog_normal_feiquanping_message).show();
            } else {
//                showProgressDialog();
//                // ??????
//                copyThread = new Thread() {
//                    @Override
//                    public void run() {
//                        for (int i = 0; i < adapter.getCheckList().size(); i++) {
//                            selectFile = dateexportItemList.get(adapter.getCheckList().get(i)).getFile();
//                            File zipfile = selectFile;
//                            if (true) {
//                                OkhttpUploadUtil.getInstance().upload("http://"+ GlobalDate.defaultServerCanShu.getIpaddr()+":"+ GlobalDate.defaultServerCanShu.getPort()+ GlobalDate.defaultServerCanShu.getUpdatedir()+ GlobalDate.deviceno + Canstant.ONLINE_LOG_DIR + zipfile.getName(), zipfile.getAbsolutePath(), new OkhttpUploadUtil.OnUploadListener() {
//                                    @Override
//                                    public void onUploadSuccess() {
//                                    }
//
//                                    @Override
//                                    public void onUploadding(int progress) {
//
//                                    }
//
//                                    @Override
//                                    public void onUploadFailed() {
//                                        Message msg = new Message();
//                                        msg.what = 2;
//                                        handler.sendEmptyMessage(2);
//                                        ret[0] = -1;
//                                        //FileCommonUtil.deleteFile(zipfile);
//                                    }
//                                }, null);
//
//                            } else {
//                                //AESHelper.encryptFile(Canstant.rawKey, tmpfile, targetfile);
//                                //FileCommonUtil.deleteFile(zipfile);
//                            }
//                            if (ret[0] == -1) {
//                                return;
//                            }
//                };
//                copyThread.start();
                showProgressDialog();
                fileNames.clear();
                for (int i = 0; i < adapter.getCheckList().size(); i++) {
                    selectFile = dateexportItemList.get(adapter.getCheckList().get(i)).getFile();
                    fileNames.add(selectFile.getAbsolutePath());
                }
                // ??????
                copyThread = new Thread() {
                    @Override
                    public void run() {
                        if (true) {
                            OkhttpUploadUtil.getInstance().doPostRequest("http://"+ GlobalDate.defaultServerCanShu.getIpaddr()+":"+ GlobalDate.defaultServerCanShu.getPort()+ GlobalDate.defaultServerCanShu.getUpdatedir()+ GlobalDate.deviceno + Canstant.ONLINE_LOG_DIR, fileNames, new Callback() {
                                @Override
                                public void onFailure(Call call, final IOException e) {
                                    LogUtils.d( "error------> " + e.getMessage());
                                    Message msg = new Message();
                                    msg.what = 2;
                                    handler.sendMessage(msg);
                                    ret[0] = -1;
                                    //FileCommonUtil.deleteFile(zipfile);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    LogUtils.d( "success---->" + response.body().string());
                                    Message msg = new Message();
                                    msg.what = 1;
                                    handler.sendMessage(msg);
                                }
                            });
                        } else {
                            //AESHelper.encryptFile(Canstant.rawKey, tmpfile, targetfile);
                            //FileCommonUtil.deleteFile(zipfile);
                        }
                    }
                };
                copyThread.start();
            }
        }
    }

    private void initLogExportItems() {
        List<File> files = FileCommonUtil.listFilesInDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_LOG_DIR, false);
        int id = 1;
        if (files != null && files.size() != 0) {
            for (File file : files) {
                if (!(file.isDirectory()))
                    dateexportItemList.add(new DateExportItem(id++, file));
            }
        } else {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage("?????????????????????");
            builder.setTitle(getString(R.string.tishi));
            builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        }
    }

    /**
     * ?????????????????????
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            LogUtils.d("showProgressDialog: ");
            progressDialog.setMessage("?????????");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    /**
     * ?????????????????????
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * ?????????????????????
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // ?????????????????????
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // ????????????????????????
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // ???????????????:???????????????????????????????????????????????????smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // ???????????????:????????????????????????????????????????????????????????????????????????
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition ??????????????????????????????smoothScrollBy????????????????????????
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // ???????????????:???????????????????????????????????????????????????smoothScrollToPosition??????????????????????????????????????????
            // ?????????onScrollStateChanged??????????????????smoothMoveToPosition????????????????????????????????????
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    public boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // ????????????????????????????????????????????????wi-fi,net?????????????????????
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // ??????NetworkInfo??????
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    LogUtils.i(i + "===??????===" + networkInfo[i].getState());
                    LogUtils.i(i + "===??????===" + networkInfo[i].getTypeName());
                    // ?????????????????????????????????????????????
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
        closeProgressDialog();
    }
}
