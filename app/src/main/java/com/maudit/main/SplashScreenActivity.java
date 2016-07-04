package com.maudit.main;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

public class SplashScreenActivity extends Activity {
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.requestWindowFeature(Window.F);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler=new Handler();
        handler.postDelayed(runnableLogin,3000);

    }
    Runnable runnableLogin=new Runnable() {
        @Override
        public void run() {
            Intent intentLogin=new Intent(SplashScreenActivity.this,LoginActivity.class);
            startActivity(intentLogin);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null){
            handler.removeCallbacks(runnableLogin);
        }
    }
}
