package com.bete.lamp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.bean.TimeDateExportItem;
import com.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class PCRTimeDateExportItemAdapter extends RecyclerView.Adapter<PCRTimeDateExportItemAdapter.ViewHolder> {
    IItem iItem;
    private LayoutInflater mInflater;
    private int mposition = -1;
    private boolean showCheckBox;
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();
    private List<TimeDateExportItem> mTimeDateExporthItemList;
    private onItemClickListener onItemClickListener;
    private List<Integer> checkList = new ArrayList<>();
    private boolean isFileVisiAble = false;

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    public void setFileVisiAble(boolean fileVisiAble) {
        isFileVisiAble = fileVisiAble;
    }

    public interface onItemClickListener {
        void onClick(View view, int pos);

        boolean onLongClick(View view, int pos);
    }

    public void setOnItemClickListener(PCRTimeDateExportItemAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    //此方法就是连接接口与activity的桥梁
    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mRlItem;
        TextView itemid;
        TextView itemtime;
        TextView itemname;

        public ViewHolder(View view) {
            super(view);
            mRlItem = (LinearLayout) view.findViewById(R.id.rl_item);
            itemid = (TextView) view.findViewById(R.id.item_id);
            itemtime = (TextView) view.findViewById(R.id.item_time);
            itemname = (TextView) view.findViewById(R.id.item_name);
        }
    }

    public PCRTimeDateExportItemAdapter(Context context, List<TimeDateExportItem> timedateexportitemList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTimeDateExporthItemList = timedateexportitemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHold = null;
        viewHold = new ViewHolder(mInflater.inflate(R.layout.item_pcrtimedateexport_0, parent, false));
        return viewHold;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        TimeDateExportItem timedateexportitem = mTimeDateExporthItemList.get(position);
        holder.itemid.setText(String.valueOf(position + 1));
        holder.itemtime.setText(timedateexportitem.getCheckTime());
        holder.itemname.setText(timedateexportitem.getFileName());

        if (!isFileVisiAble)
            holder.itemname.setVisibility(View.GONE);
        else
            holder.itemname.setVisibility(View.VISIBLE);
        mCheckStates.clear();
        if (position == mposition) {
            holder.mRlItem.setBackgroundColor(Color.parseColor("#FFACEBFF"));
        } else {
            if (position % 2 == 0)
                holder.mRlItem.setBackgroundResource(R.drawable.lamp_chaxun1_item0);
            else
                holder.mRlItem.setBackgroundResource(R.drawable.lamp_chaxun1_item1);
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
                onItemClickListener.onClick(view, position);
                //刷新界面 notify 通知Data 数据set设置Changed变化
                //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                notifyDataSetChanged();

            }
        });
        //长按监听
        holder.mRlItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mposition = holder.getAdapterPosition();
                if (mposition < 0)
                    return false;
                notifyDataSetChanged();
                return onItemClickListener.onLongClick(view, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mTimeDateExporthItemList.size();
    }

    public void setItemIndex(int position) {
        if ((position < getItemCount()) && (position >= 0))
            iItem.setOnItem(position);
    }

    public List<Integer> getCheckList() {
        checkList.clear();
        if (mposition != -1)
            checkList.add(mposition);
        return checkList;
    }

}