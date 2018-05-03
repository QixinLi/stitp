<%@ page language="java" import="java.util.*,java.net.*,java.io.*" pageEncoding="utf-8"%> 
<%@ page contentType="text/html;charset=utf-8"%> 
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<%
InputStreamReader reader = new InputStreamReader(new FileInputStream("C:\\stitpdata\\温度.txt")); // 建立一个输入流对象reader  
BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
String line = "";  
String wendu="";
String ret="";
line = br.readLine();
do{
	wendu=line;
	line = br.readLine();
}while(line != null && !line.equals(""));
String wendudata[]=wendu.split("-");
int mywendu=Integer.parseInt(wendudata[0]);
if(mywendu>=28){
    ret+="{\"wendu\":\"high\",";
}
else if(mywendu<=5){
    ret+="{\"wendu\":\"low\",";
}
else{
    ret+="{\"wendu\":\"normal\",";
}

InputStreamReader reader2 = new InputStreamReader(new FileInputStream("C:\\stitpdata\\湿度.txt")); // 建立一个输入流对象reader  
BufferedReader br2 = new BufferedReader(reader2); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
line = "";  
String shidu="";
line = br2.readLine();
do{
	shidu=line;
	line = br2.readLine();
}while(line != null && !line.equals(""));
String shidudata[]=shidu.split("-");
int myshidu=Integer.parseInt(shidudata[0]);

if(myshidu>=80){
    ret+="\"shidu\":\"high\"}";
}
else if(myshidu<=30){
    ret+="\"shidu\":\"low\"}";
}
else{
    ret+="\"shidu\":\"normal\"}";
}

out.print(ret);
%>  