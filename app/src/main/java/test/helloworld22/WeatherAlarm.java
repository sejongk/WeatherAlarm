package test.helloworld22;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import test.helloworld22.MyService.MyBinder;


public class WeatherAlarm extends Fragment {
    public double longitude;
    public double latitude;
    String description;

    int[] rain = {500,501,502,503,504,511,520,521,522,531,300,301,302,310,311,312,313,314,321};
    int[] snow = {600,601,602,611,612,615,616,620,621,622};
    int[] thunder ={200,201,202,210,211,212,221,230,231,232};
    int[] fewcloud={801,802};
    int[] manycloud={803,804};
    int fog = 741;

    TextView tempText;
    ImageView weather;


    //gps 설치
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude(); //경도
            latitude = location.getLatitude();   //위도
            getInfo();
        }
        public void onProviderDisabled(String provider) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    //서비스 바인딩
    MyService ms;
    boolean isService = false; // 서비스 중인 확인용

    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
// 서비스와 연결되었을 때 호출되는 메서드
// 서비스 객체를 전역변수로 저장
            MyBinder mb = (MyBinder) service;
            ms = mb.getService(); // 서비스가 제공하는 메소드 호출하여
// 서비스쪽 객체를 전달받을수 있슴
            isService = true;
        }
        public void onServiceDisconnected(ComponentName name) {
// 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
        }
    };

    //서비스 바인딩
public void getInfo(){
    ParseTask pT = new ParseTask();
    pT.execute();
}
    @Override
    public void onResume(){
    super.onResume();
    getInfo();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity(),MyService.class);
        getActivity().bindService(intent,conn,Context.BIND_AUTO_CREATE);
}


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_weather_alarm, container, false);

        final LocationManager lm = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);

        weather=rootView.findViewById(R.id.weather);
        tempText=rootView.findViewById(R.id.temp);

        Button btn_start = (Button)rootView.findViewById(R.id.btn_start);
        Button btn_end = (Button)rootView.findViewById(R.id.btn_end);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Service 시작",Toast.LENGTH_SHORT).show();

            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Toast.makeText(getActivity(), "Service 끝", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MyService.class);
                    getActivity().unbindService(conn);
                    getActivity().stopService(intent);
                }catch (IllegalArgumentException e){

                }
            }
        });

        Log.e("Frag", "MainFragment");
        return rootView;
    }

    private  class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";


        @Override
        protected String doInBackground(Void... params) {
            try {
                String url1= "https://api.openweathermap.org/data/2.5/weather?&APPID=4b68d3c0176ef8dc98a91decba2ef3ed&lang=en&units=metic&";
                String url2="lat="+latitude+"&lon="+longitude;
                String $url_json =url1+url2;
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
               // Log.d("FOR_LOG", resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }


        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            boolean rain_state=false;
            boolean snow_state=false;

            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray weatherArr = json.getJSONArray("weather");
                JSONObject mainOb = json.getJSONObject("main");
                JSONObject windOb = json.getJSONObject("wind");
                //description, id
                    JSONObject friend = weatherArr.getJSONObject(0);
                    int id = friend.getInt("id");
                    String description = friend.getString("description");
                    Log.d("FOR_LOG", description);

                    //temp
                Double tem = Math.floor(mainOb.getDouble("temp")-273.15);
                //wind speed
                Double speed = windOb.getDouble("speed");
                //rain snow fog 판별

                if(latitude != 0 && longitude != 0) {
                    tempText.setText(Double.toString(tem));
                    for(int i=0;i<rain.length;i++){ if(id == rain[i]) {
                        rain_state = true;
                        weather.setImageResource(R.drawable.rain);
                    } }
                    for(int i=0;i<snow.length;i++){ if(id == snow[i]) {
                        snow_state = true;
                        weather.setImageResource(R.drawable.snow);
                    } }
                  if(id==800)  weather.setImageResource(R.drawable.sunny);
                    for(int i=0;i<fewcloud.length;i++){ if(id == fewcloud[i]) {
                        weather.setImageResource(R.drawable.cloudy);
                    } }
                    for(int i=0;i<manycloud.length;i++){ if(id == manycloud[i]) {
                        weather.setImageResource(R.drawable.cloud);
                    } }
                    for(int i=0;i<thunder.length;i++){ if(id == thunder[i]) {
                        weather.setImageResource(R.drawable.rainbolt);
                    } }
                }

                //adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}