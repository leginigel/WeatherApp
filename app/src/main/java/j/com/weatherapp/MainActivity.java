package j.com.weatherapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity{

    private static String Tag = MainActivity.class.getSimpleName();
    public static RequestQueue mRequestQueue;
    public static String url ="content://com.j.provider.Data.WeatherProvider/city";
    public static BottomNavigationView bottomNavigationView;
    public static List<String> cityList = new ArrayList<>();
    private ViewPager viewPager;
    private WeatherFragmentPageAdapter WFPA;
    public static SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Setting", 0);

        SharedViewModel model = ViewModelProviders.of(this).get(SharedViewModel.class);
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
