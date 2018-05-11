package com.example.wind.smarthome;

import android.os.StrictMode;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wind on 18-3-26.
 */

public class HttpUtils {
    public static String doGet(String urlStr)
    {
        StringBuffer sb = new StringBuffer();
        try
        {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            if (conn.getResponseCode() == 200)
            {
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"UTF-8");
                int len = 0;
                char[] buf = new char[1024];

                while ((len = isr.read(buf)) != -1)
                {
                    sb.append(new String(buf, 0, len));
                }
                is.close();
                isr.close();
            }
        } catch (Exception e)
        {
        }
        return sb.toString();
    }
    public static String loadJson(String urlPath) throws Exception {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        URL url = new URL(urlPath);
        /**
         * 这里网络请求使用的是类HttpURLConnection，另外一种可以选择使用类HttpClient。
         */
        HttpURLConnection conn = (HttpURLConnection) url
                .openConnection();
        conn.setRequestMethod("GET");//使用GET方法获取
        conn.setConnectTimeout(5000);
        InputStream is = conn.getInputStream();
        //String result = HttpUtils.readMyInputStream(is);
        String result =null;
        return result;
    }
}
