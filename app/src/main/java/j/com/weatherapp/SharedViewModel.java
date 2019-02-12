package j.com.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import j.com.weatherapp.City;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> metric = new MutableLiveData<>();
    private MutableLiveData<Integer> viewpager_selected;
    private MutableLiveData<List<String>> cityList;
    private MutableLiveData<List<j.com.weatherapp.City>> City;

    public void getLocation(Context context) {
        Log.d("ViewModel", "getLocation");
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("ViewModel", "Request Permission");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)){

            }
            else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        666);
            }
            return;
        }
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        client.getLastLocation()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Log.d("ViewModel", "Latitude"+task.getResult().getLatitude());
                        Log.d("ViewModel", "Longitude"+task.getResult().getLongitude());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(task.getResult().getLatitude(), task.getResult().getLongitude(), 1);
//                            Log.d("CityName", addresses.get(0).getAddressLine(0));
//                            Log.d("StateName", addresses.get(0).getAddressLine(1));
//                            Log.d("CountryName", addresses.get(0).getAddressLine(2));
//                            Log.d("AdminArea", addresses.get(0).getAdminArea());
//                            Log.d("CountryName", addresses.get(0).getCountryName());
//                            Log.d("FeatureName", addresses.get(0).getFeatureName());
//                            Log.d("SubAdminArea", addresses.get(0).getSubAdminArea());
//                            Log.d("Locality", addresses.get(0).getLocality());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        client.getLastLocation()
                .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d("ViewModel", "Latitude"+location.getLatitude());
                        Log.d("ViewModel", "Longitude"+location.getLongitude());
                        try {
                            List<Address> addresses = geocoder.getFromLocationName("London", 10);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



    public void addCity(City city){
        List<City> temp = this.City.getValue();
        temp.add(city);
        this.City.setValue(temp);
    }

    public void removeCity(int position){
        List<City> temp = getCity().getValue();
        temp.remove(position);
        this.City.setValue(temp);
    }

    public LiveData<List<City>> getCity(){
        if (City == null) {
            City = new MutableLiveData<>();
            List<City> list = new ArrayList<>();
            list.add(new City("Taipei", "Asia", "Taiwan", "R.O.C"));
            list.add(new City("London", "Europe", "Greater London", "United Kingdom"));
            City.setValue(list);
            Log.d("ViewModel", "City = null");
        }

        Log.d("ViewModel", "City");
        return City;
    }

    public void setCity(List<City> city){
        this.City.setValue(city);
    }

    public LiveData<List<String >> getCityList(){
        Set<String> set = MainActivity.sharedPreferences.getStringSet("city", null);
//        if (set!=null) {
//            for (String i:set)
//                Log.d("ViewModel", ""+i);
//        }
        if (cityList == null) {
            Log.d("ViewModel", "citylist = null");
            cityList = new MutableLiveData<>();
            List<String> list = new ArrayList<>();

            int citySize = MainActivity.sharedPreferences.getInt("citysize", 0);
            for (int i=0; i < citySize ;i++) {
                String sharedPreferencesCity = MainActivity.sharedPreferences.getString("city" + i, null);
                if (sharedPreferencesCity == null)break;
                Log.d("ViewModel", sharedPreferencesCity);
                list.add(sharedPreferencesCity);
            }
            Log.d("ViewModel", ""+list.size());
            if (list.size() == 0) {
                list.add("Taipei");
                list.add("London");
            }
            cityList.setValue(list);
        }

        Log.d("ViewModel", "citylist");
        return cityList;
    }

    public void setCityList(List<String> list){
        cityList.setValue(list);
    }

    public void select(int item) {
        viewpager_selected.setValue(item);
    }

    public LiveData<Integer> getSelected() {
        if (viewpager_selected == null) {
            viewpager_selected = new MutableLiveData<>();
            viewpager_selected.setValue(0);
            Log.d("ViewModel", "viewpager_selected = null");
        }
        Log.d("ViewModel", "viewpager_selected");
        return viewpager_selected;
    }
}
