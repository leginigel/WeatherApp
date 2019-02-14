package j.com.weatherapp.preference;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.Set;

import j.com.weatherapp.R;

public class MapPreferenceFragmentCompat extends PreferenceDialogFragmentCompat implements OnMapReadyCallback {

    public static MapPreferenceFragmentCompat newInstance(String key){
        final MapPreferenceFragmentCompat fragment = new MapPreferenceFragmentCompat();
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        fragment.setArguments(bundle);
        return fragment;
    }

    private MapView mMapView;
    private Bundle bundle;
    private double mLat, mLng;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        Log.d(this.getClass().getSimpleName(), "onBindDialogView");
        mMapView = (MapView) view.findViewById(R.id.mapView);

        if(mMapView == null){
            throw new IllegalStateException("MapView cannot be found");
        }

        DialogPreference preference = getPreference();
        if(preference instanceof MapPreference){
            Set<String> set = ((MapPreference) preference).getMapSet();
            if (set!=null) {
                for (String i:set) {
                    Log.d("onBindDialogView", "Map" + i);
                    if(i.startsWith("x")){
                        mLat = Double.parseDouble(i.substring(1));
                        Log.d("onBindDialogView", "mLat" + mLat);
                    }
                    else if(i.startsWith("y")){
                        mLng = Double.parseDouble(i.substring(1));
                        Log.d("onBindDialogView", "mLng" + mLng);
                    }
                }
            }
        }


        mMapView.onCreate(bundle);
        mMapView.onResume();
        mMapView.getMapAsync(this);

    }

    @Override
    public void onDialogClosed(boolean b) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MapView","onMapReady");
        googleMap.setMinZoomPreference(12);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}
