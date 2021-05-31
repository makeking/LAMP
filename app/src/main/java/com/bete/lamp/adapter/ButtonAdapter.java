package com.bete.lamp.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.bete.lamp.R;
import com.bete.lamp.bean.ButtonItem;

import java.util.List;

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ViewHolder> {
        IItem iItem;
        private int mposition = -1;
        //此方法就是连接接口与activity的桥梁
        public void setiItem(IItem iItem) {
            this.iItem = iItem;
        }
        private List<ButtonItem> mButtonItemList;
        static class ViewHolder extends RecyclerView.ViewHolder {
            RadioButton buttonitemname;

            public ViewHolder(View view) {
                super(view);
                buttonitemname = (RadioButton) view.findViewById(R.id.buttonitem_name);
            }
        }
        public ButtonAdapter(List<ButtonItem> buttonitemList) {
            mButtonItemList = buttonitemList;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.buttonitemname.setOnClickListener(new View.OnClickListener() {
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
            ButtonItem buttonitem = mButtonItemList.get(position);
            //holder.buttonitemid.setText(buttonitem.getID());
            holder.buttonitemname.setText(buttonitem.getName());
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(holder.itemView.getContext(),"点击了", Toast.LENGTH_SHORT).show();
//                    iItem.setOnItem(position);
//                }
//            });

            if (position == mposition) {
                holder.buttonitemname.setChecked(true);
            } else {
                holder.buttonitemname.setChecked(false);
            }
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
            return mButtonItemList.size();
        }

    public void setPosition(int position){
        mposition = position;
        iItem.setOnItem(mposition);
        notifyDataSetChanged();
    }
}
