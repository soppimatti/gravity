package com.matti.gravity;

/**
 * SharedPrefs används och INTE Bundle savedInstanceState
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

public class SplashActivity2 extends Activity
{
    private static final String CLASS_NAME = "SplashActivity2";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.i(CLASS_NAME, "onCreate 1");

        setContentView(R.layout.splash2);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Log.i(CLASS_NAME, "onCreate sis " + savedInstanceState);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent mainIntent = new Intent(SplashActivity2.this, MainActivity2.class);
                SplashActivity2.this.startActivity(mainIntent);
                SplashActivity2.this.finish();
            }
        }, 2000);
    }

}
