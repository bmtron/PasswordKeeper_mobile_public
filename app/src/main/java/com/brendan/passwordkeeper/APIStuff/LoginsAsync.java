package com.brendan.passwordkeeper.APIStuff;

import android.os.AsyncTask;

import com.brendan.passwordkeeper.Globals.GlobalVars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.*;

public class LoginsAsync implements Runnable {
    String url;
    String authKey;
    CountDownLatch latch;
    LoginsAsync() {
        this.url = "";
        this.authKey = "";
        this.latch = new CountDownLatch(1);
    }
    public LoginsAsync(String url, String authKey, CountDownLatch latch) {
        this.url = url;
        this.authKey = authKey;
        this.latch = latch;
    }
    @Override
    public void run() {
        url = url + authKey;
        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputLine = "";

        StringBuffer response = new StringBuffer();

        while (true) {
            try {
                if (!((inputLine = in.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.append(inputLine);
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = response.toString();
        GlobalVars.apiResultString = result;

        latch.countDown();
    }
}
