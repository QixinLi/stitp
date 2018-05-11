package com.example.wind.smarthome;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import static java.lang.System.out;

/**
 * Created by wind on 18-4-9.
 */

public class TCPServerService extends Service {

    private boolean mIsServiceDestoryed=false;
    private String[] mDefinedMessages=new String[]{
            "hello","long time no see","missing you!"};

    @Override
    public void onCreate(){
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override

    public void onDestroy(){
        mIsServiceDestoryed=true;
    }

    private class TcpServer implements Runnable{
        @SuppressWarnings("resource")
        @Override
        public void run(){
            ServerSocket serverSocket=null;
            try{
                //listen local 8688 port
                serverSocket=new ServerSocket(8688);
            }catch (IOException e){
                System.err.println("establish tcp server failed, port 8688");
                e.printStackTrace();
                return;
            }

            while (!mIsServiceDestoryed){
                try{
                    //receive client request
                    final Socket client=serverSocket.accept();
                    out.println("accept request from client");
                    new Thread(){
                        @Override
                        public void run(){
                            try{
                                responseClient(client);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException{
        //receive client message
        BufferedReader reader=new BufferedReader(
                new InputStreamReader(client.getInputStream()));
        //send message to client
        PrintWriter writer=new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(client.getOutputStream())),true);
        writer.println("Welcome to chat room!");
        while (!mIsServiceDestoryed){
            String str=reader.readLine();
            System.out.println("msg from client: "+str);
            if(str==null){
                //client disconnected
                break;
            }
            int i=new Random().nextInt(mDefinedMessages.length);
            String msg=mDefinedMessages[i];
            writer.println(msg);
            System.out.println("send: "+msg);
        }
        System.out.println("Client quit.");
        //close the stream
//        MyUtils.close(writer);
//        MyUtils.close(reader);
        client.close();
    }
}
