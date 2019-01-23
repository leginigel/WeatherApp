package j.com.weatherapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import j.com.weatherapp.surfaceview.BacGImgView;
import j.com.weatherapp.surfaceview.HaloView;
import j.com.weatherapp.surfaceview.RainyView;
import j.com.weatherapp.surfaceview.SinWaveView;

public class MainActivity extends AppCompatActivity{

    private static String Tag = MainActivity.class.getSimpleName();
    public static String url ="content://com.j.provider.Data.WeatherProvider/city";
    public static RequestQueue mRequestQueue;
    public static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedViewModel model;
    public static List<String> cityList = new ArrayList<>();
    public static BottomNavigationView bottomNavigationView;
    private ViewGroup mAnimatedFrame;
    private ViewPager viewPager;
    private WeatherFragmentPageAdapter WFPA;

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
        mAnimatedFrame = findViewById(R.id.frame_mid_anim);
        mAnimatedFrame.addView(rainyView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mAnimatedFrame.removeViewAt(0);
        mAnimatedFrame.addView(haloView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

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
            viewPager.setCurrentItem(select);
        });

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
                mAnimatedFrame.removeViewAt(0);
                if (i==0) {
                    mAnimatedFrame.addView(haloView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
                else if (i==1) mAnimatedFrame.addView(rainyView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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

        Set<String> set = new LinkedHashSet<String>(cityList);
        editor = sharedPreferences.edit();
//        editor.putStringSet("city", set);
        editor.putInt("citysize", cityList.size());
        for (int i=0; i<cityList.size();i++)
            editor.putString("city" + i, cityList.get(i));
        editor.commit();
    }

}
