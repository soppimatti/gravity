package com.matti.gravity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class GravityThread extends Thread
{
    class MyRunnable implements Runnable
    {
        public long tickCnt = 0;

        public void run ()
        {
            tickListener.onRender (this.tickCnt);
        }
    }

    private static String CLASS_NAME = "GravityThread";

    /**
     * Indicate whether the surface has been created & is ready to draw
     */
    private boolean mRun = false;
    private ITickListener tickListener = null;
    private static final int SLEEP_TIME = 30;
    private MyRunnable myRunnable = null;
    private Looper mainLooper = null;
    private Handler handler = null;

    /**
     *
     */
    public GravityThread(ITickListener listener)
    {
        this.mRun = true;
        this.tickListener = listener;
        this.myRunnable = new MyRunnable ();

        this.mainLooper = Looper.getMainLooper ();
        this.handler = new Handler (this.mainLooper);

        Log.d(CLASS_NAME, "GravityThread ct " + Thread.currentThread() + ", this " + this + ", tl " + this.tickListener);
    }

    /**
     *
     */
    @Override
    public void run()
    {
        long tickCounter = 0;
        long sleepTime = 0;
        boolean contRunning = true;

        Log.d(CLASS_NAME, "run this " + this);
        while (this.mRun)
        {
            long t1 =  System.currentTimeMillis();
            if (this.tickListener != null)
            {
//                Log.d(CLASS_NAME, "run2 this " + this);

                contRunning = this.tickListener.onUpdate(tickCounter);
                if (contRunning)
                {
//                    this.myRunnable.tickCnt = tickCounter;
//                    handler.post (this.myRunnable);
                    this.tickListener.onRender(tickCounter);
                }
                else
                {
                    this.mRun = false;
                }
            }

            long t2 =  System.currentTimeMillis();

            sleepTime = ((SLEEP_TIME - (t2 - t1)) <= 0) ? 0 : (SLEEP_TIME - (t2 - t1));
            if (sleepTime > 0)
            {
                try
                {
                    Thread.sleep(sleepTime);
                }
                catch (Exception exc)
                {
                    Log.e(CLASS_NAME, "run Sleep exc " + exc.getMessage());
                }
            }
            else
            {
//                Log.d(CLASS_NAME, "run no sleep");
            }

            long t3 =  System.currentTimeMillis();
//            Log.d(CLASS_NAME, "run st " + sleepTime + ", t2 - t1 " + (t2 - t1) + ", t3 - t1 " + (t3 - t1) + ", t3 - t2 " + (t3 - t2));
//            Log.d(CLASS_NAME, "run mr " + this.mRun + ", ct " + Thread.currentThread() + ", this " + this + ", tl " + this.tickListener);

            tickCounter++;
        }

        Log.d(CLASS_NAME, "run thread exiting this " + this);
    }

    /**
     *
     */
    public void setRunning(boolean b)
    {
        Log.d(CLASS_NAME, "setRunning mr " + this.mRun + ", b " + b + ", ct " + Thread.currentThread() + ", this " + this + ", tl " + this.tickListener);

        this.mRun = b;
    }
}
