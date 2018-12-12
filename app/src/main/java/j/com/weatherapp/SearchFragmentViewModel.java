package j.com.weatherapp;

import android.arch.lifecycle.ViewModel;

public class SearchFragmentViewModel extends ViewModel {
    private String cityName;
    private String cityRegion;
    private String cityArea;
    private String cityCountry;

    public SearchFragmentViewModel(String cityName, String cityRegion, String cityArea, String cityCountry) {
        this.cityName = cityName;
        this.cityRegion = cityRegion;
        this.cityArea = cityArea;
        this.cityCountry = cityCountry;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityRegion() {
        return cityRegion;
    }

    public String getCityArea() {
        return cityArea;
    }

    public String getCityCountry() {
        return cityCountry;
    }
}
