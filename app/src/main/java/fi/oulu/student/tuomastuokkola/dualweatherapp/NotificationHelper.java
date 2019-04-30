package fi.oulu.student.tuomastuokkola.dualweatherapp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import java.util.Date;
import java.sql.Timestamp;



// Based on following Git: (because I have Oreo as test device)
// https://gist.github.com/jazzbpn/716ca836db20ea05b03c12c51c1a7601#file-oreonotification-java
public class NotificationHelper {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private String mAsk = null;
    private String mHigh = null;
    private String mLow = null;
    private String mVolume = null;
    private String mTemp = null;
    private String mCity = null;
    private String mMain = null;
    private String mDescription = null;
    Timestamp LastData;
    long time = 0;
    public NotificationHelper(Context context) {
        mContext = context;
    }
    public void getWeatherData(String temp, String city, String main, String description) {
        mTemp = temp;
        mCity = city;
        mMain = main;
        mDescription = description;
        checkData();
    }
    public void getCryptoData(String ask, String high, String low, String volume) {
        mAsk = ask;
        mHigh = high;
        mLow = low;
        mVolume = volume;
        checkData();
    }
    private void checkData() {

        if (mTemp != null && mCity != null && mMain != null && mDescription != null && mAsk != null && mHigh != null && mLow != null && mVolume != null) {

            String cryptoMessage = new String();
            String overallScoreMessage = new String();
            cryptoMessage = "\n\n" + "Cryptocurrency recap (Currency: BTC).\n\nHigh: " + mHigh + ". Low: " + mLow + ". \nAsk: " + mAsk + ". Volume: " + mVolume + ".";
            overallScoreMessage = "There's no easy way out.";

            createNotification("Weather",mDescription + " in " + mCity + ". Temperature: " + mTemp + " °C." + cryptoMessage + "\n\n" + overallScoreMessage);
            Date date= new Date();
            time = date.getTime();
            LastData = new Timestamp(time);
            Log.d("NotificationHelper", "Current Time Stamp: " + time);
        } else {
            Log.d("NotificationHelper", "All needed data not here.");
        }
    }
    /**
     * Create and push the notification
     */
    public void createNotification(String title, String message)
    {
        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent(mContext , InvestActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(message))
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Weather", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }
}
