package com.matti.gravity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

/**
 *
 */
public class MainActivity0 extends Activity
{
    private static final String CLASS_NAME = "MainActivity0";
    private Bundle savedInstanceState;


    /**
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main0);

        this.savedInstanceState = savedInstanceState;

        SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);

        Log.i(CLASS_NAME, "onCreate orientation " + getResources().getConfiguration().orientation + ", si " + savedInstanceState);
    }

    /**
     *
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        Log.i(CLASS_NAME, "onResume");
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

        Log.i(CLASS_NAME, "onPause");
    }

    /**
     *
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

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

        Log.d(CLASS_NAME, "onSaveInstanceState");
    }
}
