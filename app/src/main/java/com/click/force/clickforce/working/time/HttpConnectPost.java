package com.click.force.clickforce.working.time;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpConnectPost extends AsyncTask<String, Void, String> {
//    public static final String workurl = "http://192.168.1.116/laravelEric/public/api/";
//        public static final String workurl = "http://172.20.10.8/laravelEric/public/api/";
    public static final String workurl = "https://cua-new.holmesmind.com/api/";
    public static final String workIn = "worktimein";
    public static final String workOut = "worktimeout";
    public static final String cua = "getcua";
    public static final String checkStatus = "getstatus";
    public static final String all_data = "getUserAllData";

    public Map<String, String> params = new HashMap<>();

//    Map<String, String> params = new HashMap<>();

    @Override
    protected String doInBackground(String... requset) {
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();
        int statusCode = -1;
        try {
            URL url = new URL(requset[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoInput(true); //允許輸入流，即允許下載
            connection.setDoOutput(true); //允許輸出流，即允許上傳
            connection.setUseCaches(false); //設置是否使用緩存
            OutputStream os = connection.getOutputStream();
            DataOutputStream writer = new DataOutputStream(os);
            String jsonString = getJSONString(params); //input Map<String, String>
//                writer.writeBytes(jsonString);
            writer.write(jsonString.getBytes());

            writer.flush();
            writer.close();
            os.close();

            //Get Response
            InputStream is = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            reader.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e){
            e.printStackTrace();
            return "";
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return response.toString();
    }

    public static String getJSONString(Map<String, String> params){
        JSONObject json = new JSONObject();
        for(String key:params.keySet()) {
            try {
                json.put(key, params.get(key));
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return json.toString();
    }
}


