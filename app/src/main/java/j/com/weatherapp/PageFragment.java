package j.com.weatherapp;

import android.app.ActionBar;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import j.com.weatherapp.Data.CityWeather;
import j.com.weatherapp.Data.VolleyWeather;

import static j.com.weatherapp.MainActivity.url;

public class PageFragment extends Fragment {

    // True if the shadow view between the card header and the RecyclerView
    // is currently showing.
    private boolean mIsShowingCardHeaderShadow;

//    private List<CityWeather> cityWeatherList;
    private CityWeather mCityWeather = new CityWeather();
    private VolleyWeather mVolleyWeather;
    private VolleyResponseListener listener;

    public static Fragment newInstance(int page, String city) {
        Bundle args = new Bundle();
        args.putInt("PAGE",page);
        args.putString("City", city);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);

        mVolleyWeather = new VolleyWeather(getActivity(), mCityWeather);
//        cityWeatherList = new ArrayList<>();

        final RecyclerView rv = view.findViewById(R.id.card_recyclerview);
        final LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(lm);
        final ForecastsAdapter rvAdapter = new ForecastsAdapter(getActivity(), mCityWeather.getDayForecast());
        rv.setAdapter(rvAdapter);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), lm.getOrientation()));

        final View cardHeaderShadow = view.findViewById(R.id.card_header_shadow);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                // Animate the shadow view in/out as the user scrolls so that it
                // looks like the RecyclerView is scrolling beneath the card header.
                final boolean isRecyclerViewScrolledToTop =
                        lm.findFirstVisibleItemPosition() == 0
                                && lm.findViewByPosition(0).getTop() == 0;
                if (!isRecyclerViewScrolledToTop && !mIsShowingCardHeaderShadow) {
                    mIsShowingCardHeaderShadow = true;
                    showOrHideView(cardHeaderShadow, true);
                } else if (isRecyclerViewScrolledToTop && mIsShowingCardHeaderShadow) {
                    mIsShowingCardHeaderShadow = false;
                    showOrHideView(cardHeaderShadow, false);
                }
            }
        });

        final NestedScrollView nsv = view.findViewById(R.id.nestedscrollview);
        nsv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(
                    NestedScrollView nsv, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0 && oldScrollY > 0) {
                    // Reset the RecyclerView's scroll position each time the card
                    // returns to its starting position.
                    rv.scrollToPosition(0);
                    cardHeaderShadow.setAlpha(0f);
                    mIsShowingCardHeaderShadow = false;
                }
            }
        });

        final TextView temperature,temperature_range,realfeel,weathertext,pressure,humidity;
        temperature=view.findViewById(R.id.temperature);
        temperature_range=view.findViewById(R.id.temperature_range);
        realfeel=view.findViewById(R.id.realfeel);
        weathertext=view.findViewById(R.id.weathertext);
        pressure=view.findViewById(R.id.pressure);
        humidity=view.findViewById(R.id.humidity);

        listener = new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                // do something...
            }

            @Override
            public void onResponse(Object response) {
//                cityWeatherList.add(mCityWeather);
//                String curCon =" temp:"+ mCityWeather.getCurTemperature()+
//                        " text:"+ mCityWeather.getCurWeatherText()
//                        +" time:"+ mCityWeather.getCurLocalObservationDateTime()
//                        +" real:"+ mCityWeather.getCurRealFeelTemperature()
//                        +" humi:"+ mCityWeather.getCurRelativeHumidity()
//                        +" pres:"+ mCityWeather.getCurPressure();
                temperature.setText(String.valueOf(mCityWeather.getCurTemperature()) + " C");
//                temperature_range.setText(String.valueOf(mCityWeather.getCurTemperature()) + " C");
                realfeel.setText(String.valueOf(mCityWeather.getCurRealFeelTemperature()) + " C");
                weathertext.setText(mCityWeather.getCurWeatherText());
                pressure.setText(String.valueOf(mCityWeather.getCurPressure()) + " hpa");
                humidity.setText(String.valueOf(mCityWeather.getCurRelativeHumidity() + "%"));

                rvAdapter.notifyDataSetChanged();
            }
        };

        checkCityExist();

        return view;
    }

    public void checkCityExist(){
        ContentResolver resolver = getActivity().getContentResolver();
        Uri uri = Uri.parse(url);
        Cursor cursor = null;
        try{
            String select = "("+ CityWeather.CityWeatherEntry.COLUMN_LOCATION_KEY +"='"+ "4-315078_1_AL" +"')";
            cursor = resolver.query(uri, null, null, null, null);
            if (cursor != null) {
                Log.i("Cursor", "Fetch Data...");

                while (cursor.moveToNext()) {
                    Log.d("Cursor", cursor.getString(0));
                }
                Log.i("Cursor", "Update Data");
                mVolleyWeather.fetchCurrentCondition("4-315078_1_AL", listener, true);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i("Cursor", "Insert Data");
            mVolleyWeather.fetchCurrentCondition("4-315078_1_AL", listener, false);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(Object response);
    }

    private static void showOrHideView(View view, boolean shouldShow) {
        view.animate().alpha(shouldShow ? 1f : 0f)
                .setDuration(100)
                .setInterpolator(new DecelerateInterpolator());
    }
}
