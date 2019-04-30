package fi.oulu.student.tuomastuokkola.dualweatherapp;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat mDetector;
    private String CRYPTO_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/"; // /indices/{market}/ticker/{symbol}";
    private int mThreshold = 200;
    private String ask = new String();
    private String high = new String();
    private String low = new String();
    private String volume = new String();

    private String city = new String();
    private String temperature = new String();
    private String main = new String();
    private String description = new String();

    final int REQUEST_CODE = 123;
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?lat=65.0334415&lon=25.4424575&appid=18fdb01ff23c3baa2f7851253be77a06";
    // App ID to use OpenWeather data
    final String APP_ID = "18fdb01ff23c3baa2f7851253be77a06";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 10000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1;

    final String CITY_ID = "643493";
    final String COUNTRY_CODE = "FI";

    final String TITLE = "Title";
    final String CONTENT = "Content";

    final String CHANNEL_ID = "Weather";
    NotificationHelper notificationHelper = new NotificationHelper(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EstablishConnectionOnBitAverage(CRYPTO_URL, "BTC", "EUR");
        getWeatherData();


        Button InvestButton = (Button) findViewById(R.id.investButton);
        InvestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mInvestIntent = new Intent(getApplicationContext(), InvestActivity.class);
                mInvestIntent.putExtra("High", high);
                mInvestIntent.putExtra("Low", low);
                if (high == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wait a sec and try again!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    startActivity(mInvestIntent);
                }
            }
        });
        Button ContestButton = (Button) findViewById(R.id.contestButton);
        ContestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mContestIntent = new Intent(getApplicationContext(), ContestActivity.class);
                mContestIntent.putExtra("City", city);
                mContestIntent.putExtra("Temperature", temperature);
                mContestIntent.putExtra("Main", main);
                mContestIntent.putExtra("Description", description);
                if (city == "") {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wait a sec and try again!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    startActivity(mContestIntent);
                }
            }
        });
        Button ProgressButton = (Button) findViewById(R.id.progressButton);
        ProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mProgressIntent = new Intent(getApplicationContext(), ProgressActivity.class);
                mProgressIntent.putExtra("High", high);
                mProgressIntent.putExtra("Low", low);
                mProgressIntent.putExtra("Ask", ask);
                mProgressIntent.putExtra("Volume", volume);
                if (high == "") {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wait a sec and try again!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    startActivity(mProgressIntent);
                }
            }
        });
        Button NotificationButton = (Button) findViewById(R.id.notificationsButton);
        NotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mNotificationIntent = new Intent(getApplicationContext(), NotificationsActivity.class);
                startActivity(mNotificationIntent);
            }
        });

    }

    private void EstablishConnectionOnBitAverage(String url, String cryptocurrency, String currency) {
        log.d("Bitcoin", "URL: " + url);
        AsyncHttpClient client = new AsyncHttpClient();
        url = url + cryptocurrency + currency;
        Log.d("Bitcoin", url);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                // Log.d("Bitcoin", "JSON: " + response.toString());
                try {
                    ask = response.getString("ask");
                    high = response.getString("high");
                    low = response.getString("low");
                    volume = response.getString("volume");
                    log.d("Bitcoin", "ask: " + ask);
                    log.d("Bitcoin", "high: " + high);
                    log.d("Bitcoin", "low: " + low);
                    log.d("Bitcoin", "volume: " + volume);
                    notificationHelper.getCryptoData("" + ask, "" + high, "" + low, "" + volume);

                    //mPriceTextView.setText(exchangeRate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Log.e("Bitcoin", e.toString());
            }
        });


    }

    private void getWeatherData() {

        //RequestParams params = new RequestParams();
        //params.put("id", CITY_ID);
        //params.put("appid", APP_ID);
        EstablishConnectionOnOpenWeatherMap();
    }

    private void EstablishConnectionOnOpenWeatherMap() { //(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, new JsonHttpResponseHandler() {
            // Listens to the response of get request
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {



                Log.d("Weather", "Success! JSON: " + response.toString());
                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
                Log.d("Weather", "Temperature: " + weatherData.getTemperature());
                Log.d("Weather", "Condition: " + weatherData.getMain());
                Log.d("Weather", "City: " + weatherData.getCity());
                city = weatherData.getCity();
                temperature = weatherData.getTemperature();
                main = weatherData.getMain();
                description = weatherData.getDescription();
                notificationHelper.getWeatherData("" + weatherData.getTemperature(), "" + weatherData.getCity(), "" + weatherData.getMain(), "" + weatherData.getDescription() );

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                Log.e("Weather", "Fail " + e.toString());
                Log.e("Weather", "Status code" + statusCode);
                Log.e("Weather", WEATHER_URL);
                Toast.makeText(getApplicationContext(), "Request Failed. Please restart App.", Toast.LENGTH_SHORT).show();
                try {
                    MainActivity.this.wait(10000);
                    EstablishConnectionOnOpenWeatherMap();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

}
