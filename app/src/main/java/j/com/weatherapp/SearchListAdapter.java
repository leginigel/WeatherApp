package j.com.weatherapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import j.com.weatherapp.databinding.ViewHolderSearchBinding;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private List<City> items;
    private SharedViewModel vm;
    private Context mContext;
    public SearchListAdapter(List<City> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ViewHolderSearchBinding binding =
                ViewHolderSearchBinding.inflate(layoutInflater, viewGroup, false);
        mContext = viewGroup.getContext();
        return new SearchListAdapter.ViewHolder(binding, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder viewHolder, int i) {
        vm = ViewModelProviders.of((FragmentActivity) viewHolder.getContext()).get(SharedViewModel.class);
        viewHolder.setListAdapter(this);
        viewHolder.setBinding(items.get(i));
//        viewHolder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("pp","onclick");
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewHolderSearchBinding binding;
        private final Context context;
        private SearchListAdapter searchListAdapter;

        public ViewHolder(ViewHolderSearchBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }

        void setBinding(City city){
            binding.setCity(city);
            binding.executePendingBindings();
        }

        void setListAdapter(SearchListAdapter searchListAdapter){
            this.searchListAdapter = searchListAdapter;
            binding.setListener(searchListAdapter);
        }

        public Context getContext() {
            return context;
        }
    }

    public void onSearchToAddClick(City city){
        List<String> list = vm.getCityList().getValue();
        list.add(city.getCityName());
        Log.d(this.getClass().getSimpleName(), "onClick");
        Log.d(this.getClass().getSimpleName(), list.get(0));
        Log.d(this.getClass().getSimpleName(), city.getCityName());
        vm.setCityList(list);

        MainActivity.viewPager.setCurrentItem(list.size()-1);
        MainActivity.bottomNavigationView.setSelectedItemId(R.id.navigation_weather);
        Fragment fragment = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag("t");
        if(fragment != null)
            ((AppCompatActivity) mContext).getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
    }

    void clearItems() {
        int size = this.items.size();
        this.items.clear();
        notifyItemRangeRemoved(0, size);
    }

    void swapItems(List<City> newItems) {

        this.items.addAll(newItems);
        notifyDataSetChanged();
    }
}
