package com.bete.lamp.ui.normal;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bete.lamp.ActivityCollector;
import com.bete.lamp.R;
import com.bete.lamp.message.StateCodeEvent;
import com.bete.lamp.titleObserver.ActivityObserver;
import com.bete.lamp.titleObserver.TitleData;
import com.myutils.SharedPreferencesUtils;
import com.utils.LogUtils;
import com.leon.lfilepickerlibrary.utils.NavigationBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import me.jessyan.autosize.internal.CustomAdapt;

import static com.myutils.GlobalDate.clearDefaultLauncher;
import static com.myutils.GlobalDate.g_daohangjianEnable;
import static com.myutils.GlobalDate.g_homeEnable;
import static com.myutils.GlobalDate.setDefaultLauncher;

public abstract class BaseActivity extends AppCompatActivity implements CustomAdapt,ActivityObserver {
    private String TAG = "BaseActivity";
    public CustomDialog.Builder builder;
    public CustomDialog customDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LogUtils.d("BaseActivity", getClass().getSimpleName());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityCollector.addActivity(this);
        TitleData.getInstance().registerObserver(this);
        builder = new CustomDialog.Builder(this);
        //CloseBarUtil.closeBar();
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForegroundActvity(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
//        boolean flag=false;
        for (ActivityManager.RunningTaskInfo taskInfo : list) {
            if (taskInfo.topActivity.getShortClassName().contains(className)) { // 说明它已经启动了
//                flag = true;
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public Fragment getFragment(String className) {
        synchronized (this) {
            FragmentManager ft = getSupportFragmentManager();
            List<Fragment> list = ft.getFragments();
            for (Fragment fragment : list) {
                if (fragment.getClass().getSimpleName().equals(className)) {
                    LogUtils.d(fragment.getClass().getSimpleName() + "!!!!!!!!!!");
                    return fragment;
                }

                Fragment fragment1 = getChildFragment(fragment, className);
                if (fragment1 != null)
                    return fragment1;
            }
            return null;
        }
    }

    public Fragment getChildFragment(Fragment fragment, String className) {
        if (fragment == null || TextUtils.isEmpty(className))
            return null;
//        LogUtils.d(fragment.getClass().getSimpleName());
//        if (fragment.getClass().getSimpleName().equals(className)) {
//            LogUtils.d(fragment.getClass().getSimpleName() + "!!!!!!!!!!");
//            return fragment;
//        }
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        for (Fragment fragment1 : list) {
            if (fragment1.getClass().getSimpleName().equals(className)) {
                LogUtils.d(fragment1.getClass().getSimpleName() + "!!!!!!!!!!");
                return fragment1;
            }
            Fragment fragment2 = getChildFragment(fragment1, className);
            if (fragment2 != null)
                return fragment2;
        }
        return null;
    }

//    /**
//     * 判断某个界面是否在前台
//     *
//     * @param className 界面的类名
//     * @return 是否在前台显示
//     */
//    public boolean isForegroundFragment(String className) {
//        synchronized (this) {
//            FragmentManager ft = getSupportFragmentManager();
//            List<Fragment> list = ft.getFragments();
//            for (Fragment fragment : list) {
//                if (((BaseFragment) fragment).ismUserVisibleHint()) {
//                    if (fragment.getClass().getSimpleName().equals(className)) {
//                        LogUtils.d(fragment.getClass().getSimpleName());
//                        return true;
//                    } else {
//                        LogUtils.d(fragment.getClass().getSimpleName());
//                        if (isForegroundChildFragment(fragment, className))
//                            return true;
//                    }
//                }
//            }
//            return false;
//        }
//    }
//
//    public boolean isForegroundChildFragment(Fragment fragment, String className) {
//        if (fragment == null || TextUtils.isEmpty(className))
//            return false;
//        FragmentManager fragmentManager = fragment.getChildFragmentManager();
//        List<Fragment> list = fragmentManager.getFragments();
//        for (Fragment fragment1 : list) {
//            if (((BaseFragment) fragment1).ismUserVisibleHint()) {
//                if (fragment1.getClass().getSimpleName().equals(className)) {
//                    LogUtils.d(fragment1.getClass().getSimpleName());
//                    return true;
//                } else {
//                    LogUtils.d(fragment1.getClass().getSimpleName());
//                    if (isForegroundChildFragment(fragment1, className))
//                        return true;
//                }
//            }
//        }
//        return false;
//    }

    /**
     * 判断某个界面是否在前台
     *
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public boolean isForegroundFragment(String className) {
        synchronized (this) {
            FragmentManager ft = getSupportFragmentManager();
            List<Fragment> list = ft.getFragments();
            for (Fragment fragment : list) {
                if (fragment.getClass().getSimpleName().equals(className)) {
                    if (((BaseFragment) fragment).ismUserVisibleHint()) {
                        return true;
                    }
                    return false;
                }
//                LogUtils.d(fragment.getClass().getSimpleName());
                if (isForegroundChildFragment(fragment, className))
                    return true;
            }
            return false;
        }
    }

    public boolean isForegroundChildFragment(Fragment fragment, String className) {
        if (fragment == null || TextUtils.isEmpty(className))
            return false;

        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        for (Fragment fragment1 : list) {
            if (fragment1.getClass().getSimpleName().equals(className)) {
                if (((BaseFragment) fragment1).ismUserVisibleHint()) {
                    return true;
                }
//                LogUtils.d(fragment1.getClass().getSimpleName());
                return false;
            }
//            LogUtils.d(fragment1.getClass().getSimpleName());
            if (isForegroundChildFragment(fragment1, className))
                return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (g_daohangjianEnable) {
            NavigationBarUtil.showNavigationBar(getWindow());
        } else {
            NavigationBarUtil.hideNavigationBar(getWindow());
        }
//        if (g_homeEnable) {
//            clearDefaultLauncher(this);
//            String defPackageName =getPackageName();
//            String defClassName = "com.bete.lamp.ui.normal.SplashActivity";
//            setDefaultLauncher(this,defPackageName,defClassName);
//        } else {
//            clearDefaultLauncher(this);
//            String defPackageName = "com.android.launcher3";
//            String defClassName = "com.android.launcher3.Launcher";
//            setDefaultLauncher(this,defPackageName,defClassName);
//        }
        TitleData.getInstance().setTitleMiddle("");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //LogUtils.e(TAG, "onRestart");
    }

//    @Override
//    protected void onStop(){
//        super.onStop();
//        if(g_daohangjianEnable) {
//            NavigationBarUtil.hideNavigationBar(getWindow());
//        }else {
//            NavigationBarUtil.showNavigationBar(getWindow());
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        TitleData.getInstance().removeOberver(this);
        //LogUtils.e(TAG, "onDestroy");
    }

    public static String getAppPackageName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        //LogUtils.d("lixx", "当前应用:" + componentInfo.getPackageName());
        return componentInfo.getPackageName();
    }

    public void update(String titleTime, String titleMiddle, String titleWendu) {
        TextView tv_title_time = (TextView) findViewById(R.id.title_time);
        if (tv_title_time != null)
            tv_title_time.setText(titleTime);
    }

    /**
     * 消息接收并显示的方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StateCodeEvent commandEvent) {
        //Toast.makeText(this, "........." + String.valueOf(commandEvent), Toast.LENGTH_SHORT).show();
        LogUtils.d(commandEvent);
        if ((commandEvent.getCodeState() & (int) 0xFE) != 0) {
            if (customDialog != null)
                customDialog.dismiss();
            builder.setMessage("错误码:" + String.format("%08x", commandEvent.getCodeState()));
            builder.setTitle("错误");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            customDialog = builder.create(R.layout.dialog_normal_feiquanping_message);
            customDialog.show();
        }
    }

//    /**
//     * 消息接收并显示的方法
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(PCRLiuChengProcessValueEvent pcrLiuChengProcessValueEvent) {
//        if (pcrLiuChengProcessValueEvent.getShengyutime() <= 0) {
//            if (customDialog != null)
//                customDialog.dismiss();
//            builder.setMessage("实验完成");
//            builder.setTitle("消息");
//            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            customDialog = builder.create();
//            customDialog.show();
//        }
//    }

//    public boolean onTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//                if (v != null) {
//                    if (v.getWindowToken() != null) {
//                        imm.hideSoftInputFromWindow(v.getWindowToken(),
//                                InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                }
//            }
//        }
//        return super.onTouchEvent(ev);
//    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        hideInputWhenTouchOtherView(this, ev, null);//getExcludeTouchHideInputViews()
//        return super.dispatchTouchEvent(ev);
//    }
//
//    /**
//     * 当点击其他View时隐藏软键盘
//     * @param activity
//     * @param ev
//     * @param excludeViews  点击这些View不会触发隐藏软键盘动作
//     */
//    public static final void hideInputWhenTouchOtherView(Activity activity, MotionEvent ev, List<View> excludeViews){
//        if (ev.getAction() == MotionEvent.ACTION_DOWN){
//            if (excludeViews != null && !excludeViews.isEmpty()){
//                for (int i = 0; i < excludeViews.size(); i++){
//                    if (isTouchView(excludeViews.get(i), ev)){
//                        return;
//                    }
//                }
//            }
//            View v = activity.getCurrentFocus();
//            if (isShouldHideInput(v, ev)){
//                InputMethodManager inputMethodManager = (InputMethodManager)
//                        activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (inputMethodManager != null){
//                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//
//        }
//    }
//
//    public static final boolean isTouchView(View view, MotionEvent event){
//        if (view == null || event == null){
//            return false;
//        }
//        int[] leftTop = {0, 0};
//        view.getLocationInWindow(leftTop);
//        int left = leftTop[0];
//        int top = leftTop[1];
//        int bottom = top + view.getHeight();
//        int right = left + view.getWidth();
//        if (event.getRawX() > left && event.getRawX() < right
//                && event.getRawY() > top && event.getRawY() < bottom){
//            return true;
//        }
//        return false;
//    }
//
//    public static final boolean isShouldHideInput(View v, MotionEvent event){
//        if (v != null && (v instanceof EditText)){
//            return !isTouchView(v, event);
//        }
//        return false;
//    }

    //事件分发控制
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    /**
     * 判定是否需要隐藏
     */
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    /**
     * 隐藏软键盘
     */
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //监控/拦截/屏蔽返回键
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 0;
    }

}
