package fi.oulu.student.tuomastuokkola.dualweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class ContestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);
        Intent intent = getIntent();

        String city = intent.getStringExtra("City");
        String temperature = intent.getStringExtra("Temperature");
        String main = intent.getStringExtra("Main");
        String description = intent.getStringExtra("Description");

        TextView cityBlock = (TextView) findViewById(R.id.citybox);
        TextView temperatureBlock = (TextView) findViewById(R.id.tempbox);
        TextView mainBlock = (TextView) findViewById(R.id.mainbox);
        TextView descriptionBlock = (TextView) findViewById(R.id.descbox);

        cityBlock.setText(city);
        temperatureBlock.setText(temperature);
        mainBlock.setText(main);
        descriptionBlock.setText(description);
    }
}
