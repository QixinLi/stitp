<%@ page language="java" import="java.util.*,java.net.*,java.io.*" pageEncoding="utf-8"%> 
<%@ page contentType="text/html;charset=utf-8"%> 
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<%
InputStreamReader reader = new InputStreamReader(new FileInputStream("C:\\stitpdata\\人体热释电.txt")); // 建立一个输入流对象reader  
BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
String line = "";  
String qiti="";
line = br.readLine();
do{
	qiti=line;
	line = br.readLine();
}while(line != null && !line.equals(""));

String qitidata[]=qiti.split("-");

long day=0; 
Calendar beginDate=Calendar.getInstance();
Calendar endDate=Calendar.getInstance();
beginDate.set(Integer.parseInt(qitidata[0]),Integer.parseInt(qitidata[1])-1,Integer.parseInt(qitidata[2]),Integer.parseInt(qitidata[3]),Integer.parseInt(qitidata[4]),Integer.parseInt(qitidata[5]));
endDate.setTime(new Date());    
day=((endDate.getTimeInMillis()-beginDate.getTimeInMillis())/(60*1000));     

if(-10<day&&day<10){
    out.print("{\"ret\":\"true\"}");
}
else{
    out.print("{\"ret\":\"false\"}");
}

%>  
