package com.bete.lamp.ui.normal;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.utils.LogUtils;

public abstract class BaseFragment extends Fragment{
    public boolean ismUserVisibleHint() {
        return mUserVisibleHint;
    }

    public void setmUserVisibleHint(boolean mUserVisibleHint) {
        this.mUserVisibleHint = mUserVisibleHint;
    }

    private boolean mUserVisibleHint = true; //当前fragment是否可见
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            mUserVisibleHint = false;
            LogUtils.v("不可见");
        } else {
            mUserVisibleHint = true;
            LogUtils.v("可见");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    public void changeAppLanguage() {
//        // 本地语言设置
//        Locale myLocale;
//        if(lang.equals("en"))
//        {
//            myLocale = Locale.ENGLISH;
//        }else {
//            myLocale = Locale.CHINESE;
//        }
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
//    }
//
//    /**
//     * 消息接收并显示的方法
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(String str) {
//        if(str.equals("EVENT_REFRESH_LANGUAGE"))
//        {
//            LogUtils.d("EVENT_REFRESH_LANGUAGE");
//            changeAppLanguage();
//            recreate();//刷新界面
//        }
//    }

}
