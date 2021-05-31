package com.bete.lamp.ui.normal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bete.lamp.R;
import com.bete.lamp.customWidget.EditSpinner;
import com.bete.lamp.ui.fragment.MainShiYanFragment;
import com.bete.lamp.ui.fragment.ShiYanStep1Fragment;
import com.utils.LogUtils;
import com.utils.simpleArrayAdapter;

import static android.view.View.inflate;
import static com.myutils.GlobalDate.activity_name_list;

public class CheckActivity extends BaseActivity implements MainShiYanFragment.OnFragmentInteractionListener {
    private static final String TAG = "CheckActivity";
    private Context context;
    private RadioGroup rg_radio;
    private EditSpinner sp_title;
    private Fragment[] mFragments;
    private RadioButton rb_shiyan;
    public MainShiYanFragment mainShiYanFragment;
    //当前Fragent的下标
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = CheckActivity.this;
        setContentView(R.layout.activity_check);

        mFragments = new Fragment[]{
                //配置、实时、结果、设置
                mainShiYanFragment = new MainShiYanFragment()
        };

        sp_title = (EditSpinner) findViewById(R.id.sp_title);
        initSpinner();
        // select the first item initially
        sp_title.selectItem(0);

        rg_radio = (RadioGroup) findViewById(R.id.rg_radio);
        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //设置为默认界面 MainHomeFragment
        ft.add(R.id.check_content, mFragments[0]).commit();
        rb_shiyan = (RadioButton) findViewById(R.id.rb_shiyan);
        //RadioGroup选中事件监听 改变fragment
        rg_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_shiyan:
                        changeRadio(0);
                        break;
                }
            }
        });
        rb_shiyan.setChecked(true);
        setIndexSelected(0);
        //todo
//        startActivity(new Intent(context, ChaXunActivity.class));
//        startActivity(new Intent(context, SheZhiActivity.class));
//        startActivity(new Intent(context, HelpActivity.class));
    }

    private void initSpinner() {
        sp_title.setDropDownDrawable(getResources().getDrawable(R.drawable.gothrought), 0, 25);
        sp_title.setDropDownBackgroundResource(R.color.white);//R.drawable.custom_editor_bkg_normal//getResources().getDrawable(R.drawable.spin_background)
        sp_title.setDropDownVerticalOffset(0);
        sp_title.setDropDownDrawableSpacing(60);
        sp_title.setDropDownHeight(240);
        sp_title.setEditable(false);
        sp_title.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return activity_name_list.toArray().length;
            }

            @Override
            public Object getItem(int position) {
                return activity_name_list.toArray()[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = inflate(CheckActivity.this, R.layout.layout_item, null);
                }

//                ImageView icon = (ImageView)convertView.findViewById(R.id.item_icon);
                TextView textView = (TextView) convertView.findViewById(R.id.item_text);

                String data = (String) getItem(position);

//                icon.setImageResource(R.mipmap.ic_launcher);
                textView.setText(data);

                return convertView;
            }
        });

        // it converts the item in the list to a string shown in EditText.
        sp_title.setItemConverter(new EditSpinner.ItemConverter() {
            @Override
            public String convertItemToString(Object selectedItem) {
                String string;
                string = selectedItem.toString();
                return string;
            }
        });

        sp_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        // triggered when one item in the list is clicked
        sp_title.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.d("position:" + position);
                if (position == 0) {
//                    startActivity(new Intent(context, CheckActivity.class));
                } else if (position == 1) {
                    startActivity(new Intent(context, ProjectActivity.class));
                } else if (position == 2) {
                    startActivity(new Intent(context, ChaXunActivity.class));
                } else if (position == 3) {
                    startActivity(new Intent(context, SheZhiActivity.class));
                } else if (position == 4) {
                    startActivity(new Intent(context, HelpActivity.class));
                }
                sp_title.selectItem(0);
                sp_title.clearFocus();
            }
        });

        sp_title.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
            }
        });
    }

    //设置Fragment页面
    private void setIndexSelected(int index) {
        if (currentIndex == index) {
            return;
        }
        FragmentManager mFragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        //隐藏当前Fragment
        if (currentIndex == 0)
            ft.remove(mFragments[currentIndex]);
        else
            ft.hide(mFragments[currentIndex]);
        //判断Fragment是否已经添加
        if (mFragments[index].isAdded() || null != mFragmentManager.findFragmentByTag(index + "")) {
            ft.show(mFragments[index]);
        } else {
            //显示新的Fragment
            if (index == 0)
                mFragments[0] = new MainShiYanFragment();
            mFragmentManager.executePendingTransactions();
            ft.add(R.id.check_content, mFragments[index]).show(mFragments[index]);
            LogUtils.d(TAG + ":" + "ADD!!!!!!!!!!!!!!!!");
            ft.addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
        currentIndex = index;
    }

    public void changeRadio(int index) {
        if (currentIndex == index)
            return;
        if (index == 0) {
            setIndexSelected(index);
            currentIndex = index;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(CheckActivity.this, "this is：" + uri, Toast.LENGTH_SHORT).show();
    }

    public void goHomeHandle(View view) {
        startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isForegroundFragment("ShiYanStep1Fragment")) {
            Fragment fragment = getFragment("ShiYanStep1Fragment");
            if (fragment != null) {
                ((ShiYanStep1Fragment) fragment).onHiddenChanged(false);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isForegroundFragment("ShiYanStep1Fragment")) {
            Fragment fragment = getFragment("ShiYanStep1Fragment");
            if (fragment != null) {
                ((ShiYanStep1Fragment) fragment).onHiddenChanged(false);
            }
        }
    }
}
