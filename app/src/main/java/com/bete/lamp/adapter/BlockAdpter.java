package com.bete.lamp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.bean.BlockItem;

import java.util.ArrayList;
import java.util.List;

public class BlockAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder>/* implements TextView.OnEditorActionListener, TextWatcher, View.OnFocusChangeListener*/ {
    private Context mContext;
    private LayoutInflater mInflater;

    private List<BlockItem> editList = new ArrayList<>();
    private Boolean editEnable = true;
    private int mposition= 0;
    public BlockAdpter(Context context, List<BlockItem> list) {
        mContext = context;
        editList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHold viewHold = null;
        switch(viewType) {
            case 0:
                viewHold = new ViewHold(mInflater.inflate(R.layout.item_block_0, parent, false));
                break;
            case 1:
                viewHold = new ViewHold(mInflater.inflate(R.layout.item_block_0, parent, false));
                break;
        }
        return viewHold;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);//不使用复用 防止数据多时 复用时  多个item中的EditText填写的数据一样
        ((ViewHold) holder).mRlItem.setTag(position);
        ((ViewHold) holder).tv.setTag(position);
        ((ViewHold) holder).tv.setText(String.valueOf(position+1));//editList.get(position).getID()
        ((ViewHold) holder).mEditText.setTag(position);
        ((ViewHold) holder).mEditText.setText(String.valueOf(editList.get(position).fromPosition));
        ((ViewHold) holder).mEditText2.setTag(position);
        ((ViewHold) holder).mEditText2.setText(String.valueOf(editList.get(position).endPosition));
        if(editEnable)
        {
            ((ViewHold) holder).mEditText.setTag(position);
            ((ViewHold) holder).mEditText.setEnabled(true);
            ((ViewHold) holder).mEditText2.setTag(position);
            ((ViewHold) holder).mEditText.setEnabled(true);
        }
        else
        {
            ((ViewHold) holder).mEditText.setTag(position);
            ((ViewHold) holder).mEditText.setEnabled(false);
            ((ViewHold) holder).mEditText2.setTag(position);
            ((ViewHold) holder).mEditText.setEnabled(false);
        }
        if (position == mposition) {
            ((ViewHold) holder).mRlItem.setBackgroundColor(Color.parseColor("#ff00ff"));
        } else {
            ((ViewHold) holder).mRlItem.setBackgroundColor(Color.WHITE);
        }
        //点击监听
        ((ViewHold) holder).mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //将点击的位置传出去
                    mposition = holder.getAdapterPosition();
                     if (mposition < 0)
                     return;
                    //刷新界面 notify 通知Data 数据set设置Changed变化
                    //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                    notifyDataSetChanged();
            }
        });

        ((ViewHold) holder).mEditText.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                if(s.toString().isEmpty())
                    editList.get(position).fromPosition = 0;
                else
                    editList.get(position).fromPosition = Integer.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ((ViewHold) holder).mEditText2.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                if(s.toString().isEmpty())
                    editList.get(position).endPosition = 0;
                else
                    editList.get(position).endPosition = Integer.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return editList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position%2==0) {
            return 1;
        }
        return super.getItemViewType(position);
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        private LinearLayout mRlItem;
        private TextView tv;
        public EditText mEditText;
        public EditText mEditText2;

        public ViewHold(View itemView) {
            super(itemView);
            mRlItem = (LinearLayout) itemView.findViewById(R.id.rl_item);
            tv = (TextView) itemView.findViewById(R.id.tv);
            mEditText = (EditText) itemView.findViewById(R.id.edit1);
            mEditText2 = (EditText) itemView.findViewById(R.id.edit2);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private EditText editText;

        public MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public List<BlockItem> getEditList()
    {
        return editList;
    }

    public void setEditList(List<BlockItem> editList) {
        this.editList = editList;
    }

    public Boolean getEditEnable() {
        return editEnable;
    }

    public void setEditEnable(Boolean editEnable) {
        this.editEnable = editEnable;
    }

    public int getPosition()
    {
        return mposition;
    }

}
