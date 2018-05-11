package com.example.wind.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SecondAlarmService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        GetData getData=new GetData();
        int temper=getData.getTemperTips();
        int humi=getData.getHumidityTips();
        NotificationUtil temperUtil=new NotificationUtil(context);
        if(temper==1){
            temperUtil.notification("室内温度较高","建议打开冷气");
        }
        else if(temper==2){
            temperUtil.notification("室内温度较低","建议打开暖气");
        }
        else {

        }

        NotificationUtil humiUtil=new NotificationUtil(context);
        if (humi==1){
            humiUtil.notification("室内湿度较高","建议打开窗户除湿");
        }
        else if(humi==2){
            humiUtil.notification("室内湿度较低","建议打开增湿器");
        }
        else {

        }
    }
}
