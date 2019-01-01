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
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;


public class MyService extends Service {
    NotificationManager Notifi_M;
    public int pre_longi;
    public int pre_lati;
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
        thread = new ServiceThread(getApplicationContext(),handler, longitude, latitude);
        thread.start();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업
    @Override
    public void onDestroy() {
        if(thread != null) {
            thread.stopForever();
            thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
        }
        super.onDestroy();
    }



    class myServiceHandler extends Handler {
        @TargetApi(Build.VERSION_CODES.O)
        @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
            int state = msg.getData().getInt("state");

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
                case 4:
                    Notifi = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Rain and Cold")
                            .setContentText("지금 비오고 추워요. 조심하세요! ")
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
                case 5:
                    Notifi = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Snow")
                            .setContentText("밖이 춥고 눈이 와요!")
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
