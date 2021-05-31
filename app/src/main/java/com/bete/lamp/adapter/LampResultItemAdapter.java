package com.bete.lamp.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.bean.LampResultItem;
import com.myutils.GlobalDate;

import java.util.ArrayList;
import java.util.List;

import static com.myutils.GlobalDate.BLOCKSIZE;

public class LampResultItemAdapter extends RecyclerView.Adapter<LampResultItemAdapter.ViewHolder> {
    IItem iItem;
    private int mposition = -1;
    private List<LampResultItem> mLampResultItems;
    private onItemClickListener onItemClickListener;
    private boolean idVisiable = true;
    private boolean ctVisiable = true;
    private boolean nongduVisiable = false;
    private boolean weizhiVisiable = true;

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

    public void setIdVisiable(boolean idVisiable) {
        this.idVisiable = idVisiable;
    }

    public void setCtVisiable(boolean ctVisiable) {
        this.ctVisiable = ctVisiable;
    }

    public void setNongduVisiable(boolean nongduVisiable) {
        this.nongduVisiable = nongduVisiable;
    }


    public interface onItemClickListener {
        void onClick(View view, int pos);

        boolean onLongClick(View view, int pos);
    }

    public void setOnItemClickListener(LampResultItemAdapter.onItemClickListener onItemClickListener) {
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
        TextView itembianhao;
        TextView itemprojectname;
        TextView itemct;
        TextView itemnongdu;

        public ViewHolder(View view) {
            super(view);
            mRlItem = (LinearLayout) view.findViewById(R.id.rl_item);
            itemid = (TextView) view.findViewById(R.id.item_id);
            itemweizhi = (TextView) view.findViewById(R.id.item_weizhi);
            itembianhao = (TextView) view.findViewById(R.id.item_bianhao);
            itemprojectname = (TextView) view.findViewById(R.id.item_projectname);
            itemct = (TextView) view.findViewById(R.id.item_ct);
            itemnongdu = (TextView) view.findViewById(R.id.item_nongdu);
        }
    }

    public LampResultItemAdapter(List<LampResultItem> resultitemList) {
        mLampResultItems = resultitemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lamp_reslut, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mRlItem.setTag(position);
        LampResultItem lampResultItem = mLampResultItems.get(position);
        holder.itemid.setText(String.valueOf(position + 1));
        holder.itemweizhi.setText(getWeiZhi(lampResultItem.weizhi));
        holder.itembianhao.setText(lampResultItem.bianhao);
        holder.itemprojectname.setText(lampResultItem.projectname);
        if (lampResultItem.ct == 0x0ffff)
            holder.itemct.setText("NoTt");
        else
            holder.itemct.setText(String.format("%.2f", lampResultItem.ct));

        holder.itemnongdu.setText(String.valueOf(lampResultItem.jieguo));

        if (!weizhiVisiable) {
            holder.itemweizhi.setVisibility(View.GONE);
            holder.itemweizhi.setWidth(0);
        }
        if (!ctVisiable) {
            holder.itemct.setVisibility(View.GONE);
            holder.itemct.setWidth(0);
        }
        if (!nongduVisiable) {
            holder.itemnongdu.setVisibility(View.GONE);
            holder.itemnongdu.setWidth(0);
        }

        if (!idVisiable) {
            holder.itemid.setVisibility(View.GONE);
            holder.itemid.setWidth(0);
        }
        //判断当前checkbox的状态

        if (position == mposition) {
            holder.mRlItem.setBackgroundColor(Color.parseColor("#FFACEBFF"));
        } else {
            if(position%2==0)
                holder.mRlItem.setBackgroundResource(R.drawable.lamp_result_item_0);
            else
                holder.mRlItem.setBackgroundResource(R.drawable.lamp_result_item_1);
        }
        //点击监听
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        return mLampResultItems.size();
    }

    public void setItemIndex(int position) {
        if ((position < getItemCount()) && (position >= 0))
            iItem.setOnItem(position);
    }

    public List<Integer> getCheckList() {
        checkList.clear();
            if ((mposition != -1) && (mposition < mLampResultItems.size()))
                checkList.add((int) mLampResultItems.get(mposition).weizhi);
        return checkList;
    }

    public String getWeiZhi(float weizhi) {
        int guang, block, num;
        char A = 'A';
        if (weizhi == 0) {
            return "unknow";
        }
        weizhi = weizhi - 1;
        block = (int) (weizhi / (GlobalDate.GUANGNUM * BLOCKSIZE));
        guang = (int) ((weizhi % (GlobalDate.GUANGNUM * BLOCKSIZE)) / BLOCKSIZE);
        num = (int) ((weizhi % BLOCKSIZE));
//        return String.valueOf((char) (A + block)) + String.valueOf(num + 1) + "_" + String.valueOf(guang + 1);
        return String.valueOf((char) ('A'+block)) + String.valueOf(num + 1) + "_" + String.valueOf(guang + 1);
    }

}