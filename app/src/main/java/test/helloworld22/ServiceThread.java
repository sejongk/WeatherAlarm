package test.helloworld22;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class ServiceThread extends Thread {
    int[] rain = {500, 501, 502, 503, 504, 511, 520, 521, 522, 531};
    int[] snow = {600, 601, 602, 611, 612, 615, 616, 620, 621, 622};
    int fog = 741;
    int id;
    String description;
    Double tem;
    Double speed;
    boolean pre_rain = false;
    boolean pre_snow = false;
    boolean pre_cold = false;
    boolean cur_rain = false;
    boolean cur_snow = false;
    boolean cur_cold = false;
    boolean chg_rain = false;
    boolean chg_snow = false;
    boolean chg_cold = false;
    //gps setting
    public double longitude;
    public double latitude;

    public int pre_lati;
    public int pre_longi;
    int term = 0;

    Handler handler;
    Context context;
    boolean isRun = true;

    String currnetLoc = new String();

    public ServiceThread(Context context,Handler handler, double longitude, double latitude) {
        this.handler = handler;
        this.longitude = longitude;
        this.latitude = latitude;
        this.context = context;
        final LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }

    public void stopForever() {
        Log.e("stop", "forever");
        synchronized (this) {
            this.isRun = false;
        }
    }

    @Override
    public void run() {
        //핸들러로 보낼 Message 작성
        //반복적으로 수행할 작업을 한다.

        while (isRun) {
            // 0 맑음, 1 비, 2 눈
            ParseTask pT = new ParseTask();
            pT.execute();
            handler.sendEmptyMessage(0);//쓰레드에 있는 핸들러에게 메세지를 보냄            //rain snow fog 판별
            try {
                Thread.sleep(1800000); //10초씩 쉰다.

            } catch (Exception e) {
            }

        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            pre_lati = (int)(latitude*100);
            pre_longi =(int)(longitude*100);

            longitude = location.getLongitude(); //경도
            latitude = location.getLatitude();   //위도
            if(pre_longi != (int)(longitude*100)&& pre_lati !=(int)(latitude*100)) {
                Log.e("gpsFlag", "좌표가 바뀌었음" + longitude);

                Geocoder mGeoCoder = new Geocoder(context,Locale.KOREA);
                String sb = new String();
                try {
                    List<Address> addrs =
                            mGeoCoder.getFromLocation(latitude, longitude, 1);

                    for (Address addr : addrs) {
                        // 지명을 검색하고 문자열에 연결
                        int index = addr.getMaxAddressLineIndex();
                        for (int i = 0; i <= index; ++i) {
                            sb = addr.getThoroughfare();
                            Log.e("location", addr.getThoroughfare());
                        }
                    }
                } catch (IOException e) {
                }
                if (!sb.equals(currnetLoc)) {
                    currnetLoc = sb;
                    Log.e("flag", "도시가 바뀌었습니다.");
                } else {
                    Log.e("flag", "위치는 바뀌었으나 도시는 그대로네요");
                    longitude = pre_longi;
                    latitude = pre_lati;
                }

                //여기서 위치값이 갱신되면 이벤트가 발생한다.
                //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
                Log.d("test", "onLocationChanged, location:" + location);

                //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
                //Network 위치제공자에 의한 위치변화
                //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
            }
        }
        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }
        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        @Override
        protected String doInBackground(Void... params) {
            try {
                String url1 = "https://api.openweathermap.org/data/2.5/weather?&APPID=4b68d3c0176ef8dc98a91decba2ef3ed&lang=kr&units=metic&";
                String url2 = "lat=" + latitude + "&lon=" + longitude;
                String $url_json = url1 + url2;
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
                tem = Math.floor(mainOb.getDouble("temp") - 273.15);
                Log.e("temperature", Double.toString(tem));
                //wind speed
                speed = windOb.getDouble("speed");

                pre_rain = cur_rain;
                pre_snow = cur_snow;
                pre_cold = cur_cold;

                for (int i = 0; i < rain.length; i++) {
                    if (id == rain[i]) {
                        cur_rain = true;
                        break;
                    } else if (i == rain.length - 1) cur_rain = false;
                }
                if (pre_rain == false && cur_rain == true) chg_rain = true;
                else chg_rain = false;

                for (int i = 0; i < snow.length; i++) {
                    if (id == snow[i]) {
                        cur_snow = true;
                        break;
                    } else if (i == snow.length - 1) cur_snow = false;
                }
                if (pre_snow == false && cur_snow == true) chg_snow = true;
                else chg_snow = false;

                if (tem <= 0) {
                    cur_cold = true;
                } else cur_cold = false;

                if (pre_cold == false && cur_cold == true) chg_cold = true;
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
                Message msg4 = new Message();
                Bundle data4 = new Bundle();
                data4.putInt("state", 4);
                msg4.setData(data4);
                Message msg5 = new Message();
                Bundle data5 = new Bundle();
                data5.putInt("state", 5);
                msg5.setData(data5);


                if (chg_rain && !chg_cold)
                    handler.sendMessage(msg1);//쓰레드에 있는 핸들러에게 메세지를 보냄            //rain snow fog 판별
                if (chg_snow && !chg_cold) {
                    handler.sendMessage(msg2);//쓰레드에 있는 핸들러에게 메세지를 보냄            //rain snow fog 판별
                }
                if (chg_cold && !chg_rain && !chg_snow) {
                    Log.e("low tem", "lowlow");
                    handler.sendMessage(msg3);
                }
                if (chg_rain && chg_cold)
                    handler.sendMessage(msg4);//쓰레드에 있는 핸들러에게 메세지를 보냄            //rain snow fog 판별
                if (chg_snow && chg_cold) {
                    handler.sendMessage(msg5);//쓰레드에 있는 핸들러에게 메세지를 보냄            //rain snow fog 판별
                }
                String state = "state는 " + "과거" + pre_cold + "현재" + cur_cold + "변화" + chg_cold;
                Log.d("state", state);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}