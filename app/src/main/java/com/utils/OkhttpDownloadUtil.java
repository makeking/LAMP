package com.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpDownloadUtil {

    private static OkhttpDownloadUtil downloadUtil;
    private final OkHttpClient.Builder okHttpClientBuilder;

    public static OkhttpDownloadUtil getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new OkhttpDownloadUtil();
        }
        return downloadUtil;
    }

    private OkhttpDownloadUtil() {
        okHttpClientBuilder = new OkHttpClient().newBuilder();
    }

    /**
     * @param url 下载连接
     * @param filepath 储存下载文件的文件路径
     * @param listener 下载监听
     */
    public void download(final String url, final String filepath,final int timeout, final OnDownloadListener listener) {
        Request request = new Request.Builder().get().url(url).build();
        OkHttpClient  okHttpClient= okHttpClientBuilder.readTimeout(timeout, TimeUnit.MILLISECONDS).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    // 储存下载文件的目录
                    String destFileDir = FileCommonUtil.getFileByPath(filepath).getParent();
                    File dir = new File(destFileDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    if(FileCommonUtil.isFileExists(filepath))
                        FileCommonUtil.deleteDirOrFile(filepath);
                    File file = FileCommonUtil.getFileByPath(filepath);
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        LogUtils.e("total:" + total);
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            // 下载中更新进度条
                            listener.onDownloading(progress);
                        }
                        fos.flush();
                        // 下载完成
                        listener.onDownloadSuccess();
                } catch (Exception e) {
                    LogUtils.d(e);
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}