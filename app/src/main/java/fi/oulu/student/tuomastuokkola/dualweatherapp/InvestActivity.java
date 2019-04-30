package fi.oulu.student.tuomastuokkola.dualweatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.util.Random;

public class InvestActivity extends AppCompatActivity {

    private Button motivationalButton;
    public Float mAsk;
    //public Float mHigh;
    //public Float mLow;
    public Float mVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest);
        motivationalButton = (Button) findViewById(R.id.tapButton);
        Intent intent = getIntent();
        String high = intent.getStringExtra("High");
        String low = intent.getStringExtra("Low");
        Log.d("Invest", "High: " + high);
        Log.d("Invest", "Low: " + low);

        //mHigh = Float.parseFloat(high);
        //mLow = Float.parseFloat(low);

        generateInvestMotivationalText();
    }

    public void generateInvestMotivationalText() {
        Random randomGenerator = new Random();
        Integer random = randomGenerator.nextInt(10 - 5) + 5;
        String motivationalText = new String();

        if (random == 5) {
            motivationalText = "Not enough volume! Contribute today.";
        } else if (random == 6) {
            motivationalText = "Better invest to some Doges.";
        } else if (random == 7) {
            motivationalText = "Someone has probably stolen another BitWallet.";
        } else if (random == 8) {
            motivationalText = "What is your excuse today?";
        } else if (random == 9) {
            motivationalText = "Now might be the time to invest.";
        } else {
            motivationalText = "BTC the new tantalum?";
        }
        motivationalButton.setText(motivationalText);
    }
}
