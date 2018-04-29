<%@ page language="java" import="java.util.*,java.net.*,java.io.*" pageEncoding="utf-8"%> 
<%@ page contentType="text/html;charset=utf-8"%> 
<%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
%>
<%
InputStreamReader reader1 = new InputStreamReader(new FileInputStream("C:\\stitpdata\\温度.txt")); // 建立一个输入流对象reader  
BufferedReader br1 = new BufferedReader(reader1); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
String line = "";  
String wendu="";
line = br1.readLine();
do{
	wendu=line;
	line = br1.readLine();
}while(line != null && !line.equals(""));

InputStreamReader reader2 = new InputStreamReader(new FileInputStream("C:\\stitpdata\\湿度.txt")); // 建立一个输入流对象reader  
BufferedReader br2 = new BufferedReader(reader2); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
line = "";  
String shidu="";
line = br2.readLine();
do{
	shidu=line;
	line = br2.readLine();
}while(line != null && !line.equals(""));

InputStreamReader reader3 = new InputStreamReader(new FileInputStream("C:\\stitpdata\\气体.txt")); // 建立一个输入流对象reader  
BufferedReader br3 = new BufferedReader(reader3); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
line = "";  
String qiti="";
line = br3.readLine();
do{
	qiti=line;
	line = br3.readLine();
}while(line != null && !line.equals(""));

InputStreamReader reader4 = new InputStreamReader(new FileInputStream("C:\\stitpdata\\人体热释电.txt")); // 建立一个输入流对象reader  
BufferedReader br4 = new BufferedReader(reader4); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
line = "";  
String renti="";
line = br4.readLine();
do{
	renti=line;
	line = br4.readLine();
}while(line != null && !line.equals(""));

String str="{\"wendu\":\""+wendu+"\",\"shidu\":\""+shidu+"\",\"qiti\":\""+qiti+"\",\"renti\":\""+renti+"\"}";
out.print(str);
%>