package j.com.weatherapp.Data;

import org.json.JSONException;
import org.json.JSONObject;

public class DayWeather {
    JSONObject JsonWeather;

    String DateTime;
    double MaxTemperature;
    double MinTemperature;
    double MaxRealFeelTemperature;
    double MinRealFeelTemperature;
    Integer RainProbability;

    public JSONObject getJsonWeather() {
        return JsonWeather;
    }

    public void setJsonWeather(JSONObject jsonWeather) {
        this.JsonWeather = jsonWeather;
        try {
            setDateTime(jsonWeather.getString("Date"));
            setMaxRealFeelTemperature(
                    jsonWeather.getJSONObject("RealFeelTemperature").getJSONObject("Maximum").getDouble("Value"));
            setMaxTemperature(
                    jsonWeather.getJSONObject("Temperature").getJSONObject("Maximum").getDouble("Value"));
            setMinRealFeelTemperature(
                    jsonWeather.getJSONObject("RealFeelTemperature").getJSONObject("Minimum").getDouble("Value"));
            setMinTemperature(
                    jsonWeather.getJSONObject("Temperature").getJSONObject("Minimum").getDouble("Value"));
            setRainProbability(jsonWeather.getJSONObject("Day").getInt("RainProbability"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public double getMaxTemperature() {
        return MaxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        MaxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return MinTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        MinTemperature = minTemperature;
    }

    public double getMaxRealFeelTemperature() {
        return MaxRealFeelTemperature;
    }

    public void setMaxRealFeelTemperature(double maxRealFeelTemperature) {
        MaxRealFeelTemperature = maxRealFeelTemperature;
    }

    public double getMinRealFeelTemperature() {
        return MinRealFeelTemperature;
    }

    public void setMinRealFeelTemperature(double minRealFeelTemperature) {
        MinRealFeelTemperature = minRealFeelTemperature;
    }

    public Integer getRainProbability() {
        return RainProbability;
    }

    public void setRainProbability(Integer rainProbability) {
        RainProbability = rainProbability;
    }

    @Override
    public String toString() {
//        return getDateTime() + String.valueOf(getMaxRealFeelTemperature())
//                + String.valueOf(getMaxTemperature())
//                + String.valueOf(getMinRealFeelTemperature())
//                + String.valueOf(getMinTemperature())
//                + String.valueOf(getRainProbability());
        return getJsonWeather().toString();
    }
}
