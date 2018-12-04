package j.com.weatherapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class WeatherFragmentPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;

    private Context context;

    public WeatherFragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
                return PageFragment.newInstance(i + 1, MainActivity.cityList.get(i));
    }

    @Override
    public int getCount() {
        return MainActivity.cityList.size();
    }

}
