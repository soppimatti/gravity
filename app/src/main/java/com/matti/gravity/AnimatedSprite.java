package com.matti.gravity;

import android.graphics.*;
import android.util.Log;

/**
 * Created by matti on 18/02/16.
 */
public class AnimatedSprite extends MySprite
{
    protected int[] frameAnimations;
    protected int frameNo = 0;
    private boolean loopAnim = true;
    private boolean sequenceDone = false;
    public int ticksToAnim = 0;
    public int moveStep = 8;
    public int screenTop = 0;
    public int screenBottom = 0;
    public int screenLeft = 0;
    public int screenRight = 0;
    public boolean isToLeft = false;

    /**
     *
     */
    public AnimatedSprite(int[] frameAnimations, int width, int height, int offset, float largementFactor, float density)
    {
        super(width, height, offset, largementFactor, density);

        this.frameAnimations = frameAnimations;
    }

    /**
     *
     */
    public void draw(Canvas canvas)
    {
        float fact = this.getFact();
        fact = 1.0f;

        // Var på skärmen det hamnar. Man kan förstora och skala
        Rect dst = null;
        dst = new Rect(this.posX, this.posY,
                (int) (this.posX + this.largementFactor*this.width*this.density),
                (int) (this.posY + this.largementFactor*this.height*this.density));

        int left = -1;
        int right = -1;
        left = (int) (this.offset + (this.width*this.density)*this.frameAnimations[this.frameNo]);
        right = (int) (left + (this.width*this.density));

        // Var i bitmapen man läser
        Rect src = null;
        src = new Rect(left, 0, right, (int) (this.height*this.density));

        canvas.drawBitmap(this.bitmap, src, dst, null);
/*
        Log.d(CLASS_NAME, "draw x " + x + ", xw " + (x + this.width) + ", w " + this.width + ", h " + this.height + ", o " + this.offset +
                ", px "+ this.posX + ", py "+ this.posY + ", fn "+ this.frameNo + ", obj " + this.bitmap.toString());
*/
/*
        Log.d(CLASS_NAME, "draw x " + x + ", xw " + (x + this.width) + ", w " + this.width + ", h " + this.height + ", o " + this.offset +
                ", px "+ this.posX + ", py "+ this.posY + ", fn "+ this.frameNo + ", l " + left + ", r " + right);
*/
    }

    /**
     *
     */
    public void updateAnimation()
    {
//        Log.d(CLASS_NAME, "updateAnimation1 f " + this.frameNo + ", s " + this.sequenceDone + ", l " + this.loopAnim + ", class " + this);

        if (this.sequenceDone)
        {
            if (!this.loopAnim)
            {
//                Log.d(CLASS_NAME, "updateAnimation2 f " + this.frameNo + ", s " + this.sequenceDone + ", l " + this.loopAnim + ", class " + this);

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
    public boolean onUpdate(long ticks)
    {
//        Log.d(CLASS_NAME, "onUpdate);

        boolean isOutside = false;

        if (this.isToLeft)
        {
            if ((this.posX - this.moveStep) > 0)
            {
                this.posX -= this.moveStep;
            }
            else
            {
                isOutside = true;
            }
        }
        else
        {
            if ((this.posX + this.width) < this.screenRight)
            {
                this.posX += this.moveStep;
            }
            else
            {
                isOutside = true;
            }
        }

        if ((ticks % this.ticksToAnim) == 0)
        {
            this.updateAnimation();
        }

        return isOutside;
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
    public void resetAnimSequence()
    {
        this.sequenceDone = false;
        this.frameNo = 0;
    }

    /**
     *
     */
    public AnimatedSprite copy(AnimatedSprite other)
    {
        other.frameAnimations = this.frameAnimations;
        other.frameNo = this.frameNo;
        other.loopAnim = this.loopAnim;
        other.sequenceDone = this.sequenceDone;
        other.ticksToAnim = this.ticksToAnim;
        other.moveStep = this.moveStep;
        other.screenTop = this.screenTop;
        other.screenBottom = this.screenBottom;
        other.screenLeft = this.screenLeft;
        other.screenRight = this.screenRight;
        other.isToLeft = this.isToLeft;
        other.posX = this.posX;
        other.posY = this.posY;
        other.bitmap = this.bitmap;
        other.height = this.height;
        other.width = this.width;
        other.offset = this.offset;
        other.largementFactor = this.largementFactor;
        other.density = this.density;
        other.paint = this.paint;
        other.isBonus = this.isBonus;

        return other;
    }

    /**
     *
     */
    public AnimatedSprite clone()
    {
        AnimatedSprite newSprite = new AnimatedSprite(this.frameAnimations, this.width, this.height, this.offset, this.largementFactor, this.density);
        newSprite.frameNo = this.frameNo;
        newSprite.loopAnim = this.loopAnim;
        newSprite.sequenceDone = this.sequenceDone;
        newSprite.ticksToAnim = this.ticksToAnim;
        newSprite.moveStep = this.moveStep;
        newSprite.screenTop = this.screenTop;
        newSprite.screenBottom = this.screenBottom;
        newSprite.screenLeft = this.screenLeft;
        newSprite.screenRight = this.screenRight;
        newSprite.isToLeft = this.isToLeft;
        newSprite.bitmap = this.bitmap;
        newSprite.posX = this.posX;
        newSprite.posY = this.posY;
        newSprite.density = this.density;
        newSprite.largementFactor = this.largementFactor;
        newSprite.isBonus = this.isBonus;

        return newSprite;
    }
}
