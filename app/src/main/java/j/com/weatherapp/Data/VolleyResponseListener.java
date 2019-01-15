package j.com.weatherapp.Data;

import com.android.volley.VolleyError;

public interface VolleyResponseListener {
    void onError(VolleyError message);

    void onResponse(Object response);
}
