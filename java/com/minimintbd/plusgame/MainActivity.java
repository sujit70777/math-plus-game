package com.minimintbd.plusgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    TextView bs1, bs2, bs3;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7802066341092922~7900891810");

        sharedPreferences = getSharedPreferences("bs", MODE_PRIVATE);
        bs1 = findViewById(R.id.bs1);
        bs2 = findViewById(R.id.bs2);
        bs3 = findViewById(R.id.bs3);
        bs1.setText("Best Socre: " + sharedPreferences.getString("bs1", "0"));
        bs2.setText("Best Socre: " + sharedPreferences.getString("bs2", "0"));
        bs3.setText("Best Socre: " + sharedPreferences.getString("bs3", "0"));

    }

    public void onExpertClick(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("type", "3");
        startActivity(intent);
        finish();
    }

    public void onIntermediateClick(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("type", "2");
        startActivity(intent);
        finish();
    }

    public void onBeginnerClick(View view) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("type", "1");
        startActivity(intent);
        finish();
    }

}
