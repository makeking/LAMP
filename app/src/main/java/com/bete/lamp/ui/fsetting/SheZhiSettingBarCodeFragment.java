package com.bete.lamp.ui.fsetting;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.adapter.IItem;
import com.bete.lamp.barfragment.BarCodeImageFragment;
import com.bete.lamp.barfragment.BarCodeLampCanshuFragment;
import com.bete.lamp.barfragment.BarCodeLampLiuchengFragment;
import com.bete.lamp.barfragment.BarCodeLampProjectFragment;
import com.bete.lamp.barfragment.BarCodeScanFragment;
import com.bete.lamp.barfragment.BarCodeTitleFragment;
import com.bete.lamp.bean.BarRareData;
import com.bete.lamp.bean.PCRProject;
import com.bete.lamp.ui.normal.BaseFragment;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.utils.LogUtils;
import com.utils.SystemTimeSettingUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SheZhiSettingBarCodeFragment extends BaseFragment {
    private static final String TAG = "SheZhiSettingBarCodeFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    /////////////////////////////////////////////////////////////////////////
    private List<String> titleList = new LinkedList<>();
    private BaseFragment[] fragmentList = new BaseFragment[5];
    private BarcodeAdapter adapter;
    private int currentIndex=0;


    private PCRProject pcrProject = new PCRProject();
    private BarRareData barRareData = new BarRareData();
    public PCRProject getPcrProject() {
        return pcrProject;
    }

    public void setPcrProject(PCRProject pcrProject) {
        this.pcrProject = pcrProject;
    }

    public BarRareData getBarRareData() {
        return barRareData;
    }

    public void setBarRareData(BarRareData barRareData) {
        this.barRareData = barRareData;
    }

    public SheZhiSettingBarCodeFragment() {
        // Required empty public constructor
    }

//    public static SheZhiSettingBarCodeFragment newInstance(String param1, String param2) {
//        SheZhiSettingBarCodeFragment fragment = new SheZhiSettingBarCodeFragment();
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
//            ((BarCodeLampProjectFragment)fragmentList[0]).onHiddenChanged(true);
//            ((BarCodeLampLiuchengFragment)fragmentList[1]).onHiddenChanged(true);
//            ((BarCodeLampCanshuFragment)fragmentList[2]).onHiddenChanged(true);
//            ((BarCodeImageFragment)fragmentList[3]).onHiddenChanged(true);
//            ((BarCodeScanFragment)fragmentList[4]).onHiddenChanged(true);
//            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//            for(int i=0;i<fragmentList.length;i++) {
//                if(fragmentList[i]!=null)
//                ft.remove(fragmentList[i]);
//            }
//            ft.commit();
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
        View view = inflater.inflate(R.layout.fragment_shezhi_setting_bar_code, container, false);
        titleList.clear();
        titleList.add("基本信息");
        titleList.add("流程信息");
        titleList.add("参数信息");
        titleList.add("生成二维码");
        titleList.add("扫描二维码");
        fragmentList[0]=new BarCodeLampProjectFragment();
        //开启事务
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        //设置为默认界面 MainHomeFragment
        ft.add(R.id.right_fragment, fragmentList[0]).commit();
        RecyclerView TitleRecyclerView = (RecyclerView) view.findViewById(R.id.title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        TitleRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter = new BarcodeAdapter(titleList);
        adapter.setOnItemClickListener(new BarcodeAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                LogUtils.d("pos"+pos);
                replaceFragment(pos);
            }

            @Override
            public boolean onLongClick(View view, int pos) {
                return false;
            }
        });
        adapter.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {

            }
        });
        TitleRecyclerView.setAdapter(adapter);
        currentIndex =0;
//        replaceFragment(0);
        return view;
    }

    public void replaceFragment(int index) {
        if (currentIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragmentList[currentIndex]);
        if(fragmentList[index]==null)
        {
            if(index==0)
            fragmentList[0]=new BarCodeLampProjectFragment();
            if(index==1)
            fragmentList[1]=new BarCodeLampLiuchengFragment();
            if(index==2)
            fragmentList[2]=new BarCodeLampCanshuFragment();
            if(index==3)
            fragmentList[3]=new BarCodeImageFragment();
            if(index==4)
            fragmentList[4]=new BarCodeScanFragment();
        }
        if (!fragmentList[index].isAdded()) {
            transaction.add(R.id.right_fragment, fragmentList[index]).show(fragmentList[index]);
        } else {
            //显示新的Fragment
            transaction.show(fragmentList[index]);
        }
//        transaction.replace(R.id.right_fragment, fragmentList[index]);
        transaction.commit();
        currentIndex = index;
    }


    static class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.ViewHolder> {
        IItem iItem;

        public int getMposition() {
            return mposition;
        }

        public void setMposition(int mposition) {
            this.mposition = mposition;
        }

        private int mposition = 0;
        private List<String> mTitleList;
        private SparseBooleanArray mCheckStates = new SparseBooleanArray();
        private BarcodeAdapter.onItemClickListener onItemClickListener;

        interface onItemClickListener {
            void onClick(View view, int pos);

            boolean onLongClick(View view, int pos);
        }

        public void setOnItemClickListener(BarcodeAdapter.onItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        //此方法就是连接接口与activity的桥梁
        public void setiItem(IItem iItem) {
            this.iItem = iItem;
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            LinearLayout mRlItem;
            TextView itemname;

            public ViewHolder(View view) {
                super(view);
                mRlItem = (LinearLayout) view.findViewById(R.id.rl_item);
                itemname = (TextView) view.findViewById(R.id.item_name);
            }
        }

        public BarcodeAdapter(List<String> stringList) {
            mTitleList = stringList;
        }

        @Override
        public BarcodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barcode_title, parent, false);
            BarcodeAdapter.ViewHolder holder = new BarcodeAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final BarcodeAdapter.ViewHolder holder, final int position) {
            holder.mRlItem.setTag(position);
            String title = mTitleList.get(position);
            holder.itemname.setText(title);
            mCheckStates.clear();
            if (position == mposition) {
                holder.mRlItem.setBackgroundColor(Color.BLUE);
            } else {
                holder.mRlItem.setBackgroundColor(Color.WHITE);
            }
            //点击监听
            holder.mRlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //将点击的位置传出去
                    mposition = holder.getAdapterPosition();
                    if (mposition < 0)
                        return;
                    iItem.setOnItem(mposition);
                    //刷新界面 notify 通知Data 数据set设置Changed变化
                    //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                    notifyDataSetChanged();
                    onItemClickListener.onClick(view, position);
                }
            });
            //长按监听
            holder.mRlItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return onItemClickListener.onLongClick(view, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTitleList.size();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("onDestroy()");
    }
}
