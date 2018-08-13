package com.matti.gravity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

public class SplashActivity1 extends Activity
{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.splash);


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent mainIntent = new Intent(SplashActivity1.this, MainActivity1.class);
                SplashActivity1.this.startActivity(mainIntent);
                SplashActivity1.this.finish();
            }
        }, 1000);
    }

    /**
     *
     */
    /*
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Intent mainIntent = new Intent(this, MainActivity1.class);
        SplashActivity1.this.startActivity(mainIntent);
        SplashActivity1.this.finish();

        return true;
    }
    */
}
