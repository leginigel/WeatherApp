package j.com.weatherapp.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import j.com.weatherapp.R;

public class MapPreference extends DialogPreference {

    private int ResId = R.layout.map_setting;
    private double mLat, mLng;
    private Set<String> mMapSet = new HashSet<>();

    public MapPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MapPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public MapPreference(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
        this(context, attrs, R.attr.preferenceStyle);
    }

    public MapPreference(Context context) {
        this(context, null);
    }

    public Set<String> getMapSet() {
        return mMapSet;
    }

    public void setMapSet(Set<String> mMapSet) {
        this.mMapSet = mMapSet;

        // Save to Shared Preferences
        persistStringSet(mMapSet);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
//        return super.onGetDefaultValue(a, index);
        Log.d(this.getClass().getSimpleName(), "onGetDefaultValue");
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
//        super.onSetInitialValue(defaultValue);
        Log.d(this.getClass().getSimpleName(), "onSetInitialValue");
        setMapSet(getPersistedStringSet(mMapSet));
    }

    @Override
    public int getDialogLayoutResource() {
        Log.d(this.getClass().getSimpleName(), "getDialogLayoutResource");
        return ResId;
    }
}
