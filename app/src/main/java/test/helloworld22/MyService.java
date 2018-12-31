package test.helloworld22;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MyService extends Service {
    NotificationManager Notifi_M;
  //  public double pre_longi;
  //  public double pre_lati;
    public double longitude;
    public double latitude;

    public double getLongitude(){
        return longitude;
    }
    public double getLatitude(){
        return latitude;
    }

    myServiceHandler handler;

    ServiceThread thread;
    Notification Notifi ;
    String currnetLoc = new String();

    IBinder mBinder = new MyBinder();

    class MyBinder extends Binder {
        MyService getService() { // 서비스 객체를 리턴
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate(){
        handler= new myServiceHandler();
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        thread = new ServiceThread(handler, longitude, latitude);
        thread.start();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        thread = new ServiceThread(handler, longitude, latitude);
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업
    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.stopForever();
       // thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
          //  pre_lati = latitude;
         //   pre_longi =longitude;
            longitude = location.getLongitude(); //경도
            latitude = location.getLatitude();   //위도
            Geocoder mGeoCoder = new Geocoder(getApplicationContext(), Locale.KOREA);
            String sb = new String();
            try {
                List<Address> addrs =
                        mGeoCoder.getFromLocation(latitude, longitude, 1);

                for (Address addr : addrs) {
                    // 지명을 검색하고 문자열에 연결
                    int index = addr.getMaxAddressLineIndex();
                    for (int i = 0; i <= index; ++i) {
                        sb=addr.getThoroughfare();
                        Log.e("location",addr.getThoroughfare());
                    }
                }
            } catch (IOException e) {
            }
            if(!sb.equals(currnetLoc)){
                onDestroy();
                currnetLoc = sb;
                thread = new ServiceThread(handler, longitude, latitude);
                thread.start();
            }
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
            Log.d("test", "onLocationChanged, location:" + location);

            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
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


    class myServiceHandler extends Handler {
        @TargetApi(Build.VERSION_CODES.O)
        @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
            int state = msg.getData().getInt("state");
            final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    100, // 통지사이의 최소 시간간격 (miliSecond)
                    1, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("channel description");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            switch (state){
                case 3:
                    Notifi = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Cold")
                            .setContentText("오늘 추워요. 단단히 껴입어요!")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setTicker("알림!!!")
                            .setChannelId("channel_id")
                            .setContentIntent(pendingIntent)
                            .build();
                    Notifi.defaults = Notification.DEFAULT_SOUND;
                    //알림 소리를 한번만 내도록
                    Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
                    //확인하면 자동으로 알림이 제거 되도록
                    Notifi.flags = Notification.FLAG_AUTO_CANCEL;
                    Notifi_M.notify(777, Notifi);
                    break;
                case 1:
                    Notifi = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Rain")
                            .setContentText("지금 비와요. 우산 챙기세요!")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setTicker("알림!!!")
                            .setChannelId("channel_id")
                            .setContentIntent(pendingIntent)
                            .build();
                    Notifi.defaults = Notification.DEFAULT_SOUND;
                    //알림 소리를 한번만 내도록
                    Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
                    //확인하면 자동으로 알림이 제거 되도록
                    Notifi.flags = Notification.FLAG_AUTO_CANCEL;
                    Notifi_M.notify(777, Notifi);
                    break;
                case 2:
                    Notifi = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Snow")
                            .setContentText("지금 눈와요. 눈길 조심하세요!")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setTicker("알림!!!")
                            .setChannelId("channel_id")
                            .setContentIntent(pendingIntent)
                            .build();
                    Notifi.defaults = Notification.DEFAULT_SOUND;
                    //알림 소리를 한번만 내도록
                    Notifi.flags = Notification.FLAG_ONLY_ALERT_ONCE;
                    //확인하면 자동으로 알림이 제거 되도록
                    Notifi.flags = Notification.FLAG_AUTO_CANCEL;
                    Notifi_M.notify(777, Notifi);
                    break;
                default:
                    break;
            }


            //토스트 띄우기
            PowerManager pm = (PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            Log.e("screen on", ""+isScreenOn);
            if(isScreenOn==false)
            {
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"myapp:locked");
                wl.acquire(10000);
                PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"myapp:MyCpuLock");

                wl_cpu.acquire(10000);
            }
           // Toast.makeText(MyService.this, test, Toast.LENGTH_LONG).show();
        }
    };
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}
