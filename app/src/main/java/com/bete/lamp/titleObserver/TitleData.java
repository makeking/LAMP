package com.bete.lamp.titleObserver;

import android.util.Log;

import java.util.ArrayList;

public class TitleData implements TitleSubject {
    private static final String TAG = "TitleData";
    private ArrayList<ActivityObserver> observers;
    private  String titleTime = "";
    private  String titleMiddle = "";
    private  String titleWendu = "";

    public String getTitleTime() {
        return this.titleTime;
    }

    public void setTitleTime(String titleTime) {
        this.titleTime = titleTime;
        notifyObserver();
    }

    public String getTitleMiddle() {
        return this.titleMiddle;
    }

    public void setTitleMiddle(String titleMiddle) {
        this.titleMiddle = titleMiddle;
        notifyObserver();
    }

    public String getTitleWendu() {
        return this.titleWendu;
    }

    public void setTitleWendu(String titleWendu) {
        this.titleWendu = titleWendu;
        notifyObserver();
    }

    private static class SingletonHolder{
        private final static TitleData instance=new TitleData();
    }

    public static TitleData getInstance(){
        return TitleData.SingletonHolder.instance;
    }

    private TitleData(){
        Log.e(TAG, "create: new TitleData" );
        // init variables
        observers = new ArrayList<ActivityObserver>();
    }

    @Override
    public void registerObserver(ActivityObserver o) {
        observers.add(o);
    }

    @Override
    public void removeOberver(ActivityObserver o) {
        int i;
        i = observers.indexOf(o);
        if(i >= 0){
            observers.remove(o);
        }
    }

    @Override
    public void notifyObserver() {
        for (int i = 0; i < observers.size(); i++) { //通知每一个观察者
            ActivityObserver observer = (ActivityObserver) observers.get(i);
            observer.update(this.titleTime, this.titleMiddle, this.titleWendu);
        }
    }
}
