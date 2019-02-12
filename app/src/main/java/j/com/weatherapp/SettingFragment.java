package j.com.weatherapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takisoft.fix.support.v7.preference.EditTextPreference;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import j.com.weatherapp.service.TimeReceiver;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link PreferenceFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class SettingFragment extends PreferenceFragmentCompat {

    private OnFragmentInteractionListener mListener;
    private SharedViewModel model;
    private Preference.OnPreferenceChangeListener onListChange = (Preference preference, Object o) -> {
        Log.d("onListChange", preference.getKey() + (String) o);
        ListPreference lp = (ListPreference) preference;
        CharSequence[] entries = lp.getEntries();
        lp.setSummary(entries[lp.findIndexOfValue((String) o)]);
        if ("auto_update_value".equals(preference.getKey())) {
            Intent i = new Intent(getActivity(), TimeReceiver.class);
            i.putExtra("f", "b");
            PendingIntent p = PendingIntent.getBroadcast(getActivity(), TimeReceiver.REQUEST_CODE,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager a = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            a.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES/60 ,p);
        }
        else if ("metric".equals(preference.getKey()))
            model.setCityList(model.getCityList().getValue());
        return true;
    };
    private Preference.OnPreferenceClickListener onCheckChange = (Preference preference) ->{
        CheckBoxPreference pfCheckBox = (CheckBoxPreference) preference;
        Log.d("onCheckChange", preference.getKey() + pfCheckBox.isChecked() );

        return true;
    };
    private EditTextPreference.OnPreferenceChangeListener onEditChange = (Preference preference, Object o) ->{
        Log.d("onEditChange", preference.getKey() + (String) o);

        return true;
    };

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        //if you are using default SharedPreferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ListPreference lpMetric = (ListPreference)findPreference("metric");
        CharSequence[] entries = lpMetric.getEntries();
        lpMetric.setSummary(entries[lpMetric.findIndexOfValue(sharedPrefs.getString("metric", ""))]);

        ListPreference lpDuration = (ListPreference)findPreference("auto_update_value");
        CharSequence[] entriesDuration = lpDuration.getEntries();
        lpDuration.setSummary(entriesDuration[lpDuration.findIndexOfValue(sharedPrefs.getString("auto_update_value", ""))]);
//        onPreferenceChange(sharedPrefs, "metric");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onPreferenceTreeClick( Preference preference) {

        Log.d(this.getClass().getSimpleName(), this.getClass().getSimpleName());
//        Log.d(this.getClass().getTypeName(), this.getClass().getCanonicalName());
        if("auto_update_value".equals(preference.getKey())||"metric".equals(preference.getKey()))
        {
            ListPreference lpf = (ListPreference)findPreference(preference.getKey());
            lpf.setOnPreferenceChangeListener(onListChange);
        }
        else if ("update_open".equals(preference.getKey())){
            Preference cbpf = findPreference(preference.getKey());
            cbpf.setOnPreferenceClickListener(onCheckChange);
        }
        else if ("location".equals(preference.getKey())){
            EditTextPreference etpf = (EditTextPreference) findPreference(preference.getKey());
            etpf.setTitle("Location(TODO)");
            etpf.setOnPreferenceChangeListener(onEditChange);
            etpf.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
//                if (hasFocus) {
//                    MainActivity.bottomNavigationView.setVisibility(View.GONE);
//                    MainActivity.bottomNavigationView.setAlpha(0);
//                }
//                    else {
//                        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
//                        MainActivity.bottomNavigationView.animate().alpha(1).setDuration(1000).start();
//                    }
            });
//            Log.d("Tree", "Test " + visibility);

        }
        return super.onPreferenceTreeClick( preference);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
