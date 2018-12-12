package j.com.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import j.com.weatherapp.Data.CityWeather;
import j.com.weatherapp.databinding.ViewHoderSearchBindingImpl;
import j.com.weatherapp.databinding.ViewHolderSearchBinding;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private Context mContext;
    private List<CityWeather> items;

    public SearchListAdapter(List<CityWeather> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ViewHolderSearchBinding binding = ViewHolderSearchBinding.inflate(layoutInflater, viewGroup, false);
        return new SearchListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewHolderSearchBinding binding;

        public ViewHolder(ViewHolderSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
