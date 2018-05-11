package com.example.wind.smarthome;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationUtil {
    private Context context;
    private NotificationManager notificationManager;

    public NotificationUtil(Context context){
        this.context=context;
        notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

//    public void postNotification(int type,String th){
//        String title="";
//        String content="";
//        switch (type){
//            case 1:
//                //gas
//                title="Gas warning";  //设置通知的标题
//                content="Harmful gas inside the house, be careful!";  //设置通知的内容
//                notification(title,content);
//                break;
//            case 2:
//                //unknown movement
//                title="Unknown movement";
//                content="There is strange movement, be cautious！";
//                notification(title,content);
//                break;
//            case 3:
//                title="Gas and movement warning";
//                content="Harmful gas and strange movement inside the house, be cautious!";
//                notification(title,content);
//                break;
//            default:
//                break;
//        }
//
//        if(!th.equals("")){
//            String[] attr=th.split(" ");
//            int temper=Integer.parseInt(attr[0]);
//            int humi=Integer.parseInt(attr[1]);
//            if(temper==1){
//                title="Heat warning";
//                content="How about get some cool air?";
//                notification(title,content);
//            }
//            else if(temper==2){
//                title="Cold warning";
//                content="How about make a fire?";
//                notification(title,content);
//            }
//            else{
//
//            }
//
//            if(humi==1){
//                title="湿度太高！";
//                content="室内湿度太高，建议开窗";
//                notification(title,content);
//            }
//            else if(humi==2){
//                title="湿度过低！";
//                content="室内湿度太低，建议打开增湿器";
//                notification(title,content);
//            }
//            else {
//
//            }
//        }
//    }

    protected void notification(String title,String content){

        Notification.Builder builder=new Notification.Builder(context);
        Intent intent=new Intent(context,MainActivity.class);  //指定要跳转的页面,暂时设为不调转到任何界面
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        builder.setSmallIcon(R.mipmap.ic_launcher);  //设置图标

        builder.setContentTitle(title);
        builder.setContentText(content);

        builder.setWhen(System.currentTimeMillis());  //设置通知到来的时间
        builder.setAutoCancel(true);  //系统维护通知的消失
        builder.setTicker("new message");  //第一次提示消失的时候显示在通知栏上的
        builder.setOngoing(true);
        builder.setNumber(20);
        builder.setAutoCancel(true);

        Notification notification=builder.build();
        notification.flags=Notification.FLAG_AUTO_CANCEL;  //只有全部清除时，Notification才会清除
        notificationManager.notify(0,notification);
    }
}
