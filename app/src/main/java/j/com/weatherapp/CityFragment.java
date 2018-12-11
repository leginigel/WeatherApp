package j.com.weatherapp;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment
        implements CityRecyclerItemTouch.RecyclerItemTouchHelperListener{

    private RecyclerView rv;
    private CityListAdapter cityListAdapter;
    private List<String> cityList;
    private FrameLayout frameLayout;
    private SharedViewModel model;

    public CityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cityList = getArguments().getStringArrayList("CityList");
        cityListAdapter = new CityListAdapter(getActivity(), cityList);

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model.getCityList().observe(getActivity(), city ->{
            cityList = city;
            cityListAdapter.notifyDataSetChanged();
        });
        model.getSelected().observe(getActivity(), (pos)-> {
            cityListAdapter.setCityPage(pos); });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);

        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_city);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        frameLayout = view.findViewById(R.id.framelayout_city);

        rv = view.findViewById(R.id.city_list);
        LinearLayoutManager lm =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rv.setAdapter(cityListAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new CityRecyclerItemTouch(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv);

        return view;
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CityListAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = cityList.get(viewHolder.getAdapterPosition());

            // backup of removed item for undo purpose
            final String deletedItem = cityList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            cityListAdapter.removeItem(viewHolder.getAdapterPosition());
//            MainActivity.viewPager.setAdapter(MainActivity.WFPA);
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(frameLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    cityListAdapter.restoreCity(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("city", "stop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("city", "destroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v("city", "detach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("city", "start");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v("city", "destoryview");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v("city", "attach");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v("city", "pause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("city", "resume");
    }
}
