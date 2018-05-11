package com.example.wind.smarthome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TCPClientActivity extends Activity implements View.OnClickListener{

    private static final int MESSAGE_RECEIVE_NEW_MSG=1;
    private static final int MESSAGE_SOCKET_CONNECTED=2;

    private Button mSendButton;
    private TextView mMessageTextView;
    private EditText mMessageEditView;

    private PrintWriter mPrintWriter;
    private Socket mClientSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);

        mMessageTextView=findViewById(R.id.tcpTextView);
        mSendButton=findViewById(R.id.tcpButton);
        mSendButton.setOnClickListener(this);
        mMessageEditView=findViewById(R.id.tcpEditText);

        Intent service=new Intent(this,TCPServerService.class);
        startService(service);

        //create and start a thread
         new Thread(){
            @Override
            public void run(){
                connectTCPServer();
            }
        }.start();

        mSendButton=findViewById(R.id.tcpButton);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msg=mMessageEditView.getText().toString();
            if(!TextUtils.isEmpty(msg)&&mPrintWriter!=null){
                mPrintWriter.println(msg);
                mMessageEditView.setText("");  //clear the input text area
                String time=formatDataTime(System.currentTimeMillis());
                final String showedMsg="self"+time+":"+msg+"\n";
                mMessageTextView.setText(mMessageTextView.getText()+showedMsg);
            }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MESSAGE_RECEIVE_NEW_MSG:{
                    mMessageTextView.setText(mMessageTextView.getText()+(String)msg.obj);
                    break;
                }
                case MESSAGE_SOCKET_CONNECTED:{
                    mSendButton.setEnabled(true);
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view){
        if(view==mSendButton){
            final String msg=mMessageEditView.getText().toString();
            if(!TextUtils.isEmpty(msg)&&mPrintWriter!=null){
                mPrintWriter.println(msg);
                mMessageEditView.setText("");  //clear the input text area
                String time=formatDataTime(System.currentTimeMillis());
                final String showedMsg="self"+time+":"+msg+"\n";
                mMessageTextView.setText(mMessageTextView.getText()+showedMsg);
            }
        }
    }

    @SuppressLint("SimpleDataFormat")
    private String formatDataTime(long time){
        return new SimpleDateFormat("(HH:MM:SS)").format(new Date(time));
    }

    private void connectTCPServer(){
        Socket socket=null;
        while (socket==null){  //keep trying to connect to server
            try{
                socket=new Socket("localhost",8688);
                mClientSocket=socket;
                mPrintWriter=new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())),true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                System.out.println("connect server success");
            }catch (IOException e){
                SystemClock.sleep(1000);
                System.out.println("connect tcp server failed, retry...");
            }
        }

        try{
            //receive message from server
            BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!TCPClientActivity.this.isFinishing()){
                String msg=reader.readLine();
                System.out.println("receive:"+msg);
                if (msg != null) {
                    String time=formatDataTime(System.currentTimeMillis());
                    final String showedMsg="Server "+time+":"+msg+"\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG,showedMsg).sendToTarget();
                }
            }
            System.out.println("quit...");
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
