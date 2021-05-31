package com.bete.lamp.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bete.lamp.R;
import com.bete.lamp.message.PCRLiuChengProcessValueEvent;
import com.bete.lamp.ui.normal.BaseFragment;
import com.myutils.GlobalDate;
import com.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.myutils.GlobalDate.BLOCKNUM;
import static com.myutils.GlobalDate.g_block_states;
import static com.myutils.GlobalDate.g_currentStep;
import static com.myutils.GlobalDate.g_temp_time_shengyus;
import static com.myutils.GlobalDate.g_time_shengyus;

public class MainShiYanFragment extends BaseFragment {
    private static final String TAG = "MainShiYanFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    private OnFragmentInteractionListener mListener;
    ///////////////////////////////////////////////////////////////////////////////////
    private RadioGroup rg_shiyan_step, rg_shiyan_block;
    private Fragment[] mFragments;
    private RadioButton[] rb_shiyan_blocks;
    public ShiYanStep1Fragment shiYanStep1Fragment;
    public ShiYanStep2Fragment shiYanStep2Fragment;
    public ShiYanStep3Fragment shiYanStep3Fragment;
    public String[] blocknames;

    public MainShiYanFragment() {
        // Required empty public constructor
    }


//    public static MainShiYanFragment newInstance(String param1, String param2) {
//        MainShiYanFragment fragment = new MainShiYanFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        mFragments = new Fragment[]{
                //配置、实时、结果、设置
                shiYanStep1Fragment = new ShiYanStep1Fragment(),
                shiYanStep2Fragment = new ShiYanStep2Fragment(),
                shiYanStep3Fragment = new ShiYanStep3Fragment()
        };
        blocknames = new String[]{
                new String("Block A"),
                new String("Block B"),
                new String("Block C"),
                new String("Block D")
        };
    }

    private int currentBlock = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_shi_yan,
                container, false);

        rg_shiyan_step = (RadioGroup) view.findViewById(R.id.rg_shiyan_step);
        rg_shiyan_block = (RadioGroup) view.findViewById(R.id.rg_shiyan_block);

        rb_shiyan_blocks = new RadioButton[BLOCKNUM];
        rb_shiyan_blocks[0] =
                (RadioButton) view.findViewById(R.id.rb_shiyan_block1);
        rb_shiyan_blocks[1] =
                (RadioButton) view.findViewById(R.id.rb_shiyan_block2);
        rb_shiyan_blocks[2] =
                (RadioButton) view.findViewById(R.id.rb_shiyan_block3);
        rb_shiyan_blocks[3] =
                (RadioButton) view.findViewById(R.id.rb_shiyan_block4);

//        rg_shiyan_block.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group,
//                                         @IdRes int checkedId) {
//                switch (checkedId) {
//                    case R.id.rb_shiyan_block1:
//                        g_currentBlock = 0;
//                        if (g_block_states[g_currentBlock] == GlobalDate.BlockStateType.done) {
//                            changeRadio(2);
//                        } else
//                            changeRadio(g_currentStep[g_currentBlock]);
//                        break;
//                    case R.id.rb_shiyan_block2:
//                        g_currentBlock = 1;
//                        if (g_block_states[g_currentBlock] == GlobalDate.BlockStateType.done) {
//                            changeRadio(2);
//                        } else
//                            changeRadio(g_currentStep[g_currentBlock]);
//                        break;
//                    case R.id.rb_shiyan_block3:
//                        g_currentBlock = 2;
//                        if (g_block_states[g_currentBlock] == GlobalDate.BlockStateType.done) {
//                            changeRadio(2);
//                        } else
//                            changeRadio(g_currentStep[g_currentBlock]);
//                        break;
//                    case R.id.rb_shiyan_block4:
//                        g_currentBlock = 3;
//                        if (g_block_states[g_currentBlock] == GlobalDate.BlockStateType.done) {
//                            changeRadio(2);
//                        } else
//                            changeRadio(g_currentStep[g_currentBlock]);
//                        break;
//                }
//            }
//        });

        rg_shiyan_block.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group,
                                         @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_shiyan_block1:
                        currentBlock = 0;
                        if (g_block_states[currentBlock] == GlobalDate.BlockStateType.done) {
                            changeRadio(2,currentBlock);
                        } else
                            changeRadio(g_currentStep[currentBlock],currentBlock);
                        break;
                    case R.id.rb_shiyan_block2:
                        currentBlock = 1;
                        if (g_block_states[currentBlock] == GlobalDate.BlockStateType.done) {
                            changeRadio(2,currentBlock);
                        } else
                            changeRadio(g_currentStep[currentBlock],currentBlock);
                        break;
                    case R.id.rb_shiyan_block3:
                        currentBlock = 2;
                        if (g_block_states[currentBlock] == GlobalDate.BlockStateType.done) {
                            changeRadio(2,currentBlock);
                        } else
                            changeRadio(g_currentStep[currentBlock],currentBlock);
                        break;
                    case R.id.rb_shiyan_block4:
                        currentBlock = 3;
                        if (g_block_states[currentBlock] == GlobalDate.BlockStateType.done) {
                            changeRadio(2,currentBlock);
                        } else
                            changeRadio(g_currentStep[currentBlock],currentBlock);
                        break;
                }
            }
        });
        rb_shiyan_blocks[currentBlock].setChecked(true);
        return view;
    }

//    //设置Fragment页面
//    private void setIndexSelected(int index) {
//        //开启事务
//        LogUtils.d("setIndexSelected:" + index);
//        LogUtils.d(g_currentStep);
//        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//        //隐藏当前Fragment
//        ft.hide(mFragments[0]);
//        ft.hide(mFragments[1]);
//        ft.hide(mFragments[2]);
//        //判断Fragment是否已经添加
//        if (!mFragments[index].isAdded()) {
//            ft.add(R.id.shiyan_content, mFragments[index]).show(mFragments[index]);
//            LogUtils.d("ADD!!!!!!!!!!!!!!!!");
//        } else {
//            //显示新的Fragment
//            ft.show(mFragments[index]);
//        }
//        ft.commit();
//    }

    //设置Fragment页面
    private void setIndexSelected(int index,int num) {
        LogUtils.d("setIndexSelected:" + index);
        LogUtils.d(g_currentStep);
        FragmentManager mFragmentManager = getChildFragmentManager();
        //开启事务
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        //隐藏当前Fragment
        for (Fragment mFragment : mFragments) {
            ft.hide(mFragment);
        }
        Fragment fragment = mFragments[index];
        //todo 2021-5-25 添加以下的内容
        Bundle args = new Bundle();
        args.putInt("param", num);
        fragment.setArguments(args);
        //判断Fragment是否已经添加
        if (fragment.isAdded() || null != mFragmentManager.findFragmentByTag(index + "")) {
            ft.show(fragment);
//            ft.show(mFragments[index]);
        } else {
            //显示新的Fragment
            mFragmentManager.executePendingTransactions();
            ft.add(R.id.shiyan_content, fragment).show(fragment);
//            ft.add(R.id.shiyan_content, mFragments[index]).show(mFragments[index]);
            LogUtils.d(TAG + ":" + "ADD!!!!!!!!!!!!!!!!");
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }

    public void changeRadio(int state ,int num) {
//        if (state == 0) {
            //rb_shiyan_step1.setChecked(true);
            g_currentStep[num] = state;
            setIndexSelected(state,num);
//        }
//        if (state == 1) {
//            //rb_shiyan_step2.setChecked(true);
//            g_currentStep[num] = 1;
//            setIndexSelected(1,num);
//        }
//        if (state == 2) {
//            //rb_shiyan_step3.setChecked(true);
//            g_currentStep[num] = 2;
//            setIndexSelected(2,num);
//        }
    }

    public void getDataFromUI() {
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            LogUtils.d(TAG, "onHiddenChanged show");
            for (int i = 0; i < 4; i++) {
                if (g_time_shengyus[i] < g_temp_time_shengyus[i])
                    g_temp_time_shengyus[i] = g_time_shengyus[i];
            }
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        for (int i = 0; i < BLOCKNUM; i++) {
            if (g_block_states[i] == GlobalDate.BlockStateType.ready) {
                rb_shiyan_blocks[i].setBackgroundResource(R.drawable.rb_lamp_block_ready_selector);
            } else if (g_block_states[i] == GlobalDate.BlockStateType.running) {
                rb_shiyan_blocks[i].setBackgroundResource(R.drawable.rb_lamp_block_running_selector);
            } else {
                rb_shiyan_blocks[i].setBackgroundResource(R.drawable.rb_lamp_block_done_selector);
//                String html = "<td><font size=\"3\" color=\"red\">This is some text!</font></td>";
//                rb_shiyan_blocks[i].setText(Html.fromHtml(html));//blocknames[i] + "\n" + "实验完成"
                String text = blocknames[i] + "\n" + "Finished";
                Spannable span = new SpannableString(text);
                span.setSpan(new AbsoluteSizeSpan(40), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new AbsoluteSizeSpan(20), 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(Color.GREEN), 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                rb_shiyan_blocks[i].setText(span);
            }
        }
        LogUtils.d("onStart");
        EventBus.getDefault().register(this);
    }

    /**
     * 消息接收并显示的方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PCRLiuChengProcessValueEvent pcrLiuChengProcessValueEvent) {
        if ((pcrLiuChengProcessValueEvent.getType() == GlobalDate.EventType.timeChange) && (g_block_states[pcrLiuChengProcessValueEvent.getIndex()] == GlobalDate.BlockStateType.running)) {
            if ((g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] / 1000) != 0) {
                String text = blocknames[pcrLiuChengProcessValueEvent.getIndex()] + "\n" + String.valueOf(g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] / 1000 / 60) + ":" + String.format("%02d", g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] / 1000 % 60);
                Spannable span = new SpannableString(text);
                span.setSpan(new AbsoluteSizeSpan(40), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new AbsoluteSizeSpan(20), 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(Color.GREEN), 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(span);
//                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(blocknames[pcrLiuChengProcessValueEvent.getIndex()] + "\n" + String.valueOf(g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] / 1000/60)+":" + String.format("%02d",g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] / 1000%60));
            } else {
                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(blocknames[pcrLiuChengProcessValueEvent.getIndex()]);
            }

        }

        if ((pcrLiuChengProcessValueEvent.getType() == GlobalDate.EventType.timeChange)) {
            if (pcrLiuChengProcessValueEvent.getIndex() == 4) {
                for (int i = 0; i < 4; i++) {
                    if (g_block_states[i] == GlobalDate.BlockStateType.running)
                        if ((g_temp_time_shengyus[i] >= g_time_shengyus[i] - 5000) && g_temp_time_shengyus[i] > 0) {
                            g_temp_time_shengyus[i] = g_temp_time_shengyus[i] - 1000;
                            if ((g_temp_time_shengyus[i] / 1000) != 0) {
                                String text = blocknames[i] + "\n" + String.valueOf(g_temp_time_shengyus[i] / 1000 / 60) + ":" + String.format("%02d", g_temp_time_shengyus[i] / 1000 % 60);
                                Spannable span = new SpannableString(text);
                                span.setSpan(new AbsoluteSizeSpan(40), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                span.setSpan(new AbsoluteSizeSpan(20), 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                span.setSpan(new ForegroundColorSpan(Color.GREEN), 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(span);
//                                rb_shiyan_blocks[i].setText(blocknames[i] + "\n" + String.valueOf(g_temp_time_shengyus[i] / 1000/60) + ":"+String.format("%02d",g_temp_time_shengyus[i] / 1000%60));
                            } else {
                                rb_shiyan_blocks[i].setText(blocknames[i]);
                            }
                        }
                }
            }
        } else {
            if ((g_block_states[pcrLiuChengProcessValueEvent.getIndex()] == GlobalDate.BlockStateType.running)) {
                g_temp_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] = g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()];
                if ((g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] / 1000) != 0) {
                    String text = blocknames[pcrLiuChengProcessValueEvent.getIndex()] + "\n" + String.valueOf(g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] / 1000 / 60) + ":" + String.format("%02d", g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] / 1000 % 60);
                    Spannable span = new SpannableString(text);
                    span.setSpan(new AbsoluteSizeSpan(40), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span.setSpan(new AbsoluteSizeSpan(20), 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span.setSpan(new ForegroundColorSpan(Color.GREEN), 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(span);
//                    rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(blocknames[pcrLiuChengProcessValueEvent.getIndex()] + "\n" + String.valueOf(g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] / 1000/60) +":"+String.format("%02d",g_time_shengyus[pcrLiuChengProcessValueEvent.getIndex()] / 1000%60));
                } else {
                    rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(blocknames[pcrLiuChengProcessValueEvent.getIndex()]);
                }

            }
        }

        if (pcrLiuChengProcessValueEvent.getType() == GlobalDate.EventType.blockStateChange) {
            if (g_block_states[pcrLiuChengProcessValueEvent.getIndex()] == GlobalDate.BlockStateType.ready) {
                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(blocknames[pcrLiuChengProcessValueEvent.getIndex()]);
//                Spannable span = new SpannableString(blocknames[pcrLiuChengProcessValueEvent.getIndex()]);
//                span.setSpan(new AbsoluteSizeSpan(40), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                span.setSpan(new AbsoluteSizeSpan(20), 8, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                span.setSpan(new ForegroundColorSpan(Color.GREEN), 8, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(span);
                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setBackgroundResource(R.drawable.rb_lamp_block_ready_selector);
            } else if (g_block_states[pcrLiuChengProcessValueEvent.getIndex()] == GlobalDate.BlockStateType.done) {
                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setBackgroundResource(R.drawable.rb_lamp_block_done_selector);
//                String html = "<td><h3><font color=\"red\">This is some text!</font><h3></td><br/><td><h6><font color=\"green\">This</font><h6></td>";
//                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(Html.fromHtml(html));//blocknames[i] + "\n" + "实验完成"
                String text = blocknames[pcrLiuChengProcessValueEvent.getIndex()] + "\n" + "Finished";
                Spannable span = new SpannableString(text);
                span.setSpan(new AbsoluteSizeSpan(40), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new AbsoluteSizeSpan(20), 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(Color.GREEN), 8, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(span);
//                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setText(blocknames[pcrLiuChengProcessValueEvent.getIndex()] + "\n" + "实验完成");
            } else if (g_block_states[pcrLiuChengProcessValueEvent.getIndex()] == GlobalDate.BlockStateType.running) {
                rb_shiyan_blocks[pcrLiuChengProcessValueEvent.getIndex()].setBackgroundResource(R.drawable.rb_lamp_block_running_selector);
            }
        }
    }
}
