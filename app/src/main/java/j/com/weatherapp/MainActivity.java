package j.com.weatherapp;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import j.com.weatherapp.Data.CityWeather;
import j.com.weatherapp.Data.VolleyWeather;

public class MainActivity extends AppCompatActivity {

    private static String Tag = MainActivity.class.getSimpleName();
    public static RequestQueue mRequestQueue;
    public static String url ="content://com.j.provider.Data.WeatherProvider/city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(
                new WeatherFragmentPageAdapter(getSupportFragmentManager(), MainActivity.this));

        mRequestQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll("GG");
        }
    }


}
