import java.io.*;  
import java.net.*;

public class SocketService {
    //���������
    public static void main(String[] args) throws IOException{
        //�������20006�˿ڼ����ͻ��������TCP����  
        ServerSocket server = new ServerSocket(35447);  
        System.out.println("�����������ɹ���");  
        Socket client = null;  
        boolean f = true;  
        while(f){  
            //�ȴ��ͻ��˵����ӣ����û�л�ȡ����  
            client = server.accept();  
            System.out.println("��ͻ������ӳɹ���");  
            //Ϊÿ���ͻ������ӿ���һ���߳�  
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
            //��ȡSocket���������������ͻ��˷�������  
            PrintStream out = new PrintStream(client.getOutputStream());  
            //��ȡSocket�����������������մӿͻ��˷��͹���������  
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
            	System.out.print("�յ���Ϣ:"+msg);
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