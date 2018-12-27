package j.com.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utils {
    public static String Scale(Context context){
        String Celsius = "°C", Fahrenheit = "°F", Kelvin = "K";
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String scale = sharedPref.getString("metric", Celsius);
        switch (scale){
            case "°C":
                return Celsius;
            case "°F":
                return Fahrenheit;
            case "K":
                return Kelvin;
            default:
                return Celsius;
        }
    }

    public static Integer Scale(Context context, int temperature){
        String Celsius = "°C";
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String scale = sharedPref.getString("metric", Celsius);
        switch (scale){
            case "°C":
                return temperature;
            case "°F":
                return Math.round((temperature*9/5)+32);
            case "K":
                return Math.round(temperature+273.15f);
            default:
                return temperature;
        }
    }

    public static String week(int week){
        String s_week = "";
        switch (week){
            case 1:s_week += "Sun ";break;
            case 2:s_week += "Mon ";break;
            case 3:s_week += "Tue ";break;
            case 4:s_week += "Wed ";break;
            case 5:s_week += "Thu ";break;
            case 6:s_week += "Fri ";break;
            case 7:s_week += "Sat ";break;
        }
        return s_week;
    }
}
