package j.com.weatherapp.Data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityWeather {

    String locationKey;

    JSONObject currentCon;
    JSONArray fiveForecasts;

    String curLocalObservationDateTime;
    String curWeatherText;
    boolean curIsDayTime;
    double curTemperature;
    double curRealFeelTemperature;
    Integer curRelativeHumidity;
    Integer curPressure;

    List <DayWeather> dayForecast;

    public CityWeather() {
        dayForecast = new ArrayList<>();
    }

    public List<DayWeather> getDayForecast() {
        return dayForecast;
    }

    public JSONArray getFiveForecasts() {
        return fiveForecasts;
    }

    public void setFiveForecasts(JSONArray fiveForecasts) {
        this.fiveForecasts = fiveForecasts;
        for (int i = 0; i <=4; i++) {
            try {
                DayWeather dayWeather = new DayWeather();
                dayWeather.setJsonWeather(fiveForecasts.getJSONObject(i));
                dayForecast.add(dayWeather);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setLocationKey(String locationKey) {
        this.locationKey = locationKey;
    }

    public void setCurrentCon(JSONObject currentCon) {
        this.currentCon = currentCon;
    }

    public String getLocationKey() {
        return locationKey;
    }

    public JSONObject getCurrentCon() {
        return currentCon;
    }

    public String getCurLocalObservationDateTime() {
        return curLocalObservationDateTime;
    }

    public void setCurLocalObservationDateTime(String curLocalObservationDateTime) {
        this.curLocalObservationDateTime = curLocalObservationDateTime;
    }

    public String getCurWeatherText() {
        return curWeatherText;
    }

    public void setCurWeatherText(String curWeatherText) {
        this.curWeatherText = curWeatherText;
    }

    public boolean isCurIsDayTime() {
        return curIsDayTime;
    }

    public void setCurIsDayTime(boolean curIsDayTime) {
        this.curIsDayTime = curIsDayTime;
    }

    public double getCurTemperature() {
        return curTemperature;
    }

    public void setCurTemperature(double curTemperature) {
        this.curTemperature = curTemperature;
    }

    public double getCurRealFeelTemperature() {
        return curRealFeelTemperature;
    }

    public void setCurRealFeelTemperature(double curRealFeelTemperature) {
        this.curRealFeelTemperature = curRealFeelTemperature;
    }

    public Integer getCurRelativeHumidity() {
        return curRelativeHumidity;
    }

    public void setCurRelativeHumidity(Integer curRelativeHumidity) {
        this.curRelativeHumidity = curRelativeHumidity;
    }

    public Integer getCurPressure() {
        return curPressure;
    }

    public void setCurPressure(Integer curPressure) {
        this.curPressure = curPressure;
    }

    public static final class CityWeatherEntry implements BaseColumns {
        public static final String PATH_CITY = "city";
        public static final String CONTENT_AUTHORITY = "com.j.provider.Data.WeatherProvider";
        private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        // Content URI represents the base location for the table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CITY).build();
        // These are special type prefixes that specify if a URI returns a list or a specific item
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_CITY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CITY;

        // Define the table schema
        public static final String TABLE_NAME = "cityTable";
        public static final String COLUMN_LOCATION_KEY = "cityLocationKey";
        public static final String COLUMN_LOCAL_OBSERVATION_DATE_TIME = "cityLocalObservationDateTime";
        public static final String COLUMN_WEATHER_TEXT = "cityWeatherText";
        public static final String COLUMN_DAY_TIME = "cityIsDayTime";
        public static final String COLUMN_TEMPERATURE = "cityTemperature";
        public static final String COLUMN_REALFEEL_TEMPERATURE = "cityRealFeelTemperature";
        public static final String COLUMN_HUMIDITY = "cityRelativeHumidity";
        public static final String COLUMN_PRESSURE = "cityPressure";
        public static final String COLUMN_CURRENT_CONDITION = "cityCurrentCondition";
        public static final String COLUMN_FIVE_FORECASTS = "cityFiveForecasts";


        // Define a function to build a URI to find a specific movie by it's identifier
        public static Uri buildCityWeatherUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
