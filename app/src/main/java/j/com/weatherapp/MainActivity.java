package j.com.weatherapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.RemoteInput;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.MessagingStyle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import j.com.weatherapp.service.TimeReceiver;
import j.com.weatherapp.service.TimeService;
import j.com.weatherapp.surfaceview.BacGImgView;
import j.com.weatherapp.surfaceview.CloudView;
import j.com.weatherapp.surfaceview.HaloView;
import j.com.weatherapp.surfaceview.RainyView;
import j.com.weatherapp.surfaceview.SinWaveView;
import j.com.weatherapp.surfaceview.SnowfallView;

public class MainActivity extends AppCompatActivity{

    private static String Tag = MainActivity.class.getSimpleName();
    public static String url ="content://com.j.provider.Data.WeatherProvider/city";
    public static RequestQueue mRequestQueue;
    public static SharedPreferences sharedPreferences;
    public static boolean FIRST_OPEN = true;
    private SharedPreferences.Editor editor;
    private SharedViewModel model;
    public static List<String> cityList = new ArrayList<>();
    public static BottomNavigationView bottomNavigationView;
    private ViewGroup mAnimatedFrame;
    private ViewPager viewPager;
    private WeatherFragmentPageAdapter WFPA;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BacGImgView bacGImgView = findViewById(R.id.background_view);
        bacGImgView.setZOrderOnTop(false);

        RainyView rainyView = new RainyView(this);
        rainyView.setZOrderMediaOverlay(true);
        HaloView haloView = new HaloView(this);
        haloView.setZOrderMediaOverlay(true);
        SnowfallView snowfallView = new SnowfallView(this);
        snowfallView.setZOrderMediaOverlay(true);
        CloudView cloudView = new CloudView(this);
        cloudView.setZOrderMediaOverlay(true);
        mAnimatedFrame = findViewById(R.id.frame_mid_anim);
//        mAnimatedFrame.addView(rainyView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mAnimatedFrame.removeViewAt(0);
        mAnimatedFrame.addView(cloudView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//        SinWaveView sinWaveView = findViewById(R.id.background_view1);
//        sinWaveView.setZOrderOnTop(false);
//        RainyView haloView = findViewById(R.id.background_view1);
//        haloView.setZOrderMediaOverlay(true);
//        addContentView(haloView, new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));
//        haloView.setZOrderOnTop(true);
//        haloView.setLayoutParams(new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,200));

        sharedPreferences = getSharedPreferences("Setting", 0);

        model = ViewModelProviders.of(this).get(SharedViewModel.class);
        model.getCityList().observe(this, city ->{
            Log.d(Tag, "observe city list");
            cityList = city;

            for (String i: cityList)
                Log.d(Tag, ""+i);
            WFPA.notifyDataSetChanged();
            viewPager.setAdapter(WFPA);
        });
        model.getSelected().observe(this, select ->{
            Log.d(Tag, "observe city select");
            if (select != null) {
                viewPager.setCurrentItem(select);
                onNotifyCity(this);
            }
        });

        model.getLocation(this);

//        ActivityMainBinding binding;
        bottomNavigationView = findViewById(R.id.navigationView);
        viewPager = findViewById(R.id.viewpager);

        WFPA = new WeatherFragmentPageAdapter(
                getSupportFragmentManager(), MainActivity.this);

        CityFragment cityFragment = new CityFragment();
        SettingFragment settingFragment = new SettingFragment();
        SearchFragment searchFragment = new SearchFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.navigation_setting:
//                        viewPager.setVisibility(View.GONE);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_fragmentholder, settingFragment, "setting")
                            .commit();
                    return true;
                case R.id.navigation_list:
//                        viewPager.setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("CityList", (ArrayList<String>) cityList);
                    cityFragment.setArguments(bundle);
//                    if (!cityFragment.isAdded()){
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .add(R.id.frame_fragmentholder, cityFragment)
//                                .commit();
//                    }
//                    else
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .show(cityFragment)
//                                .commit();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_fragmentholder, cityFragment, "list")
                            .commit();
                    return true;
                case R.id.navigation_search:
//                        viewPager.setVisibility(View.GONE);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_fragmentholder, searchFragment, "search")
                            .commit();
                    return true;
                case R.id.navigation_weather:
//                        viewPager.setVisibility(View.VISIBLE);
//                        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
//                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                        }
//                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragmentholder);
//                    if(fragment != null)
//                        getSupportFragmentManager()
//                                .beginTransaction()
//                                .hide(fragment)
//                                .commit();
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragmentholder);
                    if(fragment != null)
                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(fragment)
                                .commit();
                    return true;
            }
            return false;
        });

        viewPager.setAdapter(WFPA);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d(Tag, "page select "+i);
                model.select(i);
//                bacGImgView.switchBlackScreen(true);
                mAnimatedFrame.removeViewAt(0);
                if (i==0) {
                    mAnimatedFrame.addView(haloView, 0,
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                else if (i==1)
                    mAnimatedFrame.addView(snowfallView, 0,
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                bacGImgView.switchBlackScreen(false);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

                if (i == ViewPager.SCROLL_STATE_DRAGGING) {
                    Log.d(Tag, "SCROLL_STATE_DRAGGING "+i);
//                    bacGImgView.switchBlackScreen(true);
//                    bacGImgView.setAlpha(0);
                }
                else if (i == ViewPager.SCROLL_STATE_SETTLING) {
                    Log.d(Tag, "SCROLL_STATE_SETTLING "+i);
//                    bacGImgView.switchBlackScreen(false);
//                    bacGImgView.setAlpha(1);
                }
                else if (i == ViewPager.SCROLL_STATE_IDLE) {
                    Log.d(Tag, "SCROLL_STATE_IDLE " + i);
                }
            }
        });

        mRequestQueue = Volley.newRequestQueue(this);

        createNotificationChannel();

        Intent i = new Intent(this, TimeReceiver.class);
        i.putExtra("f", "b");
        PendingIntent p = PendingIntent.getBroadcast(this, TimeReceiver.REQUEST_CODE,
                i, PendingIntent.FLAG_UPDATE_CURRENT);
//        sendBroadcast(i);

        AlarmManager a = (AlarmManager) getSystemService(ALARM_SERVICE);
        a.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES/60 ,p);
//        a.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),p);

//        Intent i = new Intent(this, TimeService.class);
//        i.putExtra("f", "b");
//        PendingIntent p = PendingIntent.getService(this, 0,
//                i, PendingIntent.FLAG_CANCEL_CURRENT);
//        startService(i);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "weather"/*getString(R.string.channel_name)*/;
            String description = "description"/*getString(R.string.channel_description)*/;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("weather", name, importance);
            channel.setDescription(description);
            channel.setShowBadge(false);
//            channel.setLightColor();
//            channel.setSound();
//            channel.setLockscreenVisibility();
//            channel.setVibrationPattern();
//            You don't have to set the sounds/lights/vibration, you CAN do it.
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
//        else {
//            builder.setContentTitle(context.getString(R.string.app_name))
//                    .setPriority(NotificationCompat.PRIORITY_HIGH)
//                    .setColor(ContextCompat.getColor(context, R.color.transparent))
//                    .setVibrate(new long[]{100, 250})
//                    .setLights(Color.YELLOW, 500, 5000)
//                    .setAutoCancel(true);
//        }
    }

    public static void onNotifyCity(@NonNull Context context/*int select*/){

        RemoteInput remoteInput = new RemoteInput.Builder("key").setLabel("Type Message").build();
        // Generate the notification action
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                R.drawable.ic_share_white, "Reply", null)
                .addRemoteInput(remoteInput).build();
//        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources() ,R.drawable.ic_search_black_24dp);
//        PageFragment pg = (PageFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
//        PageFragment pg = (PageFragment) WFPA.getItem(0);
// Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // Get the layouts to use in the custom notification
//        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.notification_small);
//        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.notification_large);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "weather")
                .setSmallIcon(R.drawable.ic_share_white)
                .setContentTitle(cityList.get(0/*select*/))
                .setContentText("Mother")
//                .setStyle(new NotificationCompat.BigPictureStyle())
//                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
//                .setCustomContentView(notificationLayout)
//                .setCustomBigContentView(notificationLayoutExpanded)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources() ,R.drawable.ic_search_black_24dp))
                .addAction(R.drawable.ic_share_white, "Yes",null)
                .addAction(action)
//                .setSound()
//                .setTimeoutAfter(10000)
//                .setOngoing(true)
                .setAutoCancel(true);
//        setAutoCancel() removes the notification when the user taps it.

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(7, mBuilder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 666: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    model.getLocation(this);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GG");
        }

        Set<String> set = new LinkedHashSet<String>(cityList);
        editor = sharedPreferences.edit();
//        editor.putStringSet("city", set);
        editor.putInt("citysize", cityList.size());
        for (int i=0; i<cityList.size();i++)
            editor.putString("city" + i, cityList.get(i));
        editor.commit();
    }

}
