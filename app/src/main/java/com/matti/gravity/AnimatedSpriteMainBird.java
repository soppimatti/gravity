package com.matti.gravity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by matti on 18/02/16.
 */
public abstract class AnimatedSpriteMainBird extends MySprite
{
    protected int[] frameAnimations;
    protected int frameNo = 0;
    protected String CLASS_NAME = "AnimatedSpriteMainBird";
    public boolean loopAnim = true;
    public boolean sequenceDone = false;
    public int speed = 0;
    public int MIN_SPEED = -20;
    public int MAX_SPEED = 20;
    public boolean crashedTop = false;
    public boolean crashedBottom = false;
    public int screenTop = 0;
    public int screenBottom = 0;
    public boolean dirRight = true;

    /**
     *
     */
    public AnimatedSpriteMainBird(int[] frameAnimations, int width, int height, int offset, float largementFactor, float density)
    {
        super(width, height, offset, largementFactor, density);

        this.frameAnimations = frameAnimations;
        this.speed = 0;
    }

    /**
     *
     */
    public void draw(Canvas canvas)
    {
        Rect dst = new Rect(this.posX, this.posY, (int) (this.posX + this.largementFactor*this.width*this.density), (int) (this.posY + this.largementFactor*this.width*this.density));

        int left = (int) (this.offset + (this.width*this.density)*this.frameAnimations[this.frameNo]);
        int right = (int) (left + (this.width*this.density));
        int top = 0;
        int bottom = (int) (this.width*this.density);
        Rect src = new Rect(left, top, right, bottom);

        canvas.drawBitmap(this.bitmap, src, dst, null);
//        Log.d(CLASS_NAME, "draw x " + x + ", t " + 0 + ", xw " + (x + this.width) + ", w " + this.width + ", h " + this.height + ", o " + this.offset + ", px "+ this.posX + ", py "+ this.posY);
//        Log.d(CLASS_NAME, "draw l " + left + ", t " + top + ", r " + right + ", b " + bottom);
    }

    /**
     *
     */
    public abstract void onUpdate(long tickCounter, int touchAction);

    /**
     *
     */
    public Rect getRect()
    {
        Rect r = new Rect(this.posX, this.posY, this.posX + this.width, this.posY + this.height);

        return r;
    }
}
