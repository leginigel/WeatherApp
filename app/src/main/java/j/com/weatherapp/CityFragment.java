package j.com.weatherapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {

    public CityFragment() {
        // Required empty public constructor
//        MainActivity.bottomNavigationView.setVisibility(View.GONE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        view.setBackgroundColor(Color.WHITE);

        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_city);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView text_toolbar = toolbar.findViewById(R.id.toolbar_title);
        text_toolbar.setText("City List");

        RecyclerView rv = view.findViewById(R.id.city_list);
        LinearLayoutManager lm =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        CityListAdapter cityListAdapter = new CityListAdapter(getActivity(), MainActivity.cityList);
        rv.setLayoutManager(lm);
        rv.setAdapter(cityListAdapter);
        // Inflate the layout for this fragment
        return view;
    }

}
