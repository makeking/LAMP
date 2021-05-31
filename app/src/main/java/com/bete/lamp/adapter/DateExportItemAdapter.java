package com.bete.lamp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.bean.DateExportItem;

import java.util.ArrayList;
import java.util.List;

public class DateExportItemAdapter extends RecyclerView.Adapter<DateExportItemAdapter.ViewHolder> {
    IItem iItem;
    private int mposition=-1;
    private boolean showCheckBox;
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();
    private List<DateExportItem> mDateExporthItemList;
    private onItemClickListener onItemClickListener;
    private List<Integer> checkList = new ArrayList<>();

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    public interface onItemClickListener {
        void onClick(View view, int pos);
        boolean onLongClick(View view, int pos);
    }
    public void setOnItemClickListener(DateExportItemAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    //此方法就是连接接口与activity的桥梁
    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mRlItem;
        TextView itemid;
        TextView itemname;
        private CheckBox mCbItem;
        private RadioButton mRbItem;

        public ViewHolder(View view) {
            super(view);
            mRlItem = (LinearLayout) view.findViewById(R.id.rl_item);
            itemid = (TextView) view.findViewById(R.id.item_id);
            itemname = (TextView) view.findViewById(R.id.item_name);
            mCbItem = (CheckBox) view.findViewById(R.id.cb_item);
            mRbItem = (RadioButton) view.findViewById(R.id.rb_item);
        }
    }

    public DateExportItemAdapter(List<DateExportItem> dateexportitemList) {
        mDateExporthItemList = dateexportitemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dateexport, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.mCbItem.setTag(position);
        holder.mRbItem.setTag(position);
        DateExportItem dateexportitem = mDateExporthItemList.get(position);
        holder.itemid.setText(String.valueOf(position + 1));
        holder.itemname.setText(dateexportitem.getFileName());

        if (position == mposition) {
            holder.mRlItem.setBackgroundColor(Color.parseColor("#FFACEBFF"));
        } else {
            if(position%2==0)
                holder.mRlItem.setBackgroundResource(R.drawable.lamp_log_item_background0);
            else
                holder.mRlItem.setBackgroundResource(R.drawable.lamp_log_item_background1);
        }

        //判断当前checkbox的状态
        if (showCheckBox) {
            holder.mCbItem.setVisibility(View.VISIBLE);
            //防止显示错乱
            holder.mCbItem.setChecked(mCheckStates.get(position, false));

            holder.mRbItem.setVisibility(View.GONE);
            //取消掉Checkbox后不再保存当前选择的状态
            holder.mRbItem.setChecked(false);
        } else {
            holder.mCbItem.setVisibility(View.GONE);
            //取消掉Checkbox后不再保存当前选择的状态
            holder.mCbItem.setChecked(false);

            holder.mRbItem.setVisibility(View.VISIBLE);
            mCheckStates.clear();
        }
        //点击监听
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showCheckBox) {
                    holder.mCbItem.setChecked(!holder.mCbItem.isChecked());
                }
                else
                {
                    //将点击的位置传出去
                    mposition = holder.getAdapterPosition();
                    if (mposition < 0)
                        return;
                    iItem.setOnItem(mposition);
                    //刷新界面 notify 通知Data 数据set设置Changed变化
                    //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                    notifyDataSetChanged();
                }
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

        //对checkbox的监听 保存选择状态 防止checkbox显示错乱
        holder.mCbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int pos = (int) compoundButton.getTag();
                if (b) {
                    mCheckStates.put(pos, true);
                } else {
                    mCheckStates.delete(pos);
                }
            }
        });
        if (!showCheckBox) {
            if (position == mposition) {
                holder.mRbItem.setChecked(true);
            } else {
                holder.mRbItem.setChecked(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDateExporthItemList.size();
    }

    public void setItemIndex(int position)
    {
        if((position<getItemCount())&&(position>=0))
            iItem.setOnItem(position);
    }

    public List<Integer> getCheckList()
    {
        checkList.clear();
        if (showCheckBox) {
            boolean key = false;
            for(int i = 0; i < mDateExporthItemList.size(); i++) {
                if (mCheckStates.get(i, false))
                    checkList.add(i);
            }
        }
        else
        {
            if(mposition!=-1)
                checkList.add(mposition);
        }
        return checkList;
    }

}