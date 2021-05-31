package com.bete.lamp.ui.fragment;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.adapter.IItem;
import com.bete.lamp.adapter.ProjectAdapter;
import com.bete.lamp.bean.Barcode;
import com.bete.lamp.bean.PCRProject;
import com.bete.lamp.bean.ProjectItem;
import com.bete.lamp.customWidget.EditSpinner;
import com.bete.lamp.message.ScanDateValueEvent;
import com.bete.lamp.thread.ScanSerial;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bete.lamp.ui.normal.CustomDialog;
import com.myutils.Canstant;
import com.myutils.GlobalDate;
import com.myutils.SharedPreferencesUtils;
import com.utils.FileCommonUtil;
import com.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.inflate;
import static com.myutils.GlobalDate.DEVICE;

public class SettingProjectStep1Fragment extends BaseFragment {
    private static final String TAG = "SettingProjectStep1Fragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    ///////////////////////////////////////////////////////////////////////////////////
    private Button bt_project_scan_add, bt_project_add, bt_project_del, bt_project_modify;
    private RecyclerView rv_project;
    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    private int selectItem = 0;
    ProjectAdapter projectAdapter;
    LinkedList<ProjectItem> projectitemlist = new LinkedList<>();
    //private String selectFile = "";
    CustomDialog.Builder builder;
    CustomDialog customDialog;

    EditText et_projectname;
    EditText et_lot;
    EditSpinner sp_fenximode;
    String[] stringArray1 = {"定性", "定量"};

    public SettingProjectStep1Fragment() {
        // Required empty public constructor
    }

//    public static SettingProjectStep1Fragment newInstance(String param1, String param2) {
//        SettingProjectStep1Fragment fragment = new SettingProjectStep1Fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_project_step1, container, false);
        builder = new CustomDialog.Builder(getActivity());
        bt_project_scan_add = (Button) view.findViewById(R.id.bt_project_scan_add);
        bt_project_add = (Button) view.findViewById(R.id.bt_project_add);
        bt_project_modify = (Button) view.findViewById(R.id.bt_project_modify);
        bt_project_del = (Button) view.findViewById(R.id.bt_project_del);
        projectAdapter = new ProjectAdapter(projectitemlist);
        rv_project = (RecyclerView) view.findViewById(R.id.rv_project);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rv_project.setLayoutManager(staggeredGridLayoutManager);
        staggeredGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_project.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            }
        });

        rv_project.setAdapter(projectAdapter);
        projectAdapter.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {
                selectItem = position;
            }
        });
        if (projectitemlist.size() > 0) {
            projectAdapter.setPosition(0);
            selectItem = 0;
        }
        ProjectButtonState();

        bt_project_scan_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DEVICE) {
                    ScanSerial.getInstance().cmdScanOpen(1);
                    builder.setMessage("请扫码");
                    builder.setTitle("提示");
                    builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();
                            if (DEVICE) {
                                ScanSerial.getInstance().cmdScanClose();
                            }
                            if (customDialog != null && customDialog.isShowing()) {
                                customDialog.dismiss();
                                customDialog = null;
                            }
                        }
                    });
                    if (customDialog != null) {
                        customDialog.dismiss();
                        customDialog = null;
                    }
                    customDialog = builder.create(R.layout.dialog_saoma_message);
                    customDialog.setCanceledOnTouchOutside(false);
                    customDialog.show();
                } else {
                    builder.setMessage("请扫码");
                    builder.setTitle("提示");
                    builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();
                            if (DEVICE) {
                                ScanSerial.getInstance().cmdScanClose();
                            }
                            if (customDialog != null && customDialog.isShowing()) {
                                customDialog.dismiss();
                                customDialog = null;
                            }
                        }
                    });
                    if (customDialog != null) {
                        customDialog.dismiss();
                        customDialog = null;
                    }
                    customDialog = builder.create(R.layout.dialog_saoma_message);
                    customDialog.setCanceledOnTouchOutside(false);
                    customDialog.show();
//                    CustomDialog.Builder builder1 = new CustomDialog.Builder(getActivity());
//                    builder1.setMessage("非真机不支持打印！");
//                    builder1.setTitle("打印");
//                    builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    CustomDialog tempCustomDialog = builder1.create(R.layout.dialog_normal_feiquanping_message);
//                    tempCustomDialog.setCanceledOnTouchOutside(false);
//                    tempCustomDialog.show();
                }
            }
        });
        bt_project_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("");
                builder.setTitle("");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (customDialog != null && customDialog.isShowing()) {
                            customDialog.dismiss();
                            customDialog = null;
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        List<File> files = FileCommonUtil.listFilesInDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR, false);
                        String tempName = "";
                        String project_lot = "";
                        GlobalDate.ProjectType project_type = GlobalDate.ProjectType.dingxing;
                        String tmpfilestr = "";
                        if ((et_projectname != null) && (!et_projectname.getText().toString().isEmpty())) {
                            tempName = et_projectname.getText().toString();
                            tmpfilestr = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR + tempName;
                        } else {
                            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                tempName = "空白" + i;
                                tmpfilestr = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR + tempName;
                                if (!files.contains(FileCommonUtil.getFileByPath(tmpfilestr))) {
                                    break;
                                }
                            }
                        }
                        if ((et_lot != null) && (!et_lot.getText().toString().isEmpty())) {
                            project_lot = et_lot.getText().toString();
                        }

                        if (sp_fenximode != null) {
                            if (sp_fenximode.getText().toString().equals(stringArray1[0]))
                                project_type = GlobalDate.ProjectType.dingxing;
                            else
                                project_type = GlobalDate.ProjectType.dingliang;
                        }

                        if (FileCommonUtil.isFileExists(tmpfilestr)) {
                            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                            builder.setMessage("项目中已存在相同文件名的配置文件,是否覆盖,返回请取消");
                            builder.setTitle("提示");
                            final String finalTmpfilestr = tmpfilestr;
                            final GlobalDate.ProjectType finalProject_type = project_type;
                            final String finalTempName = tempName;
                            final String finalTempName1 = tempName;
                            final String finalProject_lot = project_lot;
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FileCommonUtil.createOrExistsFile(finalTmpfilestr);
                                    SharedPreferencesUtils.savePCRProject(new PCRProject(finalProject_type, finalTempName, finalTempName1, finalProject_lot), finalTmpfilestr);
                                    String selectFile = finalTempName;
                                    ((SettingProjectFragment) getParentFragment()).setSelectFile(selectFile);
                                    ((SettingProjectFragment) getParentFragment()).changeRadio(1);
                                    dialog.dismiss();
                                    if (customDialog != null && customDialog.isShowing()) {
                                        customDialog.dismiss();
                                        customDialog = null;
                                    }
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_select);
                            tempCustomDialog.setCanceledOnTouchOutside(false);
                            tempCustomDialog.show();
                        } else {
                            FileCommonUtil.createOrExistsFile(tmpfilestr);
                            SharedPreferencesUtils.savePCRProject(new PCRProject(project_type, tempName, tempName, project_lot), tmpfilestr);
                            String selectFile = tempName;
                            ((SettingProjectFragment) getParentFragment()).setSelectFile(selectFile);
                            ((SettingProjectFragment) getParentFragment()).changeRadio(1);
                            if (customDialog != null && customDialog.isShowing()) {
                                customDialog.dismiss();
                                customDialog = null;
                            }
                        }
                    }
                });
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_addproject_message, null);
                customDialog = builder.create(view1);
                et_projectname = (EditText) view1.findViewById(R.id.et_projectname);
                et_lot = (EditText) view1.findViewById(R.id.et_lot);
                sp_fenximode = (EditSpinner) view1.findViewById(R.id.sp_fenximode);
                initSpinner();
                customDialog.setCanceledOnTouchOutside(false);
                customDialog.show();
            }
        });
        bt_project_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainShiYanFragment) getParentFragment()).changeRadio(1);
                goButtonModifyHandle();
            }
        });
        bt_project_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainShiYanFragment) getParentFragment()).changeRadio(1);
                builder.setMessage("请确认是否删除此项目？");
                builder.setTitle("");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (customDialog != null && customDialog.isShowing()) {
                            customDialog.dismiss();
                            customDialog = null;
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        goButtonDelHandle();
                        if (customDialog != null && customDialog.isShowing()) {
                            customDialog.dismiss();
                            customDialog = null;
                        }
                    }
                });
                if (customDialog != null) {
                    customDialog.dismiss();
                    customDialog = null;
                }
                customDialog = builder.create(R.layout.dialog_normal_feiquanping_select);
                customDialog.setCanceledOnTouchOutside(false);
                customDialog.show();
            }
        });
        return view;
    }

    private void initSpinner() {
        sp_fenximode.setDropDownDrawable(getResources().getDrawable(R.drawable.gothrought), 0, 25);
        sp_fenximode.setDropDownBackgroundResource(R.color.white);//R.drawable.custom_editor_bkg_normal//getResources().getDrawable(R.drawable.spin_background)
        sp_fenximode.setDropDownVerticalOffset(0);
        sp_fenximode.setDropDownDrawableSpacing(60);
        sp_fenximode.setDropDownHeight(100);
        sp_fenximode.setEditable(false);
        sp_fenximode.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return stringArray1.length;
            }

            @Override
            public Object getItem(int position) {
                return stringArray1[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflate(getActivity(), R.layout.layout_item, null);
                }

//                ImageView icon = (ImageView)convertView.findViewById(R.id.item_icon);
                TextView textView = (TextView) convertView.findViewById(R.id.item_text);

                String data = (String) getItem(position);

//                icon.setImageResource(R.mipmap.ic_launcher);
                textView.setText(data);

                return convertView;
            }
        });

        // it converts the item in the list to a string shown in EditText.
        sp_fenximode.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {
                String string;
                string = selectedItem.toString();
                return string;
            }
        });

        sp_fenximode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        // triggered when one item in the list is clicked
        sp_fenximode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sp_fenximode.clearFocus();
            }
        });

        sp_fenximode.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
            }
        });

        // select the first item initially
        sp_fenximode.selectItem(0);
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop();");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
        initProjectItems();
        projectAdapter.notifyDataSetChanged();
        if (projectitemlist.size() > selectItem)
            projectAdapter.setPosition(selectItem);
        ProjectButtonState();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            initProjectItems();
            projectAdapter.notifyDataSetChanged();
            if (projectitemlist.size() > selectItem)
                projectAdapter.setPosition(selectItem);
            ProjectButtonState();
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

    private void initProjectItems() {
        List<File> files = FileCommonUtil.listFilesInDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR, false);
        File file;
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o2.getName().compareTo(o1.getName());
            }
        });
        PCRProject tempPcrProject;
        projectitemlist.clear();
        if (files != null && files.size() != 0) {
            for (int i = files.size() - 1; i >= 0; i--) {
                file = files.get(i);
                if ((!file.isDirectory())) {
                    //if(SharedPreferencesUtils.isPCRProject(file.getAbsolutePath())) {
                    tempPcrProject = SharedPreferencesUtils.getPCRProject(file.getAbsolutePath());
                    int name0 = 0;
                    String name1 = file.getName();
                    String name2 = (tempPcrProject.getProject_type() == GlobalDate.ProjectType.dingxing) ? "定性" : "定量";
                    String name3 = tempPcrProject.getProject_lot();
                    String name4 = tempPcrProject.getProject_limit();
                    projectitemlist.add(new ProjectItem(name0, name1, name2, name3, name4));
                    //}
                }
            }
        }
//        if (projectitemlist.size() == 0) {
//            CustomDialog.Builder builder = new CustomDialog.Builder(this);
//            builder.setMessage("未有项目文件！");
//            builder.setTitle("项目管理");
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            builder.create(R.layout.dialog_normal_feiquanping_message).show();
//        }
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

    public void goButtonAddHandle() {
//        List<File> files = FileCommonUtil.listFilesInDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR, false);
//        String tempName = "";
//        String project_lot = "";
//        GlobalDate.ProjectType project_type = GlobalDate.ProjectType.dingxing;
//        String tmpfilestr = "";
//        if((et_projectname!=null)&&(!et_projectname.getText().toString().isEmpty()))
//        {
//            tempName =et_projectname.getText().toString();
//            tmpfilestr = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR + tempName;
//        }
//        else {
//            for (int i = 0; i < Integer.MAX_VALUE; i++) {
//                tempName = "空白" + i;
//                tmpfilestr = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR + tempName;
//                if (!files.contains(FileCommonUtil.getFileByPath(tmpfilestr))) {
//                    break;
//                }
//            }
//        }
//        if((et_lot!=null)&&(!et_lot.getText().toString().isEmpty()))
//        {
//            project_lot = et_lot.getText().toString();
//        }
//
//        if(sp_fenximode!=null)
//        {
//            if(sp_fenximode.getText().toString().equals(stringArray1[0]))
//                project_type = GlobalDate.ProjectType.dingxing;
//            else
//                project_type = GlobalDate.ProjectType.dingliang;
//        }
//
//        if(FileCommonUtil.isFileExists(tmpfilestr))
//        {
//            CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
//            builder.setMessage("项目中已存在相同文件名的配置文件,是否覆盖,返回请取消");
//            builder.setTitle("提示");
//            final String finalTmpfilestr = tmpfilestr;
//            final GlobalDate.ProjectType finalProject_type = project_type;
//            final String finalTempName = tempName;
//            final String finalTempName1 = tempName;
//            final String finalProject_lot = project_lot;
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    FileCommonUtil.createOrExistsFile(finalTmpfilestr);
//                    SharedPreferencesUtils.savePCRProject(new PCRProject(finalProject_type, finalTempName, finalTempName1, finalProject_lot), finalTmpfilestr);
//                    String selectFile = finalTempName;
//                    ((SettingProjectFragment) getParentFragment()).setSelectFile(selectFile);
//                    ((SettingProjectFragment) getParentFragment()).changeRadio(1);
//                    dialog.dismiss();
//                }
//            });
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_select);
//            tempCustomDialog.setCanceledOnTouchOutside(false);
//            tempCustomDialog.show();
//        }
    }

    private void ProjectButtonState() {
        if (projectitemlist.size() > 0) {
            bt_project_del.setEnabled(true);
            bt_project_modify.setEnabled(true);
        } else {
            bt_project_del.setEnabled(false);
            bt_project_modify.setEnabled(false);
        }
    }

    public void goButtonDelHandle() {

        LogUtils.d("del " + "projectPosition:" + selectItem);
        String tempName = projectitemlist.get(selectItem).getName();
        String tmpfilestr = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR + tempName;
        FileCommonUtil.deleteDirOrFile(tmpfilestr);
        projectitemlist.remove(selectItem);
        selectItem = (selectItem - 1 < 0) ? 0 : (selectItem - 1);
        projectAdapter.setPosition(selectItem);
        ProjectButtonState();
        projectAdapter.notifyDataSetChanged();
        LogUtils.d("projectitemlist:" + projectitemlist.size());
    }

    public void goButtonModifyHandle() {
//        Intent intent1 = new Intent(getApplicationContext(), ProjectModifyActivity.class);
//        selectFile = projectitemlist.get(selectItem).getName();
//        intent1.putExtra("selectFile", selectFile);
//        LogUtils.d(selectFile);
//        startActivity(intent1);
        String selectFile = projectitemlist.get(selectItem).getName();
        ((SettingProjectFragment) getParentFragment()).setSelectFile(selectFile);
        ((SettingProjectFragment) getParentFragment()).changeRadio(1);
    }

    PCRProject scanPcrProject;
    String scanFile = "";

    /**
     * 消息接收并显示的方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ScanDateValueEvent scanDateValueEvent) {
        ScanSerial.getInstance().cmdScanClose();
        if (scanDateValueEvent.getType() == 1) {
            scanPcrProject = Barcode.GetReagentDetailBarCode(scanDateValueEvent.getScandataBytes());
            if (scanPcrProject != null) {
                if (customDialog != null) {
                    customDialog.dismiss();
                    customDialog = null;
                }
                scanFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR + scanPcrProject.project_name;
                List<File> files = FileCommonUtil.listFilesInDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_PROJECT_DIR);
                if (files.contains(FileCommonUtil.getFileByPath(scanFile))) {
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    builder.setMessage("项目中已存在相同文件名的配置文件,是否覆盖,返回请取消");
                    builder.setTitle("项目管理");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferencesUtils.savePCRProject(scanPcrProject, scanFile);
                            initProjectItems();
                            projectAdapter.notifyDataSetChanged();
                            if (projectitemlist.size() > selectItem)
                                projectAdapter.setPosition(selectItem);
                            ProjectButtonState();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_select);
                    tempCustomDialog.setCanceledOnTouchOutside(false);
                    tempCustomDialog.show();
                    return;
                } else {
                    FileCommonUtil.createOrExistsFile(scanFile);
                    SharedPreferencesUtils.savePCRProject(scanPcrProject, scanFile);
                    initProjectItems();
                    projectAdapter.notifyDataSetChanged();
                    if (projectitemlist.size() > selectItem)
                        projectAdapter.setPosition(selectItem);
                    ProjectButtonState();
                    CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                    builder.setMessage("添加成功");
                    builder.setTitle("项目管理");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_message);
                    tempCustomDialog.setCanceledOnTouchOutside(false);
                    tempCustomDialog.show();
                    return;
                }
            } else {
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("二维码不符合要求");
                builder.setTitle("项目管理");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                CustomDialog tempCustomDialog = builder.create(R.layout.dialog_normal_feiquanping_message);
                tempCustomDialog.setCanceledOnTouchOutside(false);
                tempCustomDialog.show();
                return;
            }
        }
    }

}
