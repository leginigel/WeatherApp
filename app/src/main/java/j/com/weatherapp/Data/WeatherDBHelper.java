package j.com.weatherapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class WeatherDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "weatherList.db";

    public WeatherDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        addCityTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void addCityTable(SQLiteDatabase db){
        db.execSQL(
                "CREATE TABLE " + CityWeather.CityWeatherEntry.TABLE_NAME + " (" +
                        CityWeather.CityWeatherEntry._ID + " INTEGER PRIMARY KEY, " +
                        CityWeather.CityWeatherEntry.COLUMN_LOCATION_KEY + " TEXT NOT NULL, " +
                        CityWeather.CityWeatherEntry.COLUMN_LOCAL_OBSERVATION_DATE_TIME + " TEXT NOT NULL, " +
                        CityWeather.CityWeatherEntry.COLUMN_DAY_TIME + " BOOLEAN NOT NULL, " +
                        CityWeather.CityWeatherEntry.COLUMN_TEMPERATURE + " FLOAT NOT NULL, " +
                        CityWeather.CityWeatherEntry.COLUMN_REALFEEL_TEMPERATURE + " FLOAT NOT NULL, " +
                        CityWeather.CityWeatherEntry.COLUMN_HUMIDITY + " INTEGER NOT NULL, " +
                        CityWeather.CityWeatherEntry.COLUMN_PRESSURE + " INTEGER NOT NULL, " +
                        CityWeather.CityWeatherEntry.COLUMN_WEATHER_TEXT + " TEXT NOT NULL, " +
                        CityWeather.CityWeatherEntry.COLUMN_CURRENT_CONDITION + " TEXT NOT NULL, " +
                        CityWeather.CityWeatherEntry.COLUMN_FIVE_FORECASTS + " TEXT NOT NULL " + ");"
        );
    }
}
