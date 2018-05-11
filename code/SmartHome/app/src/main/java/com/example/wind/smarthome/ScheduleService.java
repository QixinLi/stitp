package com.example.wind.smarthome;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleService extends Service {

    public static final String TAG="ScheduleService";
    private  Handler handler;
    private Runnable runnable;

    public ScheduleService() {
        handler=new Handler();
    }

    @Override
    public void onCreate(){
        super.onCreate();

        //每十分钟获取一次信息
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent,int flags,int startId) {
        runnable = new Runnable() {
            @Override
            public void run() {
//                GetData getData = new GetData();
//
//                String result=getData.getTHTips();
//                int num=0;
//
//                //检测异常气体
//                int checkResult=getData.checkGas();
//                if(checkResult==1){
//                    num=1;
//                }
//
//                //检测异常人体活动
//                checkResult=getData.checkMovement();
//                if (checkResult==2){
//                    if(num==1){
//                        num=3;
//                    }
//                    else{
//                        num=2;
//                    }
//                }

//                postNotification(num,result);

                handler.postDelayed(this, 60*1000);//每分钟运行一次
            }
        };
        runnable.run();
        return super.onStartCommand(intent,flags,startId);
    }

//    protected void postNotification(int type,String result){
//        NotificationUtil util=new NotificationUtil(this);
//        util.postNotification(type,result);
//    }
}

