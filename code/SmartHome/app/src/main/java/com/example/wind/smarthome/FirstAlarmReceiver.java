package com.example.wind.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.example.wind.smarthome.GetData.weatherData;

public class FirstAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){

        GetData getData=new GetData();
        getData.getTipsData();

        NotificationUtil util=new NotificationUtil(context);
        util.notification(weatherData.get("weather").toString(),weatherData.get("advice").toString());
    }
}
