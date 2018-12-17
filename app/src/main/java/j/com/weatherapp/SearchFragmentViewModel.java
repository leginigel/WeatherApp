package j.com.weatherapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import j.com.weatherapp.Data.CityWeather;
import j.com.weatherapp.Data.VolleyWeather;

public class SearchFragmentViewModel extends ViewModel{

    private final MutableLiveData<List<City>> cities = new MutableLiveData<>();

    LiveData<List<City>> getCities() {
        return cities;
    }

    void searchCity(Context context, String query){
        CityWeather cityWeather = new CityWeather();
        VolleyWeather volleyWeather = new VolleyWeather(context, cityWeather);
        PageFragment.VolleyResponseListener listener = new PageFragment.VolleyResponseListener() {
            @Override
            public void onError(VolleyError message) {
//                Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show();
                List<City> city = new ArrayList<>();
                city.add(new City("Tokyo", "Asia", "Tokyo", "Japan"));
                cities.setValue(city);
            }

            @Override
            public void onResponse(Object response) {
                JSONArray location = (JSONArray) response;
                List<City> city = new ArrayList<>();
                for (int i = 0; i < location.length(); i++) {
                    try {
                        String key = location.getJSONObject(i).getString("Key");
                        String name = location.getJSONObject(i).getString("LocalizedName");
                        String region = location.getJSONObject(i).getJSONObject("Region").getString("LocalizedName");
                        String country = location.getJSONObject(i).getJSONObject("Country").getString("LocalizedName");
                        String area = location.getJSONObject(i).getJSONObject("AdministrativeArea").getString("LocalizedName");

                        city.add(new City(name, region, area, country));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                cities.setValue(city);
                Log.v("SearchViewModel", response.toString());
            }
        };
        volleyWeather.fetchLocation(query, listener, true);
    }

}
