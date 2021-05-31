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
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.bean.PCRLiuChengCanShuItem;
import com.utils.DecimalInputTextWatcher;
import com.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.myutils.GlobalDate.LiuChengMaxTemp;
import static com.myutils.GlobalDate.LiuChengMinTemp;

public class PCRLiuchengCanShuItemAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder>/* implements TextView.OnEditorActionListener, TextWatcher, View.OnFocusChangeListener*/ {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<PCRLiuChengCanShuItem> editList = new ArrayList<>();
    private int mposition = 0;

    public void setEditList(List<PCRLiuChengCanShuItem> editList) {
        this.editList = editList;
    }

    public PCRLiuchengCanShuItemAdpter(Context context, List<PCRLiuChengCanShuItem> list) {
        mContext = context;
        editList = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHold viewHold = null;
        switch (viewType) {
            case 0:
                viewHold = new ViewHold(mInflater.inflate(R.layout.item_liucheng_0, parent, false));
                break;
            case 1:
                viewHold = new ViewHold(mInflater.inflate(R.layout.item_liucheng_1, parent, false));
                break;
        }
        return viewHold;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);//不使用复用 防止数据多时 复用时  多个item中的EditText填写的数据一样
        ((ViewHold) holder).mRlItem.setTag(position);
        ((ViewHold) holder).tv.setText(String.valueOf(position + 1));//String.valueOf(editList.get(position).getID())
        editList.get(position).ID = position+1;
        ((ViewHold) holder).mEditText.setTag(position);

        if (editList.get(position).targetTemp == 0) {
            ((ViewHold) holder).mEditText.setText("");
        } else {
            if (editList.get(position).targetTemp > LiuChengMaxTemp) {
                ((ViewHold) holder).mEditText.setTextColor(Color.RED);
            } else if (editList.get(position).targetTemp < LiuChengMinTemp) {
                ((ViewHold) holder).mEditText.setTextColor(Color.RED);
            }
            else
            {
                ((ViewHold) holder).mEditText.setTextColor(Color.BLACK);
            }
            ((ViewHold) holder).mEditText.setText(String.valueOf((int)(editList.get(position).targetTemp)));
        }
        ((ViewHold) holder).mEditText2.setTag(position);
        if (editList.get(position).holdTime == 0)
            ((ViewHold) holder).mEditText2.setText("");
        else
            ((ViewHold) holder).mEditText2.setText(String.valueOf(editList.get(position).holdTime));

        ((ViewHold) holder).mEditText3.setTag(position);
        if (editList.get(position).intervalTime == 0)
            ((ViewHold) holder).mEditText3.setText("");
        else {
            if(editList.get(position).intervalTime<5)
                ((ViewHold) holder).mEditText3.setTextColor(Color.RED);
            else
                ((ViewHold) holder).mEditText3.setTextColor(Color.BLACK);
            ((ViewHold) holder).mEditText3.setText(String.valueOf(editList.get(position).intervalTime));
        }

        ((ViewHold) holder).mSwitch.setTag(position);
        ((ViewHold) holder).mSwitch.setChecked(editList.get(position).isRead);

//        if (position == mposition) {
//            ((ViewHold) holder).mRlItem.setBackgroundColor(Color.GRAY);
//        } else {
//            ((ViewHold) holder).mRlItem.setBackgroundColor(Color.WHITE);
//        }
//
//        //点击监听
//        ((ViewHold) holder).mRlItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //将点击的位置传出去
//                mposition = holder.getAdapterPosition();
//                //刷新界面 notify 通知Data 数据set设置Changed变化
//                //在这里运行notifyDataSetChanged 会导致下面的onBindViewHolder 重新加载一遍
//                notifyDataSetChanged();
//            }
//        });

//        ((ViewHold) holder).mEditText.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                if (!(s.toString().isEmpty()))
//                    try {
//                        editList.get(position).targetTemp = Double.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                    } catch (NumberFormatException e) {
//                        LogUtils.d(e);
//                    }
//                else
//                    editList.get(position).targetTemp = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        ((ViewHold) holder).mEditText2.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                if (!(s.toString().isEmpty()))
//                    try {
//                        editList.get(position).holdTime = Integer.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                    } catch (NumberFormatException e) {
//                        LogUtils.d(e);
//                    }
//                else
//                    editList.get(position).holdTime = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//        ((ViewHold) holder).mEditText3.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                if (!(s.toString().isEmpty()))
//                    try {
//                        editList.get(position).jumpStep = Integer.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                    } catch (NumberFormatException e) {
//                        LogUtils.d(e);
//                    }
//                else
//                    editList.get(position).jumpStep = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        ((ViewHold) holder).mEditText4.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                if (!(s.toString().isEmpty()))
//                    try {
//                        editList.get(position).cnt = Integer.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                    } catch (NumberFormatException e) {
//                        LogUtils.d(e);
//                    }
//                else
//                    editList.get(position).cnt = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

        ((ViewHold) holder).mEditText.addTextChangedListener(new DecimalInputTextWatcher(2, 0) {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if (((ViewHold) holder).mEditText.isFocused())
                    if (!(editable.toString().isEmpty()))
                        try {
                            if (Double.parseDouble(editable.toString()) > LiuChengMaxTemp) {
                                ((ViewHold) holder).mEditText.setTextColor(Color.RED);
                                editList.get(position).targetTemp = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                            } else if (Double.parseDouble(editable.toString()) < LiuChengMinTemp) {
                                ((ViewHold) holder).mEditText.setTextColor(Color.RED);
                                editList.get(position).targetTemp = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                            } else {
                                ((ViewHold) holder).mEditText.setTextColor(Color.BLACK);
                                editList.get(position).targetTemp = Double.parseDouble(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                            }
                        } catch (NumberFormatException e) {
                            LogUtils.d(e);
                        }
                    else
                        editList.get(position).targetTemp = 35;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }
        });

        ((ViewHold) holder).mEditText2.addTextChangedListener(new DecimalInputTextWatcher(3, 0) {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if (!(editable.toString().isEmpty()))
                    try {
                        editList.get(position).holdTime = Integer.parseInt(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    } catch (NumberFormatException e) {
                        LogUtils.d(e);
                    }
                else
                    editList.get(position).holdTime = 0;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }
        });

        ((ViewHold) holder).mEditText3.addTextChangedListener(new DecimalInputTextWatcher(3, 0) {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if (!(editable.toString().isEmpty()))
                    try {
                        if(Integer.parseInt(editable.toString())<5)
                            ((ViewHold) holder).mEditText3.setTextColor(Color.RED);
                        else
                            ((ViewHold) holder).mEditText3.setTextColor(Color.BLACK);
                        editList.get(position).intervalTime = Integer.parseInt(editable.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
                    } catch (NumberFormatException e) {
                        LogUtils.d(e);
                    }
                else
                    editList.get(position).intervalTime = 5;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
            }
        });

//        ((ViewHold) holder).mEditText3.addTextChangedListener(new TextWatcher() {//监听EditText的text变化
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // editList.set(position, s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                if (!(s.toString().isEmpty()))
//                    try {
//                        if(Integer.valueOf(s.toString())<5)
//                            ((ViewHold) holder).mEditText3.setTextColor(Color.RED);
//                        else
//                            ((ViewHold) holder).mEditText3.setTextColor(Color.BLACK);
//                        editList.get(position).intervalTime = Integer.valueOf(s.toString());//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//                    } catch (NumberFormatException e) {
//                        LogUtils.d(e);
//                    }
//                else
//                    editList.get(position).intervalTime = 5;//更新list的数据,防止rv滑动的时候重新绘制,数据还是之前的
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });

        ((ViewHold) holder).mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editList.get(position).isRead = isChecked;
            }
        });

//        if (position == mposition) {
//            ((ViewHold) holder).mRlItem.setBackgroundColor(Color.GRAY);
//        } else {
//            ((ViewHold) holder).mRlItem.setBackgroundColor(Color.WHITE);
//        }
    }

    @Override
    public int getItemCount() {
        return editList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 1;
        }
        return super.getItemViewType(position);
    }

    public void setPosition(int liuChengPosition) {
        if (editList.size() <= liuChengPosition)
            mposition = editList.size() - 1;
        else
            mposition = liuChengPosition;
    }

    public class ViewHold extends RecyclerView.ViewHolder {
        private LinearLayout mRlItem;
        private TextView tv;
        public TextView mTextView;
        public EditText mEditText;
        public EditText mEditText2;
        public EditText mEditText3;
        public EditText mEditText4;
        public Switch mSwitch;


        public ViewHold(View itemView) {
            super(itemView);
            mRlItem = (LinearLayout) itemView.findViewById(R.id.rl_item);
            tv = (TextView) itemView.findViewById(R.id.tv);
            mEditText = (EditText) itemView.findViewById(R.id.edit);
            mEditText2 = (EditText) itemView.findViewById(R.id.edit2);
            mEditText3 = (EditText) itemView.findViewById(R.id.edit3);
            mSwitch = (Switch) itemView.findViewById(R.id.sw_isread);
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

    public List<PCRLiuChengCanShuItem> getEditList() {
        return editList;
    }

    public int getPosition() {
        return mposition;
    }
}
