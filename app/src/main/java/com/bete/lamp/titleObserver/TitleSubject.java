package com.bete.lamp.titleObserver;

public interface TitleSubject {
    public void registerObserver(ActivityObserver o);//注册观察者
    public void removeOberver(ActivityObserver o);//删除观察者
    public void notifyObserver();//通知观察者
}
