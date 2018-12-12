package j.com.weatherapp;

import android.arch.lifecycle.ViewModelProviders;
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

import j.com.weatherapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{

    private static String Tag = MainActivity.class.getSimpleName();
    public static RequestQueue mRequestQueue;
    public static String url ="content://com.j.provider.Data.WeatherProvider/city";
    public static BottomNavigationView bottomNavigationView;
    public static List<String> cityList = new ArrayList<>();
    public static ViewPager viewPager;
    public static WeatherFragmentPageAdapter WFPA;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedViewModel model = ViewModelProviders.of(this).get(SharedViewModel.class);
        model.getCityList().observe(this, city ->{
            cityList = city;
            WFPA.notifyDataSetChanged();
        });

//        ActivityMainBinding binding;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

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
                            .replace(R.id.frame_fragmentholder, settingFragment, "t")
                            .commit();
                    return true;
                case R.id.navigation_list:
//                        viewPager.setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("CityList", (ArrayList<String>) cityList);
                    cityFragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_fragmentholder, cityFragment, "t")
                            .commit();
                    return true;
                case R.id.navigation_search:
//                        viewPager.setVisibility(View.GONE);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_fragmentholder, searchFragment, "t")
                            .commit();
                    return true;
                case R.id.navigation_weather:
//                        viewPager.setVisibility(View.VISIBLE);
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
        });

        viewPager.setAdapter(WFPA);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d(Tag, "page select"+i);
                model.select(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

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
