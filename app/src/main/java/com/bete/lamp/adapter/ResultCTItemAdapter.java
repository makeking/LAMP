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
import com.bete.lamp.bean.ResultItem;
import com.myutils.GlobalDate;

import java.util.ArrayList;
import java.util.List;

import static com.myutils.GlobalDate.BLOCKSIZE;

public class ResultCTItemAdapter extends RecyclerView.Adapter<ResultCTItemAdapter.ViewHolder> {
    IItem iItem;
    private int mposition=-1;
    private boolean showCheckBox;
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();
    private List<ResultItem> mresultItemList;
    private onItemClickListener onItemClickListener;
    private boolean ctVisiable=true;
    private boolean nongduVisiable=false;
    private boolean yinyangVisiable=false;
    private boolean weizhiVisiable=true;

    public void setColorEnable(boolean colorEnable) {
        this.colorEnable = colorEnable;
    }

    private boolean colorEnable = false;
    private List<Integer> checkList = new ArrayList<>();

    public void setWeizhiVisiable(boolean weizhiVisiable) {
        this.weizhiVisiable = weizhiVisiable;
    }

    public void setMposition(int mposition) {
        this.mposition = mposition;
    }

    public void setCtVisiable(boolean ctVisiable) {
        this.ctVisiable = ctVisiable;
    }

    public void setNongduVisiable(boolean nongduVisiable) {
        this.nongduVisiable = nongduVisiable;
    }

    public void setYinyangVisiable(boolean yinyangVisiable) {
        this.yinyangVisiable = yinyangVisiable;
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
    public void setOnItemClickListener(ResultCTItemAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    //此方法就是连接接口与activity的桥梁
    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mRlItem;
        TextView itemid;
        TextView itemweizhi;
        TextView itemtype;
        TextView itemct;
        TextView itemnongdu;
        TextView itemyinyang;
        private CheckBox mCbItem;

        public ViewHolder(View view) {
            super(view);
            mRlItem = (LinearLayout) view.findViewById(R.id.rl_item);
            itemid = (TextView) view.findViewById(R.id.item_id);
            itemweizhi = (TextView) view.findViewById(R.id.item_weizhi);
            itemtype = (TextView) view.findViewById(R.id.item_type);
            itemct = (TextView) view.findViewById(R.id.item_ct);
            itemnongdu = (TextView) view.findViewById(R.id.item_nongdu);
            itemyinyang = (TextView) view.findViewById(R.id.item_yinyang);
            mCbItem = (CheckBox) view.findViewById(R.id.cb_item);
        }
    }

    public ResultCTItemAdapter(List<ResultItem> resultitemList) {
        mresultItemList = resultitemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reslut_ct, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mCbItem.setTag(position);
        holder.mRlItem.setTag(position);
        ResultItem resultitem = mresultItemList.get(position);
        holder.itemid.setText(String.valueOf(position + 1));
        holder.itemweizhi.setText(getWeiZhi(resultitem.weizhi));
        holder.itemtype.setText(String.valueOf(resultitem.getType()));
        if(resultitem.ct>0)
            holder.itemct.setText(String.format("%.2f",resultitem.ct));// valueOf(resultitem.ct)
        else
            holder.itemct.setText("err");
        if(resultitem.nongdu>0)
            holder.itemnongdu.setText(String.valueOf(resultitem.nongdu));
        else
            holder.itemnongdu.setText("err");
        if(resultitem.yiyang==0)
            holder.itemyinyang.setText("ok");
        else if(resultitem.yiyang==1)
            holder.itemyinyang.setText("low");
        else
            holder.itemyinyang.setText("high");

        holder.itemtype.setVisibility(View.GONE);
        if(!weizhiVisiable) {
            holder.itemweizhi.setVisibility(View.GONE);
            holder.itemweizhi.setWidth(0);
        }
        if(!yinyangVisiable) {
            holder.itemyinyang.setVisibility(View.GONE);
            holder.itemyinyang.setWidth(0);
        }
        if(!ctVisiable) {
            holder.itemct.setVisibility(View.GONE);
            holder.itemct.setWidth(0);
        }
        if(!nongduVisiable) {
            holder.itemnongdu.setVisibility(View.GONE);
            holder.itemnongdu.setWidth(0);
        }
        //判断当前checkbox的状态
        if (showCheckBox) {
            //防止显示错乱
            holder.mCbItem.setChecked(mCheckStates.get(position, false));
            if(mCheckStates.get(position, false))
            {
                holder.mCbItem.setVisibility(View.VISIBLE);
                holder.mRlItem.setBackgroundColor(Color.BLUE);
            }
            else
            {
                holder.mCbItem.setVisibility(View.VISIBLE);
                if(colorEnable)
                {
                    if(resultitem.type==1)
                    {
                        holder.mRlItem.setBackgroundColor(Color.RED);
                    }
                    else
                    {
                        holder.mRlItem.setBackgroundColor(Color.GREEN);
                    }

                }
                else
                    holder.mRlItem.setBackgroundColor(Color.WHITE);
            }
        } else {
            holder.mCbItem.setVisibility(View.GONE);
            //取消掉Checkbox后不再保存当前选择的状态
            holder.mCbItem.setChecked(false);
            mCheckStates.clear();
            if (position == mposition) {
                holder.mRlItem.setBackgroundColor(Color.BLUE);

            } else {
                if(colorEnable)
                {
                    if(resultitem.type==1)
                    {
                        holder.mRlItem.setBackgroundColor(Color.RED);
                    }
                    else
                    {
                        holder.mRlItem.setBackgroundColor(Color.GREEN);
                    }

                }
                else
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
                        holder.mRlItem.setBackgroundColor(Color.BLUE);
                    }
                    else
                    {
                        holder.mCbItem.setVisibility(View.GONE);
                        holder.mRlItem.setBackgroundColor(Color.WHITE);
                    }
                    //notifyDataSetChanged();
                    notifyItemChanged(mposition);
                }
                else
                {
                    //将点击的位置传出去
                    notifyItemChanged(mposition);
                    mposition = holder.getAdapterPosition();
                    if (mposition < 0)
                        return;
                    iItem.setOnItem(mposition);
                    //刷新界面 notify 通知Data 数据set设置Changed变化
                    //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                    //notifyDataSetChanged();
                    notifyItemChanged(mposition);
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
        return mresultItemList.size();
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
            for(int i = 0; i < mresultItemList.size(); i++) {
                if (mCheckStates.get(i, false))
                    checkList.add((int) mresultItemList.get(i).weizhi);
            }
        }
        else
        {
            if((mposition!=-1)&&(mposition<mresultItemList.size()))
                checkList.add((int) mresultItemList.get(mposition).weizhi);
        }
        return checkList;
    }

    public String getWeiZhi(float weizhi)
    {
        int guang,block,num;char A = 'A';
        if(weizhi==0)
        {
            return "unknow";
        }
        weizhi=weizhi-1;
        guang= (int) (weizhi/(GlobalDate.BLOCKNUM* BLOCKSIZE));
        block= (int) ((weizhi%(GlobalDate.BLOCKNUM* BLOCKSIZE))/BLOCKSIZE);
        num = (int) ((weizhi%BLOCKSIZE));
        return String.valueOf((char)(A+block))+String.valueOf(num+1)+"_"+String.valueOf(guang+1);
    }

}