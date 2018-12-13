package j.com.weatherapp;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import j.com.weatherapp.Data.CityWeather;
import j.com.weatherapp.Data.VolleyWeather;
import j.com.weatherapp.databinding.FragmentSearchBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private String Tag = SearchFragment.class.getSimpleName();
    private SearchView searchView;
    private RecyclerView recyclerView;
    private SearchListAdapter searchListAdapter = new SearchListAdapter(new ArrayList<>());


    private SearchFragmentViewModel viewModel;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.search_rv);
//        CityWeather cityWeather = new CityWeather();
//        VolleyWeather volleyWeather = new VolleyWeather(getActivity(), cityWeather);
//
//        listener = new PageFragment.VolleyResponseListener() {
//            @Override
//            public void onError(VolleyError message) {
//
//            }
//
//            @Override
//            public void onResponse(Object response) {
////                SharedPreferences mSetting = getActivity().getSharedPreferences("Setting", 0);
////                SharedPreferences.Editor editor = mSetting.edit();
////                editor.putString(cityWeather.getCityName(), cityWeather.getCityKey());
////                editor.apply();
//                Log.v(Tag, response.toString());
//            }
//        };

        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                volleyWeather.fetchLocation(query, listener, true);

                searchListAdapter.clearItems();
                viewModel.searchCity(getActivity(), query);

                searchView.clearFocus();
//                searchView.setIconified(true);
//                searchView.setQuery(query, false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(searchListAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(SearchFragmentViewModel.class);
        viewModel.getCities().observe(getActivity(), (city) ->{
            searchListAdapter.swapItems(city);
        });
    }
}
