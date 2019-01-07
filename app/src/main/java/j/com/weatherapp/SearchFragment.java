package j.com.weatherapp;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private String Tag = SearchFragment.class.getSimpleName();
    private SearchView searchView;
    private RecyclerView recyclerView;
    private SearchListAdapter searchListAdapter = new SearchListAdapter(new ArrayList<>(), this);
    private SearchFragmentViewModel viewModel;
    private SharedViewModel vm;

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

        MainActivity.bottomNavigationView.setVisibility(View.GONE);
        MainActivity.bottomNavigationView.setAlpha(0);

        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                MainActivity.bottomNavigationView.setVisibility(View.GONE);
                MainActivity.bottomNavigationView.setAlpha(0);
            }
            else {
                MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
                MainActivity.bottomNavigationView.animate().alpha(1).setDuration(1000).start();
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
        vm = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    public void onSearchToAddClick(City city){
        List<String> list = vm.getCityList().getValue();
        if (!isActive(city)) {
            list.add(city.getCityName());
            vm.addCity(city);
        }
        Log.d(this.getClass().getSimpleName(), "onClick");
        Log.d(this.getClass().getSimpleName(), list.get(0));
        Log.d(this.getClass().getSimpleName(), city.getCityName());

        vm.setCityList(list);
        vm.select(list.size()-1);

        MainActivity.bottomNavigationView.setSelectedItemId(R.id.navigation_weather);
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("t");
        if(fragment != null)
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
    }

    public boolean isActive(City city){
        List<City> list = vm.getCity().getValue();
        for (City cityList : list){
            if (cityList.getCityName().equals(city.getCityName()) &&
                    cityList.getCityArea().equals(city.getCityArea()) &&
                    cityList.getCityCountry().equals(city.getCityCountry()))
                return true;
        }
        return false;
    }

}
