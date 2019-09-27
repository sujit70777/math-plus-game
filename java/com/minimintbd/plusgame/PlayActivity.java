package com.minimintbd.plusgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    TextView timer, eqn, ans;
    Button correct, incorrect;
    int n1, n2, n3, n4;
    int sum;
    int rnSum;
    String type;
    CountDownTimer timerClock;
    int time;
    Random random;
    int points = 0;
    SharedPreferences sharedPreferences;
    Boolean click, life;
    private RewardedVideoAd mRewardedVideoAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        random = new Random();
        life = false;

        sharedPreferences = getSharedPreferences("bs", MODE_PRIVATE);
        timer = findViewById(R.id.timer);
        eqn = findViewById(R.id.eqn);
        ans = findViewById(R.id.ans);
        correct = findViewById(R.id.correct);
        incorrect = findViewById(R.id.incorrect);
        type = getIntent().getStringExtra("type");
        //  Toast.makeText(getApplicationContext(), type, Toast.LENGTH_LONG).show();
        if (type.equals("1")) {
            time = 3;
            getSupportActionBar().setTitle("Beginner");
        }

        if (type.equals("2")) {
            time = 4;
            getSupportActionBar().setTitle("Intermediate");
        }

        if (type.equals("3")) {
            time = 5;
            getSupportActionBar().setTitle("Expert");
        }
        time++;

        timerClock = new CountDownTimer(time * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                //  bar.setProgress((int) millisUntilFinished / 1000);
                timer.setText((int) millisUntilFinished / 1000 + "");
            }


            @Override
            public void onFinish() {
                if (!click) {
                    GameOver();
                } else {
                    timerClock.start();
                    onPlayStart();
                }
            }
        };


        timerClock.start();
        onPlayStart();

        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click = true;
                if (rnSum == 0) {
                    timerClock.cancel();
                    timerClock.start();
                    onPlayStart();
                    points++;
                    getSupportActionBar().setSubtitle("SCORE: " + (points));
                } else {
                    GameOver();
                }
            }
        });

        incorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click = true;
                if (rnSum == 1) {
                    timerClock.cancel();
                    timerClock.start();
                    onPlayStart();
                    points++;
                    getSupportActionBar().setSubtitle("SCORE: " + (points));
                } else {
                    GameOver();
                }
            }
        });


        // -------------------- VIDEO AD MOB ----------------------

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem reward) {
                Toast.makeText(getApplicationContext(), "onRewarded! currency: " + reward.getType() + "  amount: " + reward.getAmount(), Toast.LENGTH_SHORT).show();
                // Reward the user.
                life = true;
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                //    Toast.makeText(getApplicationContext(), "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onRewardedVideoAdClosed() {
                //    Toast.makeText(getApplicationContext(), "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
                if (life) {
                    timerClock.cancel();
                    timerClock.start();
                    onPlayStart();
                    life = false;
                    loadRewardedVideoAd();
                } else {
                    gameOverExecution();
                }
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                  Toast.makeText(getApplicationContext(), errorCode+"", Toast.LENGTH_SHORT).show();
               // gameOverExecution();
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                //   Toast.makeText(getApplicationContext(), "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                //  Toast.makeText(getApplicationContext(), "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                //   Toast.makeText(getApplicationContext(), "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {
                //       Toast.makeText(getApplicationContext(), "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
                loadRewardedVideoAd();
            }
        });

        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-7802066341092922/9074372682", new AdRequest.Builder().build());
    }


    public void onPlayStart() {
        click = false;
        n1 = random.nextInt(10);
        n2 = random.nextInt(10);
        n3 = random.nextInt(10);
        n4 = random.nextInt(10);
        rnSum = random.nextInt(2);

        if (type.equals("1")) {
            eqn.setText(n1 + " + " + n2);
            ans.setText(" = " + (n1 + n2 + rnSum));
        }

        if (type.equals("2")) {
            eqn.setText(n1 + " + " + n2 + " + " + n3);
            ans.setText(" = " + (n1 + n2 + n3 + rnSum));
        }

        if (type.equals("3")) {
            eqn.setText(n1 + " + " + n2 + " + " + n3 + " + " + n4);
            ans.setText(" = " + (n1 + n2 + n3 + n4 + rnSum));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    public void GameOver() {
        timerClock.cancel();

        final AlertDialog alertDialog = new AlertDialog.Builder(PlayActivity.this).create();
        alertDialog.setTitle("GAME OVER");
        alertDialog.setMessage("For Get  a life!!!\nWatch Our Video Ad!!!");
        alertDialog.setCancelable(false);
        // alertDialog.setIcon(R.drawable.welcome);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
              //  if (isNetworkConnected()) {

                    if (mRewardedVideoAd.isLoaded()) {
                        mRewardedVideoAd.show();
                    } else {
                        gameOverExecution();
                    }
//                } else {
//                    gameOverExecution();
//                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
//                }
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "GAME OVER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
                gameOverExecution();

            }
        });

        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void gameOverExecution() {

        if (type.equals("1")) {
            int s = Integer.parseInt(sharedPreferences.getString("bs1", "0"));
            if (s < points) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("bs1", points + "");
                editor.commit();
            }
        }

        if (type.equals("2")) {
            int s = Integer.parseInt(sharedPreferences.getString("bs2", "0"));
            if (s < points) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("bs2", points + "");
                editor.commit();
            }
        }

        if (type.equals("3")) {
            int s = Integer.parseInt(sharedPreferences.getString("bs3", "0"));
            if (s < points) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("bs3", points + "");
                editor.commit();
            }
        }


        startActivity(new Intent(PlayActivity.this, MainActivity.class));
        finish();
        Toast.makeText(getApplicationContext(), "GAME OVER", Toast.LENGTH_LONG).show();

    }

//    private boolean isNetworkConnected() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
//
//        return cm.getActiveNetworkInfo() != null;
//
//    }
}
