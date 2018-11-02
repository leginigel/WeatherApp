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

import java.util.ArrayList;
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

//        RequestOptions requestOptions =
//                new RequestOptions().centerCrop()
//                        .placeholder(R.drawable.ic_album_black_24dp);
//
//        Glide.with(mContext)
//                .load(albumArt)
//                .apply(requestOptions)
//                .into(holder.albumImageView);

        holder.fivedateTextView.setText(mDayForecast.get(position).getDateTime());
//        holder.fivetextTextView.setText(mDayForecast.get(position));
//        holder.fivetemperatureTextView.setText(mDayForecast.get(position));
    }

    @Override
    public int getItemCount() {
        return mDayForecast.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fivedateTextView, fivetextTextView, fivetemperatureTextView;
        public ViewHolder(View itemView) {
            super(itemView);

            fivedateTextView = itemView.findViewById(R.id.five_date);
            fivetextTextView = itemView.findViewById(R.id.five_text);
            fivetemperatureTextView = itemView.findViewById(R.id.five_temperature);
        }
    }
}


