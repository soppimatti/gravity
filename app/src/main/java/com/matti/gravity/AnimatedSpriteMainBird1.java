package com.matti.gravity;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by matti on 18/02/16.
 */
public class AnimatedSpriteMainBird1 extends AnimatedSpriteMainBird
{
    /**
     *
     */
    public AnimatedSpriteMainBird1(int[] frameAnimations, int width, int height, int offset, float largementFactor, float density)
    {
        super(frameAnimations, width, height, offset, largementFactor, density);
    }

    /**
     *
     */
    public void onUpdate(long tickCounter, int touchAction)
    {
//        Log.d(CLASS_NAME, "onUpdate tickCounter " + tickCounter + ", ta " + touchAction + ", px " + this.posX + ", py " + this.posY + " s " + this.speed + ", st " + this.screenTop + ", sb " + this.screenBottom);

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
//            Log.d(CLASS_NAME, "onUpdate 1 y " + this.posY + ", s " + this.speed + ", h " + this.height + ", st " + this.screenTop + ", xxx1 " + (this.posY + this.speed - this.height / 2.0f) + ", xxx2 " + (this.posY + this.speed - this.height) + ", xxx3 " + (this.posY + this.speed - this.height/4.0f));
            this.speed = 0;
            this.crashedBottom = true;
            this.posY = (int) (this.screenBottom - this.height / 2.0f);
        }
        else if ((this.posY + this.speed) < this.screenTop)
        {
//            Log.d(CLASS_NAME, "onUpdate 2 y " + this.posY + ", s " + this.speed + ", h " + this.height + ", st " + this.screenTop + ", xxx1 " + (this.posY + this.speed - this.height / 2.0f) + ", xxx2 " + (this.posY + this.speed - this.height) + ", xxx3 " + (this.posY + this.speed - this.height/4.0f));
            this.speed = 0;
            this.crashedTop = true;
            this.posY = this.screenTop;
        }
        else
        {
            this.posY += this.speed;
        }

        if (this.speed >= 0)
        {
            this.frameNo = (this.dirRight ? 0 : 1);
        }
        else
        {
            this.frameNo = (this.dirRight ? 1 : 0);
        }
    }
}
