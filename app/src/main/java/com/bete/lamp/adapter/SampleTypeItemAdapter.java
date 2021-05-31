package com.bete.lamp.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.bean.SampleItem;

import java.util.ArrayList;
import java.util.List;

public class SampleTypeItemAdapter extends RecyclerView.Adapter<SampleTypeItemAdapter.ViewHolder> {
    IItem iItem;
    private int mposition=-1;
    private boolean showCheckBox;
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();
    private List<SampleItem> msampleItemList;
    private onItemClickListener onItemClickListener;
    private List<Integer> checkList = new ArrayList<>();

    public void setMsampleItemList(List<SampleItem> msampleItemList) {
        this.msampleItemList = msampleItemList;
    }
    public void setMposition(int mposition) {
        this.mposition = mposition;
    }
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
    public void setOnItemClickListener(SampleTypeItemAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    //此方法就是连接接口与activity的桥梁
    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mRlItem;
        TextView itemid;
        TextView itemtype;
        TextView itemnongdu;
        private CheckBox mCbItem;

        public ViewHolder(View view) {
            super(view);
            mRlItem = (LinearLayout) view.findViewById(R.id.rl_item);
            itemid = (TextView) view.findViewById(R.id.item_id);
            itemtype = (TextView) view.findViewById(R.id.item_type);
            itemnongdu = (TextView) view.findViewById(R.id.item_nongdu);
            mCbItem = (CheckBox) view.findViewById(R.id.cb_item);
        }
    }

    public SampleTypeItemAdapter(List<SampleItem> sampleitemList) {
        msampleItemList = sampleitemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sample_type, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mCbItem.setTag(position);
        holder.mRlItem.setTag(position);
        SampleItem sampleItem = msampleItemList.get(position);
        holder.itemid.setText(String.valueOf(position + 1));
        holder.itemtype.setText(getTypeString(sampleItem.getType()));
        if(sampleItem.getType()==1)
            holder.itemnongdu.setText(String.valueOf(sampleItem.getNongdu()));
        else
            holder.itemnongdu.setText("");
        //判断当前checkbox的状态
        if (showCheckBox) {
            //防止显示错乱
            holder.mCbItem.setChecked(mCheckStates.get(position, false));
            if(mCheckStates.get(position, false))
            {
                holder.mCbItem.setVisibility(View.VISIBLE);
                holder.mRlItem.setBackgroundColor(Color.parseColor("#ff00ff"));
            }
            else
            {
                holder.mCbItem.setVisibility(View.VISIBLE);
                holder.mRlItem.setBackgroundColor(Color.WHITE);
            }
        } else {
            holder.mCbItem.setVisibility(View.INVISIBLE);
            //取消掉Checkbox后不再保存当前选择的状态
            holder.mCbItem.setChecked(false);
            mCheckStates.clear();
            if (position == mposition) {
                holder.mRlItem.setBackgroundColor(Color.parseColor("#ff00ff"));

            } else {
                holder.mRlItem.setBackgroundColor(Color.WHITE);
            }
        }
        //点击监听
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showCheckBox) {
                    holder.mCbItem.setChecked(!holder.mCbItem.isChecked());
                    if(holder.mCbItem.isChecked())
                    {
                        holder.mCbItem.setVisibility(View.VISIBLE);
                        holder.mRlItem.setBackgroundColor(Color.parseColor("#ff00ff"));
                    }
                    else
                    {
                        holder.mCbItem.setVisibility(View.INVISIBLE);
                        holder.mRlItem.setBackgroundColor(Color.WHITE);
                    }
                    notifyDataSetChanged();
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
    }

    @Override
    public int getItemCount() {
        return msampleItemList.size();
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
            for(int i = 0; i < msampleItemList.size(); i++) {
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

    public String getTypeString(int x)
    {
        switch(x)
        {
            case 1:
                return "标准";
            case 2:
                return "待测量";
            case 3:
                return "3";
            case 4:
                return "4";
            default:
                return "";
        }
    }

}