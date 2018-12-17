package j.com.weatherapp;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import j.com.weatherapp.Data.CityWeather;
import j.com.weatherapp.Data.VolleyWeather;
import j.com.weatherapp.Weather.ForecastsAdapter;

import static j.com.weatherapp.MainActivity.Scale;
import static j.com.weatherapp.MainActivity.url;

public class PageFragment extends Fragment {

    private SwipeRefreshLayout swipeContainer;
    // True if the shadow view between the card header and the RecyclerView
    // is currently showing.
    private boolean mIsShowingCardHeaderShadow;

//    private List<CityWeather> cityWeatherList;
    private int mPage;
    private String mCity;
    private String mKey;
    private CityWeather mCityWeather;
    private VolleyWeather mVolleyWeather;
    private VolleyResponseListener listener;
    private ContentResolver mResolver;
    private SharedPreferences mSetting;

    public static Fragment newInstance(int page, String city) {
        Bundle args = new Bundle();
        args.putInt("PAGE",page);
        args.putString("City", city);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt("PAGE");
        mCity = getArguments().getString("City");
        mResolver = getActivity().getContentResolver();
        mSetting = getActivity().getSharedPreferences("Setting", 0);
        mKey = mSetting.getString(mCity, null);
        Log.i(this.getClass().getSimpleName(), "onCreate city:" + mKey);

        mCityWeather = new CityWeather();
        mVolleyWeather = new VolleyWeather(getActivity(), mCityWeather);
    }

    TextView city, temperature, temperature_range, real_feel, weather_text, pressure, humidity
            , uv_index, observer_time;
    private ForecastsAdapter rvAdapter;
    private RecyclerView rv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);

        rv = view.findViewById(R.id.card_recyclerview);
        final View cardHeaderShadow = view.findViewById(R.id.card_header_shadow);
        final LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        rvAdapter = new ForecastsAdapter(getActivity(), mCityWeather.getDayForecast());
        rv.setLayoutManager(lm);
        rv.setAdapter(rvAdapter);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), lm.getOrientation()));

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

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mKey != null)
                    mVolleyWeather.fetchCurrentCondition(mCityWeather.getCityKey(), listener, true);
                else
                    mVolleyWeather.fetchCurrentCondition(mCityWeather.getCityKey(), listener, false);
            }
        });

        nsv.setOnTouchListener(
                new NestedScrollView.OnTouchListener() {
                    private long startClickTime;

                    @Override
                    public boolean onTouch(View view, MotionEvent event) {

                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            startClickTime = System.currentTimeMillis();
//                                           Log.d("Frag", "OnTouchd");
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {

                            if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
                                // Touch was a simple tap. Do whatever.
                                if (MainActivity.bottomNavigationView.getVisibility() == View.GONE) {
                                    MainActivity.bottomNavigationView.animate().alpha(1).setDuration(500).start();
                                    MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                                }
                                else {
                                    MainActivity.bottomNavigationView.setAlpha(0);
                                    MainActivity.bottomNavigationView.setVisibility(View.GONE);
                                }
//                                               Log.d("Frag", "OnTouchu");
                                view.performClick();
                                return true;
                            } else {
                                // Touch was a not a simple tap.
//                                               Log.d("Frag", "OnTouche");
                                return false;
                            }
                        }
                        return false;
                    }
                }
        );
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // TextViews set the data at listener response

        city = view.findViewById(R.id.city);
        temperature = view.findViewById(R.id.temperature);
        temperature_range = view.findViewById(R.id.temperature_range);
        real_feel = view.findViewById(R.id.realfeel);
        weather_text = view.findViewById(R.id.weathertext);
        pressure = view.findViewById(R.id.pressure);
        humidity = view.findViewById(R.id.humidity);
        uv_index = view.findViewById(R.id.uv);
        observer_time = view.findViewById(R.id.observe_time);

        city.setText(mCity);

        setListener();

        checkCityExist();

        return view;
    }

    private void setTodayCard() {

        String MaxMin = null;
        if (!mCityWeather.getDayForecast().isEmpty()) {
            MaxMin = String.valueOf(Scale(getActivity(), (int)mCityWeather.getDayForecast().get(0).getMaxTemperature()))
                    + "° / "
                    + String.valueOf(Scale(getActivity(), (int)mCityWeather.getDayForecast().get(0).getMinTemperature()))
                    + Scale(getActivity());
        }

        SharedPreferences Temperature = getActivity().getSharedPreferences("Temperature", 0);
        SharedPreferences.Editor editor = Temperature.edit();
        editor.putString(mCity, String.valueOf((int)mCityWeather.getCurTemperature()));
        editor.apply();

        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        DateFormat toFormat = new SimpleDateFormat("MM/dd HH:mm 'updated.'");
        String sDate = null;
        try {
            sDate = toFormat.format(df.parse(mCityWeather.getCurLocalObservationDateTime()));
            c.setTime(df.parse(mCityWeather.getCurLocalObservationDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String wDate = ForecastsAdapter.week(c.get(Calendar.DAY_OF_WEEK)) + sDate;

        temperature.setText(
                String.valueOf(Scale(getActivity(), (int)mCityWeather.getCurTemperature()) + Scale(getActivity())));
        temperature_range.setText(MaxMin);
        real_feel.setText(
                String.valueOf(Scale(getActivity(), (int)mCityWeather.getCurRealFeelTemperature()) + Scale(getActivity())));
        weather_text.setText(mCityWeather.getCurWeatherText());
        pressure.setText(String.valueOf(mCityWeather.getCurPressure() + " hpa"));
        humidity.setText(String.valueOf(mCityWeather.getCurRelativeHumidity() + "%"));
        uv_index.setText(mCityWeather.getCurUVIndexText());
        observer_time.setText(wDate);

//        rvAdapter.setRecyclerViewItems(mCityWeather.getDayForecast());
        rvAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    private void setListener() {
        listener = new VolleyResponseListener() {

            @Override
            public void onError(VolleyError message) {
                if (message instanceof TimeoutError || message instanceof NoConnectionError)
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();

                if (mCityWeather.getCityKey() != null)
                    setTodayCard();
                else
                    Log.i(mCity + "onError","City Weather get LocationKey null");
            }

            @Override
            public void onResponse(Object response) {

                if (mCityWeather.getCityKey() != null) {
                    SharedPreferences.Editor editor = mSetting.edit();
                    editor.putString(mCity, mCityWeather.getCityKey());
                    editor.apply();

                    setTodayCard();

                    if (swipeContainer.isRefreshing())
                        Toast.makeText(getActivity(), "Update Success", Toast.LENGTH_LONG).show();
                }
                else
                    Log.i(mCity + "onError","City Weather get LocationKey null");
            }
        };
    }

    public void checkCityExist(){
        if (mKey != null){
            Uri uri = Uri.parse(url);
            Cursor cursor = null;
            String[] projection = {
                    CityWeather.CityWeatherEntry.COLUMN_LOCATION_KEY,
                    CityWeather.CityWeatherEntry.COLUMN_LOCAL_OBSERVATION_DATE_TIME,
                    CityWeather.CityWeatherEntry.COLUMN_DAY_TIME,
                    CityWeather.CityWeatherEntry.COLUMN_TEMPERATURE,
                    CityWeather.CityWeatherEntry.COLUMN_REALFEEL_TEMPERATURE,
                    CityWeather.CityWeatherEntry.COLUMN_HUMIDITY,
                    CityWeather.CityWeatherEntry.COLUMN_PRESSURE,
                    CityWeather.CityWeatherEntry.COLUMN_WEATHER_TEXT,
                    CityWeather.CityWeatherEntry.COLUMN_CURRENT_CONDITION,
                    CityWeather.CityWeatherEntry.COLUMN_FIVE_FORECASTS
            };
            String select = "("+ CityWeather.CityWeatherEntry.COLUMN_LOCATION_KEY +"='"+ mKey +"')";
            try{
                cursor = mResolver.query(uri, projection, select, null, null);
                if (cursor != null) {
                    Log.i(mCity + " Cursor", "Fetch Data...");

                    if(cursor.moveToFirst()) {
                        Log.d(mCity + " Cursor", cursor.getString(0) + "  " + cursor.getString(1));
                        Log.i(mCity + " Cursor", "move to first");

                        mCityWeather.setCityKey(cursor.getString(0));
//                        mCityWeather.setCurLocalObservationDateTime(cursor.getString(1));
//                        mCityWeather.setCurIsDayTime(Boolean.valueOf(cursor.getString(2)));
//                        mCityWeather.setCurTemperature(cursor.getDouble(3));
//                        mCityWeather.setCurRealFeelTemperature(cursor.getDouble(4));
//                        mCityWeather.setCurRelativeHumidity(cursor.getInt(5));
//                        mCityWeather.setCurPressure(cursor.getInt(6));
//                        mCityWeather.setCurWeatherText(cursor.getString(7));
                        mCityWeather.setCurrentCondition(new JSONObject(cursor.getString(8)));
                        mCityWeather.setFiveForecasts(new JSONArray(cursor.getString(9)));

                        Log.v(mCity + " Cursor CurrentCon", cursor.getString(8));
                        Log.v(mCity + " Cursor FiveForecasts", cursor.getString(9));
                        mVolleyWeather.fetchCurrentCondition(mKey, listener, true);
                    }
                    else {
                        mVolleyWeather.fetchCurrentCondition(mKey, listener, false);
                    }

//                while (cursor.moveToNext()) Log.d("Cursor", cursor.getString(0) + " " + cursor.getString(1));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        else{
            mVolleyWeather.fetchLocation(mCity, listener, false);
        }
    }

    public interface VolleyResponseListener {
        void onError(VolleyError message);

        void onResponse(Object response);
    }

    private static void showOrHideView(View view, boolean shouldShow) {
        view.animate().alpha(shouldShow ? 1f : 0f)
                .setDuration(100)
                .setInterpolator(new DecelerateInterpolator());
    }
}
