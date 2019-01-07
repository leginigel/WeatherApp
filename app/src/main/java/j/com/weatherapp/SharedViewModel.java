package j.com.weatherapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import j.com.weatherapp.City;

public class SharedViewModel extends ViewModel {

    private final  MutableLiveData<String> metric = new MutableLiveData<>();
    private MutableLiveData<Integer> viewpager_selected;
    private MutableLiveData<List<String>> cityList;
    private MutableLiveData<List<j.com.weatherapp.City>> City;

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
        if (cityList == null) {
            cityList = new MutableLiveData<>();
            List<String> list = new ArrayList<>();
            list.add("Taipei");
            list.add("London");
            cityList.setValue(list);
            Log.d("ViewModel", "citylist = null");
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
