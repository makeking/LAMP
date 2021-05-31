package com.bete.lamp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import com.bete.lamp.R;
import com.bete.lamp.bean.StepEntity;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.MyViewHolder> {
    IItem iItem;
    private int mposition=-1;
    private boolean isSelect;
    private onItemClickListener onItemClickListener;
    private Context mContext;
    public List<StepEntity> mDatas;

    public interface onItemClickListener {
        void onClick(View view, int pos);
        boolean onLongClick(View view, int pos);
    }
    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public int getMposition() {
        return mposition;
    }

    public void setMposition(int mposition) {
        this.mposition = mposition;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    //此方法就是连接接口与activity的桥梁
    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }


    public StepAdapter(Context context, List<StepEntity> data) {
        this.mContext = context;
        this.mDatas = data;
    }


    @Override
    public StepAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_step, parent, false);
        MyViewHolder myViewHolderw = new MyViewHolder(view);
//        LinearLayoutManager lp = new LinearLayoutManager(mContext);
//        lp.setOrientation(LinearLayoutManager.HORIZONTAL);
//        view.setLayoutManager(lp);
        return myViewHolderw;
    }

    @Override
    public void onBindViewHolder(final StepAdapter.MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);//不使用复用 防止数据多时 复用时  多个item中的EditText填写的数据一样
        holder.mRlItem.setTag(position);
        StepEntity entity = mDatas.get(position);
        holder.et_step_temp_before.setText(String.valueOf(entity.getTempBefore()));
        holder.et_step_temp_after.setText(String.valueOf(entity.getTempAfter()));
        holder.et_step_delay.setText(String.valueOf(entity.getTimemsec()));
        holder.et_step_change_speed.setText(String.valueOf(entity.getChangeSpeed()));
        holder.sw_step_read_enable.setChecked(entity.isReadEnable());

        if ((position == mposition)&&(isSelect())) {
            holder.mRlItem.setBackgroundColor(Color.parseColor("#ff00ff"));
        } else {
            holder.mRlItem.setBackgroundColor(Color.WHITE);
        }

        ((MyViewHolder) holder).et_step_temp_before.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                if(!(s.toString().isEmpty()))
                mDatas.get(position).tempBefore=Float.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ((MyViewHolder) holder).et_step_temp_after.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                if(!(s.toString().isEmpty()))
                mDatas.get(position).tempAfter=Float.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ((MyViewHolder) holder).et_step_delay.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                if(!(s.toString().isEmpty()))
                mDatas.get(position).timemsec=Integer.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ((MyViewHolder) holder).et_step_change_speed.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                if(!(s.toString().isEmpty()))
                mDatas.get(position).changeSpeed=Float.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ((MyViewHolder) holder).sw_step_read_enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatas.get(position).readEnable=isChecked;
            }
        });

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
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mRlItem;
        EditText et_step_temp_before;
        EditText et_step_temp_after;
        EditText et_step_delay;
        EditText et_step_change_speed;
        Switch sw_step_read_enable;

        public MyViewHolder(View itemView) {
            super(itemView);
            et_step_temp_before = (EditText) itemView.findViewById(R.id.et_step_temp_before);
            et_step_temp_after = (EditText) itemView.findViewById(R.id.et_step_temp_after);
            et_step_delay = (EditText) itemView.findViewById(R.id.et_step_delay);
            et_step_change_speed = (EditText) itemView.findViewById(R.id.et_step_change_speed);
            sw_step_read_enable = (Switch) itemView.findViewById(R.id.sw_step_read_enable);
            mRlItem = (LinearLayout) itemView.findViewById(R.id.ll_step_item);
        }
    }
}
