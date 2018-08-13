package com.matti.gravity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.content.res.Resources;


public class PointsHandler
{
    private static PointsHandler instance = null;
    private static String CLASS_NAME = "PointsHandler";
    private Bitmap[] numberBitmapsSingle = null;
    private Resources resources = null;
    private float screenDensity = -1.0f;
    private float largementFactor = -1.0f;
    private Paint paint03;

    /**
     *
     */
    public static PointsHandler getInstance()
    {
        if (PointsHandler.instance == null)
        {
            PointsHandler.instance = new PointsHandler();
        }

        return PointsHandler.instance;
    }

    /**
     *
     */
    private PointsHandler()
    {
    }

    /**
     *
     */
    public void init()
    {
        this.paint03 = new Paint();
        this.paint03.setColor(Color.RED);

        this.initBitmaps();

        Log.d(CLASS_NAME, "init r " + this.resources + ", lf " + this.largementFactor + ", sd " + this.screenDensity);
    }

    /**
     *
     */
    private void initBitmaps()
    {
        this.numberBitmapsSingle = new Bitmap[10];
        this.numberBitmapsSingle[0] = BitmapFactory.decodeResource(this.resources, R.drawable.zero_01);
        this.numberBitmapsSingle[1] = BitmapFactory.decodeResource(this.resources, R.drawable.one_01);
        this.numberBitmapsSingle[2] = BitmapFactory.decodeResource(this.resources, R.drawable.two_01);
        this.numberBitmapsSingle[3] = BitmapFactory.decodeResource(this.resources, R.drawable.three_01);
        this.numberBitmapsSingle[4] = BitmapFactory.decodeResource(this.resources, R.drawable.four_01);
        this.numberBitmapsSingle[5] = BitmapFactory.decodeResource(this.resources, R.drawable.five_01);
        this.numberBitmapsSingle[6] = BitmapFactory.decodeResource(this.resources, R.drawable.six_01);
        this.numberBitmapsSingle[7] = BitmapFactory.decodeResource(this.resources, R.drawable.seven_01);
        this.numberBitmapsSingle[8] = BitmapFactory.decodeResource(this.resources, R.drawable.eight_01);
        this.numberBitmapsSingle[9] = BitmapFactory.decodeResource(this.resources, R.drawable.nine_01);
    }

    /**
     *
     */
    public Bitmap createNumberBitmap(int num)
    {
        int[] numParts = this.numTodigits(num);

        Bitmap.Config config = this.numberBitmapsSingle[0].getConfig();
        Bitmap bitmap1 = Bitmap.createBitmap((int) (240*this.screenDensity), (int) (120*this.screenDensity), config);
        Canvas canvas1 = new Canvas(bitmap1);
        /*
        canvas1.drawRect(0, 0,
                240*this.screenDensity,
                120*this.screenDensity, this.paint03);
*/
        Rect src = null;
        Rect dst = null;

        // Rita på den bitmapen man skapat (bitmap1) och som ligger i canvas1
        src = new Rect(0,0, (int) (80*this.screenDensity), (int) (120*this.screenDensity));
        dst = new Rect(0, 0,
                (int) (this.largementFactor*80*this.screenDensity) + 60,
                (int) (this.largementFactor*120*this.screenDensity) + 100);
        canvas1.drawBitmap(this.numberBitmapsSingle[numParts[2]], src, dst, null);
//        canvas1.drawBitmap(this.numberBitmapsSingle[numParts[2]], src, dst, null);
/*
        // Rita på den bitmapen man skapat (bitmap1) och som ligger i canvas1
        src = new Rect(0,0, (int) (80*this.screenDensity), (int) (120*this.screenDensity));
        dst = new Rect(0, 0,
                (int) (this.largementFactor*80*this.screenDensity) + 80,
                (int) (this.largementFactor*120*this.screenDensity) + 120);
        canvas1.drawBitmap(this.numberBitmapsSingle[0], src, dst, null);
//        canvas1.drawBitmap(this.numberBitmapsSingle[numParts[2]], src, dst, null);
*/
        // Rita på den bitmapen man skapat (bitmap1) och som ligger i canvas1
        dst = new Rect((int) (this.largementFactor*80*this.screenDensity) + 60,
                0,
                (int) (this.largementFactor*160*this.screenDensity) + 120,
                (int) (this.largementFactor*120*this.screenDensity) + 100);
        canvas1.drawBitmap(this.numberBitmapsSingle[numParts[1]], src, dst, null);
//        canvas1.drawBitmap(this.numberBitmapsSingle[numParts[1]], src, dst, null);

        // Rita på den bitmapen man skapat (bitmap1) och som ligger i canvas1
        dst = new Rect((int) (this.largementFactor*160*this.screenDensity) + 100,
                0,
                (int) (this.largementFactor*240*this.screenDensity) + 160,
                (int) (this.largementFactor*120*this.screenDensity) + 100);
        canvas1.drawBitmap(this.numberBitmapsSingle[numParts[0]], src, dst, null);
//        canvas1.drawBitmap(this.numberBitmapsSingle[numParts[0]], src, dst, null);
/*
        dst = new Rect((int) (this.largementFactor*80*this.screenDensity) + 80,
                0,
                (int) (this.largementFactor*160*this.screenDensity) + 160,
                (int) (this.largementFactor*120*this.screenDensity) + 120);
        canvas1.drawBitmap(this.numberBitmapsSingle[1], src, dst, null);
//        canvas1.drawBitmap(this.numberBitmapsSingle[numParts[1]], src, dst, null);

        // Rita på den bitmapen man skapat (bitmap1) och som ligger i canvas1
        dst = new Rect((int) (this.largementFactor*160*this.screenDensity) + 160,
                0,
                (int) (this.largementFactor*240*this.screenDensity) + 240,
                (int) (this.largementFactor*120*this.screenDensity) + 120);
        canvas1.drawBitmap(this.numberBitmapsSingle[0], src, dst, null);
//        canvas1.drawBitmap(this.numberBitmapsSingle[numParts[0]], src, dst, null);
*/
        return bitmap1;
    }

    /**
     *
     */
    public int[] numTodigits(int num)
    {
        int logNum = (int) Math.log10(num);
        int[] logVals = new int[3];
//        int[] logVals = new int[logNum + 1];
        for (int i = 0; i < logVals.length; i++)
        {
            logVals[i] = 0;
        }

        int a = num;
        int b = -1;
//        Log.d(CLASS_NAME, "numTodigits logNum " + logNum);
        for (int i = logNum; i > 0; i--)
        {
            int pow = (int) Math.pow(10, i);
            b = a/pow;
            int c = a - b*pow;

            a = c;

            logVals[i] = b;
        }
        logVals[0] = a;

        return logVals;
   }

    public void setResources(Resources resources) { this.resources = resources; }
    public void setScreenDensity(float screenDensity) { this.screenDensity = screenDensity; }
    public void setLargementFactor(float largementFactor) { this.largementFactor = largementFactor; }
}
