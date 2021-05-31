package com.bete.lamp.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bete.lamp.R;
import com.bete.lamp.bean.SearchItem;

import java.util.List;


public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {
    IItem iItem;
    //此方法就是连接接口与activity的桥梁
    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }
    private List<SearchItem> mSearchItemList;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView searchitemid;
        TextView searchitemsampleno;
        TextView searchitemcheckdatetime;

        public ViewHolder(View view) {
            super(view);
            searchitemid = (TextView) view.findViewById(R.id.searchitem_id);
            searchitemsampleno = (TextView) view.findViewById(R.id.searchitem_sampleno);
            searchitemcheckdatetime = (TextView) view.findViewById(R.id.searchitem_checkdatetime);
        }
    }
    public SearchItemAdapter(List<SearchItem> searchitemList) {
        mSearchItemList = searchitemList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch(viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_0, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_1, parent, false);
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
//            }
//        });
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        SearchItem searchitem = mSearchItemList.get(position);
        //holder.searchitemid.setText(searchitem.getID());
        holder.searchitemsampleno.setText(searchitem.getSampleNO());
        holder.searchitemcheckdatetime.setText(searchitem.getCheckDateTime());
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
        return mSearchItemList.size();
    }
}
