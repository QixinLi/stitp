import java.io.*;  
import java.net.*;
import java.util.Calendar;

public class SocketService {
    //���������
    public static void main(String[] args) throws IOException{
        //�������20006�˿ڼ����ͻ��������TCP����  
        ServerSocket server = new ServerSocket(35447);  
        //ServerThread.getCurrentTime();
        ServerThread.WriteData(ServerThread.getCurrentTime(),"C:\\stitpdata\\�������͵�.txt");
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
            	if(msg.equals("GotBadAir"))
            	{
            		WriteData(getCurrentTime(),"C:\\stitpdata\\����.txt");
            	}
            	else if(msg.equals("GotPerson"))
            	{
            		WriteData(getCurrentTime(),"C:\\stitpdata\\�������͵�.txt");
            	}
            	else
            	{
            		String w_s[] = msg.split(",");
            		if(w_s.length==2)
            		{
            			WriteData(w_s[0]+","+getCurrentTime(),"C:\\stitpdata\\�¶�.txt");
            			WriteData(w_s[1]+","+getCurrentTime(),"C:\\stitpdata\\ʪ��.txt");
            		}
            		else
            		{
            			System.out.println("���ݽ�����ʽ����");
            		}
            	}
            	buf.mark(0);
            	buf.reset();
            }  
            out.close();  
            client.close();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
    }  
    static public void WriteData(String msg,String mfile) throws IOException {
        File file =new File(mfile);
        //if file doesnt exists, then create it
        if(!file.exists()){
        	// �������ļ�
        	file.createNewFile();
        }
        //true = append file
        FileWriter fileWritter = new FileWriter(mfile,true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(msg);
        bufferWritter.close();
    }
    static public String getCurrentTime() {
    	Calendar c = Calendar.getInstance();//���Զ�ÿ��ʱ���򵥶��޸�   
    	int year = c.get(Calendar.YEAR);  
    	int month = c.get(Calendar.MONTH);  
    	int date = c.get(Calendar.DATE);    
    	int hour = c.get(Calendar.HOUR_OF_DAY);   
    	int minute = c.get(Calendar.MINUTE);   
    	int second = c.get(Calendar.SECOND);    
    	
    	String ret=year + "-" + month + "-" + date + "-" +hour + "-" +minute + "-" + second + "\r\n";
    	//System.out.println(ret);   
    	return ret;
    }
}  