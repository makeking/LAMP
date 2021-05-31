package com.bete.lamp.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bete.lamp.R;
import com.bete.lamp.bean.ThermalItem;

import java.util.List;

public class ThermalInfoItemAdpter extends RecyclerView.Adapter<ThermalInfoItemAdpter.ViewHolder> {
    IItem iItem;

    //此方法就是连接接口与activity的桥梁
    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }

    private List<ThermalItem> mThermalItemList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemid;
        TextView itemname;

        public ViewHolder(View view) {
            super(view);
            itemid = (TextView) view.findViewById(R.id.item_id);
            itemname = (TextView) view.findViewById(R.id.item_name);
        }
    }

    public ThermalInfoItemAdpter(List<ThermalItem> thermalitemList) {
        mThermalItemList = thermalitemList;
    }

    @Override
    public ThermalInfoItemAdpter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thermalinfo, parent, false);
        final ThermalInfoItemAdpter.ViewHolder holder = new ThermalInfoItemAdpter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ThermalInfoItemAdpter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ThermalItem thermalitem = mThermalItemList.get(position);
        holder.itemid.setText(String.valueOf(position + 1));
        holder.itemname.setText(thermalitem.getInfo());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iItem.setOnItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mThermalItemList.size();
    }

    public void setItemIndex(int position)
    {
        if((position<getItemCount())&&(position>=0))
            iItem.setOnItem(position);
    }
}
