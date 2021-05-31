package com.bete.lamp.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bete.lamp.R;
import com.bete.lamp.bean.ReportItem;

import java.util.List;

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemAdapter.ViewHolder> {
        IItem iItem;
        //此方法就是连接接口与activity的桥梁
        public void setiItem(IItem iItem) {
            this.iItem = iItem;
        }
        private List<ReportItem> mReportItemList;
        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView itemname;
            TextView itemresult;
            TextView itemresulttip;
            TextView itemref;
            TextView itemdanwei;

            public ViewHolder(View view) {
                super(view);
                itemname = (TextView) view.findViewById(R.id.report_item_name);
                itemresult = (TextView) view.findViewById(R.id.report_item_result);
                itemresulttip = (TextView) view.findViewById(R.id.report_item_result_tip);
                itemref = (TextView) view.findViewById(R.id.report_item_ref);
                itemdanwei = (TextView) view.findViewById(R.id.report_item_danwei);
            }
        }
        public ReportItemAdapter(List<ReportItem> reportitemList) {
            mReportItemList = reportitemList;
        }
        @Override
        public com.bete.lamp.adapter.ReportItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch(viewType) {
                case 0:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_0, parent, false);
                    break;
                case 1:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_1, parent, false);
                    break;
            }
            ViewHolder holder = new ViewHolder(view);
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
//                Toast.makeText(v.getContext(), "you clicked image " + searchitem.getCheckDateTime(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
            return holder;
        }
        @Override
        public void onBindViewHolder(final com.bete.lamp.adapter.ReportItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            ReportItem reportitem = mReportItemList.get(position);
            //holder.reportitemid.setText(reportitem.getID());
            holder.itemname.setText(reportitem.getName());
            holder.itemresulttip.setText(reportitem.getResultTip());
            holder.itemresult.setText(reportitem.getResult());
            holder.itemref.setText(reportitem.getRef());
            holder.itemdanwei.setText(reportitem.getDanwei());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iItem.setOnItem(position);
                }
            });
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
            return mReportItemList.size();
        }
}
