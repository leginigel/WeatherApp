package j.com.weatherapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.VolleyError;

import j.com.weatherapp.Data.CityWeather;
import j.com.weatherapp.Data.VolleyWeather;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private String Tag = SearchFragment.class.getSimpleName();
    private SearchView searchView;
    private PageFragment.VolleyResponseListener listener;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.searchView);
        CityWeather cityWeather = new CityWeather();
        VolleyWeather volleyWeather = new VolleyWeather(getActivity(), cityWeather);;

        listener = new PageFragment.VolleyResponseListener() {
            @Override
            public void onError(VolleyError message) {

            }

            @Override
            public void onResponse(Object response) {
//                SharedPreferences mSetting = getActivity().getSharedPreferences("Setting", 0);
//                SharedPreferences.Editor editor = mSetting.edit();
//                editor.putString(cityWeather.getCityName(), cityWeather.getCityKey());
//                editor.apply();
                Log.v(Tag, response.toString());
                setCityList();
            }
        };

        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                volleyWeather.fetchLocation(query, listener, true);
                searchView.clearFocus();
//                searchView.setIconified(true);
//                searchView.setQuery(query, false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Tag, "Text Change");
                return false;
            }
        });

        return view;
    }

    private void setCityList(){

    }
}
