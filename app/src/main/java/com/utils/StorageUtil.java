package com.utils;

import android.content.Context;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class StorageUtil {

    private StorageUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String SDROOT = "/storage/sdcard1";
    //public static String UPANROOT = "/storage/usbotg";
    public static String UPANROOT = "/storage/usbotg";
    public static String UPANROOTX = "/storage/usbotg";
//    public static String UPANROOT = "/storage/usbotg/usbotg-sda2";
//    public static String UPANROOTX = "/storage/usbotg/usbotg-sda";
    public static List<StorageInfo> listAllStorage(Context context) {
        ArrayList<StorageInfo> storages = new ArrayList<StorageInfo>();
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList", paramClasses);
            Object[] params = {};
            Object[] invokes = (Object[]) getVolumeList.invoke(storageManager, params);

            if (invokes != null) {
                StorageInfo info = null;
                for (int i = 0; i < invokes.length; i++) {
                    Object obj = invokes[i];
                    Method getPath = obj.getClass().getMethod("getPath", new Class[0]);
                    String path = (String) getPath.invoke(obj, new Object[0]);
                    info = new StorageInfo(path);

                    Method getVolumeState = StorageManager.class.getMethod("getVolumeState", String.class);
                    String state = (String) getVolumeState.invoke(storageManager, info.path);
                    info.state = state;

                    Method isRemovable = obj.getClass().getMethod("isRemovable", new Class[0]);
                    info.isRemoveable = ((Boolean) isRemovable.invoke(obj, new Object[0])).booleanValue();
                    storages.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        storages.trimToSize();
        return storages;
    }

    public static List<StorageInfo> getAvaliableStorage(List<StorageInfo> infos){
        List<StorageInfo> storages = new ArrayList<StorageInfo>();
        for(StorageInfo info : infos){
            File file = new File(info.path);
            if ((file.exists()) && (file.isDirectory()) && (file.canWrite())) {
                if (info.isMounted()) {
                    storages.add(info);
                }
            }
        }
        return storages;
    }

    public static boolean IsSDExist(Context context)
    {
        ArrayList<StorageInfo> storages = new ArrayList<StorageInfo>();
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList", paramClasses);
            Object[] params = {};
            Object[] invokes = (Object[]) getVolumeList.invoke(storageManager, params);

            if (invokes != null) {
                StorageInfo info = null;
                for (int i = 0; i < invokes.length; i++) {
                    Object obj = invokes[i];
                    Method getPath = obj.getClass().getMethod("getPath", new Class[0]);
                    String path = (String) getPath.invoke(obj, new Object[0]);

                    if(path.equals(SDROOT)) {
                        Method getVolumeState = StorageManager.class.getMethod("getVolumeState", String.class);
                        String state = (String) getVolumeState.invoke(storageManager, path);
                        if (state.equals("mounted")) {
                            File file = new File(path);
                            if ((file.exists()) && (file.isDirectory()) && (file.canWrite())) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isUpanExist(Context context)
    {
        ArrayList<StorageInfo> storages = new ArrayList<StorageInfo>();
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList", paramClasses);
            Object[] params = {};
            Object[] invokes = (Object[]) getVolumeList.invoke(storageManager, params);

            if (invokes != null) {
                StorageInfo info = null;
                for (int i = 0; i < invokes.length; i++) {
                    Object obj = invokes[i];
                    Method getPath = obj.getClass().getMethod("getPath", new Class[0]);
                    String path = (String) getPath.invoke(obj, new Object[0]);

                    if(path.contains(UPANROOTX)) {
                        Method getVolumeState = StorageManager.class.getMethod("getVolumeState", String.class);
                        String state = (String) getVolumeState.invoke(storageManager, path);
                        if (state.equals("mounted")) {
                            File file = new File(path);
                            if ((file.exists()) && (file.isDirectory()) && (file.canWrite())) {
                                UPANROOT = path;
                                LogUtils.d("path:"+path);
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
