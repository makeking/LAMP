package com.bete.lamp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bete.lamp.AppApplication;
import com.bete.lamp.R;
import com.bete.lamp.bean.EepromItem;
import com.utils.simpleArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EepromItemAdapter extends RecyclerView.Adapter<EepromItemAdapter.ViewHolder> {
    IItem iItem;
    Context mContext;
    private int mposition = -1;
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();
    private List<EepromItem> mEepromItemList;
    private onItemClickListener onItemClickListener;

    private List<Integer> checkList = new ArrayList<>();

    public int getMposition() {
        return mposition;
    }

    public void setMposition(int mposition) {
        this.mposition = mposition;
    }

    public interface onItemClickListener {
        void onClick(View view, int pos);

        boolean onLongClick(View view, int pos);

        void onCheckedChanged(CompoundButton compoundButton, boolean b, int pos);
    }

    public void setOnItemClickListener(EepromItemAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //此方法就是连接接口与activity的桥梁
    public void setiItem(IItem iItem) {
        this.iItem = iItem;
    }

    //implements CompoundButton.OnCheckedChangeListener
    static class ViewHolder extends RecyclerView.ViewHolder {
        //private boolean onBind;
        LinearLayout mRlItem;
        TextView itemid;
        EditText itemname;
        EditText itemaddr;
        EditText itemlen;
        EditText itemcontent;
        EditText itemprop;
        Spinner itemtype;
        CheckBox mCb_able;

        public ViewHolder(View view) {
            super(view);
            mRlItem = (LinearLayout) view.findViewById(R.id.rl_item);
            itemid = (TextView) view.findViewById(R.id.tv_id);
            itemname = (EditText) view.findViewById(R.id.et_name);
            itemaddr = (EditText) view.findViewById(R.id.et_addr);
            itemlen = (EditText) view.findViewById(R.id.et_len);
            itemcontent = (EditText) view.findViewById(R.id.et_content);
            itemprop = (EditText) view.findViewById(R.id.et_prop);
            itemtype = (Spinner) view.findViewById(R.id.spin_type);
            mCb_able = (CheckBox) view.findViewById(R.id.cb_able);
        }
    }

//    public EepromItemAdapter(List<EepromItem> eepromItemList) {
//        mEepromItemList = eepromItemList;
//    }

    public EepromItemAdapter(Context context, List<EepromItem> eepromItemList) {
        mContext = context;
        mEepromItemList = eepromItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_eeprom, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    EepromItem eepromItem;
    List<String> typeStringList = Arrays.asList(AppApplication.getAppContext().getResources().getStringArray(R.array.eepromtype));

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);//不使用复用 防止数据多时 复用时  多个item中的EditText填写的数据一样
        holder.mCb_able.setTag(position);
        holder.mRlItem.setTag(position);
        (holder).itemname.setTag(position);
        (holder).itemaddr.setTag(position);
        (holder).itemcontent.setTag(position);
        (holder).itemprop.setTag(position);
        (holder).itemlen.setTag(position);
        eepromItem = mEepromItemList.get(position);
        holder.itemname.setText(eepromItem.name);
        holder.itemaddr.setText(eepromItem.addr);
        holder.itemlen.setText(eepromItem.len);
        holder.itemcontent.setText(eepromItem.value);
        holder.itemprop.setText(eepromItem.prop);
        holder.itemid.setText(String.valueOf(position + 1));
        //适配器
        simpleArrayAdapter type_Adapter1 = new simpleArrayAdapter<String>(AppApplication.getAppContext(), R.layout.spinner_showed_item, typeStringList);
        holder.itemtype.setAdapter(type_Adapter1);
        if (typeStringList.contains(eepromItem.type)) {
            holder.itemtype.setSelection(typeStringList.indexOf(eepromItem.type));
        } else
            holder.itemtype.setSelection(0);

        holder.mCb_able.setChecked(eepromItem.isAble);

        if (position == mposition) {
            holder.mRlItem.setBackgroundColor(Color.BLUE);
        } else {
            holder.mRlItem.setBackgroundColor(Color.WHITE);
        }

        holder.itemname.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if ((holder).itemname.isFocused())
                    if (!(editable.toString().isEmpty()))
                    mEepromItemList.get(position).name = editable.toString();//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }
        });

        holder.itemaddr.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if ((holder).itemaddr.isFocused())
                    if (!(editable.toString().isEmpty()))
                        mEepromItemList.get(position).addr = editable.toString();//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }
        });

        holder.itemcontent.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if ((holder).itemcontent.isFocused())
                    if (!(editable.toString().isEmpty()))
                        mEepromItemList.get(position).value = editable.toString();//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }
        });

        holder.itemlen.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if ((holder).itemlen.isFocused())
                    if (!(editable.toString().isEmpty()))
                        mEepromItemList.get(position).len = editable.toString();//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }
        });

        holder.itemprop.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if ((holder).itemprop.isFocused())
                    if (!(editable.toString().isEmpty()))
                        mEepromItemList.get(position).prop = editable.toString();//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }
        });

        //点击监听
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将点击的位置传出去
                mposition = position;
                //刷新界面 notify 通知Data 数据set设置Changed变化
                //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
                notifyDataSetChanged();
            }
        });

        //长按监听
        holder.mRlItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //return onItemClickListener.onLongClick(view, position);
                return true;
            }
        });

        //对checkbox的监听 保存选择状态 防止checkbox显示错乱
        holder.mCb_able.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int pos = (int) compoundButton.getTag();
                mEepromItemList.get(pos).isAble = b;
                //onItemClickListener.onCheckedChanged(compoundButton, b,position);
            }
        });

        holder.itemtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int p, long id) {
                mEepromItemList.get(position).type = typeStringList.get(p);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private double getDoubleFromEditText(EditText v) {
        String tempstr;
        double tempdouble;
        tempstr = v.getText().toString();
        try {
            tempdouble = Double.valueOf(tempstr);
            return tempdouble;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return mEepromItemList.size();
    }

    public void setItemIndex(int position) {
        if ((position < getItemCount()) && (position >= 0))
            iItem.setOnItem(position);
    }


    public List<Integer> getCheckList() {
        checkList.clear();
        if ((mposition != -1) && (mposition < mEepromItemList.size()))
            checkList.add(mposition);
        return checkList;
    }
    class MyTextWatcher implements TextWatcher {
        public MyTextWatcher() {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            if (TextUtils.isEmpty(text)) return;
            //TODO：可在此处额外添加代码
        }
    }
}