package com.matti.gravity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

public class SplashActivity extends Activity
{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.splash);
    }

    /**
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Intent mainIntent = new Intent(this, MainActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();

        return true;
    }
}
