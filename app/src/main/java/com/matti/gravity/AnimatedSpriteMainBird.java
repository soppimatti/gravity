package com.matti.gravity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by matti on 18/02/16.
 */
public class AnimatedSpriteMainBird extends MySprite
{
    protected int[] frameAnimations;
    protected int frameNo = 0;
    private String CLASS_NAME = "AnimatedSpriteMainBird";
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
    public void updateAnimation()
    {
        Log.d(CLASS_NAME, "updateAnimation1 f " + this.frameNo + ", s " + this.sequenceDone + ", l " + this.loopAnim + ", class " + this);

        if (this.sequenceDone)
        {
            if (!this.loopAnim)
            {
                Log.d(CLASS_NAME, "updateAnimation2 f " + this.frameNo + ", s " + this.sequenceDone + ", l " + this.loopAnim + ", class " + this);

                return;
            }
        }

        this.frameNo++;

        if (this.frameNo >= this.frameAnimations.length)
        {
            if (this.loopAnim)
            {
                this.frameNo = 0;
            }
            else
            {
                this.frameNo = this.frameAnimations.length - 1;
            }

            this.sequenceDone = true;
        }
    }

    /**
     *
     */
    public void setLoopAnim(boolean doLoop)
    {
        this.loopAnim = doLoop;

//        Log.d(CLASS_NAME, "setLoopAnim doLoop " + doLoop);
    }

    /**
     *
     */
    public boolean isAnimSequenceDone()
    {
        return this.sequenceDone;
    }

    /**
     *
     */
    public void onUpdate(long tickCounter, int touchAction)
    {
//        Log.d(CLASS_NAME, "onUpdate tickCounter " + tickCounter + ", ta " + touchAction);

        this.crashedTop = false;
        this.crashedBottom = false;
/*
        if (touchAction == 1)
        {
            if ((this.speed - 1) > this.MIN_SPEED)
            {
                this.speed--;
            }
        }
        else if (touchAction == -1)
        {
            if ((this.speed + 1) < this.MAX_SPEED)
            {
                this.speed++;
            }
        }
        else
        {
            this.speed = 0;
        }
*/
        if (touchAction == -1)
        {
            this.speed = 10;
        }
        else if (touchAction == 1)
        {
            this.speed = -10;
        }
        else
        {
            this.speed = 0;
        }

        if ((this.posY + this.speed + this.height / 2.0) > this.screenBottom)
        {
//                Log.d(CLASS_NAME, "onUpdate D");
            this.speed = 0;
            this.crashedBottom = true;
            this.posY = (int) (this.screenBottom - this.height / 2.0f);
        }
        else if ((this.posY + this.speed) < this.screenTop)
        {
            Log.d(CLASS_NAME, "onUpdate U y " + this.posY + ", s " + this.speed + ", h " + this.height + ", st " + this.screenTop + ", xxx1 " + (this.posY + this.speed - this.height / 2.0f) + ", xxx2 " + (this.posY + this.speed - this.height) + ", xxx3 " + (this.posY + this.speed - this.height/4.0f));
            this.speed = 0;
            this.crashedTop = true;
            this.posY = this.screenTop;
//            Log.d(CLASS_NAME, "onUpdate U y " + this.posY + ", s " + this.speed + ", h " + this.height + ", st " + this.screenTop);
        }
        else
        {
            this.posY += this.speed;
        }

//        Log.d(CLASS_NAME, "onUpdate U y " + this.posY + ", s " + this.speed + ", h " + this.height + ", st " + this.screenTop);

        if (this.speed >= 0)
        {
            this.frameNo = (this.dirRight ? 0 : 1);
        }
        else
        {
            this.frameNo = (this.dirRight ? 1 : 0);
        }
    }

    /**
     *
     */
    public void onUpdate2(long tickCounter, int touchAction)
    {
//        Log.d(CLASS_NAME, "onUpdate tickCounter " + tickCounter + ", ta " + touchAction);

        this.crashedTop = false;
        this.crashedBottom = false;

        if (touchAction == MotionEvent.ACTION_DOWN)
        {
            if ((this.speed - 1) > this.MIN_SPEED)
            {
                this.speed--;
            }
        }
        else if (touchAction == MotionEvent.ACTION_MOVE)
        {
            if ((this.speed - 1) > this.MIN_SPEED)
            {
                this.speed--;
            }
        }
        else if (touchAction == MotionEvent.ACTION_UP)
        {
            if ((this.speed + 1) < this.MAX_SPEED)
            {
                this.speed++;
            }
        }
        else
        {
            if ((this.speed + 1) < this.MAX_SPEED)
            {
                this.speed++;
            }
        }

        if ((this.posY + this.speed + this.height / 2.0) > this.screenBottom)
        {
//                Log.d(CLASS_NAME, "onUpdate D");
            this.speed = 0;
            this.crashedBottom = true;
            this.posY = (int) (this.screenBottom - this.height / 2.0f);
        }
        else if ((this.posY + this.speed) < this.screenTop)
        {
            Log.d(CLASS_NAME, "onUpdate U y " + this.posY + ", s " + this.speed + ", h " + this.height + ", st " + this.screenTop + ", xxx1 " + (this.posY + this.speed - this.height / 2.0f) + ", xxx2 " + (this.posY + this.speed - this.height) + ", xxx3 " + (this.posY + this.speed - this.height/4.0f));
            this.speed = 0;
            this.crashedTop = true;
            this.posY = this.screenTop;
//            Log.d(CLASS_NAME, "onUpdate U y " + this.posY + ", s " + this.speed + ", h " + this.height + ", st " + this.screenTop);
        }
        else
        {
//            this.posY += this.speed;
        }

//        Log.d(CLASS_NAME, "onUpdate U y " + this.posY + ", s " + this.speed + ", h " + this.height + ", st " + this.screenTop);

        if (this.speed >= 0)
        {
            this.frameNo = (this.dirRight ? 0 : 1);
        }
        else
        {
            this.frameNo = (this.dirRight ? 1 : 0);
        }
    }

    /**
     *
     */
    public Rect getRect()
    {
        Rect r = new Rect(this.posX, this.posY, this.posX + this.width, this.posY + this.height);

        return r;
    }
}
