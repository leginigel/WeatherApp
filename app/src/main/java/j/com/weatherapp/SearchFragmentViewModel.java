package j.com.weatherapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import j.com.weatherapp.Data.CityWeather;
import j.com.weatherapp.Data.VolleyResponseListener;
import j.com.weatherapp.Data.VolleyWeather;

public class SearchFragmentViewModel extends ViewModel{

    private final MutableLiveData<List<City>> cities = new MutableLiveData<>();
    public ObservableBoolean isLoading = new ObservableBoolean(false);

    public LiveData<List<City>> getCities() {
        return cities;
    }

    public void searchCity(Context context, String query){
        isLoading.set(true);
        CityWeather cityWeather = new CityWeather();
        VolleyWeather volleyWeather = new VolleyWeather(context, cityWeather);
        VolleyResponseListener listener = new VolleyResponseListener() {
            @Override
            public void onError(VolleyError message) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show();
                List<City> city = new ArrayList<>();
                city.add(new City("Tokyo", "Asia", "Tokyo", "Japan"));
                cities.setValue(city);
                isLoading.set(false);
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
                isLoading.set(false);
                Log.v("SearchViewModel", response.toString());
            }
        };
        volleyWeather.fetchLocation(query, listener, true);
    }

}
