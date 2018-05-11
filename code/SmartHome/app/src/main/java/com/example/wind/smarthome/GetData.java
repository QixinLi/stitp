package com.example.wind.smarthome;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GetData {

    protected static HashMap<String,Object> leaveData;
    protected static HashMap<String,Object> backData;
    protected static HashMap<String,Object> equipmentData;
    protected static HashMap<String,Object> weatherData;

    public GetData(){
        leaveData=new HashMap<>();
        backData=new HashMap<>();
        equipmentData=new HashMap<>();
        weatherData=new HashMap<>();
    }

    /*
    调取聚合接口
    获取当时气温、湿度、建议等信息
     */
    protected void getTipsData(){
        try{
            String data=HttpUtils.doGet("http://v.juhe.cn/weather/index?format=2&cityname" +
                    "=%E5%8D%97%E4%BA%AC&key=66022d283653094507dc70f68872f0a3");
            JSONObject jsonObject=new JSONObject(data);
            String reason=jsonObject.getString("reason");
            if(reason.equals("successed!")){
                JSONObject resultObject=jsonObject.getJSONObject("result");
                JSONObject currCondition=resultObject.getJSONObject("sk");

                weatherData.put("temper",currCondition.getInt("temp"));
                weatherData.put("humi",currCondition.getString("humidity"));

                JSONObject todayObject=resultObject.getJSONObject("today");
                weatherData.put("weather",todayObject.getString("weather"));
                weatherData.put("advice",todayObject.getString("dressing_advice"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    用于调取服务器接口，获取室内的温度、湿度、上一次有人经过等信息
     */
    protected void getEquipData(){

        //获取设备情况
        try{
            String equipData=HttpUtils.doGet("http://47.96.162.8/stitp/getAll.jsp");
            JSONObject jsonObject=new JSONObject(equipData);

            //获取温度
            String temperRecord=jsonObject.getString("wendu");
            String[] temperArrs=temperRecord.split("-");
            equipmentData.put("temper",temperArrs[0]);

            //获取获取的时间
            String time=temperArrs[1]+"/"+temperArrs[2]+temperArrs[3]+" "+temperArrs[4]+":"+temperArrs[5]+":"+temperArrs[6];
            equipmentData.put("time",time);

            //获取湿度
            String humiRecord=jsonObject.getString("shidu");
            String[] humiArrs=humiRecord.split("-");
            equipmentData.put("humi",humiArrs[0]);

            //获取上一次捕获有害气体的时间
            String gas=jsonObject.getString("qiti");
            String[] gasArrs=gas.split("-");
            String gasStr=gasArrs[0]+"/"+gasArrs[1]+"/"+gasArrs[2]+" "+gasArrs[3]+":"+gasArrs[4]+":"+gasArrs[5];
            equipmentData.put("gas",gasStr);

            //获取上一次人体通过监测器的时间
            String lastPass=jsonObject.getString("renti");
            String[] passArrs=lastPass.split("-");
            String passStr=passArrs[0]+"/"+passArrs[1]+"/"+passArrs[2]+" "+passArrs[3]+":"+passArrs[4]+":"+passArrs[5];
            equipmentData.put("lastPass",passStr);

        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    向服务器发请求，获取最新的用户生活习惯的数据
    获取用户离开的时间以及回家的时间等信息
     */
    protected void getTimeData(){
        try {
            //先把这些数据当做正常的
            //获取日常离家出门和回家的时间
            String timeData = HttpUtils.doGet("http://47.96.162.8/stitp/getOutput.jsp");
            JSONObject obj=new JSONObject(timeData);
            JSONArray array=obj.getJSONArray("foiluserdata");

            for(int i=0;i< array.length();i++){
                JSONObject jsonObject=array.getJSONObject(i);
                String type=jsonObject.getString("Type");
                if(type.equals("leave")){
                    leaveData.put("type",type) ;
                    leaveData.put("time",jsonObject.getInt("Time"));
                    leaveData.put("isWeekday",jsonObject.getBoolean("isWeekday"));
                    leaveData.put("isSat",jsonObject.getBoolean("isSat"));
                    leaveData.put("isSun",jsonObject.getBoolean("isSun"));
                    leaveData.put("isHoliday",jsonObject.getBoolean("isHoliday"));
                    leaveData.put("weather",jsonObject.getString("wCondition"));
                }
                else{
                    backData.put("type",type) ;
                    backData.put("time",jsonObject.getInt("Time"));
                    backData.put("isWeekday",jsonObject.getBoolean("isWeekday"));
                    backData.put("isSat",jsonObject.getBoolean("isSat"));
                    backData.put("isSun",jsonObject.getBoolean("isSun"));
                    backData.put("isHoliday",jsonObject.getBoolean("isHoliday"));
                    backData.put("weather",jsonObject.getString("wCondition"));
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected boolean checkGas() {
        //检测有害气体
        String gasData = HttpUtils.doGet("http://47.96.162.8/stitp/getQitiReturn.jsp");
        try {
            JSONObject gasObject = new JSONObject(gasData);
            String gas = gasObject.getString("ret");
            if (gas.equals("true")) {
                //如果检测到家里有有害气体
                //通知用户
                return true;
            } else {
                //不需要额外操作
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean checkMovement(){
        //检测非正常人体移动行为
        String movement=HttpUtils.doGet("http://47.96.162.8/stitp/getRentiReturn.jsp");
        try{
            JSONObject moveObject=new JSONObject(movement);
            String move=moveObject.getString("ret");
            if(move.equals("false")){
                //如果今天不是节假日
                //并且检测今天在用户出去之后，回家之前有人体移动，则通知用户
                if(!isHoliday()){
                    getTimeData();
                    int leaveTime=Integer.parseInt(leaveData.get("time").toString());
                    int backTime=Integer.parseInt(backData.get("time").toString());

                    //获取当前系统时间
                    Date currentTime=new Date();
                    SimpleDateFormat format=new SimpleDateFormat("HH");
                    String time=format.format(currentTime);

                    //解析当前的获取的当前时间
                    int hour=Integer.parseInt(time);

                    //当在用户离家后一小时或回家前一小时的区间内发现人体移动信号，则提醒用户
                    if(hour>leaveTime&&hour<backTime){
                        //提醒用户
                        return true;
                    }
                }
                else {
                    //在当前系统设置下，节假日和周日设定为用户不规律出去、回家，不会用户检测行为
                    //因此不会通知用户，因此不执行操作
                    //但会记录到数据库中
                    return false;
                }
            }
            else{
                //不需要额外操作
                return false;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /*
    调用聚合API接口查看是否是节假日或周日
    是节假日或周日返回true，否则返回false
     */
    private boolean isHoliday(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String time=format.format(new Date());

        //聚合api中要求“-”后不能接零，及月和天不能以“0”开头
        String[] arrs=time.split("-");
        int mon= Integer.parseInt(arrs[1]);
        int day=Integer.parseInt(arrs[2]);
        time=arrs[0]+"-"+mon+"-"+day;

        String requestString=HttpUtils.doGet("http://v.juhe.cn/calendar/day?date="+time+"&key=cfca66deb09b6bfa36fdc15e3d36a2b0");

        try{
            JSONObject jsonObject=new JSONObject(requestString);
            String reason=jsonObject.getString("reason");
            if(reason.equals("successed!")){
                JSONObject resultObject=jsonObject.getJSONObject("result");
                JSONObject dataObject=resultObject.getJSONObject("data");
                String weekday=dataObject.getString("weekday");
                if(weekday.equals("星期日")){
                    return true;
                }
                if(dataObject.has("holiday")){
                    //当dataObject不存在holiday这一项时表示今天是节假日
                    //返回true
                    return true;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /*
    访问服务器，获取当前室内温湿度情况
    {"wendu":"high/low/normal","shidu":"high/low/normal"}
    返回结果为字符串:int+“ ”+int，前者表示温度情况，后者表示湿度情况
     */
    protected int getTemperTips(){
        try{
            String tips=HttpUtils.doGet("http://47.96.162.8/stitp/getWSReturn.jsp");
            JSONObject tipsObject=new JSONObject(tips);

            String temper=tipsObject.getString("wendu");

            //生成温度提示信息
            if(temper.equals("high")){
                return 1;
            }
            else if(temper.equals("low")){
                return 2;
            }
            else{
                return 3;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 3;
    }

    protected int getHumidityTips(){
        try{
            String tips=HttpUtils.doGet("http://47.96.162.8/stitp/getWSReturn.jsp");
            JSONObject jsonObject=new JSONObject(tips);
            String humi=jsonObject.getString("shidu");

            //返回湿度信息
            if(humi.equals("high")){
                return 1;
            }
            else if(humi.equals("low")){
                return 2;
            }
            else {
                return 3;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 3;
    }
}
