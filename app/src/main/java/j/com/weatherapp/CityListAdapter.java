package j.com.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    private List<String> mCityList;
    private Context mContext;

    public CityListAdapter(Context context, List<String> list) {
        this.mCityList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_holder_city, viewGroup, false);
        return new CityListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityListAdapter.ViewHolder viewHolder, int i) {

        viewHolder.CityName.setText(mCityList.get(i));
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CityName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CityName = itemView.findViewById(R.id.city_name);
        }
    }
}
