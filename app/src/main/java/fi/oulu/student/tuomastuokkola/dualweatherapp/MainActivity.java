package fi.oulu.student.tuomastuokkola.dualweatherapp;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    private GestureDetectorCompat mDetector;

    private int mThreshold = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
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
            if (verticalDifference > mThreshold && horizontalDifference > mThreshold) {
                Log.d(DEBUG_TAG, "South East -> North West. "); // swipe from southeast to northwest
            } else if (verticalDifference < -mThreshold && horizontalDifference < -mThreshold) {
                Log.d(DEBUG_TAG, "North West -> South East. "); // swipe from northwest to southeast
            } else if (verticalDifference < -mThreshold && horizontalDifference > mThreshold) {
                // from northeast to southwest
                Log.d(DEBUG_TAG, "North East -> South East. ");
            } else if (horizontalDifference < -mThreshold && verticalDifference > mThreshold) {
                Log.d(DEBUG_TAG, "South East -> North East. ");
            } else if (verticalDifference > mThreshold || verticalDifference < -mThreshold) {
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

}
