package j.com.weatherapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class WeatherProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private WeatherDBHelper mOpenDBHelper;

    private static final int CITY = 200;
    private static final int CITY_ID = 201;

    @Override
    public boolean onCreate() {

        mOpenDBHelper = new WeatherDBHelper(getContext());
//        Log.d("Provider","onCreate " + sUriMatcher.match(Uri.parse("content://com.j.provider.WeatherProvider/city")));
        return true;
    }

    public static UriMatcher buildUriMatcher(){
        String content = CityWeather.CityWeatherEntry.CONTENT_AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, CityWeather.CityWeatherEntry.PATH_CITY, CITY);
        matcher.addURI(content, CityWeather.CityWeatherEntry.PATH_CITY + "/#", CITY_ID);

        return matcher;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenDBHelper.getWritableDatabase();
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            case CITY:
                retCursor = db.query(
                        CityWeather.CityWeatherEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CITY_ID:
                long _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        CityWeather.CityWeatherEntry.TABLE_NAME,
                        projection,
                        CityWeather.CityWeatherEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch(sUriMatcher.match(uri)){
            case CITY:
                return CityWeather.CityWeatherEntry.CONTENT_TYPE;
            case CITY_ID:
                return CityWeather.CityWeatherEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenDBHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch(sUriMatcher.match(uri)){
            case CITY:
                _id = db.insert(CityWeather.CityWeatherEntry.TABLE_NAME, null, values);
                if(_id > 0){
                    returnUri = CityWeather.CityWeatherEntry.buildCityWeatherUri(_id);
                } else{
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Use this on the URI passed into the function to notify any observers that the uri has
        // changed.
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenDBHelper.getWritableDatabase();
        int rows; // Number of rows effected

        switch(sUriMatcher.match(uri)){
            case CITY:
                rows = db.delete(CityWeather.CityWeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because null could delete all rows:
        if(selection == null || rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenDBHelper.getWritableDatabase();
        int rows;

        switch(sUriMatcher.match(uri)){
            case CITY:
                rows = db.update(CityWeather.CityWeatherEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
}
