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
String str="";
line = br.readLine();
do{
	line+="#";
	str+=line;
	line = br.readLine();
}while(line != null);
out.print(str);
%>  