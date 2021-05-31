package com.bete.lamp.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.bete.lamp.R;
import com.bete.lamp.bean.CheckBoxItem;

import java.util.LinkedList;
import java.util.List;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.ViewHolder> {
    IItem iItem;
    int mresource;

    //此方法就是连接接口与activity的桥梁
    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }

    public void setmCheckBoxItemList(List<CheckBoxItem> mCheckBoxItemList) {
        this.mCheckBoxItemList = mCheckBoxItemList;
    }

    public List<CheckBoxItem> mCheckBoxItemList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkboxitemname;

        public ViewHolder(View view) {
            super(view);
            checkboxitemname = (CheckBox) view.findViewById(R.id.checkboxitem_name);
        }
    }

    public CheckBoxAdapter(List<CheckBoxItem> checkboxitemList) {
        mCheckBoxItemList = checkboxitemList;
    }

    public CheckBoxAdapter(List<CheckBoxItem> checkboxitemList, int resource) {
        mresource = resource;
        mCheckBoxItemList = checkboxitemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHold = null;
        viewHold = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(mresource, parent, false));
        return viewHold;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setIsRecyclable(false);
        holder.checkboxitemname.setTag(position);
        CheckBoxItem checkBoxItem = mCheckBoxItemList.get(position);
        holder.checkboxitemname.setText(checkBoxItem.getName());
        holder.checkboxitemname.setChecked(checkBoxItem.isCheck);
        holder.checkboxitemname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheckBoxItemList.get(position).isCheck = isChecked;
                iItem.setOnItem(position);
                //刷新界面 notify 通知Data 数据set设置Changed变化
                //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                //notifyDataSetChanged();
            }
        });
        if (!checkBoxItem.isShow) {
            holder.checkboxitemname.setEnabled(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mCheckBoxItemList.size();
    }

    public List<CheckBoxItem> getEditList() {
        return mCheckBoxItemList;
    }

    public Boolean isAllSelect() {
        for (int i = 0; i < mCheckBoxItemList.size(); i++) {
            if (mCheckBoxItemList.get(i).isShow)
                if (!mCheckBoxItemList.get(i).isCheck)
                    return false;
        }
        return true;
    }

    public LinkedList<Integer> checkdlist = new LinkedList<>();

    public LinkedList<Integer> getCheckedList() {
        checkdlist.clear();
        for (int i = 0; i < mCheckBoxItemList.size(); i++) {
            if (mCheckBoxItemList.get(i).isShow)
                if (mCheckBoxItemList.get(i).isCheck)
                    checkdlist.add(i);
        }
        return checkdlist;
    }

    public int getShowItemCount() {
        int count = 0;
        for (int i = 0; i < mCheckBoxItemList.size(); i++) {
            if (mCheckBoxItemList.get(i).isShow)
                count++;
        }
        return count;
    }

}
