package j.com.weatherapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WeatherFragmentPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"Taipei", "London", "Tab3"};
    private Context context;

    public WeatherFragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        return PageFragment.newInstance(i + 1, tabTitles[i]);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
