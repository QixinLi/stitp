package com.example.wind.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class FifteenAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){

        GetData getData=new GetData();
        boolean movement = getData.checkMovement();
        boolean gas = getData.checkGas();

        if(movement){
            NotificationUtil util=new NotificationUtil(context);
            util.notification("警告","未知人体活动，请小心！");
        }

        if(gas){
            NotificationUtil util=new NotificationUtil(context);
            util.notification("警告","检测到有害气体，请小心！");
        }

    }
}
