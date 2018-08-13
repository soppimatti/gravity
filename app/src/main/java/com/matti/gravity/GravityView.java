package com.matti.gravity;

//import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.*;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by matti on 11/02/16.
 */
public class GravityView extends SurfaceView implements SurfaceHolder.Callback, ITickListener
{
    private static String CLASS_NAME = "GravityView";

    private Context mContext;
    private Activity parentActivity;
    private SurfaceHolder surfaceHolder;
    private static final int PAC_MAN_HIGHT = 20;
    private static final int PAC_MAN_WIDTH = 20;
    private int screenWidth = -1;
    private int screenHeight = -1;
    private List<AnimatedSprite> currentHostileSprites;
    private AnimatedSprite[] hostileSprites;
    private Bitmap flippedMain = null;
    private Bitmap flippedMainBonus = null;
    private Bitmap[] bitmaps;
    private Bitmap[] mainBirdBitmaps;
    private Bitmap[] bitmapsRun;
    private Bitmap[] bitmapsDebug;
    private AnimatedSprite currentHostileSprite;
    private AnimatedSprite explosionSprite;
    private AnimatedSpriteMainBird mainBirdSprite;
    private Bitmap wavesBitmap;
    private Bitmap skyBitmap;
    private Paint paint01;
    private Paint paint02;
    private Paint paint03;
    private GravityThread thread;
    private static final int WAVES_ANIM_INTERVAL_MAX = 30;
    private int wavesAnimIntervalCnt = 0;
    private int spriteX = -1;
    private static int MAIN_BIRD_MAXX = 600;
    private static int MAIN_BIRD_MINX = 300;
    private int mainBirdXDir = 1;
    private int mainBirdX = 1;
    private int spriteY = -1;
    private int orangeSpriteX = -1;
    private int orangeSpriteY = -1;
    private Resources resources = null;
    public static final int LARGEMENT_FACTOR = 2;
    private int touchAction = -1;
    private boolean crashed = false;
    private boolean crashedTop = false;
    private boolean crashedBottom = false;
    private boolean crashedHostile = false;
    private int spriteSize = -1;
    private long ticks = 0;
    private int[] intColorBits;
    private int intColorIdx2;
    private int intColorDir = 1;
    private int crashedProgress = 0;
    private int startDragX = 0;
    private int endDragX = 0;
    private float screenDensity = -1.0f;
    private int highScore = -1;
    private SharedPreferences sharedPrefs = null;
    boolean noCrash = false;
    int noCrashCounter = 0;

    /**
     *
     */
    public GravityView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.mContext = context;

        Log.d(CLASS_NAME, "GravityView D " + this.getResources().getDisplayMetrics().density);

        this.screenDensity = this.getResources().getDisplayMetrics().density;

        // register our interest in hearing about changes to our surface
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);

        // create thread only; it's started in surfaceCreated()
        this.thread = new GravityThread(this);

        this.resources = context.getResources();

        float largementFactor = this.getFact(this.screenDensity);

        this.initBitmaps();

        this.wavesBitmap = this.bitmaps[1];

        this.skyBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.sky13);

        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        this.flippedMain = Bitmap.createBitmap(this.mainBirdBitmaps[0], 0, 0, this.mainBirdBitmaps[0].getWidth(), this.mainBirdBitmaps[0].getHeight(), matrix, true);
        this.flippedMainBonus = Bitmap.createBitmap(this.mainBirdBitmaps[1], 0, 0, this.mainBirdBitmaps[1].getWidth(), this.bitmaps[2].getHeight(), matrix, true);

        this.mainBirdSprite = new AnimatedSpriteMainBird(new int[]{0, 1}, 80, 80, 0, largementFactor, this.screenDensity);
        this.mainBirdSprite.setBitmap(this.bitmaps[2]);
//        this.mainBirdSprite.setBitmap(flippedMain);

        this.hostileSprites = new AnimatedSprite[6];
        this.hostileSprites[0] = new AnimatedSprite(new int[]{0, 1, 2, 1}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[0].setBitmap(this.bitmaps[3]);

        this.hostileSprites[1] = new AnimatedSprite(new int[]{0, 1, 2, 1}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[1].setBitmap(this.bitmaps[4]);

        this.hostileSprites[3] = new AnimatedSprite(new int[]{0, 1, 2, 1}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[3].setBitmap(this.bitmaps[6]);

        this.hostileSprites[4] = new AnimatedSprite(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[4].setBitmap(this.bitmaps[7]);

        this.hostileSprites[2] = new AnimatedSprite(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[2].setBitmap(this.bitmaps[5]);

        this.hostileSprites[5] = new AnimatedSprite(new int[]{0, 1, 2, 3, 4, 5, 6, 6, 5, 4, 3, 2, 1, 0}, 80, 80, 0, largementFactor, this.screenDensity);
        this.hostileSprites[5].setBitmap(this.bitmaps[8]);
        this.hostileSprites[5].isBonus = true;

        this.currentHostileSprite = this.hostileSprites[0];
//        this.currentHostileSprite = this.hostileSprites[5];

        this.currentHostileSprites = new ArrayList<AnimatedSprite>();

        this.explosionSprite = new AnimatedSprite(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, 80, 80, 0, largementFactor, this.screenDensity);
        this.explosionSprite.setBitmap(this.bitmaps[0]);
        this.explosionSprite.posX = 100;
        this.explosionSprite.posY = 100;
        this.explosionSprite.ticksToAnim = 3;
        this.explosionSprite.setLoopAnim(false);

        this.paint01 = new Paint();
        this.paint01.setAntiAlias(true);
        this.paint01.setARGB(255, 50, 100, 100);

        this.paint02 = new Paint();
        this.paint02.setColor(Color.CYAN);
        this.paint02.setStyle(Paint.Style.FILL);
        this.paint02.setTextSize(40);

        this.paint03 = new Paint();
        this.paint03.setColor(Color.RED);
        this.paint03.setTextSize(40);

        setFocusable(true);

        Log.d(CLASS_NAME, "GravityView SIZE_FACTOR " + this.screenDensity);

        this.initIntColorBits();
    }

    /**
     *
     */
    private void initBitmaps()
    {
        this.mainBirdBitmaps = new Bitmap[3];
        this.mainBirdBitmaps[0] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites01_04_160x80);
        this.mainBirdBitmaps[1] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites01_04_5_160x80);
        this.mainBirdBitmaps[2] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites01_04_2_160x80);

        this.bitmapsRun = new Bitmap[9];
        this.bitmapsRun[0] = BitmapFactory.decodeResource(this.resources, R.drawable.explosion05);
        this.bitmapsRun[1] = BitmapFactory.decodeResource(this.resources, R.drawable.waves03);
        this.bitmapsRun[2] = this.mainBirdBitmaps[0];

        this.bitmapsRun[3] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites03_02_240x80);
        this.bitmapsRun[4] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites03_03_240x80);
        this.bitmapsRun[5] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites04_05_720x80);
        this.bitmapsRun[6] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites02_02_240x80);
        this.bitmapsRun[7] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites04_03_720x80);
        this.bitmapsRun[8] = BitmapFactory.decodeResource(this.resources, R.drawable.hearts07);
//        this.bitmapsRun[8] = BitmapFactory.decodeResource(this.resources, R.drawable.penguin_animation04);

        this.bitmapsDebug = new Bitmap[9];
        this.bitmapsDebug[0] = BitmapFactory.decodeResource(this.resources, R.drawable.explosion05_2);
        this.bitmapsDebug[1] = BitmapFactory.decodeResource(this.resources, R.drawable.waves03);
        this.bitmapsDebug[2] = this.mainBirdBitmaps[2];
//        this.bitmapsDebug[2] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites01_04_2_160x80);
//        this.bitmapsDebug[2] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites01_04_5_160x80);

        this.bitmapsDebug[3] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites03_02_2_240x80);
        this.bitmapsDebug[4] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites03_03_2_240x80);
        this.bitmapsDebug[5] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites04_05_2_720x80);
        this.bitmapsDebug[6] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites02_02_2_240x80);
        this.bitmapsDebug[7] = BitmapFactory.decodeResource(this.resources, R.drawable.bird_sprites04_03_2_720x80);
//        this.bitmapsDebug[8] = BitmapFactory.decodeResource(this.resources, R.drawable.penguin_animation03);
        this.bitmapsDebug[8] = BitmapFactory.decodeResource(this.resources, R.drawable.hearts07);

//        this.bitmaps = this.bitmapsDebug;
        this.bitmaps = this.bitmapsRun;
    }

    /**
     *
     */
    private void initIntColorBits()
    {
        Log.d(CLASS_NAME, "initIntColorBits");

        this.intColorIdx2 = 0;
        this.intColorBits = new int[24];
        for (int i = 1; i <= 24; i++)
        {
            this.intColorBits[i - 1] = (int) Math.pow(2, i) - 1;
        }
    }

    /**
     *
     */
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        Log.d(CLASS_NAME, "onAttachedToWindow");
    }

    /**
     *
     */
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        Log.d(CLASS_NAME, "onDetachedFromWindow");
    }

    /**
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(CLASS_NAME, "onMeasure w " + widthMeasureSpec + ", h " + heightMeasureSpec);
    }

    /**
     *
     */
    @Override
    protected void onWindowVisibilityChanged(int visibility)
    {
        super.onWindowVisibilityChanged(visibility);
        Log.d(CLASS_NAME, "onWindowVisibilityChanged v " + visibility);
    }

    /**
     *
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus)
    {
        Log.d(CLASS_NAME, "onWindowFocusChanged hasWindowFocus " + hasWindowFocus + ", thread state " +
                ((this.thread == null) ? "NULL" : this.thread.getState().toString()) +
                ", h " + this.getHeight() + ", w " + this.getWidth());

        if (!hasWindowFocus)
        {
            if (this.thread == null)
            {
                Log.e(CLASS_NAME, "onWindowFocusChanged");
                return;
            }
        }
        else
        {
            this.killThread(false);
            this.thread = new GravityThread(this);
            this.thread.start();
        }
    }

    /**
     *
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        Log.d(CLASS_NAME, "surfaceChanged w " + width + ", h " + height + ", d " + this.getResources().getDisplayMetrics().density);

        this.screenHeight = height;
        this.screenWidth = width;

        this.spriteX = (int) (this.screenWidth / 2.0);
        this.spriteY = (int) (this.screenHeight / 2.0);

        this.spriteSize = this.screenHeight / 5;

        this.mainBirdSprite.screenTop = 70;
        this.mainBirdSprite.screenBottom = screenHeight - 130;

        int posX = (int) ((this.screenWidth / 2.0) - (this.mainBirdSprite.width*this.screenDensity*this.getFact(this.screenDensity))/2.0f);
        this.mainBirdSprite.posX = posX;
        this.mainBirdSprite.posY = (int) (this.screenHeight / 2.0);
        this.mainBirdSprite.MAX_SPEED = 20*((Math.min(this.screenHeight, this.screenWidth) > 720) ? 2 : 1);
        this.mainBirdSprite.MIN_SPEED = -20*((Math.min(this.screenHeight, this.screenWidth) > 720) ? 2 : 1);

        this.mainBirdX = this.mainBirdSprite.posX;
        this.mainBirdXDir = 1;

        for (AnimatedSprite hostileSprite : this.hostileSprites)
        {
            hostileSprite.screenRight = width;
            hostileSprite.screenLeft = 0;
            hostileSprite.screenTop = 70;
            hostileSprite.screenBottom = screenHeight - 130;
            hostileSprite.ticksToAnim = 3;
            hostileSprite.updatePos(0, 200);
        }

        this.currentHostileSprites.add(this.currentHostileSprite.clone());

        GravityView.MAIN_BIRD_MAXX = (int) (posX + (this.screenWidth / 6.0f));
        GravityView.MAIN_BIRD_MINX = (int) (posX - (this.screenWidth / 6.0f));

        Log.d(CLASS_NAME, "surfaceChanged ss " + this.spriteSize);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.d(CLASS_NAME, "surfaceCreated");
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.d(CLASS_NAME, "surfaceDestroyed");

        this.killThread(false);

//        this.sensorManager.unregisterSensors();
    }

    /**
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
//        Log.d (CLASS_NAME, "onTouchEvent a " + event.getAction () + ", x " + event.getX() + ", y " + event.getY());

        this.touchAction = event.getAction();

            if (!this.crashed)
            {
                this.crashedTop = false;
                this.crashedBottom = false;
                this.crashedHostile = false;
            }
            else
            {
                if (this.crashedProgress == 2)
                {
                    if (this.touchAction == MotionEvent.ACTION_DOWN)
                    {
                        this.startDragX = (int) event.getX();
                        this.endDragX = this.startDragX;
                    }

                    if (this.touchAction == MotionEvent.ACTION_UP)
                    {
                        this.endDragX = (int) event.getX();
                    }

//                    Log.d (CLASS_NAME, "onTouchEvent sx " + this.startDragX + ", ex " + this.endDragX);

                    if (Math.abs(this.startDragX - this.endDragX) > 200)
                    {
                        if (this.startDragX > this.endDragX)
                        {
                            this.reset();
                        }
                        else
                        {
                            Log.d (CLASS_NAME, "onTouchEvent Quit ");
                            this.killThread(false);
                            ((MainActivity1) this.mContext).finishAffinity();
                        }
                    }
                }
//                Log.d (CLASS_NAME, "onTouchEvent cp " + this.crashedProgress);
            }

        return true;
    }

    /**
     *
     */
    private void reset()
    {
        Log.d (CLASS_NAME, "onTouchEvent Restart ");
        this.killThread(false);

        this.crashed = false;
        this.crashedTop = false;
        this.crashedBottom = false;
        this.crashedHostile = false;
        this.crashedProgress = 0;
        this.ticks = 0;

//        this.mainBirdSprite.posX = (int) ((this.screenWidth / 2.0) - (this.mainBirdSprite.width/2.0));
        this.mainBirdSprite.posX = (int) ((this.screenWidth / 2.0) - (this.mainBirdSprite.width*this.screenDensity*this.getFact(this.screenDensity))/2.0f);
        this.mainBirdSprite.posY = (int) (this.screenHeight / 2.0);
        this.explosionSprite.resetAnimSequence();

        this.mainBirdX = this.mainBirdSprite.posX;
        this.mainBirdXDir = 1;

        for (int i = 0; i < this.hostileSprites.length; i++)
        {
            this.hostileSprites[i].updatePos(0, 300);
        }

        this.currentHostileSprites.clear();
        this.currentHostileSprites.add(this.currentHostileSprite.clone());

        this.thread = new GravityThread(this);
        this.thread.start();

        this.mainBirdSprite.setBitmap(this.mainBirdBitmaps[0]);
    }

    /**
     *
     */
    private void killThread(boolean changeLevel)
    {
        Log.d(CLASS_NAME, "killThread changeLevel " + changeLevel);

        if (this.thread == null)
        {
            Log.e(CLASS_NAME, "killThread thread null");
            return;
        }

        boolean retry = true;
        Log.d(CLASS_NAME, "killThread1 changeLevel " + changeLevel);
        this.thread.setRunning(false);
        Log.d(CLASS_NAME, "killThread2 changeLevel " + changeLevel);
        while (retry)
        {
            Log.d(CLASS_NAME, "killThread3 changeLevel " + changeLevel);
            try
            {
                Log.d(CLASS_NAME, "killThread4 changeLevel " + changeLevel);
                this.thread.join();
                Log.d(CLASS_NAME, "killThread5 changeLevel " + changeLevel);
                retry = false;
            }
            catch (InterruptedException e)
            {
                Log.e(CLASS_NAME, "killThread exc " + e.getMessage());
            }
        }

        this.thread = null;
        Log.d(CLASS_NAME, "killThread6 t " + ((this.thread == null) ? "NULL" : this.thread.toString()));

        if (changeLevel)
        {
            Log.d(CLASS_NAME, "killThread7 changeLevel " + changeLevel);
            Intent mainIntent = new Intent(this.parentActivity, SplashActivity2.class);
            Log.d(CLASS_NAME, "killThread8 changeLevel " + changeLevel);
            this.parentActivity.startActivity(mainIntent);
            Log.d(CLASS_NAME, "killThread9 changeLevel " + changeLevel);
            this.parentActivity.finish();
            Log.d(CLASS_NAME, "killThread10 changeLevel " + changeLevel);
        }
    }

    /**
     *
     */
    private void drawRestartText(Canvas canvas, Rect r)
    {
//                Log.d(CLASS_NAME, "drawRestartText");
//        this.explosionSprite.draw(canvas);
        if (this.explosionSprite.isAnimSequenceDone())
        {
            float textSize = this.paint02.getTextSize();
            this.paint02.setTextSize(40*((Math.min(this.screenHeight, this.screenWidth) > 720) ? 2 : 1));
            this.crashedProgress = 2;

            String text = "<--  Restart          Quit  --> ";
            canvas.getClipBounds(r);
            int cHeight = r.height();
            int cWidth = r.width();
            this.paint02.setTextAlign(Paint.Align.LEFT);
            this.paint02.getTextBounds(text, 0, text.length(), r);
            float x = cWidth / 2f - r.width() / 2f - r.left;
            float y = cHeight / 2f + r.height() / 2f - r.bottom;
            canvas.drawText(text, x, y, this.paint02);

            this.paint02.setTextSize(textSize);
        }
    }

    /**
     *
     */
    private void doDraw(Canvas canvas)
    {
        if (canvas == null)
        {
            Log.d(CLASS_NAME, "doDraw canvas null");

            return;
        }

        int color = this.newColor();
        this.paint01.setColor(color);

        canvas.drawRect(0, 0, this.screenWidth, this.screenHeight, this.paint01);

        if (this.spriteX == -1)
        {
            Log.d(CLASS_NAME, "doDraw spriteX -1");

            return;
        }

        if (this.screenWidth == -1)
        {
            Log.d(CLASS_NAME, "doDraw screenWidth -1");

            return;
        }

        Rect src = new Rect();
        Rect dst;

        if (this.currentHostileSprites.size() > 0)
        {
            for (AnimatedSprite animSprite : this.currentHostileSprites)
            {
                animSprite.draw(canvas);
            }
        }

        if (this.crashed)
        {
            this.explosionSprite.draw(canvas);
            this.drawRestartText(canvas, src);
        }
        else
        {
            this.mainBirdSprite.draw(canvas);
        }

        // Roof
        canvas.drawRect(0, 0, this.screenWidth, 70, this.paint03);

        // Floor
        dst = new Rect(0, this.screenHeight - 100, this.screenWidth, this.screenHeight);
        if (this.wavesAnimIntervalCnt > (WAVES_ANIM_INTERVAL_MAX / 2))
        {
            src = new Rect(30, 0, this.wavesBitmap.getWidth(), this.wavesBitmap.getHeight());
        }
        else
        {
            src = new Rect(0, 0, this.wavesBitmap.getWidth() - 30, this.wavesBitmap.getHeight());
        }
        canvas.drawBitmap(this.wavesBitmap, src, dst, null);

        Calendar calendar = Calendar.getInstance();
        long secs = calendar.get(Calendar.SECOND);

        canvas.drawText(("Points " + this.ticks + ", sec " + secs + ", w " + this.screenWidth + ", h " + this.screenHeight + ", d " + this.getResources().getDisplayMetrics().density + ", high " + this.highScore), 10, 40, this.paint02);
    }

    /**
     *
     */
    public boolean onUpdate(long tickCounter)
    {
//        Log.d(CLASS_NAME, "onUpdate tickCounter " + tickCounter + ", ta " + this.touchAction + ", tdt " + this.touchDownTime);

        if (this.screenWidth == -1) {
            Log.d(CLASS_NAME, "onUpdate screenWidth -1");

            return true;
        }

        if (this.crashed)
        {
            this.explosionSprite.onUpdate(tickCounter);

            if (this.crashedProgress == 2)
            {
                return false;
            }

            return true;
        }

        this.crashedTop = false;
        this.crashedBottom = false;
        this.crashedHostile = false;
        int hostileSpriteY = -1;
        int hostileSpriteX = -1;

    if (this.mainBirdXDir > 0)
    {
        if ((this.mainBirdX + 1) > GravityView.MAIN_BIRD_MAXX)
        {
            this.mainBirdXDir = -1;
            if (this.noCrash)
            {
                this.mainBirdSprite.setBitmap(this.flippedMainBonus);
            }
            else
            {
                this.mainBirdSprite.setBitmap(this.flippedMain);
            }
        }
        else
        {
            this.mainBirdX++;
        }
    }
    else
    {
        if ((this.mainBirdX - 1) < GravityView.MAIN_BIRD_MINX)
        {
            this.mainBirdXDir = 1;
            if (this.noCrash)
            {
                this.mainBirdSprite.setBitmap(this.mainBirdBitmaps[1]);
            }
            else
            {
                this.mainBirdSprite.setBitmap(this.mainBirdBitmaps[0]);
            }
        }
        else
        {
            this.mainBirdX--;
        }
    }
    this.mainBirdSprite.posX = this.mainBirdX;
    this.mainBirdSprite.dirRight = (this.mainBirdXDir == 1);

//    Log.d(CLASS_NAME, "onUpdate mbx " + this.mainBirdX + ", mbxd " + this.mainBirdXDir);

        if (((this.ticks + 1) % 200) == 0)
        {
            float random = (float) Math.random();
            hostileSpriteY = (int) (random * (this.screenHeight - 400) + 100);

            random = ((int) (random * 100)) % this.hostileSprites.length;
            AnimatedSprite animSprite = this.hostileSprites[(int) random].clone();
            if (random < 3)
            {
                hostileSpriteX = 0;
            }
            else
            {
                hostileSpriteX = this.screenWidth;
            }

            if (random == 5.0f)
            {
                animSprite.moveStep = 5;
                animSprite.isBonus = true;
            }
            else
            {
                animSprite.moveStep = (int) (2 * random * ((Math.min(this.screenHeight, this.screenWidth) > 720) ? 2 : 1) + 5);
                animSprite.isBonus = false;
            }
            animSprite.posX = hostileSpriteX;
            animSprite.posY = hostileSpriteY;
            animSprite.isToLeft = (hostileSpriteX > 2);

            Log.d(CLASS_NAME, "updatePos r3 " + random + ", ms " + animSprite.moveStep + ", isb " + animSprite.isBonus);

            this.currentHostileSprites.add(animSprite);
        }

        this.mainBirdSprite.onUpdate(tickCounter, this.touchAction);
        this.crashedTop = this.mainBirdSprite.crashedTop;
        this.crashedBottom = this.mainBirdSprite.crashedBottom;

        if (!this.crashedTop && !this.crashedBottom)
        {
            Rect r = this.mainBirdSprite.getRect();

            for (AnimatedSprite animSprite : this.currentHostileSprites)
            {
                boolean intersects = animSprite.intersectsWith(this.mainBirdSprite);
                if (intersects)
                {
                    if (animSprite.isBonus)
                    {
                        this.noCrash = true;
                        this.noCrashCounter = 0;
                        if (this.mainBirdXDir == 1)
                        {
                            this.mainBirdSprite.setBitmap(this.mainBirdBitmaps[1]);
                        }
                        else
                        {
                            this.mainBirdSprite.setBitmap(this.flippedMainBonus);
                        }
                        this.currentHostileSprites.remove(animSprite);
                    }
                    else
                    {
                        this.mainBirdSprite.speed = 0;
                        this.crashedHostile = true;
                    }

                    Log.d(CLASS_NAME, "updatePos r4 isb " + animSprite.isBonus);

                    break;
                }
            }
//                Log.d(CLASS_NAME, "onUpdate i " + intersects);
        }

        for (AnimatedSprite animSprite : this.currentHostileSprites)
        {
//                Log.d(CLASS_NAME, "updatePos r5 " + animSprite.posY);
            boolean isOutside = animSprite.onUpdate(tickCounter);
            if (isOutside)
//            if (false)
            {
                float random = (float) Math.random();
                hostileSpriteY = (int) (random * (this.screenHeight - 400) + 100);

                random = ((int) (random * 100)) % this.hostileSprites.length;
                if (random < 3)
                {
                    hostileSpriteX = 0;
                }
                else
                {
                    hostileSpriteX = this.screenWidth;
                }

                this.hostileSprites[(int) random].copy(animSprite);

                float random2 = ((int) (Math.random() * 10) + 5);
                animSprite.moveStep = (int) random2;
                animSprite.posX = hostileSpriteX;
                animSprite.posY = hostileSpriteY;
                animSprite.isToLeft = (hostileSpriteX > 2);
                animSprite.isBonus = ((random == 5.0f) ? true : false);

//                Log.d(CLASS_NAME, "updatePos r2 " + random + ", r22 " +  + random2 + ", ms " + animSprite.moveStep + ", bm " + this.hostileSprites[(int) random].bitmap.toString());
                Log.d(CLASS_NAME, "updatePos r2 " + random + ", r22 " +  + random2 + ", ms " + animSprite.moveStep + ", isb " + animSprite.isBonus);
            }
        }

//            Log.d(CLASS_NAME, "updatePos r4 " + this.currentHostileSprites.get(0).posY);

        this.wavesAnimIntervalCnt = (((this.wavesAnimIntervalCnt + 1) > WAVES_ANIM_INTERVAL_MAX) ? 0 : (this.wavesAnimIntervalCnt + 1));

        if (this.crashedTop || this.crashedBottom || this.crashedHostile)
        {
            if (this.noCrash)
            {
                this.ticks++;
            }
            else
            {
                this.ticks = (((this.ticks - 1) < 0) ? 0 : (this.ticks - 1));
                this.crashed = true;
                this.crashedProgress = 1;
            }
        }
        else
        {
            this.ticks++;
        }

        if (this.noCrashCounter < 200)
        {
            this.noCrashCounter++;
        }
        else
        {
            if (this.noCrash)
            {
                if (this.mainBirdXDir == 1)
                {
                    this.mainBirdSprite.setBitmap(this.mainBirdBitmaps[0]);
                }
                else
                {
                    this.mainBirdSprite.setBitmap(this.flippedMain);
                }
            }
//            Log.d(CLASS_NAME, "updatePos r6");

            this.noCrash = false;
        }

        if (this.crashed)
        {
            this.explosionSprite.posX = (int) (this.mainBirdSprite.posX - (this.mainBirdSprite.width/4.0f));
            this.explosionSprite.posX = (int) (this.mainBirdSprite.posX);
            this.explosionSprite.posY = this.mainBirdSprite.posY;

            this.setHighScore((int) this.ticks);
        }

        return true;
    }

    /**
     *
     */
    public void onRender(long tickCounter)
    {
//        Log.d(CLASS_NAME, "onRender tickCounter " + tickCounter);

        Canvas c = null;
        try
        {
            c = this.surfaceHolder.lockCanvas(null);
            synchronized (this.surfaceHolder)
            {
                this.doDraw(c);
            }
        }
        finally
        {
            if (c != null)
            {
                this.surfaceHolder.unlockCanvasAndPost(c);
            }
        }
    }

    /**
     *
     */
    public Bundle saveState(Bundle map)
    {
        Log.d(CLASS_NAME, "saveState x " + this.spriteX + ", y " + this.spriteY + ", ox " + this.orangeSpriteX + ", oy " + this.orangeSpriteY);

        synchronized (this.surfaceHolder)
        {
            if (map != null)
            {
                map.putInt("SPRITE_X", this.spriteX);
                map.putInt("SPRITE_Y", this.spriteY);
                map.putInt("O_SPRITE_X", this.orangeSpriteX);
                map.putInt("GRAVITY_HIGH_SCORE", this.highScore);
                map.putInt("O_SPRITE_Y", this.orangeSpriteY);
            }
        }

        return map;
    }

    /**
     *
     */
    public void restoreState(Bundle map)
    {
        Log.d(CLASS_NAME, "restoreState map " + map);

        synchronized (this.surfaceHolder)
        {
            if (map != null)
            {
                int x = map.getInt("SPRITE_X", -1);
                int y = map.getInt("SPRITE_Y", -1);

                if (x != -1)
                {
                    if ((x + PAC_MAN_WIDTH * this.screenDensity * LARGEMENT_FACTOR) < this.screenWidth)
                    {
                        this.spriteX = x;
                    }
                    else
                    {
                        this.spriteX = (int) (this.screenWidth - PAC_MAN_WIDTH * this.screenDensity * LARGEMENT_FACTOR);
                    }

                    if ((y + PAC_MAN_HIGHT * this.screenDensity * LARGEMENT_FACTOR) < this.screenHeight)
                    {
                        this.spriteY = y;
                    }
                    else
                    {
                        this.spriteY = (int) (this.screenHeight - PAC_MAN_HIGHT * this.screenDensity * LARGEMENT_FACTOR);
                    }

                    this.orangeSpriteX = map.getInt("O_SPRITE_X");
                    this.orangeSpriteY = map.getInt("O_SPRITE_Y");
                }

                Log.d(CLASS_NAME, "restoreState x " + x + ", y " + y + ", sx " + this.spriteX + ", sy " + this.spriteY + ", ox " + this.orangeSpriteX + ", oy " + this.orangeSpriteY);
            }
        }
    }

    /**
     *
     */
    public void onTick(long tickCounter)
    {
//        Log.d(CLASS_NAME, "onTick tickCounter " + tickCounter);
    }

    /**
     *
     */
    private int newColor()
    {
        if (this.intColorDir == 1)
        {
            if ((this.intColorIdx2 + 1) > 0xFF)
            {
                this.intColorDir = -1;
            }
        }
        else
        {
            if ((this.intColorIdx2 - 1) < 0)
            {
                this.intColorDir = 1;
            }
        }

        this.intColorIdx2 = this.intColorIdx2 + this.intColorDir;

        int color = (0xFF << 24) | (0x00 << 16) | (0x44 << 8) | this.intColorIdx2;

        return color;
    }

    /**
     *
     */
    private float getFact(float density)
    {
        float fact = -1;
        if (density == 2.625f)
        {
            fact = 0.75f;
        }
        else if (density == 3)
        {
            fact = 0.60f;
        }
        else if (density == 1)
        {
            fact = 1.0f;
        }
        else
        {
            fact = 1.0f;
        }

        return fact;
    }

    /**
     *
     */
    public void setSharedPrefs(SharedPreferences sharedPrefs)
    {
        this.sharedPrefs = sharedPrefs;
        this.highScore = this.sharedPrefs.getInt("GRAVITY_HIGH_SCORE", -1);
    }

    /**
     *
     */
    private void setHighScore(int highScore)
    {
        Log.d(CLASS_NAME, "setHighScore highScore " + highScore);

        if (highScore > this.highScore)
        {
            this.highScore = highScore;
            SharedPreferences.Editor editor = this.sharedPrefs.edit();
            editor.putInt("GRAVITY_HIGH_SCORE", highScore);
            editor.commit();
        }
    }

    /**
     *
     */
    public int getHighScore()
    {
        Log.d(CLASS_NAME, "getHighScore highScore " + this.highScore);

        return this.highScore;
    }

    /**
     *
     */
    public void setParentActivity(Activity activity)
    {
        this.parentActivity = activity;
    }

}
