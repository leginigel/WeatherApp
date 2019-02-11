package j.com.weatherapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import j.com.weatherapp.MainActivity;

public class TimeService extends IntentService {

    String temperature;
    Handler handler = new Handler();
    public TimeService() {
        super("Time");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Temperature", 0);
        temperature = preferences.getString("Taipei", null);
//        long endTime = System.currentTimeMillis() + 5*1000;
//        Log.d(this.getPackageName(), this.getClass().getName() + System.currentTimeMillis());
//        while (System.currentTimeMillis() < endTime){
//
//        }
        Runnable runnable = () -> {
            Log.v("TimeService", " Temperature " + temperature);
            MainActivity.onNotifyCity(getApplicationContext());
//                handler.postDelayed(this, 10000);
        };
        handler.post(runnable);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
