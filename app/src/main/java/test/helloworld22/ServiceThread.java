package test.helloworld22;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceThread extends Thread {
    int[] rain = {500,501,502,503,504,511,520,521,522,531};
    int[] snow = {600,601,602,611,612,615,616,620,621,622};
    int fog = 741;
    int id;
    String description;
    Double tem;
    Double speed;
    boolean pre_rain=false;
    boolean pre_snow=false;
    boolean pre_cold = false;
    boolean cur_rain=false;
    boolean cur_snow=false;
    boolean cur_cold = false;
    boolean chg_rain=false;
    boolean chg_snow=false;
    boolean chg_cold = false;
//gps setting
public double longitude;
    public double latitude;

    int term = 0;

    Handler handler;
    boolean isRun = true;



    public ServiceThread(Handler handler,double longitude, double latitude){
        this.handler = handler;
        this.longitude =longitude;
        this.latitude = latitude;
    }

    public void stopForever(){
        Log.e("stop","forever");
        synchronized (this) {
            this.isRun = false;
        }
    }
    @Override
    public void run(){
        //핸들러로 보낼 Message 작성
        //반복적으로 수행할 작업을 한다.

        while(isRun) {

            // 0 맑음, 1 비, 2 눈

                ParseTask pT = new ParseTask();
                pT.execute();

                handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄            //rain snow fog 판별

                     try{
                         Thread.sleep(50000); //10초씩 쉰다.
                     } catch (Exception e) {}

        }
    }

    private  class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String url1 = "https://api.openweathermap.org/data/2.5/weather?&APPID=4b68d3c0176ef8dc98a91decba2ef3ed&lang=kr&units=metic&";
                String url2 = "lat="+latitude+"&lon="+longitude;
                String $url_json = url1+url2;
                URL url = new URL($url_json);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                resultJson = buffer.toString();
                Log.d("FOR_LOG", resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }


        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray weatherArr = json.getJSONArray("weather");
                JSONObject mainOb = json.getJSONObject("main");
                JSONObject windOb = json.getJSONObject("wind");
                //description, id
                JSONObject friend = weatherArr.getJSONObject(0);
                id = friend.getInt("id");
                description = friend.getString("description");
                Log.d("FOR_LOG", description);

                //temp
               tem = Math.floor(mainOb.getDouble("temp")-273.15);
               Log.e("temperature",Double.toString(tem));
                //wind speed
               speed = windOb.getDouble("speed");

                pre_rain = cur_rain;
                pre_snow = cur_snow;
                pre_cold = cur_cold;

                for(int i=0;i<rain.length;i++){
                    if( id == rain[i]) {
                        cur_rain = true;
                        break;
                    }
                    else if(i==rain.length-1) cur_rain = false;
                }
                if(pre_rain ==false  && cur_rain == true) chg_rain = true;
                else chg_rain = false;

                for(int i=0;i<snow.length;i++){
                    if(id == snow[i]) {
                        cur_snow = true;
                        break;
                    }
                    else if(i==snow.length-1) cur_snow = false;
                }
                if(pre_snow==false  && cur_snow == true) chg_snow = true;
                else chg_snow = false;

                if(tem>0) {
                    cur_cold = true;
                }
                else cur_cold=false;

                if(pre_cold==false  && cur_cold == true) chg_cold = true;
                else chg_cold = false;

                //메세지 처리
                Message msg0 = new Message();
                Bundle data0 = new Bundle();
                data0.putInt("state", 0);
                msg0.setData(data0);
                Message msg1 = new Message();
                Bundle data1 = new Bundle();
                data1.putInt("state", 1);
                msg1.setData(data1);
                Message msg2 = new Message();
                Bundle data2 = new Bundle();
                data2.putInt("state", 2);
                msg2.setData(data2);
                Message msg3 = new Message();
                Bundle data3 = new Bundle();
                data3.putInt("state", 3);
                msg3.setData(data3);
                if (chg_cold == true) {
                    Log.e("low tem", "lowlow");
                    handler.sendMessage(msg3);
                }
                if (chg_rain == true)
                    handler.sendMessage(msg1);//쓰레드에 있는 핸들러에게 메세지를 보냄            //rain snow fog 판별
                if (chg_snow == true) {
                    handler.sendMessage(msg2);//쓰레드에 있는 핸들러에게 메세지를 보냄            //rain snow fog 판별
                }
                String state = "state는 "+"과거"+pre_cold+"현재"+cur_cold+"변화"+chg_cold;
                Log.d("state",state);
               } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
