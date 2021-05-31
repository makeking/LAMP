package com.bete.lamp.ui.normal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.bete.lamp.ui.fragment.HelpFragment;
import com.myutils.GlobalDate;
import com.utils.LogUtils;
import com.utils.simpleArrayAdapter;

import static android.view.View.inflate;
import static com.myutils.GlobalDate.activity_name_list;
import static com.myutils.GlobalDate.g_device_state;

public class HelpActivity extends BaseActivity implements HelpFragment.OnFragmentInteractionListener {
    private static final String TAG = "HelpActivity";
    private Context context;
    private RadioGroup rg_radio;
    private EditSpinner sp_title;
    private Fragment[] mFragments;
    private RadioButton rb_help;
    public HelpFragment helpFragment;
    //当前Fragent的下标
    private int currentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = HelpActivity.this;
        setContentView(R.layout.activity_help);
        mFragments = new Fragment[]{
                //配置、实时、结果、设置
                helpFragment = new HelpFragment()
        };

        sp_title = (EditSpinner) findViewById(R.id.sp_title);
        initSpinner();
        // select the first item initially
        sp_title.selectItem(4);

        rg_radio = (RadioGroup) findViewById(R.id.rg_radio);
        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //设置为默认界面 MainHomeFragment
        ft.add(R.id.help_content, mFragments[0]).commit();
        rb_help = (RadioButton) findViewById(R.id.rb_help);
        //RadioGroup选中事件监听 改变fragment
        rg_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chaxun:
                        changeRadio(0);
                        break;
                }
            }
        });
        rb_help.setChecked(true);
        setIndexSelected(0);
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
                    convertView = inflate(HelpActivity.this, R.layout.layout_item, null);
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
                if(position==0)
                {
                    //todo 先打开帮助界面的内容 ，之后再次进行修改
//                    if (g_device_state == GlobalDate.DeviceStateType.needcheck) {
//                        CustomDialog.Builder builder = new CustomDialog.Builder(HelpActivity.this);
//                        builder.setMessage(getString(R.string.yiqixuyaozijian));
//                        builder.setTitle(getString(R.string.tishi));
//                        builder.setPositiveButton(getString(R.string.queding), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                startActivity(new Intent(context, SelfCheckActivity.class));
//                            }
//                        });
//                        builder.setNegativeButton(getString(R.string.quxiao), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                        CustomDialog customDialog = builder.create(R.layout.dialog_normal_feiquanping_select);
//                        customDialog.setCanceledOnTouchOutside(false);
//                        customDialog.setCancelable(false);
//                        customDialog.show();
//                    }
//                    else if (g_device_state == GlobalDate.DeviceStateType.ready) {
                        startActivity(new Intent(context, CheckActivity.class));
//                    }
                }
                else if(position==1)
                {
                    startActivity(new Intent(context, ProjectActivity.class));
                }
                else if(position==2)
                {
                    startActivity(new Intent(context, ChaXunActivity.class));
                }
                else if(position==3)
                {
                    startActivity(new Intent(context, SheZhiActivity.class));
                }
                else if(position==4)
                {
//                    startActivity(new Intent(context, HelpActivity.class));
                }
                sp_title.selectItem(4);
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
        ft.hide(mFragments[currentIndex]);
        //判断Fragment是否已经添加
        if (mFragments[index].isAdded()||null != mFragmentManager.findFragmentByTag( index + "" )) {
            ft.show(mFragments[index]);
        } else {
            //显示新的Fragment
            mFragmentManager.executePendingTransactions();
            ft.add(R.id.help_content, mFragments[index]).show(mFragments[index]);
            LogUtils.d(TAG+":"+"ADD!!!!!!!!!!!!!!!!");
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
        Toast.makeText(HelpActivity.this, "this is：" + uri, Toast.LENGTH_SHORT).show();
    }

    public void goHomeHandle(View view) {
        startActivity(new Intent(context, MainActivity.class));
//        finish();

    }

}
