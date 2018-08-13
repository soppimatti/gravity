package com.matti.gravity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 *
 */
public class MainActivity0 extends Activity implements View.OnClickListener
{
    private static final String CLASS_NAME = "MainActivity0";
    private Bundle savedInstanceState;
    private Button level1Button = null;
    private Button level2Button = null;

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

        this.level1Button = (Button) this.findViewById(R.id.level1Button);
        this.level2Button = (Button) this.findViewById(R.id.level2Button);

        this.level1Button.setOnClickListener(this);
        this.level2Button.setOnClickListener(this);

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

    /**
     *
     */
    @Override
    public void onClick(View v)
    {
        Log.d(CLASS_NAME, "onClick v " + v);

        if (v instanceof Button)
        {
            Button b = (Button) v;

            if (b.equals(this.level1Button))
            {
                this.level1ButtonClick();
            }
            else if (b.equals(this.level2Button))
            {
                this.level2ButtonClick();
            }
        }
    }

    /**
     *
     */
    private void level1ButtonClick()
    {
        Log.d(CLASS_NAME, "level1ButtonClick");

        Intent mainIntent = new Intent(MainActivity0.this, SplashActivity1.class);
        MainActivity0.this.startActivity(mainIntent);
        MainActivity0.this.finish();
    }

    /**
     *
     */
    private void level2ButtonClick()
    {
        Log.d(CLASS_NAME, "level2ButtonClick");

        Intent mainIntent = new Intent(MainActivity0.this, SplashActivity2.class);
        MainActivity0.this.startActivity(mainIntent);
        MainActivity0.this.finish();
    }
}
