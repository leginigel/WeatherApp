package j.com.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.ListPreference;

/**
 * A simple {@link PreferenceFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SettingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener{

    private OnFragmentInteractionListener mListener;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    @Override
    public void onCreatePreferencesFix(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference listPreference = findPreference("auto_update_value");
//        listPreference.setVisible(false);
        //if you are using default SharedPreferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        onPreferenceChange(sharedPrefs, "auto_update_value");
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

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        ListPreference lp = (ListPreference)findPreference(preference.getKey());
        CharSequence[] entries = lp.getEntries();
        lp.setSummary(entries[lp.findIndexOfValue((String) o)]);
        return true;
    }

    @Override
    public boolean onPreferenceTreeClick( Preference preference) {

        if("auto_update_value".equals(preference.getKey())||"metric".equals(preference.getKey()))
        {
            ListPreference lpp = (ListPreference)findPreference(preference.getKey());
            lpp.setOnPreferenceChangeListener(this);
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
