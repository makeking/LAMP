package com.bete.lamp.ui.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bete.lamp.R;
import com.bete.lamp.adapter.ViewAdapter;
import com.bete.lamp.navigator.ScaleCircleNavigator;
import com.bete.lamp.ui.normal.BaseFragment;
import com.myutils.BitMapUtil;
import com.myutils.Canstant;
import com.utils.FileCommonUtil;
import com.utils.LogUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HelpFragment extends BaseFragment {
    private static final String TAG = "HelpFragment";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    private OnFragmentInteractionListener mListener;
    /////////////////////////////////////////////////////////////////////////
    private Context context;
    private int gloabIndex, gloabCount;
    private LayoutInflater viewinflater;
    //private View view1, view2, view3, view4;
    private View[] view1 = new View[8];
    private ViewPager viewPager;  //对应的viewPager
    private List<View> viewList = new ArrayList<View>();// 将要分页显示的View装入数组中;//view数组
    private MagicIndicator indicator;
    //    private ImageView img_help;
//    private TextView tv_help;
    private ImageView[] img_help = new ImageView[4];
    private TextView[] tv_help = new TextView[4];

    public HelpFragment() {
        // Required empty public constructor
    }

//    public static HelpFragment newInstance(String param1, String param2) {
//        HelpFragment fragment = new HelpFragment();
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
        LogUtils.e("HelpFragment onCreate");
        // 获取系统的内存
        ActivityManager _ActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo minfo = new ActivityManager.MemoryInfo();
        _ActivityManager.getMemoryInfo(minfo);
        LogUtils.e("HelpFragment onCreate 当前可用内存为:" + (minfo.availMem / (1024 * 1024) + "MB"));

    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d(TAG, "onStop();");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d(TAG, "onStart");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //TODO now visible to user
            LogUtils.d(TAG, "onHiddenChanged show");
        } else {
            //TODO now invisible to user
            LogUtils.d("onHiddenChanged hide");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
//        onHiddenChanged(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        context = getActivity();

        viewPager = (ViewPager) view.findViewById(R.id.help_viewpager);
        indicator = (MagicIndicator) view.findViewById(R.id.help_bottom_indicator);

        viewinflater = getActivity().getLayoutInflater();
//        List<File> fileList =FileCommonUtil.listFilesInDirWithFilter(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Canstant.INNER_HELP_DIR,".png",false);
//        if(fileList!=null) {
//            Collections.sort(fileList, new Comparator<File>() {
//                @Override
//                public int compare(File o1, File o2) {
//                    return o2.getName().compareTo(o1.getName());
//                }
//            });
//            for (int i=0;i<fileList.size();i++)
//            {
//                if(FileCommonUtil.isFileExists(fileList.get(i).getAbsolutePath().replaceAll("\\.png", "")))
//                {
//                    View  view11 = inflater.inflate(R.layout.help_viewpager_one, null);
//
//                    ImageView img_help11 = (ImageView) view11.findViewById(R.id.img_help);
////                    WebView webView = (WebView) view11.findViewById(R.id.webView);
//                    TextView tv_help11 = (TextView) view11.findViewById(R.id.tv_help);
//                    img_help11.setImageBitmap(BitMapUtil.getBitmap(fileList.get(i).getAbsolutePath(),521,305));
//                    tv_help11.setText(getStringFromFile(fileList.get(i).getAbsolutePath().replaceAll("\\.png", "")));
//                    //tv_help11.setText(Html.fromHtml(getStringFromFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "/LAMP/web/081518330500.html")));
////                    WebSettings webSettings = webView.getSettings();
////                    webSettings.setJavaScriptEnabled(true);
////                    webSettings.setBlockNetworkImage(false);
////                    //webSettings.setBuiltInZoomControls(true);
////                    //webSettings.setSupportZoom(true);
////                    webSettings.setUseWideViewPort(true);
////                    webSettings.setLoadWithOverviewMode(true);
////                    webSettings.setAllowFileAccess(true);
////                    webSettings.setSupportMultipleWindows(true);
////                    webSettings.setDomStorageEnabled(true);
////                    webSettings.setDefaultFontSize(30); //设置显示字体的大小
////                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
////                        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
////                    }
////                    //控制webview不可点击
////                    webView.setOnTouchListener(new View.OnTouchListener() {
////                        @Override
////                        public boolean onTouch(View v, MotionEvent event) {
////                            return false;
////                        }
////                    });
//////                    webView.loadUrl("https://www.baidu.com/");
////                    webView.loadUrl("file:///sdcard/LAMP/web/081518330500.html");
//                    viewList.add(view11);
//                }
//            }
//        }

        for (int i = 0; i < 8; i++) {
            View view1 = inflater.inflate(R.layout.help_viewpager_one, null);
            ImageView img_help1 = (ImageView) view1.findViewById(R.id.img_help);
            TextView tv_help1 = (TextView) view1.findViewById(R.id.tv_help);
            img_help1.setImageBitmap(getImageFromAssetsFile(getActivity(), "imagehelp" + (i + 1) + ".png"));
            tv_help1.setText(getStringFromAssetsFile(getActivity(), "stringhelp" + (i + 1)));
            viewList.add(view1);
        }

//        for (int i = 0; i < 8; i++) {
//            view1[i] = inflater.inflate(R.layout.help_viewpager_one, null);
//            viewList.add(view1[i]);
//            img_help[i] = (ImageView) view1[i].findViewById(R.id.img_help);
//            tv_help[i] = (TextView) view1[i].findViewById(R.id.tv_help);
//            img_help[i].setImageBitmap(getImageFromAssetsFile(getActivity(), "imagehelp" + (i + 1) + ".jpg"));
//            tv_help[i].setText(getStringFromAssetsFile(getActivity(), "stringhelp" + (i + 1)));
//        }

        PagerAdapter adapter = new ViewAdapter(viewList);
        viewPager.setAdapter(adapter);
        ScaleCircleNavigator navigator = new ScaleCircleNavigator(getActivity());
        navigator.setCircleCount(viewList.size());
        navigator.setNormalCircleColor(Color.DKGRAY);
        navigator.setSelectedCircleColor(Color.CYAN);
        navigator.setCircleClickListener(new ScaleCircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int index) {
                viewPager.setCurrentItem(index);
            }
        });
        indicator.setNavigator(navigator);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                indicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                indicator.onPageScrollStateChanged(state);
            }
        });
        ViewPagerHelper.bind(indicator, viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));


                return viewList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        gloabCount = pagerAdapter.getCount();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //读取文本文件中的内容
    public static String getStringFromFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }

//    /**
//     * 从文件中获取字符串
//     *
//     * @param fileName
//     * @param context
//     * @return
//     */
//    public static String getStringFromFile(Context context, String fileName) {
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            BufferedReader bf = new BufferedReader(new InputStreamReader(
//                    new FileInputStream(new File(fileName))));
//            String line;
//            while ( (line = bf.readLine()) != null ) {
//                stringBuilder.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return stringBuilder.toString();
//    }


    /* 读取Assets文件夹中的图片资源
     * @param context
     * @param fileName 图片名称
     * @return
     */
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 从文件中获取字符串
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getStringFromAssetsFile(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
