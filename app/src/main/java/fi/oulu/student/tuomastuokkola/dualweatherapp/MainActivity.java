package fi.oulu.student.tuomastuokkola.dualweatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import static com.loopj.android.http.AsyncHttpClient.log;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat mDetector;
    private String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/"; // /indices/{market}/ticker/{symbol}";
    private int mThreshold = 200;
    private String ask = new String();
    private String high = new String();
    private String low = new String();
    private String volume = new String();

    final int REQUEST_CODE = 123;
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 10000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1;

    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    LocationManager mLocationManager;
    LocationListener mLocationListener;


    private JSONObject mRetrievedData;
    TextView mPriceTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        EstablishConnectionOnBitAverage(BASE_URL, "BTC", "EUR");
        getWeatherData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            float horizontalDifference = event1.getX() - event2.getX(); // if difference pos => left , neg => right
            float verticalDifference = event1.getY() - event2.getY(); // if difference pos => down, neg => up
            if (verticalDifference > mThreshold || verticalDifference < -mThreshold) {
                // if diff is bigger than threshold OR if difference is smaller than negative threshold
                if (verticalDifference > mThreshold) {// pos => North
                    Log.d(DEBUG_TAG, "South -> North. Invest.");
                    Intent mInvestIntent = new Intent(getApplicationContext(), InvestActivity.class);

                    startActivity(mInvestIntent);
                } else {
                    Log.d(DEBUG_TAG, "North -> South. Notifications.");
                    Intent mNotificationIntent = new Intent(getApplicationContext(), NotificationsActivity.class);
                    startActivity(mNotificationIntent);
                }
            } else if (horizontalDifference > mThreshold || horizontalDifference < -mThreshold) {
                if (horizontalDifference > mThreshold) {// pos => North
                    Log.d(DEBUG_TAG, "East -> West. Contest.");
                    Intent mContestIntent = new Intent(getApplicationContext(), ContestActivity.class);
                    startActivity(mContestIntent);
                } else {
                    Log.d(DEBUG_TAG, "West -> East. Progress.");
                    Intent mProgressIntent = new Intent(getApplicationContext(), ProgressActivity.class);
                    startActivity(mProgressIntent);
                }
            } else {
                Log.d(DEBUG_TAG, "Something very sinister just happened...");
            }
            //Log.d(DEBUG_TAG, "Velocity X: " + velocityX);
            //Log.d(DEBUG_TAG, "Velocity Y: " + velocityY);
            //Log.d(DEBUG_TAG, "Difference X: " + horizontalDifference);
            //Log.d(DEBUG_TAG, "Difference Y: " + verticalDifference);
            return true;
        }
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
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Weather", "onLocationChanged() callback received.");

                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

                Log.d("Weather", "Longitude is " + longitude);
                Log.d("Weather", "Longitude is " + latitude);

                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);
                EstablishConnectionOnClima(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Weather", "onStatusChanged() callback received.");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Weather", "onProviderEnabled() callback received.");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Weather", "onProviderDisabled() callback received.");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);

    }

    private void EstablishConnectionOnClima(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
            // Listens to the response of get request
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Weather", "Success! JSON: " + response.toString());
                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
                Log.d("Weather", "Temperature: " + weatherData.getTemperature());
                Log.d("Weather", "Condition: " + weatherData.getCondition());
                Log.d("Weather", "City: " + weatherData.getCity());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                Log.e("Weather", "Fail " + e.toString());
                Log.d("Weather", "Status code" + statusCode);
                Toast.makeText(getApplicationContext(), "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
