package com.bete.lamp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.bean.ProjectItem;

import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
        IItem iItem;
        private int mposition = -1;
        //此方法就是连接接口与activity的桥梁
        public void setiItem(IItem iItem) {
            this.iItem = iItem;
        }
        private List<ProjectItem> mProjectItemList;
        static class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll_item;
            TextView tv_name1;
            TextView tv_name2;
            TextView tv_name3;
            TextView tv_name4;
            TextView tv_name5;
            public ViewHolder(View view) {
                super(view);
                ll_item =  (LinearLayout)view.findViewById(R.id.ll_item);
                tv_name1 = (TextView)view.findViewById(R.id.tv_name1);
                tv_name2 = (TextView)view.findViewById(R.id.tv_name2);
                tv_name3 = (TextView)view.findViewById(R.id.tv_name3);
                tv_name4 = (TextView)view.findViewById(R.id.tv_name4);
                tv_name5 = (TextView)view.findViewById(R.id.tv_name5);
            }
        }
        public ProjectAdapter(List<ProjectItem> projectitemList) {
            mProjectItemList = projectitemList;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            holder.setIsRecyclable(false);//不使用复用 防止数据多时 复用时  多个item中的EditText填写的数据一样
            holder.ll_item.setTag(position);
            ProjectItem projectItem = mProjectItemList.get(position);

            holder.tv_name1.setText(String.valueOf(position+1));
            holder.tv_name2.setText(projectItem.getName());
            holder.tv_name3.setText(projectItem.getFangShi());
            holder.tv_name4.setText(projectItem.getLot());
            holder.tv_name5.setText(projectItem.getLimit());

            holder.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //将点击的位置传出去
                    mposition = holder.getAdapterPosition();
                    if (mposition<0)
                        return;
                    iItem.setOnItem(mposition);
                    //刷新界面 notify 通知Data 数据set设置Changed变化
                    //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                    notifyDataSetChanged();
                }
            });

            if (position == mposition) {
                holder.ll_item.setBackgroundColor(Color.parseColor("#55A1EBFF"));
            } else {
                holder.ll_item.setBackgroundColor(Color.parseColor("#00ffffff"));
            }

//            if (position == mposition) {
//                holder.ll_item.setBackgroundResource(R.drawable.project_item_select);
//            } else {
//                holder.ll_item.setBackgroundResource(R.drawable.project_item_normal);
//            }
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
            return mProjectItemList.size();
        }

    public void setPosition(int position){
        mposition = position;
        iItem.setOnItem(mposition);
        notifyDataSetChanged();
    }
}
