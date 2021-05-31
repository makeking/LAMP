package com.bete.lamp.ui.fsetting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

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
import com.utils.AESHelper;
import com.utils.FileCommonUtil;
import com.utils.LogUtils;
import com.utils.StorageUtil;
import com.utils.SystemTimeSettingUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.utils.StorageUtil.isUpanExist;

public class SheZhiSettingUsbLogExportFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingUsbLogExportFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private Button bt_export_log;
    private RecyclerView recyclerView;
    private File selectFile = null;
    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    private int current_index=0;
    private ProgressDialog progressDialog;
    private Thread copyThread;
    DateExportItemAdapter adapter;
    private List<DateExportItem> dateexportItemList = new ArrayList<>();


    private boolean isShowCheck;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("日志拷贝完成！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            else if(msg.what==2){
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("日志拷贝失败！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            else if(msg.what==3){
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("日志加密失败！");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
        }
    };
    public SheZhiSettingUsbLogExportFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingUsbLogExportFragment newInstance(String param1, String param2) {
//        SheZhiSettingUsbLogExportFragment fragment = new SheZhiSettingUsbLogExportFragment();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_usb_log_export, container, false);
        bt_export_log = (Button) view.findViewById(R.id.bt_export_log);
        bt_export_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goExportSelectHandle();
            }
        });
        initLogExportItems();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_log_usb);
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
            public void onScrolled(RecyclerView recyclerView,int dx,int dy){
                super.onScrolled(recyclerView,dx,dy);
                LinearLayoutManager l = (LinearLayoutManager)recyclerView.getLayoutManager();
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
     * 点击监听
     */
    private void initListener() {
        //ａｄａｐｔｅｒ中定义的监听事件　可以根据isShowCheck判断当前状态，设置点击Ｉｔｅｍ之后是查看大图（未实现　跳到下一个Ａｃｔｉｖｉｔｙ即可）还是选中ｃｈｅｃｋｂｏｘ*/
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

    public void goExportSelectHandle(){
        Toast.makeText(getActivity(), adapter.getCheckList().toString(), Toast.LENGTH_SHORT).show();
        if(!isUpanExist(getActivity()))
        {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage("未检测到设备U盘！");
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    return;
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        }else {
            if ((adapter.getCheckList()==null)||(adapter.getCheckList().isEmpty())) {
                CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
                builder1.setMessage("未选中文件！");
                builder1.setTitle("提示");
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return;
                    }
                });
                builder1.create(R.layout.dialog_normal_feiquanping_message).show();
            } else {
                showProgressDialog();
                // 打印
                copyThread = new Thread() {
                    @Override
                    public void run() {
                        for (int i = 0; i < adapter.getCheckList().size(); i++) {
                            selectFile = dateexportItemList.get(adapter.getCheckList().get(i)).getFile();
                            String filename = selectFile.getName();
                            String targetfile = StorageUtil.UPANROOT + "/" + Canstant.UPAN_DAOCHU_LOG_DIR + "/" + filename;
                            if (true) {
                                try {
                                    FileCommonUtil.copy(selectFile.getAbsolutePath(), targetfile, false);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Message msg = new Message();
                                    msg.what = 2;
                                    handler.sendMessage(msg);
                                    return;
                                }
                            } else {
                                AESHelper.encryptFile(Canstant.rawKey, selectFile.getAbsolutePath(), targetfile);
                            }

                            if (!FileCommonUtil.isFileExists(targetfile)) {
                                Message msg = new Message();
                                msg.what = 2;
                                handler.sendMessage(msg);
                                return;
                            }
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                };
                copyThread.start();
            }
        }
    }

    private void initLogExportItems(){
        List<File> files= FileCommonUtil.listFilesInDir(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + Canstant.INNER_LOG_DIR,false);
        int id=1;
        if (files != null && files.size() != 0) {
            for (File file : files) {
                if (!(file.isDirectory()))
                    dateexportItemList.add(new DateExportItem(id++, file));
            }
        }
        else
        {
            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
            builder.setMessage("未有日志文件！");
            builder.setTitle("提示");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create(R.layout.dialog_normal_feiquanping_message).show();
        }
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            LogUtils.d( "showProgressDialog: ");
            progressDialog.setMessage("拷贝中");
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

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeProgressDialog();
        LogUtils.d("onDestroy()");
    }
}
