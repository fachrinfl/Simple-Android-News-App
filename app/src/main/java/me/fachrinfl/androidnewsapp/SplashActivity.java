package me.fachrinfl.androidnewsapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static maes.tech.intentanim.CustomIntent.customType;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;
    @BindView(R.id.progressBar_linear)
    ProgressBar progressBar_linear;
    Handler handler;
    Runnable runnable;
    Timer timer;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        String fontTitle = "fonts/Ubuntu-Bold.ttf";
        String fontSubTitle = "fonts/Ubuntu-Light.ttf";

        tvTitle.setTypeface(Typeface.createFromAsset(getBaseContext().getAssets(), fontTitle));
        tvSubTitle.setTypeface(Typeface.createFromAsset(getBaseContext().getAssets(), fontSubTitle));

        progressBar_linear.setVisibility(View.VISIBLE);
        progressBar_linear.setProgress(0);
        progressBar_linear.setSecondaryProgress(0);
        progressBar_linear.setMax(100);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                if (++i <= 100) {
                    progressBar_linear.setProgress(i);
                    progressBar_linear.setSecondaryProgress(i + 10);
                } else {
                    timer.cancel();
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    customType(SplashActivity.this, "fadein-to-fadeout");
                    finish();
                }
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 100, 50);
    }
}
