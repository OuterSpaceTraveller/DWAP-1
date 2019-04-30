package fi.oulu.student.tuomastuokkola.dualweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class ProgressActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_progress);
        Intent intent = getIntent();

        String high = intent.getStringExtra("High");
        String low = intent.getStringExtra("Low");
        String ask = intent.getStringExtra("Ask");
        String volume = intent.getStringExtra("Volume");

        Log.d("Progress", "high: " + high);
        Log.d("Progress", "low: " + low);
        Log.d("Progress", "ask: " + ask);
        Log.d("Progress", "vol: " + volume);

        TextView highBlock = (TextView) findViewById(R.id.highbox);
        TextView lowBlock = (TextView) findViewById(R.id.lowbox);
        TextView askBlock = (TextView) findViewById(R.id.askbox);
        TextView volumeBlock = (TextView) findViewById(R.id.volumebox);

        highBlock.setText(high);
        lowBlock.setText(low);
        askBlock.setText(ask);
        volumeBlock.setText(volume);
    }
}
