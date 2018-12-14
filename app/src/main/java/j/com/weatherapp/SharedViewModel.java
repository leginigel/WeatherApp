package j.com.weatherapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {

    private final  MutableLiveData<String> metric = new MutableLiveData<>();
    private final MutableLiveData<Integer> selected = new MutableLiveData<>();
    private MutableLiveData<List<String>> cityList;

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
        selected.setValue(item);
    }

    public LiveData<Integer> getSelected() {
        return selected;
    }
}
