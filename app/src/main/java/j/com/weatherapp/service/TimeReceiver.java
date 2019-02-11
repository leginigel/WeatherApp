package j.com.weatherapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class TimeReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(this.getClass().getSimpleName(), this.getClass().getName() + System.currentTimeMillis());

        Intent i = new Intent(context, TimeService.class);
        context.startService(i);
    }
}
