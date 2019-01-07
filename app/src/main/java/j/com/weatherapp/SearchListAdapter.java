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
    private SearchFragment searchFragment;
    public SearchListAdapter(List<City> items, SearchFragment searchFragment) {
        this.items = items;
        this.searchFragment = searchFragment;
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
//        vm = ViewModelProviders.of((FragmentActivity) viewHolder.getContext()).get(SharedViewModel.class);
        viewHolder.setSearchFragment(searchFragment);
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
        private SearchFragment searchFragment;

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
        }

        void setSearchFragment(SearchFragment searchFragment){
            this.searchFragment = searchFragment;
            binding.setListener(searchFragment);
        }

        public Context getContext() {
            return context;
        }
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
