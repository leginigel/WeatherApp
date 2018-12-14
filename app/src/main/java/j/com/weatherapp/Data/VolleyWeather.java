package j.com.weatherapp.Data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import j.com.weatherapp.MainActivity;
import j.com.weatherapp.PageFragment;

public class VolleyWeather {
    private static final String Host = "http://dataservice.accuweather.com";
    private static final String apiKey = "?apikey=uHtD8X8XUwOpXPKbFDMijg7KttBv5GfN";
    private String Tag = VolleyWeather.class.getSimpleName();
    private String City;
    CityWeather mCityWeather;
    private Context mContext;
    private static String language = "&language=en-us";
    private String detail = "&details=true";
    private String metric = "&metric=true";

    public VolleyWeather(Context mContext, CityWeather mCityWeather) {
        this.mContext = mContext;
        this.mCityWeather = mCityWeather;
    }

    public void fetchLocation(final String city, final PageFragment.VolleyResponseListener listener, final boolean fromSearch) {
        this.City = city;
        Log.d(Tag, City + " fetch Location");
        String url = Host + "/locations/v1/search" + apiKey + language  + "&q=" + city;

        JsonArrayRequest req = new JsonArrayRequest(
                JsonArrayRequest.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
//                            String result =
//                                    City + "City key is " + response.getJSONObject(0).getString("Key");
//                            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                            String key = response.getJSONObject(0).getString("Key");

                            mCityWeather.setLocation(response);
//                            mCityWeather.setCityKey(key);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            if (!fromSearch)
                                fetchCurrentCondition(mCityWeather.getCityKey(), listener, false);
                            else
                                listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(Tag, City + " Error fetch Location key");
                        VolleyLog.e("Error: ", error.getMessage());
                        error.printStackTrace();
                        listener.onError(error);
                    }
                }
        )
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Accept-Encoding", "gzip");
//                return headers;
//            }
//        }
                ;
        /* Add your Requests to the RequestQueue to execute */
        MainActivity.mRequestQueue.add(req);
    }

    public void fetchCurrentCondition
            (final String key, final PageFragment.VolleyResponseListener listener, final boolean exist) {
        Log.i(Tag, City + " fetch current condition");
        String url = Host + "/currentconditions/v1/" + key + apiKey + detail + language;

        JsonArrayRequest req = new JsonArrayRequest(
                JsonArrayRequest.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            String DateTime =
                                    response.getJSONObject(0).getString("LocalObservationDateTime");
                            Boolean IsDay =
                                    response.getJSONObject(0).getBoolean("IsDayTime");
                            double Temperature =
                                    response.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Metric").getDouble("Value");
                            double RealFeel =
                                    response.getJSONObject(0).getJSONObject("RealFeelTemperature").getJSONObject("Metric").getDouble("Value");
                            Integer Humidity =
                                    response.getJSONObject(0).getInt("RelativeHumidity");
                            Integer Pressure =
                                    response.getJSONObject(0).getJSONObject("Pressure").getJSONObject("Metric").getInt("Value");
                            String WeatherText =
                                    response.getJSONObject(0).getString("WeatherText");
                            JSONObject CurrentCondition =
                                    response.getJSONObject(0);
                            String WindDirection =
                                    response.getJSONObject(0).getJSONObject("Wind").getJSONObject("Direction").getString("Localized");
                            double WindSpeed =
                                    response.getJSONObject(0).getJSONObject("Wind").getJSONObject("Speed").getJSONObject("Metric").getDouble("UnitType");
                            String UVIndexText =
                                    response.getJSONObject(0).getString("UVIndexText");

                            mCityWeather.setCurrentCondition(CurrentCondition);
//                            mCityWeather.setCurPressure(Pressure);
//                            mCityWeather.setCurIsDayTime(IsDay);
//                            mCityWeather.setCurWeatherText(WeatherText);
//                            mCityWeather.setCurTemperature(Temperature);
//                            mCityWeather.setCurRelativeHumidity(Humidity);
//                            mCityWeather.setCurRealFeelTemperature(RealFeel);
//                            mCityWeather.setCurLocalObservationDateTime(DateTime);
//
//                            mCityWeather.setCurWindDirection(WindDirection);
//                            mCityWeather.setCurWindSpeed(WindSpeed);
//                            mCityWeather.setCurUVIndexText(UVIndexText);

                            ContentValues values = dataCityStore(DateTime, IsDay, Temperature, RealFeel,
                                    Humidity,  Pressure,  WeatherText,  CurrentCondition.toString());
                            ContentResolver resolver = mContext.getContentResolver();
                            Uri uri = Uri.parse(MainActivity.url);

                            if (exist) {
                                String select =
                                        "("+ CityWeather.CityWeatherEntry.COLUMN_LOCATION_KEY +"='"+ key +"')";
                                try {
                                    Log.i(Tag, City + " Update current condition");
                                    resolver.update(uri, values, select, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Log.i(Tag, City + " Insert current condition");
                                    resolver.insert(uri, values);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            listener.onResponse(response);
                            fetchFiveForecasts(listener);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(Tag, City + " Error fetch current condition");
                        VolleyLog.e("Error: ", error.getMessage());
                        error.printStackTrace();
                        if (exist)
                            listener.onError(error);
                        else
                            Toast.makeText(mContext, "Data Error", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        /* Add your Requests to the RequestQueue to execute */
        MainActivity.mRequestQueue.add(req);
    }

    public void fetchFiveForecasts(final PageFragment.VolleyResponseListener listener) {
        Log.i(Tag, City + " fetch Five Forecasts");
        String url = Host + "/forecasts/v1/daily/5day/" + mCityWeather.getCityKey() + apiKey
                + detail + metric + language;

        JsonObjectRequest req = new JsonObjectRequest(
                JsonObjectRequest.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray FiveForecasts = response.getJSONArray("DailyForecasts");
                            mCityWeather.getDayForecast().clear();
                            mCityWeather.setFiveForecasts(FiveForecasts);
                            dataFiveForecastsCityUpdateDB(FiveForecasts.toString());
                            Log.i(Tag, City + " Success fetch Five Forecasts");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            listener.onResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(Tag, City + " Error fetch Five Forecasts");
                        VolleyLog.e("Error: ", error.getMessage());
                        error.printStackTrace();
                        listener.onError(error);
                    }
                }
        );
        /* Add your Requests to the RequestQueue to execute */
        MainActivity.mRequestQueue.add(req);
    }

    public ContentValues dataCityStore(String DateTime, Boolean IsDay, double Temperature, double RealFeel,
                                Integer Humidity, Integer Pressure, String WeatherText, String CurrentCondition) {

        ContentValues values = new ContentValues();

        values.put(CityWeather.CityWeatherEntry.COLUMN_LOCATION_KEY, mCityWeather.getCityKey());
        values.put(CityWeather.CityWeatherEntry.COLUMN_LOCAL_OBSERVATION_DATE_TIME, DateTime);
        values.put(CityWeather.CityWeatherEntry.COLUMN_DAY_TIME, IsDay);
        values.put(CityWeather.CityWeatherEntry.COLUMN_TEMPERATURE, Temperature);
        values.put(CityWeather.CityWeatherEntry.COLUMN_REALFEEL_TEMPERATURE, RealFeel);
        values.put(CityWeather.CityWeatherEntry.COLUMN_HUMIDITY, Humidity);
        values.put(CityWeather.CityWeatherEntry.COLUMN_PRESSURE, Pressure);
        values.put(CityWeather.CityWeatherEntry.COLUMN_WEATHER_TEXT, WeatherText);
        values.put(CityWeather.CityWeatherEntry.COLUMN_CURRENT_CONDITION,CurrentCondition);
        values.put(CityWeather.CityWeatherEntry.COLUMN_FIVE_FORECASTS, "");

        return values;
    }

    public void dataFiveForecastsCityUpdateDB (String FiveForecasts){

        Uri uri = Uri.parse(MainActivity.url);
        ContentValues values = new ContentValues();
        values.put(CityWeather.CityWeatherEntry.COLUMN_FIVE_FORECASTS, FiveForecasts);
        String select = "("+ CityWeather.CityWeatherEntry.COLUMN_LOCATION_KEY +"='"
                                                            + mCityWeather.getCityKey() + "')";

        ContentResolver resolver = mContext.getContentResolver();
        resolver.update(uri, values, select, null);
    }

}
