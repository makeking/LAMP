package com.bete.lamp.barfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.adapter.IItem;
import com.bete.lamp.ui.normal.BaseActivity;
import com.bete.lamp.ui.normal.BaseFragment;
import com.utils.LogUtils;


import java.util.LinkedList;
import java.util.List;


public class BarCodeTitleFragment extends BaseFragment {

    private List<String> titleList = new LinkedList<>();
    private List<BaseFragment> fragmentList = new LinkedList<>();
    private BarcodeAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bar_code_title, container, false);
        RecyclerView TitleRecyclerView = (RecyclerView) view.findViewById(R.id.title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        TitleRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter = new BarcodeAdapter(initTiTles());
        adapter.setOnItemClickListener(new BarcodeAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                replaceFragment(fragmentList.get(pos));
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
        return view;
    }

    private List<String> initTiTles() {
//        titleList.add("????????????");
//        fragmentList.add(new BarCodeProjectFragment());
////        titleList.add("????????????");
////        fragmentList.add(new BarCodeStdFragment());
//        titleList.add("?????????");
//        fragmentList.add(new BarCodeQcFragment());
//        titleList.add("?????????0");
//        fragmentList.add(new BarCodeTest0Fragment());
//        titleList.add("?????????");
//        fragmentList.add(new BarCodeTestFragment());

        titleList.add("????????????");
        fragmentList.add(new BarCodeLampProjectFragment());
        titleList.add("????????????");
        fragmentList.add(new BarCodeLampLiuchengFragment());
        titleList.add("????????????");
        fragmentList.add(new BarCodeLampCanshuFragment());
        titleList.add("???????????????");
        fragmentList.add(new BarCodeImageFragment());
        titleList.add("???????????????");
        fragmentList.add(new BarCodeScanFragment());
        return titleList;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.right_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    static class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.ViewHolder> {
        IItem iItem;

        public int getMposition() {
            return mposition;
        }

        public void setMposition(int mposition) {
            this.mposition = mposition;
        }

        private int mposition = -1;
        private List<String> mTitleList;
        private SparseBooleanArray mCheckStates = new SparseBooleanArray();
        private onItemClickListener onItemClickListener;

        interface onItemClickListener {
            void onClick(View view, int pos);

            boolean onLongClick(View view, int pos);
        }

        public void setOnItemClickListener(onItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        //??????????????????????????????activity?????????
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barcode_title, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mRlItem.setTag(position);
            String title = mTitleList.get(position);
            holder.itemname.setText(title);
            mCheckStates.clear();
            if (position == mposition) {
                holder.mRlItem.setBackgroundColor(Color.BLUE);
            } else {
                holder.mRlItem.setBackgroundColor(Color.WHITE);
            }
            //????????????
            holder.mRlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //???????????????????????????
                    mposition = holder.getAdapterPosition();
                    if (mposition < 0)
                        return;
                    iItem.setOnItem(mposition);
                    //???????????? notify ??????Data ??????set??????Changed??????
                    //???????????????notifyDataSetChanged ??????????????????onBindViewHolder ??????????????????
                    notifyDataSetChanged();
                    onItemClickListener.onClick(view, position);
                }
            });
            //????????????
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            LogUtils.d( "onHiddenChanged show");
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
            //????????????
//            ((BarCodeLampProjectFragment)fragmentList.get(0)).onHiddenChanged(true);
//            ((BarCodeLampLiuchengFragment)fragmentList.get(1)).onHiddenChanged(true);
//            ((BarCodeLampCanshuFragment)fragmentList.get(2)).onHiddenChanged(true);
//            ((BarCodeImageFragment)fragmentList.get(3)).onHiddenChanged(true);
//            ((BarCodeScanFragment)fragmentList.get(4)).onHiddenChanged(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
