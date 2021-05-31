package com.bete.lamp.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bete.lamp.R;
import com.bete.lamp.adapter.IItem;
import com.bete.lamp.adapter.PCRTimeDateExportItemAdapter;
import com.bete.lamp.bean.TimeDateExportItem;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.myutils.Canstant;
import com.myutils.GlobalDate;
import com.utils.FileCommonUtil;
import com.utils.LogUtils;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.myutils.GlobalDate.g_chaxun_checkTimeStr;
import static com.myutils.GlobalDate.g_chaxun_selectDir;

public class ChaXunStep1Fragment extends BaseFragment {
    private static final String TAG = "ChaXunStep1Fragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    ///////////////////////////////////////////////////////////////////////////////////
    private EditText et_chaxun_time,et_chaxun_sampleno, et_chaxun_file;
    private Button bt_chaxun_step1_chaxun1, bt_chaxun_step1_chaxun2,bt_chaxun_step1_chaxun3;
    private RecyclerView rv_chaxun_list;
    private List<TimeDateExportItem> timedateexportItemList = new ArrayList<>();
    PCRTimeDateExportItemAdapter adapter;
    private File selectDir = null;
    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    private int current_index = 0;

    private ProgressDialog progressDialog;
    private Thread listThread;

    private TimePickerView before_pvCustomTime;
    SimpleDateFormat dateFormatterStart;
    String str_starttime;
    Calendar calendar_before;
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

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                showProgressDialog("请稍等…");
            }
            if (msg.what == 2) {
                refreshUI();
                closeProgressDialog();
            }
            if (msg.what == 3) {
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("数据文件已删除");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
            if (msg.what == 4) {
                refreshUI();
                closeProgressDialog();
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("未有数据文件");
                builder.setTitle("查询");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create(R.layout.dialog_normal_feiquanping_message).show();
            }
        }
    };


    public ChaXunStep1Fragment() {
        // Required empty public constructor
    }

//    public static ChaXunStep1Fragment newInstance(String param1, String param2) {
//        ChaXunStep1Fragment fragment = new ChaXunStep1Fragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        calendar_before = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cha_xun_step1, container, false);
        et_chaxun_time = (EditText) view.findViewById(R.id.et_chaxun_time);
        et_chaxun_sampleno = (EditText) view.findViewById(R.id.et_chaxun_sampleno);
        et_chaxun_file = (EditText) view.findViewById(R.id.et_chaxun_file);
        rv_chaxun_list = (RecyclerView) view.findViewById(R.id.rv_chaxun_list);
        bt_chaxun_step1_chaxun1 = (Button) view.findViewById(R.id.bt_chaxun_step1_chaxun1);
        bt_chaxun_step1_chaxun2 = (Button) view.findViewById(R.id.bt_chaxun_step1_chaxun2);
        bt_chaxun_step1_chaxun3 = (Button) view.findViewById(R.id.bt_chaxun_step1_chaxun3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_chaxun_list.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        int space = 15;
//        rv_chaxun_list.addItemDecoration(new SpacesItemDecoration(space));
        rv_chaxun_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
//        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
//        mLinearSnapHelper.attachToRecyclerView(rv_chaxun_list);
        adapter.setShowCheckBox(false);
        adapter.setOnItemClickListener(new PCRTimeDateExportItemAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
            }

            @Override
            public boolean onLongClick(View view, int pos) {
                return false;
            }

        });
        Calendar calendar = Calendar.getInstance();
        dateFormatterStart = new SimpleDateFormat("yyyy-MM-dd");
        str_starttime = dateFormatterStart.format(calendar.getTime());//获取当前时间
        et_chaxun_time.setText(str_starttime);
        et_chaxun_time.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        et_chaxun_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });

        et_chaxun_file.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showFilePickDlg();
                    return true;
                }
                return false;
            }
        });

        et_chaxun_file.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showFilePickDlg();
                }
            }
        });

        bt_chaxun_step1_chaxun1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_starttime = et_chaxun_time.getText().toString();
                GlobalDate.strstart = str_starttime + "-00-00-00";
                GlobalDate.strend = str_starttime + "-23-59-59";
                GlobalDate.searchtype = 1;
                initDateExportItems();
                //refreshUI();
            }
        });
        bt_chaxun_step1_chaxun2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_id = et_chaxun_sampleno.getText().toString();
                GlobalDate.id = str_id;
                GlobalDate.searchtype = 2;
                initDateExportItems();
                //refreshUI();
            }
        });

        bt_chaxun_step1_chaxun3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_id = et_chaxun_file.getText().toString();
                if (!FileCommonUtil.isFileExists(str_id)) {
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                } else {
                    g_chaxun_selectDir = FileCommonUtil.getFileByPath(str_id).getParentFile().getName();
                    g_chaxun_checkTimeStr = FileCommonUtil.getFileByPath(str_id).getParentFile().getName();
//                    g_is_chaxun_file = true;
                    String absolutePath = str_id;
//                    g_have_chaxun = true;
                    ((CXChaXunFragment) getParentFragment()).changeRadio(1);
                }
            }
        });

        return view;
    }

    private void initDateExportItems() {
        listThread = new Thread() {
            @Override
            public void run() {
                int no = 1;
                Cursor cursor = null;
                timedateexportItemList.clear();
                if (GlobalDate.searchtype == 1) {
                    String starttime = GlobalDate.strstart;
                    String endtime = GlobalDate.strend;
                    LogUtils.w("longstart" + Long.toString(GlobalDate.longstart) + ":" + "longend" + Long.toString(GlobalDate.longend));
                    LogUtils.w("starttime" + starttime + ":" + "endtime" + endtime);
                    cursor = LitePal.findBySQL("select * from CheckDirItem where checkdatetime >= ? and checkdatetime < ? group by checkdatetime order by checkdatetime desc", starttime, endtime);
                } else if (GlobalDate.searchtype == 2) {
                    cursor = LitePal.findBySQL("select * from CheckDirItem where  samplenumber = ? group by checkdatetime order by checkdatetime desc", GlobalDate.id);
                }

                if ((cursor != null) && (cursor.moveToFirst())) {
                    LogUtils.d("cursor.getCount()" + cursor.getCount());
                    if (cursor.getCount() > 1000) {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                    // 循环遍历cursor 
                    do {
                        // 拿到每一行name 与hp的数值
                        String name = cursor.getString(cursor.getColumnIndex("samplenumber"));
                        String hp = cursor.getString(cursor.getColumnIndex("checkdatetime"));
                        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_DATA_DIR + cursor.getString(cursor.getColumnIndex("dir"));
                        timedateexportItemList.add(new TimeDateExportItem(no, hp, dir));
                    } while ( cursor.moveToNext() );
                    // 关闭
                    cursor.close();
                }
                if (timedateexportItemList.size() == 0) {
                    Message msg1 = new Message();
                    msg1.what = 4;
                    handler.sendMessage(msg1);
                } else {
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        };
        listThread.start();
    }

    private void refreshUI() {
        if (adapter == null) {
            adapter = new PCRTimeDateExportItemAdapter(getContext(),timedateexportItemList);
            adapter.setFileVisiAble(false);
            rv_chaxun_list.setAdapter(adapter);
            adapter.setiItem(new IItem() {
                @Override
                public void setOnItem(int position) {
                    GlobalDate.g_chaxun_selectDir = timedateexportItemList.get(position).getFile().getName();
                    GlobalDate.g_chaxun_checkTimeStr = timedateexportItemList.get(position).getCheckTime();
                    String absolutePath = timedateexportItemList.get(position).getFile().getAbsolutePath();
                    if (!FileCommonUtil.isFileExists(absolutePath)) {
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    } else {
                        ((CXChaXunFragment) getParentFragment()).changeRadio(1);
                    }
                }
            });
        } else {
            adapter.notifyDataSetChanged();
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

    protected void showDatePickDlg() {
//        Calendar calendar = Calendar.getInstance();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year,monthOfYear,dayOfMonth);
//                SimpleDateFormat dateFormatterStart = new SimpleDateFormat("yyyy-MM-dd");
//                String str_starttime = dateFormatterStart.format(calendar.getTime());//获取当前时间
//                et_chaxun_time.setText(str_starttime);
//            }
//        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.getDatePicker().setCalendarViewShown(false);
//        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
//        datePickerDialog.show();

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        if(startDate.compareTo(endDate)>=0)
        {
            startDate.set(endDate.get(Calendar.YEAR)-1,endDate.get(Calendar.MONTH),endDate.get(Calendar.DAY_OF_MONTH));
        }
        endDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        //时间选择器
        before_pvCustomTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Calendar calendar = Calendar.getInstance();
                calendar_before.setTime(date);
                str_starttime = dateFormatterStart.format(calendar_before.getTime());//获取当前时间
                et_chaxun_time.setText(str_starttime);
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                before_pvCustomTime.returnData();
                                before_pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                before_pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();
        before_pvCustomTime.show();
    }

    private int REQUESTCODE_FROM_FRAGMENT = 1001;
    protected void showFilePickDlg() {
        String dir;
        String path = et_chaxun_file.getText().toString();
        if (FileCommonUtil.isFileExists(path)) {
            dir = FileCommonUtil.getDirName(path);
        } else {
            dir = "/sdcard/LAMP/data/";
        }
        new LFilePicker()
                .withSupportFragment(ChaXunStep1Fragment.this)
                .withRequestCode(REQUESTCODE_FROM_FRAGMENT)
                //.withStartPath("/storage/emulated/0/Download")
                .withStartPath(path)
                //.withFileFilter(new String[]{""})
                .withIsGreater(false)
                .withChooseMode(true)
                .withMutilyMode(false)
                .withFileSize(500 * 1024)
                .start();
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
                    String selectPath = list.get(0);
                    et_chaxun_file.setText(selectPath);
//                    bitmap_createqr = BitmapFactory.decodeFile(savePath);
//                    img_createqr.setImageBitmap(bitmap_createqr);
                    LogUtils.d(selectPath);
                } else {
                    Toast.makeText(getActivity(), "未选中文件", Toast.LENGTH_SHORT);
                }
            }
        }
    }

    public void onButtonPressed(Uri uri) {
    }

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
    }
}
