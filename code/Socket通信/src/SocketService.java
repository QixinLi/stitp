import java.io.*;  
import java.net.*;

public class SocketService {
    //搭建服务器端
    public static void main(String[] args) throws IOException{
        //服务端在20006端口监听客户端请求的TCP连接  
        ServerSocket server = new ServerSocket(35447);  
        System.out.println("服务器创建成功！");  
        Socket client = null;  
        boolean f = true;  
        while(f){  
            //等待客户端的连接，如果没有获取连接  
            client = server.accept();  
            System.out.println("与客户端连接成功！");  
            //为每个客户端连接开启一个线程  
            new Thread(new ServerThread(client)).start();  
        }  
        server.close();  
    }

}
class ServerThread implements Runnable {  

    private Socket client = null;  
    public ServerThread(Socket client){  
        this.client = client;  
    }  

    @Override  
    public void run() {  
        try{  
            //获取Socket的输出流，用来向客户端发送数据  
            PrintStream out = new PrintStream(client.getOutputStream());  
            //获取Socket的输入流，用来接收从客户端发送过来的数据  
            InputStream in=client.getInputStream();
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));  
            
            boolean flag =true;  
            while(flag){  
            	in=client.getInputStream();
            	buf = new BufferedReader(new InputStreamReader(in));  
            	int len=0;
            	String msg="";
            	while((len=buf.read())!=0)
            	{
            		if((char)len!='#')
            		{
            			msg+=(char)len;
            			//System.out.print((char)len);
            		}
            		else
            		{
            			break;
            		}
            	}
            	System.out.print("收到消息:"+msg);
            	System.out.println();
            	buf.mark(0);
            	buf.reset();
            }  
            out.close();  
            client.close();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
    }  
}  