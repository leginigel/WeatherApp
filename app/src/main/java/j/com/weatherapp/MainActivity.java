package j.com.weatherapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String Tag = MainActivity.class.getSimpleName();
    public static RequestQueue mRequestQueue;
    public static String url ="content://com.j.provider.Data.WeatherProvider/city";
    public static BottomNavigationView bottomNavigationView;
    public static ArrayList<String> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList.add("Taipei");
        cityList.add("London");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        WeatherFragmentPageAdapter WFPA =
                new WeatherFragmentPageAdapter(getSupportFragmentManager(), MainActivity.this);
        CityFragment cityFragment = new CityFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_setting:
                        return true;
                    case R.id.navigation_list:
                        viewPager.setVisibility(View.GONE);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragmentholder, cityFragment, "t")
                                .commit();
                        return true;
                    case R.id.navigation_search:
                        return true;
                    case R.id.navigation_weather:
                        viewPager.setVisibility(View.VISIBLE);
//                        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
//                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//                        }
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag("t");
                        if(fragment != null)
                        getSupportFragmentManager()
                                .beginTransaction()
                                .remove(fragment)
                                .commit();
                        return true;
                }
                return false;
            }
        });
        viewPager.setAdapter(WFPA);

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
