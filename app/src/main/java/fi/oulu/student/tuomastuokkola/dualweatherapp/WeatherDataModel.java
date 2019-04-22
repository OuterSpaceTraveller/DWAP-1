package fi.oulu.student.tuomastuokkola.dualweatherapp;

import org.json.JSONException;
import org.json.JSONObject;

class WeatherDataModel {

    private String mTemperature;
    private Integer mCondition;
    private String mCity;

    public String getTemperature() {
        return mTemperature;
    }
    public Integer getCondition() {
        return mCondition;
    }
    public String getCity() {
        return mCity;
    }
    public static WeatherDataModel fromJson(JSONObject jsonObject) {
        try {
            WeatherDataModel weatherData = new WeatherDataModel();
            weatherData.mCity = jsonObject.getString("name");
            weatherData.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");

            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") -273.15;
            int roundedValue = (int) Math.rint(tempResult);

            weatherData.mTemperature = Integer.toString(roundedValue);

            return weatherData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
