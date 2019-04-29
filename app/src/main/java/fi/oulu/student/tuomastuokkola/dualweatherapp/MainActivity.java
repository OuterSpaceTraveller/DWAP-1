package fi.oulu.student.tuomastuokkola.dualweatherapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
    // String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    // LocationManager locationManager;
    // LocationListener locationListenerGPS;
    // Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        EstablishConnectionOnBitAverage(CRYPTO_URL, "BTC", "EUR");
        getWeatherData();

        /*
        mContext = this;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationListenerGPS = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.d("LatLon", "Latitude: " + latitude);
                Log.d("LatLon", "Longitude: " + longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Provider", "Status: changed");
                Log.d("Provider", "Status: " + status);
                Log.d("Provider", "Extras: " + extras.toString());
                Log.d("Provider", "Provider: " + provider);

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Provider", "Provider: enabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Provider", "Provider: disabled");
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, locationListenerGPS);
        isLocationEnabled();
    }*/
    /*protected void onResume () {
        super.onResume();
        isLocationEnabled();
    }*/
    /*private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
    */
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
                    Intent mNotificationIntent = new Intent(getApplicationContext(), NotificationsActivity.class);
                    startActivity(mNotificationIntent);
                } else {
                    Log.d(DEBUG_TAG, "North -> South. Notifications.");
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
            } else if (horizontalDifference > mThreshold || horizontalDifference < -mThreshold) {
                if (horizontalDifference > mThreshold) {// pos => North
                    Log.d(DEBUG_TAG, "East -> West. Contest.");
                    Intent mProgressIntent = new Intent(getApplicationContext(), ProgressActivity.class);
                    startActivity(mProgressIntent);
                } else {
                    Log.d(DEBUG_TAG, "West -> East. Progress.");
                    Intent mContestIntent = new Intent(getApplicationContext(), ContestActivity.class);
                    startActivity(mContestIntent);
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
