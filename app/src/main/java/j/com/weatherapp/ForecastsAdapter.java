package j.com.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import j.com.weatherapp.Data.DayWeather;

public class ForecastsAdapter extends RecyclerView.Adapter<ForecastsAdapter.ViewHolder>{

    private Context mContext;
    private List<DayWeather> mDayForecast;

    public ForecastsAdapter(Context context, List<DayWeather> list) {
        this.mDayForecast = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ForecastsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View forecastView = inflater.inflate(R.layout.view_holder_item, parent, false);

        return new ForecastsAdapter.ViewHolder(forecastView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastsAdapter.ViewHolder holder, int position) {

        RequestOptions requestOptions =
                new RequestOptions().centerCrop()
                        .placeholder(R.drawable.ic_wb_sunny_black_24dp);

        String noIcon = String.valueOf(mDayForecast.get(position).getIcon());
        if (mDayForecast.get(position).getIcon() < 10)
            noIcon = "0" + noIcon;
        String img_url = "https://developer.accuweather.com/sites/default/files/" + noIcon + "-s.png";
        Glide.with(mContext)
                .load(img_url)
                .apply(requestOptions)
                .into(holder.Icon);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        DateFormat toFormat = new SimpleDateFormat("MM-dd");
        String sDate = null;
        try {
            sDate = toFormat.format(df.parse(mDayForecast.get(position).getDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.FiveDate.setText(sDate);
//        holder.FiveDate.setText(mDayForecast.get(position).getDateTime());
//        holder.fivetextTextView.setText(mDayForecast.get(position).get);
        holder.MaxTemperature.setText(String.valueOf((int) mDayForecast.get(position).getMaxTemperature() + "°C"));
        holder.MinTemperature.setText(String.valueOf((int)mDayForecast.get(position).getMinTemperature() + "°C"));
        holder.Rain.setText(String.valueOf(mDayForecast.get(position).getRainProbability() + "%"));
    }

    @Override
    public int getItemCount() {
        return mDayForecast.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Icon;
        TextView FiveDate, FiveText, MaxTemperature, MinTemperature, Rain;
        public ViewHolder(View itemView) {
            super(itemView);
            Icon = itemView.findViewById(R.id.weather_image);
            FiveDate = itemView.findViewById(R.id.date);
            FiveText = itemView.findViewById(R.id.five_text);
            MaxTemperature = itemView.findViewById(R.id.max_temperature);
            MinTemperature = itemView.findViewById(R.id.min_temperature);
            Rain = itemView.findViewById(R.id.rain);
        }
    }
}


