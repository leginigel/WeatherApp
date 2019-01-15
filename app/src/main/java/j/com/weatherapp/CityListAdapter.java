package j.com.weatherapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import static j.com.weatherapp.Utils.Scale;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    private List<String> mCityList;
    private Context mContext;
    private int mCityPage;
    private SharedViewModel vm;

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
        vm = ViewModelProviders.of((FragmentActivity) mContext).get(SharedViewModel.class);
        viewHolder.CityCard.setOnClickListener(v -> {
//            MainActivity.viewPager.setCurrentItem(i);
//            MainActivity.viewPager.setVisibility(View.VISIBLE);
            vm.select(i);
            MainActivity.bottomNavigationView.setSelectedItemId(R.id.navigation_weather);
            Fragment fragment = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag("t");
            if(fragment != null)
                ((AppCompatActivity) mContext).getSupportFragmentManager()
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
        });
        viewHolder.CityTemp.setText(String.valueOf(
                Scale(mContext, Integer.parseInt(Temperature.getString(mCityList.get(i), "0"))) + Scale(mContext)));
        viewHolder.CityName.setText(mCityList.get(i));
        if (mCityPage == i) viewHolder.CityCard.setBackgroundColor(Color.LTGRAY);
        else viewHolder.CityCard.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView CityCard;
        TextView CityName;
        TextView CityTemp;
        RelativeLayout BackGround;
        View BottomShadow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CityCard = itemView.findViewById(R.id.city_card);
            CityName = itemView.findViewById(R.id.city_name);
            CityTemp = itemView.findViewById(R.id.city_temperature);
            BackGround = itemView.findViewById(R.id.view_background);
            BottomShadow = itemView.findViewById(R.id.city_card_bottom_shadow);
        }
    }

    public void setCityPage(int page){
        mCityPage = page;
    }

    public int getCityPage() {
        return mCityPage;
    }

    public void moveItem(int fromPos, int toPos){
        mCityList.add(toPos, mCityList.remove(fromPos));
        notifyItemMoved(fromPos, toPos);
        vm.setCityList(mCityList);
    }

    public void removeItem(int pos){
        mCityList.remove(pos);
        notifyItemRemoved(pos);
        vm.setCityList(mCityList);
//        vm.removeCity(pos);
    }

    public void restoreCity(String city, int pos){
        mCityList.add(pos, city);
        notifyItemInserted(pos);
        vm.setCityList(mCityList);
    }
}
