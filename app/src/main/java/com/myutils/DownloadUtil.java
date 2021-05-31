package com.myutils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.webkit.MimeTypeMap;

/**
 * 下载类, 默认下载春雨医生APK, 也可以下载其他APK.
 * 存储位置: /sdcard/download/filename
 * <p/>
 * Created by C.L.Wang on 15/7/27.
 */
public class DownloadUtil {

    private static final String CHUNYU_DOWNLOAD_URL = "http://172.16.16.181:8080/lamp-web/download/app-release.apk";
//            "http://www.chunyuyisheng.com/download/chunyu/latest/";

    private static final String APK_TYPE =
            "application/vnd.android.package-archive";

    private static final String DEFAULT_FILE_NAME = "app-release.apk";

    private static final String DEFAULT_TITLE = "春雨医生";

    private Context mContext; // 下载进行
    private String mUrl; // URL地址
    private String mFileName; // 文件名
    private String mTitle; // 通知栏标题

    private DownloadManager mDownloadManager; // 下载管理器
    private long mDownloadId; // 下载ID

    // 下载完成的接收器
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == mDownloadId) {
                installApp(mDownloadId);
                destroyArgs();
            }
        }
    };

    /**
     * 默认构造器, 下载春雨医生APK
     *
     * @param context 上下文
     */
    public DownloadUtil(Context context) {
        this(context, CHUNYU_DOWNLOAD_URL, DEFAULT_FILE_NAME, DEFAULT_TITLE);
    }

    /**
     * 参数构造器, 下载其他文件
     *
     * @param context  活动
     * @param url      URL
     * @param fileName 存储文件名
     * @param title    通知栏标题
     */
    public DownloadUtil(Context context, String url, String fileName, String title) {
        mContext = context;
        mUrl = url;
        mFileName = fileName;
        mTitle = title;
        initArgs();
    }

    /**
     * 下载文件
     */
    public void download() {
        // 设置下载Url
        Uri resource = Uri.parse(mUrl);
        DownloadManager.Request request = new DownloadManager.Request(resource);

        // 设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(mUrl));
        request.setMimeType(mimeString);

        // 下载完成时在进度条提示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // 存储sdcard目录的download文件夹
        request.setDestinationInExternalPublicDir("/download/", mFileName);
        request.setTitle(mTitle);

        // 开始下载
        mDownloadId = mDownloadManager.enqueue(request);
    }

    // 初始化
    private void initArgs() {
        mDownloadManager = (DownloadManager) mContext.getSystemService((Context.DOWNLOAD_SERVICE));
        mContext.registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    // 析构
    private void destroyArgs() {
        mContext.unregisterReceiver(mReceiver);
    }

    // 安装App
    private void installApp(long downloadId) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        Uri downloadFileUri = mDownloadManager.getUriForDownloadedFile(downloadId);
        install.setDataAndType(downloadFileUri, APK_TYPE);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(install);
    }

}