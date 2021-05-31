package com.bete.lamp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bete.lamp.R;
import com.bete.lamp.bean.SegmentEntity;

import java.util.LinkedList;

public class SegmentAdapter extends RecyclerView.Adapter<SegmentAdapter.MyViewHolder> {
    IItem iItem;

    private int mposition=0,nposition=0;
    private onItemClickListener onItemClickListener;

    private Context mContext;

    private LinkedList<SegmentEntity> mData;

    public interface onItemClickListener {
        void onClick(View view, int pos);
        boolean onLongClick(View view, int pos);
    }
    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //此方法就是连接接口与activity的桥梁
    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }

    public SegmentAdapter(Context context, LinkedList<SegmentEntity> datas) {
        this.mContext = context;
        this.mData = datas;
    }

    public LinkedList<SegmentEntity> getmData() {
        return mData;
    }

    public void setmData(LinkedList<SegmentEntity> mData) {
        this.mData = mData;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_segment, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.setIsRecyclable(false);//不使用复用 防止数据多时 复用时  多个item中的EditText填写的数据一样
        final SegmentEntity entity = (SegmentEntity) mData.get(position);
        holder.et_segment_cycle.setText(String.valueOf(entity.getCycleCnt()));

        final StepAdapter stepAdapter = new StepAdapter(mContext, entity.getStepEntityLinkedList());
        if(position==mposition)
        {
            stepAdapter.setSelect(true);
            stepAdapter.setMposition(nposition);
            stepAdapter.notifyDataSetChanged();
        }
        else
        {
            stepAdapter.setSelect(false);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.stepList.setLayoutManager(linearLayoutManager);
        holder.stepList.setAdapter(stepAdapter);
        holder.stepList.setVisibility(View.VISIBLE);

        ((MyViewHolder) holder).et_segment_cycle.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                if(!(s.toString().isEmpty()))
                    mData.get(position).cycleCnt=Integer.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        stepAdapter.setiItem(new IItem() {
                @Override
                public void setOnItem(int position) {
                    //selectFile = sampleItemList.get(position).getFile();
                }
            });

        stepAdapter.setOnItemClickListener(new StepAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                if((nposition!=pos)||(holder.getAdapterPosition()!=mposition)) {
                    nposition = pos;
                    mposition = holder.getAdapterPosition();
                    if (mposition < 0)
                        return;
                    notifyDataSetChanged();
                }
            }

            @Override
            public boolean onLongClick(View view, int pos) {
                return false;
            }
        });

//        ////////////////////////////////////////////////
//        if (position == mposition) {
//            //holder.mRlItem.setBackgroundColor(Color.parseColor("#ff00ff"));
//            stepAdapter.setSelect(true);
//            stepAdapter.notifyDataSetChanged();
//        } else {
//            //holder.mRlItem.setBackgroundColor(Color.WHITE);
//            stepAdapter.setSelect(false);
//            stepAdapter.notifyDataSetChanged();
//        }

        //点击监听
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //将点击的位置传出去
//                mposition = holder.getAdapterPosition();
//                iItem.setOnItem(mposition);
//                //刷新界面 notify 通知Data 数据set设置Changed变化
//                //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
//                notifyDataSetChanged();//
//                onItemClickListener.onClick(view, position);
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
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mRlItem;
        EditText et_segment_cycle;
        RecyclerView stepList;

        public MyViewHolder(View itemView) {
            super(itemView);
            et_segment_cycle = (EditText) itemView.findViewById(R.id.et_segment_cycle);
            stepList = (RecyclerView) itemView.findViewById(R.id.stepRv);
            mRlItem = (LinearLayout) itemView.findViewById(R.id.rl_item);
        }
    }

    public int getMposition()
    {
        return mposition;
    }

    public int getNposition()
    {
        return nposition;
    }

    public void setMposition(int mposition) {
        this.mposition = mposition;
    }

    public void setNposition(int nposition) {
        this.nposition = nposition;
    }
}
