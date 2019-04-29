package fi.oulu.student.tuomastuokkola.dualweatherapp;

import org.json.JSONException;
import org.json.JSONObject;

class WeatherDataModel {

    private String mTemperature;
    private String mMain;
    private String mCity;
    private String mDescription;


    public String getTemperature() {
        return mTemperature;
    }
    public String getMain() {
        return mMain;
    }
    public String getCity() {
        return mCity;
    }
    public String getDescription() { return mDescription; }

    public static WeatherDataModel fromJson(JSONObject jsonObject) {
        try {
            WeatherDataModel weatherData = new WeatherDataModel();
            weatherData.mCity = jsonObject.getString("name");
            weatherData.mMain = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherData.mDescription = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

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
