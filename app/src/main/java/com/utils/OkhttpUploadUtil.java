package com.utils;

import com.myutils.GlobalDate;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import static com.myutils.Canstant.ONLINE_UPDATEULR;

public class OkhttpUploadUtil {

    private static OkhttpUploadUtil uploadUtil;
    private final OkHttpClient.Builder okHttpClientBuilder;

    public static OkhttpUploadUtil getInstance() {
        if (uploadUtil == null) {
            uploadUtil = new OkhttpUploadUtil();
        }
        return uploadUtil;
    }

    private OkhttpUploadUtil() {
        okHttpClientBuilder = new OkHttpClient().newBuilder();
    }

    /**
     * @param mediaType MediaType
     * @param uploadUrl put请求地址
     * @param localPath 本地文件路径
     * @return 响应的结果 和 HTTP status code
     * @throws IOException
     */
        public void upload(String uploadPath, String localPath,final OnUploadListener listener,final ProgressListener progressListener){
        File file = new File(localPath);
        String url= "http://"+ GlobalDate.defaultServerCanShu.getIpaddr()+":"+GlobalDate.defaultServerCanShu.getPort()+ONLINE_UPDATEULR;
        LogUtils.d(url);
//        RequestBody body = RequestBody.create(mediaType, file);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if(progressListener==null) {
            builder.addFormDataPart("file", uploadPath+file.getName(), createCustomRequestBody(MEDIA_TYPE_TEXT, file, new ProgressListener() {//file.getName()
                @Override public void onProgress(long totalBytes, long remainingBytes, boolean done) {
                    System.out.print((totalBytes - remainingBytes) * 100 / totalBytes + "%");
                }
            }));
        }
        else {
            builder.addFormDataPart("file",uploadPath+file.getName(), createCustomRequestBody(MEDIA_TYPE_TEXT, file, progressListener));//file.getName()
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        //修改各种 Timeout
        OkHttpClient okHttpClient = okHttpClientBuilder.connectTimeout(600, TimeUnit.SECONDS).readTimeout(200, TimeUnit.SECONDS).writeTimeout(600, TimeUnit.SECONDS).build();
        //如果不需要可以直接写成 OkHttpClient client = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                listener.onUploadFailed();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listener.onUploadSuccess();
            }
        });
    }

    private static final MediaType MEDIA_TYPE_ZIP = MediaType.parse("application/zip; charset=utf-8");
    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_OBJECT = MediaType.parse("application/octet-stream");
    public static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final ProgressListener listener) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return file.length();
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public String put(MediaType mediaType, String uploadUrl, String localPath) throws IOException {
        File file = new File(localPath);
        RequestBody body = RequestBody.create(mediaType, file);
        Request request = new Request.Builder()
                .url(uploadUrl)
                .put(body)
                .build();
        //修改各种 Timeout
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .build();
        //如果不需要可以直接写成 OkHttpClient client = new OkHttpClient.Builder().build();

        Response response = client
                .newCall(request)
                .execute();
        return response.body().string() + ":" + response.code();
    }

    public interface ProgressListener {
        void onProgress(long totalBytes, long remainingBytes, boolean done);
    }

    public interface OnUploadListener {
        /**
         * 下载成功
         */
        void onUploadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onUploadding(int progress);

        /**
         * 下载失败
         */
        void onUploadFailed();
    }

    private static OkHttpClient client;

    /**
     * 创建一个OkHttpClient的对象的单例
     * @return
     */
    private synchronized static OkHttpClient getOkHttpClientInstance() {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    //设置连接超时等属性,不设置可能会报异常
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS);

            client = builder.build();
        }
        return client;
    }
    /**
     * 获取文件MimeType
     *
     * @param filename
     * @return
     */
    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }


    /**
     * 获得Request实例(不带进度)
     * @param url
     * @return
     */
    private static Request getRequest(String url,String uploadPath, List<String> fileNames) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(getRequestBody(uploadPath,fileNames));
        return builder.build();
    }

    /**
     * 通过上传的文件的完整路径生成RequestBody
     * @param fileNames 完整的文件路径
     * @return
     */
    private static RequestBody getRequestBody(String uploadPath, List<String> fileNames) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) { //对文件进行遍历
            File file = new File(fileNames.get(i)); //生成文件
            //根据文件的后缀名，获得文件类型
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart( //给Builder添加上传的文件
                    "image",  //请求的名字
                    uploadPath+file.getName(), //文件的文字，服务器端用来解析的
                    RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
            );
        }
        return builder.build(); //根据Builder创建请求
    }

    /**
     * 根据url，发送异步Post请求(不带进度)
     * @param url 提交到服务器的地址
     * @param fileNames 完整的上传的文件的路径名
     * @param callback OkHttp的回调接口
     */
    public static void doPostRequest(String uploadPath, List<String> fileNames, Callback callback) {
        String url = "http://"+GlobalDate.defaultServerCanShu.getIpaddr()+":"+GlobalDate.defaultServerCanShu.getPort()+ONLINE_UPDATEULR;
        Call call = getOkHttpClientInstance().newCall(getRequest(url,uploadPath,fileNames));
        call.enqueue(callback);
    }

    //获取字符串
    public static String getString(Response response) throws IOException {
        if (response != null && response.isSuccessful()) {
            return response.body().string();
        }
        return null;
    }

}