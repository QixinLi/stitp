<%@ page language="java" import="java.util.*,java.net.*,java.io.*" pageEncoding="utf-8"%> 
<%@ page contentType="text/html;charset=utf-8"%> 
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<%
InputStreamReader reader = new InputStreamReader(new FileInputStream("C:\\stitpdata\\气体.txt")); // 建立一个输入流对象reader  
BufferedReader br = new BufferedReader(reader3); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
String line = "";  
String qiti="";
line = br.readLine();
do{
	qiti=line;
	line = br.readLine();
}while(line != null && !line.equals(""));
Calendar c = Calendar.getInstance();//可以对每个时间域单独修改   
int year = c.get(Calendar.YEAR);  
int month = c.get(Calendar.MONTH);   
int date = c.get(Calendar.DATE);    
int hour = c.get(Calendar.HOUR_OF_DAY);   
int minute = c.get(Calendar.MINUTE);   
int second = c.get(Calendar.SECOND);

long ret=getDaySub(year+"-"+month+"-"+data+"-"+hour+"-"+minute+"-"+second,qiti);
if(-2<ret<2){
    out.print("{\"ret\":\"true\"");
}
else{
    out.print("{\"ret\":\"false\"");
}

%>  
<%
public long getDaySub(String beginDateStr,String endDateStr){
    long day=0;
    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");    
    java.util.Date beginDate;
    java.util.Date endDate;
    try
    {
        beginDate = format.parse(beginDateStr);
        endDate= format.parse(endDateStr);    
        day=(endDate.getTime()-beginDate.getTime())/(60*1000);    
        //System.out.println("相隔的天数="+day);   
    } catch (ParseException e)
    {
        // TODO 自动生成 catch 块
        e.printStackTrace();
    }   
    return day;
} 
%>