package com.bete.lamp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.bean.UsrItem;

import java.util.List;

public class UsrItemAdapter extends RecyclerView.Adapter<UsrItemAdapter.ViewHolder> {
        IItem iItem;

    public int getMposition() {
        return mposition;
    }

    private int mposition = -1;
        //此方法就是连接接口与activity的桥梁
        public void setiItem(IItem iItem) {
            this.iItem = iItem;
        }
        private List<UsrItem> usrItemList;
        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView usritemname;
            Button usritemdel;
            TextView usritemid;
            LinearLayout mRlItem;
            public ViewHolder(View view) {
                super(view);
                mRlItem = (LinearLayout) view.findViewById(R.id.rl_item);
                usritemid = (TextView) view.findViewById(R.id.tv_item_id);
                usritemname = (TextView) view.findViewById(R.id.tv_item_name);
                usritemdel = (Button) view.findViewById(R.id.bt_item_del);
            }
        }
        public UsrItemAdapter(List<UsrItem> usritemList) {
            usrItemList = usritemList;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usr, parent, false);
            final ViewHolder holder = new ViewHolder(view);

//        holder.searchitemid.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                SearchItem searchitem = mSearchItemList.get(position);
//            }
//        });
//        holder.searchitemsampleno.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                SearchItem searchitem = mSearchItemList.get(position);
//            }
//        });
//        holder.searchitemcheckdatetime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                SearchItem searchitem = mSearchItemList.get(position);
//            }
//        });
            return holder;
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            UsrItem usritem = usrItemList.get(position);
            //holder.buttonitemid.setText(buttonitem.getID());
            holder.usritemname.setText(usritem.getName());
            holder.usritemid.setText("用户"+String.valueOf(position)+":");
            holder.usritemdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //将点击的位置传出去
                    mposition = holder.getAdapterPosition();
                    if (mposition < 0)
                        return;
                    iItem.setOnItem(mposition);
                    //刷新界面 notify 通知Data 数据set设置Changed变化
                    //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                    notifyDataSetChanged();
                }
            });

            if (position == mposition) {
                holder.usritemname.setBackgroundColor(R.drawable.lamp_user_item_select);
            } else {
                    holder.usritemname.setBackgroundResource(R.drawable.lamp_user_item_unselect);
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
                }
            });

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    iItem.setOnItem(position);
//                }
//            });
        }

        @Override
        public int getItemViewType(int position) {
            if (position%2==0) {
                return 1;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return usrItemList.size();
        }

        public void setPosition(int position){
            mposition = position;
            iItem.setOnItem(mposition);
            notifyDataSetChanged();
        }


}
