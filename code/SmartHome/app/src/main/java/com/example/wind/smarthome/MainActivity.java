package com.example.wind.smarthome;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

import static com.example.wind.smarthome.GetData.equipmentData;
import static com.example.wind.smarthome.GetData.weatherData;
import static com.example.wind.smarthome.GetData.leaveData;
import static com.example.wind.smarthome.GetData.backData;

public class MainActivity extends AppCompatActivity {

    private TextView outsideTemper,outsideHumi,outsideWea,adviceText;
    private TextView insideTemper,insideHumi,insideGas,insideMovement;

    private int leaveTime,backTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        outsideTemper=findViewById(R.id.main_outsideTemper);
        outsideHumi=findViewById(R.id.main_outsideHumidity);
        outsideWea=findViewById(R.id.main_outsideWeather);
        adviceText=findViewById(R.id.main_advice);

        insideTemper=findViewById(R.id.main_insideTemper);
        insideHumi=findViewById(R.id.main_insideHumidity);
        insideGas=findViewById(R.id.main_insideGas);
        insideMovement=findViewById(R.id.main_insideMovement);

        loadLayout();

        //ScheduleService
//        Intent intent=new Intent(MainActivity.this,ScheduleService.class);
//        startService(intent);

        //调用闹钟方法
        setFifteenAlarm();
        setFirstDayAlarm();
        setSecondDayAlarm();
    }

    private void loadLayout(){
//
        GetData data=new GetData();

        //设置提醒信息
        data.getTipsData();

        if (weatherData!=null){
            outsideTemper.setText(weatherData.get("temper").toString()+"℃");
            outsideHumi.setText("湿度:\n"+weatherData.get("humi").toString());
            outsideWea.setText("天气:\n"+weatherData.get("weather").toString());
            adviceText.setText(data.weatherData.get("advice").toString());
        }

        //先从服务器获取设备信息
        data.getEquipData();

        //设置检测到的信息
        if(equipmentData!=null){
            insideTemper.setText("室内温度:\n"+equipmentData.get("temper").toString()+"℃");
            insideHumi.setText("室内湿度:\n"+equipmentData.get("humi").toString()+"%");
            insideGas.setText("上一次检测到有害气体:\n"+equipmentData.get("gas").toString());
            insideMovement.setText("上一次检测到人体动作:\n"+equipmentData.get("lastPass").toString());
        }

        data.getTimeData();
        if(leaveData!=null){
            leaveTime=Integer.parseInt(leaveData.get("time").toString());
        }
        if(backData!=null){
            backTime=Integer.parseInt(backData.get("time").toString());
        }

    }

    private void setFifteenAlarm(){
        Intent alarmIntent=new Intent(MainActivity.this,FifteenAlarmReceiver.class);
        PendingIntent sender=PendingIntent.getBroadcast(MainActivity.this,0,alarmIntent,0);

        long firstTime= SystemClock.elapsedRealtime();
        long systemTime=System.currentTimeMillis();

        //设置闹钟
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        //设置时区b并设定闹钟
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        //选择的定时时间
        long selectedTime=calendar.getTimeInMillis();
        //如果当前时间大于设置的时间，那么从第二天的设定时间开始
        if(systemTime>selectedTime){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            selectedTime=calendar.getTimeInMillis();
        }

        //计算现在时间到设定时间的差
        long time=selectedTime-systemTime;
        firstTime+=time;

        AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime,AlarmManager.INTERVAL_FIFTEEN_MINUTES,sender);
    }

    private void setFirstDayAlarm(){
        Intent alarmIntent=new Intent(MainActivity.this,FirstAlarmReceiver.class);
        PendingIntent firstSender=PendingIntent.getBroadcast(MainActivity.this,0,alarmIntent,0);

        long firstTime= SystemClock.elapsedRealtime();
        long systemTime=System.currentTimeMillis();

        //设置闹钟
        Calendar firstCalendar=Calendar.getInstance();
        firstCalendar.setTimeInMillis(System.currentTimeMillis());

        //设置时区b并设定闹钟
        firstCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        firstCalendar.set(Calendar.MINUTE,50);
        firstCalendar.set(Calendar.HOUR_OF_DAY,leaveTime-1);
        firstCalendar.set(Calendar.SECOND,0);
        firstCalendar.set(Calendar.MILLISECOND,0);

        //选择的定时时间
        long selectedTime=firstCalendar.getTimeInMillis();
        //如果当前时间大于设置的时间，那么从第二天的设定时间开始
        if(systemTime>selectedTime){
            firstCalendar.add(Calendar.DAY_OF_MONTH,1);
            selectedTime=firstCalendar.getTimeInMillis();
        }

        //计算现在时间到设定时间的差
        long time=selectedTime-systemTime;
        firstTime+=time;

        AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime,AlarmManager.INTERVAL_DAY,firstSender);
    }

    private void setSecondDayAlarm(){
        Intent alarmIntent=new Intent(MainActivity.this,FirstAlarmReceiver.class);
        PendingIntent secondSender=PendingIntent.getBroadcast(MainActivity.this,0,alarmIntent,0);

        long firstTime= SystemClock.elapsedRealtime();
        long systemTime=System.currentTimeMillis();

        //设置闹钟
        Calendar secondCalendar=Calendar.getInstance();
        secondCalendar.setTimeInMillis(System.currentTimeMillis());

        //设置时区b并设定闹钟
        secondCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        secondCalendar.set(Calendar.MINUTE,50);
        secondCalendar.set(Calendar.HOUR_OF_DAY,backTime-1);
        secondCalendar.set(Calendar.SECOND,0);
        secondCalendar.set(Calendar.MILLISECOND,0);

        //选择的定时时间
        long selectedTime=secondCalendar.getTimeInMillis();
        //如果当前时间大于设置的时间，那么从第二天的设定时间开始
        if(systemTime>selectedTime){
            secondCalendar.add(Calendar.DAY_OF_MONTH,1);
            selectedTime=secondCalendar.getTimeInMillis();
        }

        //计算现在时间到设定时间的差
        long time=selectedTime-systemTime;
        firstTime+=time;

        AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime,AlarmManager.INTERVAL_DAY,secondSender);
    }
}
