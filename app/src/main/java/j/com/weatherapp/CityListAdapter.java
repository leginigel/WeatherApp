package j.com.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
        SharedPreferences Temperature = mContext.getSharedPreferences("Temperature", Context.MODE_PRIVATE);

        viewHolder.CityCard.setOnClickListener(v -> {
            MainActivity.viewPager.setCurrentItem(i);
//            MainActivity.viewPager.setVisibility(View.VISIBLE);
            MainActivity.bottomNavigationView.setSelectedItemId(R.id.navigation_weather);
            Fragment fragment = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag("t");
            if(fragment != null)
                ((AppCompatActivity) mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
        });
        viewHolder.CityTemp.setText(Temperature.getString(mCityList.get(i), null));
        viewHolder.CityName.setText(mCityList.get(i));
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView CityCard;
        TextView CityName;
        TextView CityTemp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CityCard = itemView.findViewById(R.id.city_card);
            CityName = itemView.findViewById(R.id.city_name);
            CityTemp = itemView.findViewById(R.id.city_temperature);
        }
    }
}
