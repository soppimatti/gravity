package com.matti.gravity;

/**
 * SharedPrefs används och INTE Bundle savedInstanceState
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

/**
 *
 */
public class MainActivity2 extends Activity
{
    private static final String CLASS_NAME = "MainActivity2";
    private GravityView2 gravityView;
    private Bundle savedInstanceState;

    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main2);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.savedInstanceState = savedInstanceState;

        this.gravityView = (GravityView2) this.findViewById(R.id.gravity_view2);

//        Log.i(CLASS_NAME, "onCreate orientation " + getResources().getConfiguration().orientation);

//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);
        this.gravityView.setSharedPrefs(sp);

        Log.i(CLASS_NAME, "onCreate sis " + savedInstanceState);
    }

    /**
     *
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        Log.i(CLASS_NAME, "onResume this " + Thread.currentThread());

//        this.gravityView.restoreState(this.savedInstanceState);
    }

    /**
     *
     */
    @Override
    protected void onRestart()
    {
        super.onRestart();

        Log.i(CLASS_NAME, "onRestart");
    }

    /**
     *
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        this.gravityView.onPause();

        Log.i(CLASS_NAME, "onPause");
    }

    /**
     *
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

//        this.savedInstanceState = this.gravityView.saveState(this.savedInstanceState);

        Log.i(CLASS_NAME, "onDestroy");
    }

    /**
     * Notification that something is about to happen, to give the Activity a
     * chance to save state.
     *
     * @param outState a Bundle into which this Activity should save its state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        Log.d(CLASS_NAME, "onSaveInstanceState this " + Thread.currentThread());

//        this.savedInstanceState = this.gravityView.saveState(outState);
    }

    /**
     *
     */
    protected String getStringExtra(Bundle savedInstanceState, String id)
    {
        String s;
        if (savedInstanceState != null)
        {
            s = (String) savedInstanceState.getSerializable(id);
            Log.d(CLASS_NAME, "getStringExtra1 s " + s);
        }
        else
        {
            Bundle extras = getIntent().getExtras();
            if (extras != null)
            {
                s = extras.getString(id);
                Log.d(CLASS_NAME, "getStringExtra2 s " + s);
            }
            else
            {
                s = null;
                Log.d(CLASS_NAME, "getStringExtra3 s " + s);
            }
        }

        return s;
    }
}
