package com.matti.gravity;

import android.graphics.*;
import android.util.Log;

/**
 * Created by matti on 18/02/16.
 */
public class MySprite
{
    protected int posX = 0;
    protected int posY = 0;
    protected Bitmap bitmap;
    protected int height = 0;
    protected int width = 0;
    protected static String CLASS_NAME = "MySprite";
    protected int offset;
    protected float largementFactor;
    protected float density;
    protected Paint paint = null;
    public boolean isBonus = false;

    /**
     *
     */
    public MySprite(int pacManWidth, int pacManHeight, int offset, float largementFactor, float density)
    {
        this.width = pacManWidth;
        this.height = pacManHeight;
        this.offset = offset;
        this.largementFactor = largementFactor;
        this.density = density;
        this.paint = new Paint(Color.BLUE);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    /**
     *
     */
    public boolean intersectsWith(MySprite that)
    {
        Rect r = new Rect(that.posX, that.posY, that.posX + that.width, that.posY + that.height);
        r = new Rect((int) (that.posX + that.width/2.0),
                (int) (that.posY + that.height/2.0),
                (int) (that.posX + that.width/2.0),
                (int) (that.posY + that.height/2.0 ));

        r = new Rect(that.posX, that.posY, (int) (that.posX + this.largementFactor*this.width*that.density), (int) (that.posY + this.largementFactor*this.width*that.density));
        r.right = (int) (r.right*0.94f);
        r.bottom = (int) (r.bottom*0.94f);

        boolean intersects = this.intersectsWith(r);

//        Log.d(CLASS_NAME, "intersectsWith intersects " + intersects);

        return intersects;
    }

    /**
     *
     */
    public boolean intersectsWith(Rect that)
    {
        Rect r = null;

        r = new Rect(this.posX, this.posY, (int) (this.posX + this.largementFactor*this.width*this.density), (int) (this.posY + this.largementFactor*this.width*this.density));
        r.right = (int) (r.right*0.94f);
        r.bottom = (int) (r.bottom*0.94f);

        boolean intersects = r.intersects(that.left, that.top, that.right, that.bottom);

//        Log.d(CLASS_NAME, "intersectsWith intersects " + intersects + ", rl " + r.left + ", rt " + r.top + ", rr " + r.right + ", rb " + r.bottom + ", tl " + that.left + ", tt " + that.top + ", tr " + that.right + ", tb " + that.bottom);

        return intersects;
    }

    /**
     *
     */
    protected float getFact()
    {
        float fact = -1;
        if (this.largementFactor == 1)
        {
            fact = 2;
        }
        else if (this.largementFactor == 2)
        {
            fact = 1;
        }
        else if (this.largementFactor == 3)
        {
            fact = 1.3f;
        }
        else
        {
            fact = 1;
        }

        return fact;
    }

    /**
     *
     */
    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    /**
     *
     */
    public void updatePos(int x, int y)
    {
//        Log.d(CLASS_NAME, "updatePos x " + x + ", y " + y + ", bm " + this.bitmap.toString());

        this.posX = x;
        this.posY = y;
    }
}
